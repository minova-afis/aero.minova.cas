package aero.minova.core.application.system.controller;

import static java.nio.file.Files.readAllBytes;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.core.application.system.FilesService;
import lombok.val;

@RestController
// benötigt, damit JUnit-Tests nicht abbrechen
@ConditionalOnProperty(prefix = "application.runner", value = "enabled", havingValue = "true", matchIfMissing = true)
public class FilesController {

	@Autowired
	FilesService files;
	// Log für Fehlermeldungen
	Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");
	// Log für die Anfragen der User ohne SQL
	Logger userLogger = LoggerFactory.getLogger("UserLogger");
	// Log für File Hashes und Zipps
	Logger filesLogger = LoggerFactory.getLogger("FilesLogger");

	@RequestMapping(value = "files/read", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public @ResponseBody byte[] getFile(@RequestParam String path) throws Exception {
		path = path.replace('\\', '/');
		val inputPath = files.checkLegalPath(path);
		userLogger.info(SecurityContextHolder.getContext().getAuthentication().getName() + ": files/read: " + path);
		return readAllBytes(inputPath);
	}

	@RequestMapping(value = "files/hash", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public @ResponseBody byte[] getHash(@RequestParam String path) throws Exception {
		path = path.replace('\\', '/');
		userLogger.info(SecurityContextHolder.getContext().getAuthentication().getName() + ": checking Hash for file: " + path);
		String md5FilePath = files.getMd5Folder() + "/" + path.replace(files.getSystemFolder().toString(), "") + ".md5";
		files.checkLegalPath(md5FilePath);
		return getFile(md5FilePath);
	}

	@RequestMapping(value = "upload/logs", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody void getLogs(@RequestBody byte[] log) throws Exception {
		// Doppelpunkte müssen raus, da Sonderzeichen im Filenamen nicht erlaubt sind
		String logFolderName = ("/Log-" + LocalDateTime.now()).replace(":", "-");
		File logFileFolder = Paths.get((files.getLogsFolder() + logFolderName)).toFile();
		File logPath = new File(logFileFolder.toString() + ".zip");
		logFileFolder.mkdirs();

		userLogger.info(SecurityContextHolder.getContext().getAuthentication().getName() + ": Storing: " + logPath);
		Files.write(logPath.toPath(), log);

		// hochgeladenes File unzippen
		userLogger.info(SecurityContextHolder.getContext().getAuthentication().getName() + ": Unzipping File: " + logPath);
		unzipFile(logPath, logFileFolder.getAbsolutePath().toString());
	}

	public void hashFile(Path p) throws IOException {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("msg.MD5Error");
		}
		md.update(readAllBytes(p));
		String fx = "%0" + (md.getDigestLength() * 2) + "x";
		byte[] hashOfFile = String.format(fx, new BigInteger(1, md.digest())).getBytes(StandardCharsets.UTF_8);

		// Path für die neue MD5-Datei zusammenbauen
		String mdDataName = p.toString().replace(files.getSystemFolder().toString(), files.getMd5Folder()).replace('\\', '/');

		// Alle benötigten Ordner erstellen
		File md5DirectoryStructure = new File(mdDataName.substring(0, mdDataName.lastIndexOf('/')));
		if (md5DirectoryStructure.mkdirs()) {
			filesLogger.info("Creating directory " + md5DirectoryStructure);
		}

		// erzeugt die Datei, falls sie noch nicht existiert und überschreibt sie, falls sie schon exisitert
		File hashedFile = new File(mdDataName + ".md5");
		filesLogger.info("CAS: Hashing: " + hashedFile.getAbsolutePath());

		Files.write(Paths.get(hashedFile.getAbsolutePath()), hashOfFile);
	}

	// je höher die Zahl bei @Order, desto später wird die Methode ausgeführt
	@EventListener(ApplicationReadyEvent.class)
	@Order(2)
	@RequestMapping(value = "files/hashAll")
	public void hashAll() throws IOException {
		List<Path> programFiles = files.populateFilesList(files.getSystemFolder());
		for (Path path : programFiles) {
			// Mit dieser If-Abfrage wird verhindert, dass es .md5-Dateiketten gibt
			if (path.toString().contains(files.getMd5Folder())) {
				continue;
			}
			// wir wollen nicht keinen Hash von einem Directory ( zips allerdings schon)
			if (!path.toFile().isDirectory()) {
				hashFile(path);
			}

		}
	}

	// je niedriger die Zahl bei @Order, desto früher wird die Methode ausgeführt
	@EventListener(ApplicationReadyEvent.class)
	@Order(1)
	@RequestMapping(value = "files/zipAll")
	public void zipAll() throws Exception {
		List<Path> programFiles = files.populateFilesList(files.getSystemFolder());
		for (Path path : programFiles) {
			String fileSuffix = path.toString().substring(path.toString().lastIndexOf(".") + 1, path.toString().length());
			// es kann sein, dass von einem vorherigen Start bereits gezippte Dateien vorhanden sind, welche schon gehashed wurden
			if (fileSuffix.toLowerCase().equals("md5")) {
				String filePrefix = path.toString().substring(0, path.toString().lastIndexOf("."));
				fileSuffix = path.toString().substring(filePrefix.lastIndexOf(".") + 1, path.toString().length());
			}
			// wir wollen nicht noch einen zip von einer zip Datei, wir wollen allerdings hier NUR Directories haben
			if ((!fileSuffix.toLowerCase().contains("zip")) && (path.toFile().isDirectory())) {
				byte[] zipDataOfFile = getZip(path);
				File zippedFile = new File(path + ".zip");
				filesLogger.info("CAS: Zipping: " + zippedFile.getAbsolutePath());
				// erzeugt die Datei, falls sie noch nicht existiert und überschreibt sie, falls sie schon exisitert
				Files.write(Paths.get(zippedFile.getAbsolutePath()), zipDataOfFile);
			}
		}
	}

	/*
	 * Vorsicht! getZip ist nicht determinstisch
	 */
	@RequestMapping(value = "files/zip", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public @ResponseBody byte[] getZip(@RequestParam Path path) throws Exception {
		val inputPath = files.checkLegalPath(path.toString());
		List<Path> fileList = files.populateFilesList(path);
		File zipFile = new File(path + ".zip");
		// erzeugt Datei, falls sie noch nicht existiert, macht ansonsten nichts
		zipFile.createNewFile();

		// dadurch bekommt man beim entpacken auch den Ordner, in dem der Inhalt ist, und nicht nur den Inhalt
		String sourcePath = path.toString().substring(0, path.toString().lastIndexOf(File.separator));
		zip(sourcePath, zipFile, fileList);
		return readAllBytes(Paths.get(zipFile.getAbsolutePath()));
	}

	public void zip(String source, File zipFile, List<Path> fileList) throws RuntimeException {
		ZipEntry ze = null;
		try {
			// Jede Datei wird einzeln zu dem ZIP hinzugefügt.
			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);
			for (Path filePath : fileList) {

				// noch mehr zipps in einer zip sind sinnlos
				if (filePath.toFile().isFile() && (!filePath.toString().contains("zip"))) {
					ze = new ZipEntry(filePath.toString().substring(source.length() + 1, filePath.toString().length()).replace('\\', '/'));

					// CreationTime der Zip und Änderungs-Zeitpunkt der Zip auf diese festen Zeitpunkte setzen, da sich sonst jedes Mal der md5 Wert ändert,
					// wenn die Zip erstellt wird.
					ze.setCreationTime(FileTime.from(Instant.EPOCH));
					ze.setTime(0);
					zos.putNextEntry(ze);

					// Jeder Eintrag wird nacheinander in die ZIP Datei geschrieben mithilfe eines Buffers.
					FileInputStream fis = new FileInputStream(filePath.toFile());
					int len;
					byte[] buffer = new byte[1024];
					BufferedInputStream entryStream = new BufferedInputStream(fis, 2048);
					while ((len = entryStream.read(buffer, 0, 1024)) != -1) {
						zos.write(buffer, 0, len);
					}
					zos.closeEntry();
					fis.close();
				}
			}
			zos.close();
			fos.close();
		} catch (Exception e) {
			throw new RuntimeException("msg.ZipError %" + ze.getName());
		}
	}

	public static void unzipFile(File fileZip, String destDirName) throws IOException {
		byte[] buffer = new byte[1024];
		FileInputStream fis;
		try {
			fis = new FileInputStream(fileZip);
			ZipInputStream zis = new ZipInputStream(fis);
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				String zippedFileEntry = ze.getName();
				File newFile = new File(destDirName + File.separator + zippedFileEntry);
				// create directories for sub directories in zip
				new File(newFile.getParent()).mkdirs();
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				zis.closeEntry();
				ze = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
			fis.close();
		} catch (IOException e) {
			throw new RuntimeException("msg.UnZipErrorError %" + fileZip + " %" + destDirName);
		}
	}
}