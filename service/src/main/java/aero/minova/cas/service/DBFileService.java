package aero.minova.cas.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.service.model.DBFile;
import aero.minova.cas.service.repository.DBFileRepository;
import jakarta.annotation.PostConstruct;

/**
 * Extends CAS with the ability to retreive files from the database (tFile table).
 * 
 * It automatically creates ZIPs on the fly and also patches Forms/OPs to enable WFC/FreeTables visualization of v11 Modules.
 * 
 * Since we use binary values and MD5 calculation, we currently must access the connection directly...
 */
@Service
public class DBFileService {
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	CustomLogger customLogger;
	
	private Boolean dbFilesAvailable = null;
	
	@org.springframework.beans.factory.annotation.Value("${dbfiles.default.app:sis}")
	String defaultAppShortName;
	
	@org.springframework.beans.factory.annotation.Value("${dbfiles.active:true}")
	boolean isDBFilesActive;
	
	@org.springframework.beans.factory.annotation.Value("${dbfiles.patch.active:true}")
	boolean isWFCPatchActive;
	
	private int calculateCRC32(byte[] data) {
    	if(data == null)
    		return 0;
        CRC32 crc = new CRC32();
        crc.update(data);
        return (int)crc.getValue();
	}
	
	private byte[] calculateMD5(byte[] content) {
		if(content == null)
			return null;
	    try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        return md.digest(content);
	    } catch (NoSuchAlgorithmException e) {
	        customLogger.logError("MD5 algorithm not available", e);
	        return null;
	    }
	}
	
	/**
	 * Create Zip-File. If no entries are given an empty zip-file is created
	 * @param files
	 * @return
	 */
	private byte[] createZip(List<DBFile> files, Function<DBFile, byte[]> contentMapper) {
		if(contentMapper == null)
			contentMapper = (f -> (f.getValue() != null ? f.getValue() : f.getDefaultValue()));
	    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
	         ZipOutputStream zos = new ZipOutputStream(baos)) {

	        for (DBFile file : files) {
	            byte[] content = contentMapper.apply(file);
	            if (content == null)
	            	continue;
	            ZipEntry entry = new ZipEntry(file.getKeyText());
	            entry.setTime(0); // Otherwise current time will be used and ZIPs with same ocntent will become different MD5/CRCs
	            zos.putNextEntry(entry);
	            zos.write(content);
	            zos.closeEntry();
	        }

	        zos.finish();
	        return baos.toByteArray();
	    } catch (Exception e) {
	        customLogger.logError("Failed to create ZIP for files with infix", e);
	        return null;
	    }
	}
	
	/**
	 * Read out files with given infix in the path-name and push the zip back to the DB.
	 * These ZIP files are CAS/WFC specific, so CAS should just make what it needs from what is there
	 * @param infix
	 * @param path
	 */
	private void createZipAndSave(String infix, String path, String zipSubDir, Function<DBFile, byte[]> contentMapper) {
		if(zipSubDir == null) 
			zipSubDir = infix;
		long tic = System.currentTimeMillis();
		List<DBFile> files = getDbFileRepository().findByKeyTextContainingAndActiveTrue(infix);
		if(files.isEmpty())
			return;
		String appPrefix = null;
		// Cut-out prefix
		for(DBFile file : files) {
			String name = file.getKeyText();
			if(name.contains("/")) {
				if(appPrefix == null)
					appPrefix = name.substring(0, name.indexOf("/") + 1); // Memory afis/tta for virtual path of the zip
				// "xyz/internationalization/messages_de.properties" -> "messages_de.properties"
				String nameWithinZip = name.substring(name.toLowerCase().indexOf(infix.toLowerCase()) + infix.length());
				// "messages_de.properties" -> "i18n/messages_de.properties"
				nameWithinZip = zipSubDir + nameWithinZip;
				file.setKeyText(nameWithinZip);
			}
		}
		if(appPrefix == null)
			appPrefix = "";
		
		final String targetPath = (appPrefix + path).replace("//", "/"); // Just in case path already had a preceeding /
		
	    // Step 1: create Zip
	    byte[] zipData = createZip(files, contentMapper);
	    
	    // Step 2: Get existing entry or create a new one...
	    saveFile(targetPath, zipData);
	    
	    customLogger.logSetup("ZIP saved to DB as " + targetPath + " with " + files.size() + " entries in " + (System.currentTimeMillis() - tic) + "ms");

	}
	
	/** Save given bytes to a DB file
	 * @param path Path with possible path
	 * @param data
	 * @return
	 */
	DBFile saveFile(String path, byte[] data) {
	    DBFile toSave = getDbFileRepository().findFirstByKeyTextLikeAndActiveTrue(path)
	        .orElseGet(() -> {
	            DBFile newFile = new DBFile();
	            newFile.setKeyText(path);
	            newFile.setActive(true);
	            return newFile;
	        });
		
	    toSave.setDefaultValue(null); // In case it was previously set
	    toSave.setDefaultValueCRC(null);
	    toSave.setDefaultValueMD5(null);
	    
	    // Store as Value -- not as DefaultValue, that are supposed to be exactly as they are in the application definition (module)
	    toSave.setValue(data);
	    toSave.setValueCRC(calculateCRC32(data));
	    toSave.setValueMD5(calculateMD5(data));
	    
	    toSave.setLastUser("CAS");
	    toSave.setLastDate(LocalDateTime.now());

	    getDbFileRepository().save(toSave);
	    return toSave;
	}
	
	/** No autowire -- as the table may not be existent at all...
	 * @return
	 */
	private DBFileRepository getDbFileRepository() {
	    return applicationContext.getBean(DBFileRepository.class);
	}


	/** Returns file if there is any
	 * @param path
	 */
	public byte[] getFile(String path) {
	    if(!isDBFilesAvailable() || path == null)
	        return null;
	    
	    try {
	    	Optional<DBFile> found;
		    if(path.startsWith("images/")) {
		    	if(path.toLowerCase().endsWith(".ico"))
		    		path = path.substring(0, path.length() - ".ico".length()); // Remove .ico postfix
		    	// ### Image Case ###
			    // Request: "images/Site", actual DB content is "<appShortName>/images/Site/16x16.png", "...32x32.png" - we select the last one
		    	found = getDbFileRepository().findTopByKeyTextLikeAndActiveTrueOrderByKeyTextDesc("%" + path + "/%.png");		    		
	    	} else {
	    		// Default file...
		    	found = getDbFileRepository().findFirstByKeyTextLikeAndActiveTrue("%" + path);
	    	}
		    byte[] toRet = found.map(file -> file.getValue() != null ? file.getValue() : file.getDefaultValue())
		            			.orElse(null);
		    // On-the-fly patching of Forms/OPs for WFC
		    if(isWFCPatchActive && path.toLowerCase().endsWith(".xml"))
		    	return patchXMLForm(path, toRet);
		    return toRet;
	    } catch(Exception ex) {
	    	customLogger.logError("Failed to load file " + path + " from the db", ex);
	    	return null;
	    }
	}
	
	public byte[] getMD5(String path) {
	    if (!isDBFilesAvailable() || path == null)
	        return null;

	    try {
	    	// On-the-fly patching of Forms/OPs for WFC
	    	if(isWFCPatchActive && path.toLowerCase().endsWith(".xml"))
	    		return calculateMD5(getFile(path));
	        return getDbFileRepository().findFirstMD5OnlyByFileName(path)
	            .map(view -> {
	                byte[] valueMD5 = view.getValueMD5();
	                byte[] defaultValueMD5 = view.getDefaultValueMD5();
	                return (valueMD5 != null) ? valueMD5 : defaultValueMD5;
	            })
	            .orElse(null);
	    } catch(Exception ex) {
	        customLogger.logError("Failed to load MD5 for file " + path + " from the db", ex);
	        return null;
	    }
	}
	
	/** Prepare WFC-specific zip-files using the single files in the tFile:
	 * - reports.zip
	 * - images.zip
	 * - plugins.zip
	 * - i18n.zip with automatically added WFC specific messages
	 */
	private void initDBFiles() {
		if(isWFCPatchActive) {
			// Patch XBS (Option-Page entries)
			byte[] xbs = getFile("application.xbs");
			if(xbs != null) try {
				xbs = patchXBS(xbs);
				saveFile("application.xbs", xbs);
				customLogger.logSetup("XBS patched");
			} catch(Exception ex) {
				customLogger.logError("Failed to integrate patch XBS", ex);
			}
		}
		
		// Create ZIPs for reports, images, plugins and i18n
		createZipAndSave("/images/", "images.zip", "images/", null);
		createZipAndSave("/reports/", "reports.zip", "reports/", null);
		createZipAndSave("/plugins/", "plugins.zip", "plugins/", null);
		// We extend properties with WFC-specific values...
		createZipAndSave("/internationalization/", "i18n.zip", "i18n/", (f) -> {
			// Extend properties with WFC-specific messages...
			byte[] data = (f.getValue() != null ? f.getValue() : f.getDefaultValue());
			try {
				String fileName = f.getKeyText();
				if(fileName.contains("/")) // ".../internationalization/messages_de.properties" -> "messages_de.properties"
					fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
				byte[] casData = DBFileService.class.getResourceAsStream(fileName).readAllBytes();
				if (casData != null) {
					Properties a = new Properties();
					a.load(new ByteArrayInputStream(data));
					Properties b = new Properties();
					b.load(new ByteArrayInputStream(casData));
					a.putAll(b);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					a.store(baos, "CAS extended");
					data = baos.toByteArray();
				}
			} catch (Exception ex) {
				// We are not too harsh with these logs, as many languages dont exist for CAS
				customLogger.logSetup("Failed to integrate CAS messages to " + f.getKeyText() + " " + ex.getMessage());
			}
			return data;
		});
	}
	
	private boolean isDBFilesAvailable() {
		if(dbFilesAvailable != null)
			return dbFilesAvailable.booleanValue();
		
	    setup(); // Should never land here, but maybe later we remove postcontruct from setup
	    
		return dbFilesAvailable.booleanValue();
	}
	
	/**
	 * Modify XBS to be compatible with WFC
	 */
	protected byte[] patchXBS(byte[] xbs) {
		try {
			Document doc = XMLUtils.getDocument(xbs);
			for (Element el : XMLUtils.findElementWithAttribute(doc.getDocumentElement(), "name", (v) -> (v != null && v.equalsIgnoreCase("OptionPages")), null)) {
				String formFile = ((Element) el.getParentNode()).getAttribute("name");
				byte[] fiForm = getFile(formFile);

				for (Element op : XMLUtils.toArray(el.getChildNodes())) { // node
					String opFile = op.getAttribute("name");
					byte[] fiOp = getFile(opFile);

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
												customLogger.logInfo("No primary keys defined within " + opFile + ". Auto check for Key " + opKey + "not possible");
											} else {
												customLogger.logInfo(opKey + " not found in " + opFile + " -> auto change to " + keys.get(0).getAttribute("name"));
												opKey = keys.get(0).getAttribute("name");
											}
										}
									} else {
										customLogger.logInfo(opFile + " not found. Auto-fixing keys not possible");
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
											customLogger.logInfo(formFile + " does not have #" + keyNr + " key");
										} else {
											formKey = keys.get(keyNr).getAttribute("name");
										}
									} else {
										customLogger.logInfo(formFile + " not found. Auto-fixing keys not possible");
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
			return XMLUtils.toBytes(doc);
		} catch (Exception ex) {
			customLogger.logError("Failed to patch XBS", ex);
			return xbs;
		}
	}
	
	/**
	 * Automatically update forms to be compatible with WFC
	 * 1. Icon entries: remove .ico postfix
	 * 2. Set procedure prefix to "sp"/"op" if not defined at all
	 */
	protected byte[] patchXMLForm(String fileName, byte[] data) {
		try {
			boolean isOptionPage = (fileName != null && fileName.toLowerCase().endsWith(".op.xml"));
			Document doc = XMLUtils.getDocument(data);
			if(doc == null)
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
			customLogger.logError("Failed to patch form", ex);
		}
		return data;
	}
	
	@PostConstruct
	private void setup() {
		try {
	        // Einfach mal testen, ob das Repository funktioniert:
	        getDbFileRepository().count(); // Ein harmloser, schneller Aufruf
	        dbFilesAvailable = true;
		    initDBFiles();
	    } catch (Exception e) {
	    	customLogger.logSetup("DBFileService inactive: tFile table not found.");
	    	customLogger.logError("DBFileService setup failed", e);
	        dbFilesAvailable = false;
	    }
	}
}