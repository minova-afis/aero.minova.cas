package aero.minova.cas;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ch.minova.foundation.rest.db.model.RegistryEntry;
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

	/**
	 * Inject all defined option pages and FORM into the basic form
	 * @param path
	 * @param toRet
	 * @return
	 */
	public byte[] resolveXMLForm(String path, byte[] form) {
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
			RegistryNode ops = registryService.getSubtree((path.startsWith("/") ? "" : "/") + path + "/OptionPages/");
			for(RegistryNode op : ops.getChildren().values()) try {
				byte[] opCode = dbFileService.getFile(op.getName());
				if(opCode == null) {
					customLogger.logError("Failed to add " + op.getName() + " option page to " + path + ": option page doesn't exist in tFile");
					continue;
				}
				Document opDoc = XMLUtils.getDocument(form);
				addOptionPage(formDoc, opDoc);
			} catch(Exception ex1) {
				customLogger.logError("Failed to add " + op.getName() + " option page to " + path, ex1);				
			}
		} catch (Exception ex) {
			customLogger.logError("Failed to resolve XML form " + path, ex);
		}
	
		
		// Add to cache
		if(toRet != null && path != null) {
			synchronized(CACHE) {
				CACHE.put(path.toLowerCase(), new CacheEntry(initialHash, toRet));
			}
		}
		return null;
	}
	
	/**
	 * Integrate option page into form
	 * See https://github.com/minova-afis/aero.minova.cas/issues/1473
	 */
	private void addOptionPage(Document form, Document op) {
		// Grids are integrated as-is, i.e. added at the end detail
		
		// <form><detail><page>... or <form><detail><head> are converted to <optionpage>
		
		
	}
}
