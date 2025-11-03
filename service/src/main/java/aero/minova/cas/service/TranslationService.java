package aero.minova.cas.service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import aero.minova.cas.CustomLogger;
import jakarta.annotation.PostConstruct;

@Service
public class TranslationService {
	@Autowired
	DBFileService dbFileService;
	
	@Autowired
	public CustomLogger customLogger;
	
	private Map<String, String> cachedTranslations = new HashMap<>();
	
	/** Translate XML to the given language
	 * @param xml
	 * @param lang
	 * @return
	 */
	public byte[] translateXML(String fileName, byte[] xml, String lang) {
		if(lang == null || lang.isEmpty())
			return xml; // No translation needed
		// 1. Open XML
		// 2. Search all text and title attributes, that start with "@"-3 T
		// 3. Replace attributes according to language
		// 4. return the modified XML
		try {
			Locale locale = new Locale(lang);
			Document doc = XMLUtils.getDocument(xml);
			for(String attr : new String[] {"text", "title", "unit-text"}) {
				if(cachedTranslations.isEmpty())
					reloadTranslations(); // Just in case the translation file was not loaded yet
				// FIXME when reload? Automatically every now and then?
				
				for (Element el : XMLUtils.findElementWithAttribute(doc.getDocumentElement(), attr, (v) -> v != null && v.startsWith("@"))) {
					String key = el.getAttribute(attr); // Remove leading @
					String value = translate(key, locale);
					if (value != null)
						el.setAttribute(attr, value);
				}
			}
			return XMLUtils.toBytes(doc);
		} catch (Exception ex) {
			customLogger.userLogger.warn("TranslationService: Failed to translate " + fileName, ex);
		}
		return xml;
	}
	
	public String translate(String key, Locale locale) {
		if(key == null)
			return null;
		while(key.startsWith("@"))
			key = key.substring(1);
		
		String keylc = key.toLowerCase();
		
		// 1. Resolve references...
		for(int maxDepth = 0; maxDepth < 10; maxDepth++) {
			String value = cachedTranslations.get(keylc);
			if(value == null || !value.startsWith("->"))
				break;
			keylc = value.substring(2);
		}
		
		// Search schemata
		// country-lang -> lang -> default
		String toRet = null;
		if(locale != null) {
			String country = locale.getCountry();
			String lang = locale.getLanguage();
			if(lang != null && !lang.isEmpty()) {
				if(country != null && !country.isEmpty()) {
					// Country + lang
					toRet = cachedTranslations.get((key + "_" + country + "-" + lang).toLowerCase());
				}
				if(toRet == null) {
					// lang
					toRet = cachedTranslations.get((key + "_" + lang).toLowerCase());
				}
			}	
		}
		// Default
		if(toRet == null) {
			toRet = cachedTranslations.get(keylc);
		}
		return (toRet == null ? key : toRet);
	}
	
	@PostConstruct
	private void setup() {
		try {
	        reloadTranslations();
	    } catch (Exception e) {
	    	customLogger.setupLogger.error("TranslationService: failed to initialize", e);
	    }
	}
	
	public void reloadTranslations() {
		// 1. Load messages2 XML
		byte[] file = dbFileService.getFile("messages2.xml");
		if(file == null)
			return; // Error was already logged
		
		try {
			Document doc = XMLUtils.getDocument(file);
			Map<String, String> newCache = new HashMap<>();
			// 2. Search for property-Tags (has name-attribute and default+value children). For every child:
			//   2.a. create map entry with: "name_lang = value" or "name = value" for default
			for(Element el : XMLUtils.findElement(doc.getDocumentElement(), "property")) {
				String name = el.getAttribute("name");
				if(name == null || name.isEmpty())
					continue;
				for(Element ch : XMLUtils.toArray(el.getChildNodes())) {
					String content = ch.getAttribute("content");
					if(content == null || content.isEmpty())
						continue;
					if(ch.getTagName().equals("default")) {
						newCache.put(name.toLowerCase(), content);
					} else if(ch.getTagName().equals("value")) {
						String country = ch.getAttribute("country");
						String lang = ch.getAttribute("lang");
						newCache.put((name + "_" + (country == null || country.isEmpty() ? "" : country+"-") + lang).toLowerCase(), content);
					}
				}
			}
			
			// 3. Search for ref-property-Tags (name and refvalue attribute) and create map entry with "name = '->' + refvalue"
			for(Element el : XMLUtils.findElement(doc.getDocumentElement(), "ref-property")) {
				String name = el.getAttribute("name");
				String ref = el.getAttribute("refvalue");
				if(name == null || name.isEmpty() || ref == null || ref.isEmpty())
					continue;
				newCache.put(name.toLowerCase(), "->"+ref.toLowerCase());
			}
			cachedTranslations = newCache;
		} catch (Exception ex) {
			customLogger.userLogger.warn("TranslationService: Failed to load messages2.xml from DB", ex);
		}
	}
}
