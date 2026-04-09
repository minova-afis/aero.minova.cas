package aero.minova.cas;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.minova.foundation.rest.db.model.RegistryNode;
import ch.minova.foundation.rest.db.service.FileService;
import ch.minova.foundation.rest.db.service.RegistryService;
import ch.minova.foundation.rest.db.service.XMLUtils;

@Component
/** LEGACY!
 * Component that is used to patch 11 artifacts with WFC or to modify forms with option-pages,
 * to enable slim front-ends
 * See also https://github.com/minova-afis/aero.minova.cas/issues/1473
 */
public class ContentPatcher {
	private static final String NODE_FORM = "form";
	private static final String NODE_DETAIL = "detail";
	private static final String NODE_PAGE = "page";
	private static final String NODE_HEAD = "head";
	private static final String NODE_GRID = "grid";
	private static final String NODE_DYNAMIC = "dynamic";
	private static final String NODE_EVENTS = "events";
	private static final String NODE_OPTIONPAGE = "optionpage";
	
	private static final String ATTR_ICON = "icon";
	private static final String ATTR_ID = "id";
	private static final String ATTR_NAME = "name";
	private static final String ATTR_TEXT = "text";
	private static final String ATTR_MAP_TO = "map-to";
	private static final String ATTR_KEY_TYPE = "key-type";
	private static final String ATTR_TITLE = "title";
	private static final String ATTR_PROPERTY = "property";
	private static final String ATTR_VISIBLE = "visible";
	
	private static final String XBS_NODE_OPTIONPAGES = "OptionPages";
//	private static final String XBS_NODE_DEPENDINGFIELDS = "DependingFields";
	private static final String XBS_VAL_PRIORITY = "priority";
	private static final String XBS_VAL_VISIBLE = "visible";
	private static final String XBS_VAL_VISIBLE_WHEN = "visible-when";
	
	
	@Autowired
	FileService dbFileService;
	
	@Autowired
	RegistryService registryService;
	
	@Autowired
	public CustomLogger customLogger;
	
	public ContentPatcher() {
	}
	
	private Map<String, CacheEntry> CACHE = new HashMap<>();
	
	static class CacheEntry {
		int hash;
		byte[] data;
		
		public CacheEntry(int hash, byte[] data) {
			super();
			this.hash = hash;
			this.data = data;
		}
	}
	
	public void clearCache() {
		synchronized(CACHE) {
			CACHE.clear();
		}
	}

	/**
	 * Automatically update forms to be compatible with WFC
	 * 1. Icon entries: remove .ico postfix
	 * 2. Set procedure prefix to "sp"/"op" if not defined at all
	 */
	public byte[] patchXMLForm(String fileName, byte[] data) {
		try {
			boolean isOptionPage = (fileName != null && fileName.toLowerCase().endsWith(".op.xml"));
			Document doc = XMLUtils.getDocument(data);
			if (doc == null)
				return data;
			// Fix prefixes and IDs
			for (Element el : XMLUtils.findElementWithAttribute(doc.getDocumentElement(), "procedure-suffix", null, null)) {
				if (!el.hasAttribute("id"))
					el.setAttribute("id", el.getTagName());
				if (!el.hasAttribute("procedure-prefix")) {
					// Also support grids defined within normal forms
					boolean useOPPrefix = isOptionPage || !("detail".equalsIgnoreCase(el.getTagName()));
					el.setAttribute("procedure-prefix", (useOPPrefix ? "op" : "sp"));
				}
			}
			return XMLUtils.toBytes(doc);
		} catch (Exception ex) {
			customLogger.logError("Failed to patch form " + fileName, ex);
		}
		return data;
	}
	
	/**
	 * Modify XBS to be compatible with WFC. Uses internal hashing to avoid heavy operations
	 */
	public byte[] patchXBS(String path, byte[] xbs) {
		if(xbs == null)
			return null;
		
		// Is there cached entry?
		int initialHash = Arrays.hashCode(xbs);
		CacheEntry ce;
		if(path != null && (ce = CACHE.get(path.toLowerCase())) != null && ce.hash == initialHash) {
			return ce.data;
		}
		
		byte[] toRet = xbs;
		try {
			Document doc = XMLUtils.getDocument(xbs);
			for (Element el : XMLUtils.findElementWithAttribute(doc.getDocumentElement(), "name", (v) -> (v != null && v.equalsIgnoreCase("OptionPages")),
					null)) {
				String formFile = ((Element) el.getParentNode()).getAttribute("name");
				byte[] fiForm = dbFileService.getFile(formFile);

				for (Element op : XMLUtils.toArray(el.getChildNodes())) { // node
					String opFile = op.getAttribute("name");
					byte[] fiOp = dbFileService.getFile(opFile);

					for (Element map : XMLUtils.toArray(op.getChildNodes())) { // map
						for (Element entry : XMLUtils.toArray(map.getChildNodes())) { // entry
							if (entry.hasAttribute("key") && entry.getAttribute("key").toLowerCase().startsWith("key")) {
								// Convert <entry key="Key0" value="KeyLong"/> -> <entry key="KeyLong" value="KeyLong"/>
								String currentKey = entry.getAttribute("key");
								int keyNr = (currentKey.length() > 3 ? Integer.parseInt(currentKey.substring(3)) : 0);
								String opKey = entry.getAttribute("value");
								String formKey = "KeyLong";
								// Check the OP key
								try {
									if (fiOp != null) {
										Document form = XMLUtils.getDocument(fiOp);
										List<Element> keys = XMLUtils.findElementWithAttribute(form.getDocumentElement(), "key-type",
												v -> (v != null && v.equalsIgnoreCase("primary")), null);
										boolean found = false;
										for (Element key : keys)
											found |= opKey.equalsIgnoreCase(key.getAttribute("name"));
										if (!found) {
											if (keys.isEmpty()) {
												customLogger.logFiles("No primary keys defined within " + opFile + ". Auto check for Key " + opKey + "not possible");
											} else {
												customLogger.logFiles(opKey + " not found in " + opFile + " -> auto change to " + keys.get(0).getAttribute("name"));
												opKey = keys.get(0).getAttribute("name");
											}
										}
									} else {
										customLogger.logFiles(opFile + " not found. Auto-fixing keys not possible");
									}
								} catch (Exception ex) {
									customLogger.logError(opFile + " OP keys fixing failed", ex);
								}
								entry.setAttribute("key", opKey);

								// Fix form key
								try {
									if (fiForm != null) {
										Document form = XMLUtils.getDocument(fiForm);
										List<Element> keys = XMLUtils.findElementWithAttribute(form.getDocumentElement(), "key-type",
												v -> (v != null && v.equalsIgnoreCase("primary")), null);
										if (keys.isEmpty() || keys.size() <= keyNr) {
											customLogger.logFiles(formFile + " does not have #" + keyNr + " key");
										} else {
											formKey = keys.get(keyNr).getAttribute("name");
										}
									} else {
										customLogger.logFiles(formFile + " not found. Auto-fixing keys not possible");
									}
								} catch (Exception ex) {
									customLogger.logError(opFile + " key auto-fix failed", ex);
								}
								entry.setAttribute("value", formKey);
							}
						}
					}
				}
			}
			toRet = XMLUtils.toBytes(doc);
		} catch (Exception ex) {
			customLogger.logError("Failed to patch XBS " + path, ex);
		}
		// Add to cache
		if(toRet != null && path != null) {
			synchronized(CACHE) {
				CACHE.put(path.toLowerCase(), new CacheEntry(initialHash, toRet));
			}
		}
		return toRet;
	}
	
	/** Returns the priority of given OptionPage or Integer.MAX_VALUE if none defined
	 * @throws NumberFormatException If the number of the priority is invalid
	 */
	private static double getOptionPagePriority(RegistryNode op) throws NumberFormatException {
		if(op != null) {
			for(RegistryNode ch : op.getChildren().values()) {
				if(XBS_VAL_PRIORITY.equalsIgnoreCase(ch.getName())) try {
					return Double.parseDouble(ch.getValue());
				} catch(NumberFormatException ex) {
					throw new NumberFormatException("Bad priority defined for " + op.getName() + ": " + ch.getValue());
				}
			}
		}
		return Integer.MAX_VALUE;
	}

	/**
	 * Inject all defined option pages and FORM into the basic form.
	 * @param path
	 * @param toRet
	 * @return
	 */
	public byte[] resolveXMLForm(String application, String path, byte[] form) {
		if(form == null)
			return null;
		
		// Is there cached entry?
		int initialHash = Arrays.hashCode(form);
		CacheEntry ce;
		if(path != null && (ce = CACHE.get(path.toLowerCase())) != null && ce.hash == initialHash) {
			return ce.data;
		}
		
		byte[] toRet = form;
		
		try {
			Document formDoc = XMLUtils.getDocument(form);
			// We use registry service for that...
			// E.g. AFIS/Vat.xml/OptionPages/VatSite.op.xml/Key0=KeyLong
			// But also double keys as two entries...
			// AFIS/LuSupplierCustomer.xml/OptionPages/LuSupplierCustomerExHydrant.op.xml/Key0
			// AFIS/LuSupplierCustomer.xml/OptionPages/LuSupplierCustomerExHydrant.op.xml/Key1
			String prefix = (application == null ? "" : application) +
					 		(path.startsWith("/") ? "" : "/") + path +
							"/"+XBS_NODE_OPTIONPAGES+"/";
			
			// getSubtree converts value list to a tree, based on the given prefix, e.g.
			//   AFIS/LuSupplierCustomer.xml/OptionPages/LuSupplierCustomerExHydrant.op.xml/Key0
			//   AFIS/LuSupplierCustomer.xml/OptionPages/LuSupplierCustomerExHydrant.op.xml/Key1
			//   AFIS/LuSupplierCustomer.xml/OptionPages/LuSupplierCustomerExHydrant.op.xml/Priority
			// ->
			//   OptionPages
			//             |- LuSupplierCustomerExHydrant.op.xml
			//                                                  |- Key0 = SupplierKey
			//                                                  |- Key1 = CustomerKey
			//                                                  |- Priority = 2
			RegistryNode ops = registryService.getSubtree(prefix); // E.g. AFIS/Item.xml/OptionPages
			if(ops != null && !ops.getChildren().isEmpty()) {
				List<RegistryNode> opList = new LinkedList<>(ops.getChildren().values());
				// The option-pages can also be defined with prioritiy. Probably to enforce one OP working before the other (wyld)
				opList.sort((a, b) -> Double.compare(getOptionPagePriority(a), getOptionPagePriority(b)));
				for(RegistryNode op : opList) try {
					byte[] opCode = dbFileService.getFile(op.getName());
					if(opCode == null) {
						customLogger.logError("Failed to add " + op.getName() + " option page to " + path + ": option page doesn't exist in tFile");
						continue;
					}
					Document opDoc = XMLUtils.getDocument(opCode);
					// Now we need the key-Map:
					// - Key = Key within the parent Detail, e.g. KeyLong (by name, new-stlye) or Key0 (by index, legacy-style)
					// - Value = key within option page, e.g. SupplierKey (only by name)
					Map<String, String> keyMapping = new LinkedHashMap<String, String>();
					// Further attributes written in the XBS, such as "visible" or "visible-when"
					Map<String, String> opAtttributes = new LinkedHashMap<String, String>();
					for(RegistryNode opNode : op.getChildren().values()) {
						String key = opNode.getName();
						if(opNode.getChildren().isEmpty() && opNode.hasValue()) {
							if(XBS_VAL_VISIBLE.equalsIgnoreCase(key) ||
							   XBS_VAL_VISIBLE_WHEN.equalsIgnoreCase(key) ||
							   XBS_VAL_PRIORITY.equalsIgnoreCase(key)) {
								opAtttributes.put(key, opNode.getValue());
							} else {
								keyMapping.put(key, opNode.getValue());
							}
						}
					}
					addOptionPage(formDoc, opDoc, keyMapping, opAtttributes);
				} catch(Exception ex1) {
					customLogger.logError("Failed to add " + op.getName() + " option page to " + path, ex1);				
				}
			}
			toRet = XMLUtils.toBytes(formDoc);
		} catch (Exception ex) {
			customLogger.logError("Failed to resolve XML form " + path, ex);
		}
	
		
		// Add to cache
		if(toRet != null && path != null) {
			synchronized(CACHE) {
				CACHE.put(path.toLowerCase(), new CacheEntry(initialHash, toRet));
			}
		}
		return toRet;
	}
	
	/**
	 * Integrate option page into form
	 * See https://github.com/minova-afis/aero.minova.cas/issues/1473
	 */
	private void addOptionPage(Document form, Document op, Map<String, String> keyMapping, Map<String, String> opAttributes) throws IllegalArgumentException {
		if (form == null || op == null)
			return;

		// Get the detail part first
		Element detail = XMLUtils.findFirstElement(form.getDocumentElement(), NODE_DETAIL);
		if (detail == null)
			throw new IllegalArgumentException("Form has no detail");

		Element opRoot = op.getDocumentElement();
		Element toAdd = null;
		if (NODE_GRID.equalsIgnoreCase(opRoot.getTagName())) {
			// Grids are integrated as-is, i.e. added at the end of detail
			toAdd = (Element)form.importNode(opRoot, true);

		} else if (NODE_FORM.equalsIgnoreCase(opRoot.getTagName())) {
			// <form><detail><page>... or <form><detail><head> are converted to <optionpage>

			NodeList opRootChildren = opRoot.getElementsByTagName(NODE_DETAIL);
			if (opRootChildren.getLength() == 0 || !(opRootChildren.item(0) instanceof Element))
				throw new IllegalArgumentException("Option page has no detail");

			Element opDetail = (Element) opRootChildren.item(0);
			Node opDetailChildNode = opDetail.getFirstChild();
			while (opDetailChildNode != null && !(opDetailChildNode instanceof Element))
				opDetailChildNode = opDetailChildNode.getNextSibling();

			if (!(opDetailChildNode instanceof Element))
				throw new IllegalArgumentException("Option page's detail has a wrong child");

			Element headOrPage = (Element) opDetailChildNode;
			if (!NODE_PAGE.equalsIgnoreCase(headOrPage.getTagName()) && !NODE_HEAD.equalsIgnoreCase(headOrPage.getTagName())) {
				throw new IllegalArgumentException("Option page's detail must either contain head or page child");
			}

			// Create <optionpage>
			Element optionPage = form.createElement(NODE_OPTIONPAGE);

			// Copy child content from page/head into optionpage
			NodeList children = headOrPage.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node imported = form.importNode(children.item(i), true);
				optionPage.appendChild(imported);
			}

			// Copy image and text from form
			optionPage.setAttribute(ATTR_ICON, opRoot.getAttribute(ATTR_ICON));
			optionPage.setAttribute(ATTR_TEXT, opRoot.getAttribute(ATTR_TITLE));

			NamedNodeMap toCopy = opDetail.getAttributes();
			for (int i = 0; i < toCopy.getLength(); i++) {
				Node attr = toCopy.item(i);
				// Do not take-over the ID if its equal to "Detail" (same as the default main detail-id)
				if(ATTR_ID.equalsIgnoreCase(attr.getNodeName()) && "detail".equalsIgnoreCase(attr.getNodeValue()))
					continue;
				optionPage.setAttribute(attr.getNodeName(), attr.getNodeValue());
			}

			// Also transfer attributes from page/head itself if present
			NamedNodeMap sourceAttrs = headOrPage.getAttributes();
			for (int i = 0; i < sourceAttrs.getLength(); i++) {
				Node attr = sourceAttrs.item(i);
				optionPage.setAttribute(attr.getNodeName(), attr.getNodeValue());
			}

			// Integrate events as last child of <optionpage>
			opRootChildren = opRoot.getElementsByTagName(NODE_EVENTS);
			if (opRootChildren.getLength() > 0 && opRootChildren.item(0) instanceof Element) {
				Element events = (Element) opRootChildren.item(0);
				Node importedEvents = form.importNode(events, true);
				optionPage.appendChild(importedEvents);
			}

			toAdd = optionPage;
			
		} else {
			throw new IllegalArgumentException("Unsupported option page type: " + opRoot.getTagName());
		}
		// Apply attributes
		applyAttributes(form, toAdd, opAttributes);
		// Now set the key bindings with map-to attribute
		addKeyMappings(detail, toAdd, keyMapping);
		// Add grid or optionpage
		detail.appendChild(toAdd);
	}
	
	/** The option page may also have attributes set in the Registry, such as "visible" and "visible-when"
	 * It *-when attributes are converted into the corresponding dynamic-blocks
	 * @param detail
	 * @param toAdd
	 * @param opAttributes
	 * @throws IllegalArgumentException
	 */
	private void applyAttributes(Document formDoc, Element toAdd, Map<String, String> opAttributes) throws IllegalArgumentException {
		if(opAttributes == null || formDoc == null || toAdd == null)
			return;
		for(Entry<String, String> entry : opAttributes.entrySet()) {
			String key = entry.getKey();
			if(XBS_VAL_VISIBLE.equalsIgnoreCase(key)) {
				toAdd.setAttribute(ATTR_VISIBLE, entry.getValue());
			} else if(key.toLowerCase().endsWith("-when")) {
				// Generate dynamics block: <dynamic property="visible">!app.isSUMode()</dynamic>
				String property = key.substring(0, key.length() - "-when".length());
				Element dynamic = formDoc.createElement(NODE_DYNAMIC);
				dynamic.setAttribute(ATTR_PROPERTY, property);
				dynamic.setTextContent(entry.getValue());
				if(toAdd.getFirstChild() != null) {
					toAdd.insertBefore(dynamic, toAdd.getFirstChild());
				} else {
					toAdd.appendChild(dynamic);
				}
			} else if(XBS_VAL_PRIORITY.equalsIgnoreCase(key)) {
				// Do nothing, handled already
			} else {
				customLogger.filesLogger.info("Unknown '" + key + "' registry attribute for option page");
			}
		}
	}
	
	/** 
	 * Adds map-to attribute to all option-page key-fields pointing to the according parent keys
	 * Supports legacy (index-based, e.g. Key0, Key1) and new parent key names (name-based, e.g. KeyLong)
	 * @param detail Form's detail node
	 * @param toAdd either grid or optionpage node
	 * @param keyMapping Key = index or name-based detail key, value = name-based op's key
	 * @throws IllegalArgumentException
	 */
	private void addKeyMappings(Element detail, Element toAdd, Map<String, String> keyMapping) throws IllegalArgumentException {
		if(keyMapping == null || detail == null || toAdd == null)
			return;
		// Now set the key bindings with map-to attribute
		for(Entry<String, String> entry : keyMapping.entrySet()) {
			String detailKey = entry.getKey();
			final String opKey = entry.getValue();
			if(detailKey.isEmpty() || opKey.isEmpty())
				throw new IllegalArgumentException("Empty key-mapping detected for " + toAdd.getTagName());
			// First find the field-value with index
			Element opField = XMLUtils.findFirstElementWithAttribute(toAdd, "name", val -> opKey.equalsIgnoreCase(val));
			if(opField == null)
				throw new IllegalArgumentException("Mapped key '"+opKey+"' not found in " + toAdd.getTagName());
			// Separate between legacy (index-based) and new-style (name-based)
			if(detailKey.toLowerCase().startsWith("key") && Character.isDigit(detailKey.charAt(detailKey.length()-1))) {
				// Legacy, by index
				int keyIndex = Integer.parseInt(detailKey.substring("key".length()));
				List<Element> detailKeys = XMLUtils.findElementWithAttribute(detail, ATTR_KEY_TYPE, val -> "primary".equalsIgnoreCase(val));
				if(keyIndex >= detailKeys.size())
					throw new IllegalArgumentException("Failed to map op-key '"+opKey+"' to a detail key #"+keyIndex+": detail has only " +detailKeys.size() + " keys");
				// Now change index-based key to name-based
				detailKey = detailKeys.get(keyIndex).getAttribute(ATTR_NAME);
			}
			// Tell the UI how the key is mapped to parent -- by name
			opField.setAttribute(ATTR_MAP_TO, detailKey);
		}
	}
}
