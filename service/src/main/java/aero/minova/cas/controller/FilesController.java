package aero.minova.cas.controller;

import static java.nio.file.Files.readAllBytes;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import aero.minova.cas.CustomLogger;
import aero.minova.cas.service.FilesService;
import lombok.val;

@RestController
// benötigt, damit JUnit-Tests nicht abbrechen
@ConditionalOnProperty(prefix = "application.runner", value = "enabled", havingValue = "true", matchIfMissing = true)
public class FilesController {

	@Autowired
	FilesService fileService;

	@Autowired
	public CustomLogger customLogger;

	@Autowired
	SqlProcedureController spc;

	// TODO Extension vorerst entfernt, aber für später aufheben
	// TODO Bytes in JSON durch BASE64 darstellen
//	@PostConstruct
//	public void setup() throws Exception {
//		// fügt Extension hinzu
//		spc.registerExctension("files/read", inputTable -> {
//			try {
//				SqlProcedureResult result = new SqlProcedureResult();
//				result.setResultSet(getFiles(inputTable.getRows()));
//				result.setReturnCode(1);
//				return new ResponseEntity(result, HttpStatus.ACCEPTED);
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			}
//		});
//		spc.registerExctension("files/hash", inputTable -> {
//			try {
//				SqlProcedureResult result = new SqlProcedureResult();
//				result.setResultSet(getHashes(inputTable.getRows()));
//				result.setReturnCode(1);
//				return new ResponseEntity(result, HttpStatus.ACCEPTED);
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			}
//		});
//	}
//
//	public Table getFiles(@RequestParam List<Row> pathList) throws Exception {
//		Table fileBytesTable = new Table();
//		fileBytesTable.addColumn(new Column("FileName", DataType.STRING));
//		fileBytesTable.addColumn(new Column("FileBytes", DataType.STRING));
//
//		for (Row row : pathList) {
//			Row fileBytesRow = new Row();
//			String fileName = row.getValues().get(0).getStringValue().replace('\\', '/');
//
//			// Überprüfen, ob File existiert und überprüfen, ob Berechtigung für dieses File gegeben sind
//			val filePath = files.checkLegalPath(fileName);
//			List<Row> privileges = svc.getPrivilegePermissions("files/read:" + fileName).getRows();
//
//			if (!privileges.isEmpty()) {
//				logger.info("files/read: " + filePath);
//				fileBytesRow.addValue(new Value(fileName, null));
//				fileBytesRow.addValue(new Value(readAllBytes(filePath).toString(), null));
//				fileBytesTable.addRow(fileBytesRow);
//			} else {
//				throw new RuntimeException("msg.PrivilegeError %" + fileName);
//			}
//		}
//		return fileBytesTable;
//	}
//
//	public Table getHashes(@RequestParam List<Row> pathList) throws Exception {
//		Table fileBytesTable = new Table();
//		fileBytesTable.addColumn(new Column("FileName", DataType.STRING));
//		fileBytesTable.addColumn(new Column("FileBytes", DataType.STRING));
//
//		for (Row row : pathList) {
//			Row fileBytesRow = new Row();
//			String fileName = row.getValues().get(0).getStringValue().replace('\\', '/');
//			String toBeResolved = fileName.replace(files.getSystemFolder().toString() + "/", "") + ".md5";
//			Path md5FilePath = files.getMd5Folder().resolve(toBeResolved);
//
//			// Überprüfen, ob File existiert und überprüfen, ob Berechtigung für dieses File gegeben sind
//			val filePath = files.checkLegalPath(md5FilePath);
//
//			// Beim Überprüfen der Berechtigung schauen wir, ob die Berechtigung für die Datei, zu welcher der Hash gehört, freigegeben ist
//			List<Row> privileges = svc.getPrivilegePermissions("files/read:" + fileName).getRows();
//
//			if (!privileges.isEmpty()) {
//				logger.info("checking Hash for file: " + fileName);
//				fileBytesRow.addValue(new Value(fileName, null));
//				fileBytesRow.addValue(new Value(readAllBytes(filePath).toString(), null));
//				fileBytesTable.addRow(fileBytesRow);
//			} else {
//				throw new RuntimeException("msg.PrivilegeError %" + fileName);
//			}
//		}
//		return fileBytesTable;
//	}

	/**
	 * Verarbeitet User-Anfragen zum Senden eines Files. Falls das angefragte File gefunden werden kann, wird es zurückgegeben, andernfalls wird entweder eine
	 * FileNotFoundException oder eine IllegalAccessException geworfen.
	 * 
	 * @param path
	 *            Der Pfad des Files, welches der User anfragt, als String.
	 * @return byte[] des angefragten Files.
	 * @throws Exception
	 *             Entweder eine IllegalAccessException, falls der Pfad außerhalb des System-Ordner liegt, oder FileNotFoundException, falls es keine Datei mit
	 *             diesem Namen in dem gewünschten Pfad gibt.
	 */
	@RequestMapping(value = "files/read", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public @ResponseBody byte[] getFile(@RequestParam String path) throws Exception {
		path = path.replace('\\', '/');

		if (path.contains("application.mdi")) {
			return fileService.readMDI();
		}

		String extension = FilenameUtils.getExtension(path);
		// Zur Abwertskompatibilität Dateieindung überprüfen und, falls diese Zip ist, getZip aufrufen.
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
	 *             Entweder eine IllegalAccessException, falls der Pfad außerhalb des System-Ordner liegt, oder FileNotFoundException, falls es keine Datei mit
	 *             diesem Namen in dem gewünschten Pfad gibt.
	 */
	@RequestMapping(value = "files/hash", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public @ResponseBody byte[] getHash(@RequestParam String path) throws Exception {
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
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("msg.MD5Error");
		}
		md.update(readAllBytes(filePath));
		String fx = "%0" + (md.getDigestLength() * 2) + "x";
		byte[] hashOfFile = String.format(fx, new BigInteger(1, md.digest())).getBytes(StandardCharsets.UTF_8);

		// Path für die neue MD5-Datei zusammenbauen
		Path mdDataName = fileService.getMd5Folder().resolve(p);

		// Alle benötigten Ordner erstellen
		if (mdDataName.getParent().toFile().mkdirs()) {
			customLogger.logFiles("Creating directory " + mdDataName);
		}

		// erzeugt die Datei, falls sie noch nicht existiert und überschreibt sie, falls sie schon exisitert
		File hashedFile = new File(mdDataName + ".md5");
		customLogger.logFiles("Hashing: " + hashedFile.getAbsolutePath());

		Files.write(Paths.get(hashedFile.getAbsolutePath()), hashOfFile);
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
		List<Path> programFiles = fileService.populateFilesList(fileService.getSystemFolder());
		for (Path path : programFiles) {
			if (path.startsWith(fileService.getZipsFolder().getParent().toString())) {
				continue;
			}
			String fileSuffix = path.toString().substring(path.toString().lastIndexOf(".") + 1, path.toString().length());
			// es kann sein, dass von einem vorherigen Start bereits gezippte Dateien vorhanden sind, welche schon gehashed wurden
			if (fileSuffix.equalsIgnoreCase("md5")) {
				String filePrefix = path.toString().substring(0, path.toString().lastIndexOf("."));
				fileSuffix = path.toString().substring(filePrefix.lastIndexOf(".") + 1, path.toString().length());
			}
			// wir wollen nicht noch einen zip von einer zip Datei, wir wollen allerdings hier NUR Directories haben
			if ((!fileSuffix.toLowerCase().contains("zip")) && (path.toFile().isDirectory())) {
				createZip(fileService.getSystemFolder().toAbsolutePath().relativize(path.toAbsolutePath()));
			}
		}
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
	 * Vorsicht! createZip ist nicht determinstisch
	 */
	@RequestMapping(value = "files/createZip", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public void createZip(@RequestParam Path path) throws Exception {

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