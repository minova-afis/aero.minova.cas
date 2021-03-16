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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.val;

@RestController
// benötigt, damit JUnit-Tests nicht abbrechen
@ConditionalOnProperty(prefix = "application.runner", value = "enabled", havingValue = "true", matchIfMissing = true)
public class FilesController {

	@Autowired
	FilesService files;
	Logger logger = LoggerFactory.getLogger(SqlViewController.class);

	@RequestMapping(value = "files/read", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public @ResponseBody byte[] getFile(@RequestParam String path) throws Exception {
		val inputPath = files.getSystemFolder().resolve(path).toAbsolutePath().normalize();
		if (!inputPath.startsWith(files.getSystemFolder())) {
			throw new IllegalAccessException("msg.PathError %" + path + " %" + inputPath);
		}
		return readAllBytes(inputPath);
	}

	@RequestMapping(value = "files/hash", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public @ResponseBody byte[] getHash(@RequestParam String path) throws Exception {
		val inputPath = files.getSystemFolder().resolve(path).toAbsolutePath().normalize();
		if (!inputPath.startsWith(files.getSystemFolder())) {
			throw new IllegalAccessException("msg.PathError %" + path + " %" + inputPath);
		}
		return hashFile(inputPath);
	}

	private static byte[] hashFile(Path p) throws IOException {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("msg.MD5Error");
		}
		md.update(readAllBytes(p));

		String fx = "%0" + (md.getDigestLength() * 2) + "x";
		return String.format(fx, new BigInteger(1, md.digest())).getBytes(StandardCharsets.UTF_8);
	}

	// je höher die Zahl bei @Order, desto später wird die Methode ausgeführt
	@EventListener(ApplicationReadyEvent.class)
	@Order(2)
	@RequestMapping(value = "files/hashAll")
	public void hashAll() throws IOException {
		List<String> programFiles = files.populateFilesList(files.getSystemFolder().toFile());
		for (String path : programFiles) {
			String fileSuffix = path.substring(path.lastIndexOf(".") + 1, path.length());
			// bevor die Dateien gehashed werden, werden sie gezipped (siehe @Order)
			if (fileSuffix.toLowerCase().equals("zip")) {
				String filePrefix = path.substring(0, path.lastIndexOf("."));
				fileSuffix = path.substring(filePrefix.lastIndexOf(".") + 1, path.length());
			}
			// wir wollen nicht noch einen Hash von einer gehashten Datei und auch keinen Hash von einem Directory
			if ((!fileSuffix.toLowerCase().contains("md5")) && (!new File(path).isDirectory())) {
				byte[] hashOfFile = hashFile(Paths.get(path));
				File hashedFile = new File(path + ".md5");
				// erzeugt die Datei, falls sie noch nicht existiert und überschreibt sie, falls sie schon exisitert
				logger.info("Hashing: " + hashedFile.getAbsolutePath());
				Files.write(Paths.get(hashedFile.getAbsolutePath()), hashOfFile);
			}

		}
	}

	// je niedriger die Zahl bei @Order, desto früher wird die Methode ausgeführt
	@EventListener(ApplicationReadyEvent.class)
	@Order(1)
	@RequestMapping(value = "files/zipAll")
	public void zipAll() throws Exception {
		List<String> programFiles = files.populateFilesList(files.getSystemFolder().toFile());
		for (String path : programFiles) {
			String fileSuffix = path.substring(path.lastIndexOf(".") + 1, path.length());
			// es kann sein, dass von einem vorherigen Start bereits gezippte Dateien vorhanden sind, welche schon gehashed wurden
			if (fileSuffix.toLowerCase().equals("md5")) {
				String filePrefix = path.substring(0, path.lastIndexOf("."));
				fileSuffix = path.substring(filePrefix.lastIndexOf(".") + 1, path.length());
			}
			// wir wollen nicht noch einen zip von einer zip Datei
			if ((!fileSuffix.toLowerCase().contains("zip")) && (new File(path)).isDirectory()) {
				byte[] zipDataOfFile = getZip(path);
				File zippedFile = new File(path + ".zip");
				logger.info("Zipping: " + zippedFile.getAbsolutePath());
				// erzeugt die Datei, falls sie noch nicht existiert und überschreibt sie, falls sie schon exisitert
				Files.write(Paths.get(zippedFile.getAbsolutePath()), zipDataOfFile);
			}
		}
	}

	@RequestMapping(value = "files/zip", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public @ResponseBody byte[] getZip(@RequestParam String path) throws Exception {
		val inputPath = files.getSystemFolder().resolve(path).toAbsolutePath().normalize();
		if (!inputPath.startsWith(files.getSystemFolder())) {
			throw new IllegalAccessException("msg.PathError %" + path + " %" + inputPath);
		}
		List<String> fileList = files.populateFilesList(new File(path));
		File zipFile = new File(path + ".zip");
		// erzeugt Datei, falls sie noch nicht existiert, macht ansonsten nichts
		zipFile.createNewFile();
		zip(path.toString(), zipFile, fileList);
		return readAllBytes(Paths.get(zipFile.getAbsolutePath()));
	}

	public void zip(String source, File zipFile, List<String> fileList) throws RuntimeException {
		ZipEntry ze = null;
		try {
			// jede Datei wird einzeln zu dem ZIP hinzugefügt
			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);
			for (String filePath : fileList) {

				if (new File(filePath).isFile()) {
					ze = new ZipEntry(filePath.substring(source.length() + 1, filePath.length()));
					if (!ze.getName().equals(zipFile.getName())) {
						zos.putNextEntry(ze);
					}

					// jeder Eintrag wird nacheinander in die ZIP Datei geschrieben mithilfe eines Buffers
					FileInputStream fis = new FileInputStream(filePath);
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

}