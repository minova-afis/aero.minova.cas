package aero.minova.cas.controller;

import static java.nio.file.Files.readAllBytes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.cas.ContentPatcher;
import aero.minova.cas.CustomLogger;
import aero.minova.cas.service.FilesService;
import ch.minova.foundation.db.NextGen;
import ch.minova.foundation.rest.db.service.FileService;
import ch.minova.foundation.rest.db.service.RegistryService;
import ch.minova.foundation.rest.db.service.TranslationService;
import lombok.val;

@RestController
// benötigt, damit JUnit-Tests nicht abbrechen
@ConditionalOnProperty(prefix = "application.runner", value = "enabled", havingValue = "true", matchIfMissing = true)
public class FilesController {
	@Autowired
	FileService dbFileService;
	
	@Autowired
	RegistryService registryService;
	
	@Autowired
	TranslationService translationService;
	
	@Autowired
	FilesService fileService;

	@Autowired
	public CustomLogger customLogger;

	@Autowired
	SqlProcedureController spc;
	
	@Autowired
	ContentPatcher contentPatcher;

	@org.springframework.beans.factory.annotation.Value("${generate.mdi.per.user:true}")
	boolean generateMDIPerUser;

	@org.springframework.beans.factory.annotation.Value("${fat.jar.mode:false}")
	boolean isFatJarMode;
	
	// Is set to false in MCAS mode, as the integrated (demo) XBS would lead to wrong application name detection
	@org.springframework.beans.factory.annotation.Value("${readPackedXBS:true}")
	boolean readPackedXBS;
	
    @Value("${application:#{null}}")
    private String application;
	
	@org.springframework.beans.factory.annotation.Value("${ng.api.dbfiles:true}")
	boolean isDBFilesActive;

	@org.springframework.beans.factory.annotation.Value("${ng.api.dbfiles.patch.active:true}")
	boolean isWFCPatchActive;
	
	@org.springframework.beans.factory.annotation.Value("${ng.api.dbfiles.resolveforms:false}")
	boolean isResolveFormsActive;
	
	@org.springframework.beans.factory.annotation.Value("${ng.api.dbregistry:false}")
	boolean isDBRegistryActive;
	
	@org.springframework.beans.factory.annotation.Value("${ng.api.preferdbfiles:false}")
	boolean isDBFilesPreferred;
	
	/** Calculate MD5
	 * @param content
	 * @return
	 */
	private byte[] calculateMD5(byte[] content) {
		if (content == null)
			return null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return md.digest(content);
		} catch (NoSuchAlgorithmException e) {
			customLogger.logError("MD5 algorithm not available", e);
			return null;
		}
	}
	
	/** Get requested file from the DB and
	 * 1. patch XML forms it for WFC
	 * 2. Add option pages directly into forms 
	 * 3. trananslate forms and mdi if language is specified
	 * @param path
	 * @param lang
	 * @return
	 */
	byte[] getFileFromTable(String path, String lang) {
		if(path == null)
			return null;
		byte[] toRet = null;
		
		// E.g. if "Driver.xml" is asked -> request "afis/Driver.xml"
		if(application != null && !application.isEmpty() &&
			!path.toLowerCase().startsWith(application.toLowerCase()) &&
			!path.toLowerCase().startsWith("/" + application.toLowerCase())) {
			toRet = dbFileService.getFile(application + (path.startsWith("/") ? "" : "/") + path);
			if(toRet == null) {
				// Fallback
				toRet = dbFileService.getFile(path);
			}
		} else {
			toRet = dbFileService.getFile(path);
		}
		
		// On-the-fly patching of Forms/OPs for WFC
		if (isWFCPatchActive && path.toLowerCase().endsWith(".xml"))
			toRet = contentPatcher.patchXMLForm(path, toRet);
		
		// On the fly inclusion of option-pages and forms -- the UI should get a finished definition
		// https://github.com/minova-afis/aero.minova.cas/issues/1473
		if(isResolveFormsActive && application != null && !application.isEmpty() && path.toLowerCase().endsWith(".xml"))
			toRet = contentPatcher.resolveXMLForm(application, path, toRet);
		
		// Try to translate if XML or MDI -- if no language is given, no translation will be done
		if(path.toLowerCase().endsWith(".xml") || path.toLowerCase().endsWith(".mdi"))
			toRet = translationService.translateXML(path, toRet, lang);
		
		// On-the-fly XBS patching
		if (isWFCPatchActive && path.toLowerCase().endsWith("xbs"))
			toRet = contentPatcher.patchXBS(path, toRet);
		
		return toRet;
	}
	
	public byte[] getMD5FromTable(String path, String lang) {
		if(path == null)
			return null;
		// On-the-fly patching of Forms/OPs for WFC
		if (isWFCPatchActive && path.toLowerCase().endsWith(".xml"))
			return calculateMD5(getFileFromTable(path, lang));
		return dbFileService.getMD5(path);
	}
			
	/**
	 * Verarbeitet User-Anfragen zum Senden eines Files. Falls das angefragte File gefunden werden kann, wird es zurückgegeben, andernfalls wird entweder eine
	 * FileNotFoundException oder eine IllegalAccessException geworfen.
	 *
	 * @param path
	 *            Der Pfad des Files, welches der User anfragt, als String.
	 * @return byte[] des angefragten Files.
	 * @throws Exception
	 *             Entweder eine IllegalAccessException, falls der Pfad außerhalb des System-Ordners liegt, oder FileNotFoundException, falls es keine Datei mit
	 *             diesem Namen in dem gewünschten Pfad gibt.
	 */
	@RequestMapping(value = "files/read", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public @ResponseBody byte[] getFile(@RequestParam String path, @RequestParam(required = false) String lang) throws Exception {
		// Should we generate XBS from tRegistry?
		if (isDBRegistryActive && RegistryService.canHandleXBSRequest(path)) {
			return getXBSFromRegistry(path);
		}

		// Are Files from DB preferered compared to File System?
		if (isDBFilesActive && isDBFilesPreferred) {
 			byte[] toRet = getFileFromTable(path, lang);
			if(toRet != null)
				return toRet;
		}
		
		// Zuerst prüfen, ob application.mdi aus Datenbank gelesen werden soll
		if (generateMDIPerUser && path.contains("application.mdi")) {
			// Falls es beim Auslesen der Mdi zu einem Fehler kommt, wird stattdessen eine StandardMdi aus dem Root-Path zurückgegeben.
			try {
				return fileService.readMDI();
			} catch (Exception e) {
				customLogger.logError("Mdi could not be read. It will be loaded from the system file path.", e);
			}
		}
		
		// Are Files from DB active but not preferred compared to File System?
		if(isDBFilesActive && !isDBFilesPreferred) {
 			byte[] toRet = getFileFromTable(path, lang);
			if(toRet != null)
				return toRet;
		}
		
		// Even without tRegistry we want to have the ability to change XBS entries with System.env and System.properties
		byte[] toRet = getIncludedFile(path);
		if(path.toLowerCase().endsWith(".xbs") && toRet != null) try {
			toRet = NextGen.Registry.create()
					                .withXBS(new ByteArrayInputStream(toRet))
					                .withEnv()
					                .getXBS();
		} catch(Exception ex) {
			customLogger.logError("Failed to auto-update XBS with ENV", ex);
		}
		
		return toRet;
	}
	
	/** Get file included in this CAS installation -- either within the jar or on the file system
	 * @param path
	 * @return
	 * @throws Exception
	 */
	private byte[] getIncludedFile(String path) throws Exception {
		// Bei fatJarMode Dateien aus Resourcen
		if (isFatJarMode) {
			if (!path.startsWith("/")) {
				path = "/" + path;
			}
			if (path.endsWith(".zip")) {
				return getZip(path.substring(0, path.length() - 4));
			}
			InputStream is = getClass().getResourceAsStream(path);
			if(is == null)
				throw new IOException(path + " file not included in CAS build");
			return is.readAllBytes();
		}

		// Ansonsten Dateisystem nutzen
		path = path.replace('\\', '/');

		String extension = FilenameUtils.getExtension(path);
		// Zur Abwärtskompatibilität Dateiendung überprüfen und, falls diese Zip ist, getZip aufrufen.
		if (extension.equalsIgnoreCase("zip")) {
			return getZip(path);
		}
		val inputPath = fileService.checkLegalPath(Paths.get(path));
		customLogger.logUserRequest("files/read: " + path);
		return readAllBytes(inputPath);
	}

	/**
	 * Sucht die MD5-Datei bestimmten Files im Internal/MD5-Verzeichnis und gibt diese zurück.
	 *
	 * @param path
	 *            Der Pfad des Files, zu welchem der User die MD5 Datei möchte, als String.
	 * @return Der MD5 Wert des angefragten Files als byte[].
	 * @throws Exception
	 *             Entweder eine IllegalAccessException, falls der Pfad außerhalb des System-Ordners liegt, oder FileNotFoundException, falls es keine Datei mit
	 *             diesem Namen in dem gewünschten Pfad gibt.
	 */
	@RequestMapping(value = "files/hash", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public @ResponseBody byte[] getHash(@RequestParam String path, @RequestParam(required = false) String lang) throws Exception {
		// Should we generate XBS from tRegistry?
		if (isDBRegistryActive && RegistryService.canHandleXBSRequest(path)) {
			 byte[] xbsCode = getXBSFromRegistry(path);
			 return calculateMD5(xbsCode);
		}
		
		// Try tFiles first
		if (isDBFilesActive && isDBFilesPreferred) {
			// Translated files have different contents based on language
 			if(lang != null && path.toLowerCase().endsWith(".xml") || path.toLowerCase().endsWith(".mdi")) {
 				byte[] content = getFileFromTable(path, lang);
 				return calculateMD5(content);
 			}
			byte[] toRet = dbFileService.getMD5(path);
			if(toRet != null)
				return toRet;
		}
		
		if (generateMDIPerUser && path.contains("application.mdi")) {
			// Falls es beim Auslesen der Mdi zu einem Fehler kommt, wird stattdessen eine StandardMdi aus dem Root-Path zurückgegeben.
			try {
				byte[] mdi =  fileService.readMDI();
				return calculateMD5(mdi);
			} catch (Exception e) {
				customLogger.logError("Mdi could not be read. It will be loaded from the system file path.", e);
			}
		}
		
		// Are Files from DB active but not preferred compared to File System?
		if(isDBFilesActive && !isDBFilesPreferred) {
			// Translated files have different contents based on language
 			if(lang != null && path.toLowerCase().endsWith(".xml") || path.toLowerCase().endsWith(".mdi")) {
 				byte[] content = getFileFromTable(path, lang);
 				return calculateMD5(content);
 			}
			
			byte[] toRet = dbFileService.getMD5(path);
			if(toRet != null)
				return toRet;
		}
		
		// Even in non-fatjar mode we want in-situ MD5 calculation for mdi and xbs -- as both may have dynamic content
		boolean liveMD5calc = path.toLowerCase().endsWith(".mdi") ||
				              path.toLowerCase().endsWith(".xbs");
		
		if (isFatJarMode || liveMD5calc) {
			if (!path.startsWith("/")) {
				path = "/" + path;
			}
			final byte[] pathContent;
			if (path.endsWith(".zip")) {
				pathContent = getZip(path);
			} else {
				pathContent = getFile(path, lang);
			}
			byte[] md5 = calculateMD5(pathContent);
			String fx = "%0" + (md5.length * 2) + "x";
			return String.format(fx, new BigInteger(1, md5)).getBytes(StandardCharsets.UTF_8);
		}
		path = path.replace('\\', '/');
		customLogger.logUserRequest("files/hash: " + path);
		// Wir wollen den Pfad ab dem SystemsFolder, denn dieser wird im MD5 Ordner nachgestellt.
		String toBeResolved = path + ".md5";

		Path md5FilePath;
		String extension = FilenameUtils.getExtension(path);

		// Falls man den Hash eines Zip-Files möchte, liegen diese jetzt im Internal-Ordner
		if (extension.equalsIgnoreCase("zip")) {
			md5FilePath = fileService.getMd5Folder().resolve("Internal").resolve("Zips").resolve(toBeResolved);
		} else {
			md5FilePath = fileService.getMd5Folder().resolve(toBeResolved);
		}

		fileService.checkLegalPath(md5FilePath);
		return readAllBytes(md5FilePath);
	}

	@RequestMapping(value = "files/zip", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public @ResponseBody byte[] getZip(@RequestParam String path) throws Exception {
		// Try tFiles first
		if(isDBFilesActive) {
			byte[] toRet = getFileFromTable(path, null);
			if(toRet != null)
				return toRet;
		}
		
		if (isFatJarMode) {
			final var pathStr = path.toString();
			if (!path.startsWith("/")) {
				path = "/" + path;
			}
			final List<String> matchingResources = new ArrayList<>();
			final var deployedResources = new String(getClass().getResourceAsStream("/aero.minova.app.resources/deployed.resources.txt").readAllBytes());
			for (final var resourceListPath : deployedResources.split("\n")) {
				if (resourceListPath.isBlank()) {
					continue;
				}
				final var resourceList = new String(getClass().getResourceAsStream(resourceListPath).readAllBytes());
				for (final var resource : resourceList.split("\n")) {
					if (resource.isBlank()) {
						continue;
					}
					if (resource.startsWith(pathStr) && !matchingResources.contains(resource)) {
						matchingResources.add(resource);
					}
				}
			}
			String resourcePathLog = null;
			ZipEntry ze = null;
			ByteArrayOutputStream fos = new ByteArrayOutputStream();
			try (ZipOutputStream zos = new ZipOutputStream(fos);) {

				for (String resourcePath : matchingResources) {
					resourcePathLog = resourcePath;
					if (isFileResource(resourcePath)) {
						ze = new ZipEntry(resourcePath.substring(1));

						// CreationTime der Zip und Änderungs-Zeitpunkt der Zip auf diese festen
						// Zeitpunkte setzen, da sich sonst jedes Mal der md5 Wert ändert,
						// wenn die Zip erstellt wird.
						ze.setCreationTime(FileTime.from(Instant.EPOCH));
						ze.setTime(0);
						zos.putNextEntry(ze);

						final var resourceContent = getClass().getResourceAsStream(resourcePath).readAllBytes();
						zos.write(resourceContent, 0, resourceContent.length);
						zos.closeEntry();
					}
				}
			} catch (Exception e) {
				if (ze != null) {
					customLogger.logFiles("Error while zipping file " + ze.getName());
					throw new RuntimeException("msg.ZipError %" + ze.getName());
				} else {
					// Landet nur hier, wenn es nicht mal bis in das erste if geschafft hat.
					customLogger.logFiles("Error while accessing file path for file to zip.");
					throw new RuntimeException("Error while accessing file path " + resourcePathLog + " for file to zip.", e);
				}
			} finally {
				fos.close();
			}
			return fos.toByteArray();
		}
		path = path.replace('\\', '/');
		customLogger.logUserRequest("files/zip: " + path);
		String toBeResolved = path;
		String extension = FilenameUtils.getExtension(path);

		if (!extension.equalsIgnoreCase("zip")) {
			// Wir wollen den Pfad ab dem SystemsFolder, denn dieser wird im Zips Ordner nachgestellt.
			toBeResolved = toBeResolved + ".zip";
		}
		Path zipFilePath = fileService.getZipsFolder().resolve(toBeResolved);
		fileService.checkLegalPath(zipFilePath);
		return readAllBytes(zipFilePath);
	}

	/**
	 * Die User-Anfrage zum Uploaden einer Log-Datei des Users. Der User schickt diese Datei als Zip. Die Datei wird dann im Internal/UserLogs gespeichert.
	 *
	 * @param log
	 *            Die gezippte Log-Datei als byte[].
	 * @throws IOException
	 *             Falls die Datei nicht geschrieben oder entpackt werden kann.
	 */
	@RequestMapping(value = "upload/logs", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody void getLogs(@RequestBody byte[] log) throws IOException {
		if (isFatJarMode) {
			customLogger.logUserRequest("Ignoring `upload/logs` because the fat jar mode is active and therefore not file system access is used.");
			return;
		}
		// Doppelpunkte müssen raus, da Sonderzeichen im Filenamen nicht erlaubt sind
		String logFolderName = ("Log-" + LocalDateTime.now()).replace(":", "-");
		File logFileFolder = fileService.getLogsFolder().resolve(logFolderName).toFile();
		File logPath = new File(logFileFolder + ".zip");
		logFileFolder.mkdirs();

		customLogger.logUserRequest("Storing: " + logPath);
		Files.write(logPath.toPath(), log);

		// hochgeladenes File unzippen
		customLogger.logUserRequest("Unzipping File: " + logPath);
		fileService.unzipFile(logPath, logFileFolder.toPath());
	}

	/**
	 * Erzeugt eine MD5-Datei im Internal/MD5-Directory zu der übergebenen Datei.
	 *
	 * @param p
	 *            Der Pfad der Datei, welche gehashed werden soll.
	 * @throws Exception
	 *             Falls die Datei nicht geschrieben oder gelesen werden kann.
	 */
	public void hashFile(Path p) throws Exception {
		final val filePath = fileService.checkLegalPath(p);
		byte[] md5 = calculateMD5(readAllBytes(filePath));
		String fx = "%0" + (md5.length * 2) + "x";
		byte[] hashOfFile = String.format(fx, new BigInteger(1, md5)).getBytes(StandardCharsets.UTF_8);

		// Path für die neue MD5-Datei zusammenbauen
		Path mdDataName = fileService.getMd5Folder().resolve(p);

		// Alle benötigten Ordner erstellen
		if (mdDataName.getParent().toFile().mkdirs()) {
			customLogger.logFiles("Creating directory " + mdDataName);
		}

		// erzeugt die Datei, falls sie noch nicht existiert und überschreibt sie, falls sie schon existiert
		File hashedFile = new File(mdDataName + ".md5");
		customLogger.logFiles("Hashing: " + hashedFile.getAbsolutePath());

		Files.write(Paths.get(hashedFile.getAbsolutePath()), hashOfFile);
	}
	
	/**
	 * Modern Minova Registry can be modified via:
	 * - fully compliant Spring-tech: application.properties incl. profiles, System.env, System.properties
	 * - ENV vars starting with REG_* prefix
	 * - XBS files
	 * - tREgistry table
	 * 
	 * Such that different sources are just different carriers of the Registry values
	 * 
	 * In common applications (services) Registry is created like this:
	 * Map<String, String> registry =
	 *    NextGen.Registry.create()
	 *                    .withApplicationProperties()
	 *                    .withEnv()
	 *                    .withDB(null)
	 *                    .get();
	 *                    
	 * ... CAS however is special, since the Registry it constructs here is not _for itself_ but rather
	 *     for external application users. I.e. the application.properties of CAS are defining CAS' itself and not
	 *     for example AFIS/SIS/... applications. Therefore in this function we are _not_ updating the actual
	 *     App registry with CAS' application.properties.
	 *     
	 * Beginning with 2026 the requested XBS is constructed from
	 * 1. Packaged XBS (if any)
	 * 2. ENV vars (REG_* prefixed keys)
	 * 3. tRegistry
	 * 
	 * @param path may be a requested XBS file or a app short name (afis)
	 * @return
	 * @throws RuntimeException
	 */
	private byte[] getXBSFromRegistry(String path) throws RuntimeException {
		// tRegistry can contain branches for multiple applications (tta/*, afis/*, sis/*)
		// In general the application is detected in the _packaged_ XBS (ApplicationID) -- which is not
		// feasible with µCAS, as no application specific files are included (ony basic application.xbs with CAS specific code)
		// Therefore
		// - if specific XBS was requested (AFIS.xbs) => use the name as application name
		// - if application.xbs is requested => use application
		try {
			byte[] xbs = null;
			if(readPackedXBS) {
				try {
					xbs = getIncludedFile(path);
					customLogger.logFiles("Internal XBS found for " + path + " -> update with ENV and tRegistry");
				} catch(Exception ex) {
					customLogger.logFiles("No internal XBS found for " + path + " -> construct new with ENV and tRegistry");
				}
			}
			
			// Build-up registry
			NextGen.Registry reg = NextGen.Registry.create()
							.tryWithXBS(xbs == null ? null : new ByteArrayInputStream(xbs))
			                .withEnv()
			                .with(registryService::loadInto); // We use JPA registry connector instead of the built-in MSSQL (.withDB);
			
			if(reg.get().isEmpty()) {
				String appPrefix = RegistryService.autoCorrectAppPrefix(path);
				//customLogger.logError("Failed to generate XBS from tRegistry -- no values found" + (appPrefix == null ? "" : " for Application '"+appPrefix+"'"));
				throw new RuntimeException("Failed to generate XBS -- no values found in ENV or tRegistry" + (appPrefix == null ? "" : " for Application '"+appPrefix+"'"));
			}
			return reg.getXBS();
		} catch (Exception e) {
			throw new RuntimeException("Failed to generate XBS", e);
		}
	}

	/**
	 * Hashed beim Starten des CAS alle Dateien und speichert deren MD5-Dateien im Internal/MD5-Ordner.
	 *
	 * @throws Exception
	 *             Falls die MD5-Dateien nicht geschrieben werden können.
	 */
	// je höher die Zahl bei @Order, desto später wird die Methode ausgeführt
	@EventListener(ApplicationReadyEvent.class)
	@Order(2)
	@RequestMapping(value = "files/hashAll")
	public void hashAll() throws Exception {
		if (isFatJarMode) {
			// In diesem Modus werden die Zips bei jeder Anfrage on the fly neu generiert.
			return;
		}
		List<Path> programFiles = fileService.populateFilesList(fileService.getSystemFolder());
		for (Path path : programFiles) {
			// Mit dieser If-Abfrage wird verhindert, dass es .md5-Dateiketten gibt
			if (path.startsWith(fileService.getMd5Folder())) {
				continue;
			}
			// wir wollen keine Hashes von einem Directory ( zips allerdings schon)
			if (!path.toFile().isDirectory()) {
				hashFile(fileService.getSystemFolder().toAbsolutePath().relativize(path.toAbsolutePath()));
			}

		}
	}

	/**
	 * Zipped beim Starten des CAS alle Dateien und speichert deren Zip-Dateien im Internal/Zips.
	 *
	 * @throws Exception
	 *             Falls Dateien nicht gezipped werden konnten oder der Dateipfad außerhalb des Root-Directories zeigt.
	 */
	// je niedriger die Zahl bei @Order, desto früher wird die Methode ausgeführt
	@EventListener(ApplicationReadyEvent.class)
	@Order(1)
	@RequestMapping(value = "files/zipAll")
	public void zipAll() throws Exception {
		if (isFatJarMode) {
			// In diesem Modus werden die Zips bei jeder Anfrage on the fly neu generiert.
			return;
		}
		List<Path> programFiles = fileService.populateFilesList(fileService.getSystemFolder());
		for (Path path : programFiles) {
			if (path.startsWith(fileService.getZipsFolder().getParent().toString())) {
				continue;
			}

			// XXX String fileSuffix = path.toString().substring(path.toString().lastIndexOf(".") + 1 );
			String fileSuffix = FilenameUtils.getExtension(path.toString());
			// es kann sein, dass von einem vorherigen Start bereits gezippte Dateien vorhanden sind, welche schon gehashed wurden
			if (fileSuffix.equalsIgnoreCase("md5")) {
				String filePrefix = path.toString().substring(0, path.toString().lastIndexOf("."));
				// XXX fileSuffix = path.toString().substring(filePrefix.lastIndexOf(".") + 1 );
				fileSuffix = FilenameUtils.getExtension(path.toString());
			}
			// wir wollen nicht noch einen zip von einer zip Datei, wir wollen allerdings hier NUR Directories haben
			if ((!fileSuffix.toLowerCase().contains("zip")) && (path.toFile().isDirectory())) {
				createZip(fileService.getSystemFolder().toAbsolutePath().relativize(path.toAbsolutePath()));
			}
		}
	}


	private boolean isFileResource(String resourcePath) {
		return !resourcePath.endsWith("/");
	}

	/**
	 * Erstellt eine Zip-Datei und speichert diese im Internal/Zips-Ordner.
	 *
	 * @param path
	 *            Die Datei, zu welcher eine Zip-Datei erstellt werden soll.
	 * @throws Exception
	 *             Falls gewünschte Datei außerhalb des Dateisystems liegt, sie nicht existiert oder ein Fehler beim Zippen auftritt.
	 */
	/*
	 * Vorsicht! createZip ist nicht deterministisch
	 */
	@RequestMapping(value = "files/createZip", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public void createZip(@RequestParam Path path) throws Exception {
		if (isFatJarMode) {
			// In diesem Modus werden die Zips bei jeder Anfrage on the fly neu generiert.
			return;
		}
		List<Path> fileList = fileService.populateFilesList(fileService.getSystemFolder().resolve(path));

		// Path für die neue ZIP-Datei zusammenbauen
		Path zipDataName = fileService.getZipsFolder().resolve(path);
		// Alle benötigten Ordner erstellen
		File zipDirectoryStructure = zipDataName.toFile().getParentFile();
		if (zipDirectoryStructure.mkdirs()) {
			customLogger.logFiles("Creating directory " + zipDirectoryStructure);
		}

		File zipFile = new File(zipDataName + ".zip");
		// erzeugt Datei, falls sie noch nicht existiert, macht ansonsten nichts
		if (zipFile.createNewFile()) {
			customLogger.logFiles("Empty file created: " + zipFile.getName());
		}

		customLogger.logFiles("Zipping: " + zipFile);
		fileService.zip(fileService.getSystemFolder().toString(), zipFile, fileList);
	}

}