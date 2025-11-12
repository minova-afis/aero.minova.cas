package aero.minova.cas.service;

import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.service.model.RegistryEntry;
import aero.minova.cas.service.repository.RegistryRepository;
import jakarta.annotation.PostConstruct;

/**
 * Extends CAS with the ability to retreive registry (XBS) values from the database (tRegistry table)
 * 
 */
@Service
public class RegistryService {
	/**
	 * If a xbs-file name is given instead of app-prefix, it will be auto-derived
	 * @param appPrefix
	 * @return
	 */
	public static String autoCorrectAppPrefix(String appPrefix) {
		if(appPrefix == null)
			return null;
		// Auto correct file-name to prefix
		appPrefix = appPrefix.trim().toLowerCase();
		if(appPrefix.endsWith(".xbs")) {
			// "dir/afis.xbs" -> "dir/afis"
			appPrefix = appPrefix.substring(0, appPrefix.length() - ".xbs".length());
			// "dir/afis" -> "afis"
			if(appPrefix.contains("/"))
				appPrefix = appPrefix.substring(appPrefix.lastIndexOf("/") + 1);
		}
		
		while(appPrefix.startsWith("/"))
			appPrefix = appPrefix.substring(1);
		while(appPrefix.endsWith("/"))
			appPrefix = appPrefix.substring(0, appPrefix.length() - 1);
		
		if("application".equals(appPrefix))
			return null;
		
		return appPrefix;
	}
	
	/** True if this service can handle XBS requests for the given file name,
	 * i.e. auto-generate the XBS from tRegistry
	 * @param fileName
	 * @return
	 */
	public static boolean canHandleXBSRequest(String fileName) {
		if(fileName == null)
			return false;
		fileName = fileName.trim().toLowerCase();
		return (fileName.endsWith("application.xbs") ||
				fileName.endsWith("afis.xbs") ||
				fileName.endsWith("dispo.xbs") ||
				fileName.endsWith("sis.xbs") ||
				fileName.endsWith("tta.xbs"));
	}
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	CustomLogger customLogger;
	
	private Boolean dbRegistryAvailable = null;
	
	@org.springframework.beans.factory.annotation.Value("${dbregistry.active:true}")
	boolean isDBRegistryActive;

	
	/** Get value with key (e.g. "/JobExecutor/ServiceHost" to match "/afis/JobExecutor/ServiceHost" or "/dispo/JobExecutor/ServiceHost")
	 */
	public String find(String key, String defaultValue) {
	    if(!isRegistryAvailable() || key == null)
	        return defaultValue;
	    
	    try {
	    	Optional<RegistryEntry> found = getRegistryRepository().findFirstByKeyTextLikeAndActiveTrue("%" + key);
		    String toRet = found.map(file -> file.getValue() != null ? file.getValue() : file.getDefaultValue())
        			            .orElse(defaultValue);
		    return (toRet == null ? defaultValue : toRet);
	    } catch(Exception ex) {
	    	customLogger.logError("Failed to load tRegistry value for key %" + key + "", ex);
	    	return defaultValue;
	    }
	}
	
	/**
	 * Read-in tRegistry values and generate XBS code. Should only be used for legacy code that requires XBS.
	 * Otherwise, use get()/find() methods.
	 * @param appPrefix Application prefix to filter (e.g. "afis" or "dispo"). If left empty/null all entries are mixed into
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws TransformerException 
	 */
	public byte[] getXBSCode(String appPrefix) throws TransformerException, ParserConfigurationException {
		// Check param
		appPrefix = autoCorrectAppPrefix(appPrefix);

		List<RegistryEntry> entries = (appPrefix == null || appPrefix.trim().isEmpty()
												? listAllEntries()
												: listEntries(appPrefix + "/"));
		if(entries == null || entries.isEmpty())
			return null;

        /** XBS Example:
		 * <?xml version="1.0" encoding="UTF-8" standalone="no"?>
		 * <preferences>
		 *    <root>
		 *      <map/>
		 *       <node name="minova">
	 	 *          <map/>
		 *          <node filename="TankTerminalAutomation.xbs" name="tankterminalautomation">
		 *             <map>
		 *                <entry key="siteaddress2" value="Kaiser-Wilhelm-Straße 135"/>
		 *                <entry key="siteaddress3" value="D-12247 Berlin"/>
		 *                <entry key="sitefax" value="+49 (30) 779982-25"/>
		 *                <entry key="sitephone" value="+49 (30) 779982-12"/>
		 *                <entry key="Visualization" value="otdBER_visu.xml"/>
		 *             </map>
		 *             <node name="Additive.xml">
		 *                <map/>
		 *                <node name="OptionPages">
		 *                   <map/>
		 *                   <node name="AdditiveRebooking.op.xml">
		 *                      <map>
		 *                         <entry key="key0" value="KeyLong"/>
		 *                      </map>
		 *                   </node>
		 *                </node>
		 *             </node>
		 *          </node>
		 *       </node>
		 *    </root>
		 * </preferences>
         * 
         */
        DocumentBuilder b = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = b.newDocument();
        Element root = (Element)doc.appendChild(doc.createElement("preferences"));
        root = (Element)root.appendChild(doc.createElement("root"));
        root = (Element)root.appendChild(doc.createElement("node"));
        root.setAttribute("name", "minova");
        
		for(RegistryEntry re : entries) {
			String path = re.getKeyText();
			// ##### Check and correct ####
			if(path == null || path.trim().isEmpty())
				continue;
			// "/afis/JobExecutor/ServiceHost" -> "afis/JobExecutor/ServiceHost"
			while(path.startsWith("/"))
				path = path.substring(1);
			// Expect "appname/..." -- otherwise skip
			int at = path.indexOf("/");
			if(at == -1)
				continue;
			
			// #### Lets go ####
			// Cut out the app-name: "afis/JobExecutor/ServiceHost" -> "JobExecutor/ServiceHost"
			path = path.substring(at + 1).trim();
			// Auto create nodes
			Element addInto = root;
			while((at = path.indexOf("/")) != -1) {
				String nodeName = path.substring(0, at);
				// Check if node already exists
				Element nextNode = null;
				for(Element el : XMLUtils.toArray(addInto.getChildNodes())) {
					if(el.getNodeName().equals("node") && nodeName.equalsIgnoreCase(el.getAttribute("name"))) {
						nextNode = el;
						break;
					}
				}
				// Not found -> create
				if(nextNode == null) {
					nextNode = (Element)addInto.appendChild(doc.createElement("node"));
					nextNode.setAttribute("name", nodeName);
				}
				// Next
				addInto = nextNode;
				path = path.substring(at + 1).trim();
			}
			// Finally add entry
			if(path.isEmpty())
				continue; // we land here if only the node was defined, e.g. "/afis/JobExecutor/"
			
			// Create map-entry if not existent
			Element map = null;
			for(Element el : XMLUtils.toArray(addInto.getChildNodes())) {
				if(el.getNodeName().equals("map")) {
					map = el;
					break;
				}
			}
			if(map == null)
				map = (Element)addInto.appendChild(doc.createElement("map"));
			Element entry = (Element)map.appendChild(doc.createElement("entry"));
			entry.setAttribute("key", path);
			if(re.getValue() != null || re.getDefaultValue() != null)
				entry.setAttribute("value", re.getValue() != null ? re.getValue() : re.getDefaultValue());
		}
		return XMLUtils.toBytes(doc);
	}
	
	/** Get value with exact key (e.g. "/afis/JobExecutor/ServiceHost")
	 */
	public String get(String key) {
		return get(key, null);
	}

	/** Get value with exact key (e.g. "/afis/JobExecutor/ServiceHost")
	 */
	public String get(String key, String defaultValue) {
	    if(!isRegistryAvailable() || key == null)
	        return defaultValue;
	    
	    try {
	    	Optional<RegistryEntry> found = getRegistryRepository().findFirstByKeyTextAndActiveTrue(key);
		    String toRet = found.map(file -> file.getValue() != null ? file.getValue() : file.getDefaultValue())
        			            .orElse(defaultValue);
		    return (toRet == null ? defaultValue : toRet);
	    } catch(Exception ex) {
	    	customLogger.logError("Failed to load tRegistry value for key " + key + "", ex);
	    	return defaultValue;
	    }
	}
	
	/** No autowire -- as the table may not be existent at all...
	 * @return
	 */
	private RegistryRepository getRegistryRepository() {
	    return applicationContext.getBean(RegistryRepository.class);
	}
	
	private boolean isRegistryAvailable() {
		if(dbRegistryAvailable != null)
			return dbRegistryAvailable.booleanValue();
		
	    setup(); // Should never land here, but maybe later we remove postcontruct from setup
	    
		return dbRegistryAvailable.booleanValue();
	}
	
	/** List all active registry entries as keys
	 */
	public List<String> list(String prefix) {
	    if(!isRegistryAvailable())
	        return null;
	    
	    try {
	    	if(prefix == null || prefix.trim().isEmpty()) {
	    		return  getRegistryRepository().findAllByActiveTrue()
	    				.stream()
	    				.map(entry -> entry.getValue() != null ? entry.getValue() : entry.getDefaultValue())
	    				.toList();
	    	} else {
	    		if(!prefix.endsWith("/"))
	    			prefix = prefix + "/"; // We want to match only children
	    		return  getRegistryRepository().findByKeyTextStartingWithAndActiveTrue(prefix)
	    				.stream()
	    				.map(entry -> entry.getValue() != null ? entry.getValue() : entry.getDefaultValue())
	    				.toList();
	    	}
	    } catch(Exception ex) {
	    	customLogger.logError("Failed to list tRegistry values"+ (prefix == null || prefix.trim().isEmpty() ? "" : " for key " + prefix), ex);
	    	return null;
	    }
	}
	
	/** List all active registry entries as keys
	 */
	public List<String> listAll() {
	    return list(null);
	}
	
	/** List all active registry entries
	 */
	public List<RegistryEntry> listAllEntries() {
	    return listEntries(null);
	}
	
	/** List all active registry entries
	 */
	public List<RegistryEntry> listEntries(String prefix) {
	    if(!isRegistryAvailable())
	        return null;
	    
	    try {
	    	if(prefix == null || prefix.trim().isEmpty()) {
	    		return getRegistryRepository().findAllByActiveTrue();
	    	} else {
	    		if(!prefix.endsWith("/"))
	    			prefix = prefix + "/"; // We want to match only children
	    		return getRegistryRepository().findByKeyTextStartingWithAndActiveTrue(prefix);
	    	}
	    } catch(Exception ex) {
	    	customLogger.logError("Failed to list tRegistry values"+ (prefix == null || prefix.trim().isEmpty() ? "" : " for key " + prefix), ex);
	    	return null;
	    }
	}
	
	@PostConstruct
	private void setup() {
		try {
	        // Just test if the table is available
	        getRegistryRepository().count(); // Simple and fast query
	        dbRegistryAvailable = true;
		    // Do some init if required later
	    } catch (Exception e) {
	    	customLogger.logSetup("DB-Registry inactive: tRegistry table not found.");
	    	customLogger.logError("DB-Registry setup failed", e);
	        dbRegistryAvailable = false;
	    }
	}
}