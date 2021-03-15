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

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.val;

@RestController
@DependsOn("FilesService")
public class FilesController {

	@Autowired
	FilesService files;

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

	/*
	 * wird zwar bei Programmstart ausgeführt, aber durch die DependsOn-Notation müsste es erst nach der setup-Methode von der FilesService Klasse aufgerufen
	 * werden
	 */
	@PostConstruct
	public void hashAll() throws IOException {
		List<String> programFiles = files.populateFilesList(files.getProgramFilesFolder().toFile());
		for (String path : programFiles) {
			String fileSuffix = path.substring(path.lastIndexOf(".") + 1, path.length());
			// wir wollen nicht noch einen Hash von einer gehashten Datei
			if (!fileSuffix.toLowerCase().equals("md5")) {
				byte[] hashOfFile = hashFile(Paths.get(path));
				File hashedFile = new File(path.substring(0, path.lastIndexOf(".")) + ".md5");
				// erzeugt auch die Datei, falls sie noch nicht existiert und überschreibt sie, falls sie schon exisitert
				Files.write(Paths.get(path), hashOfFile);
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
		// Name des Ordners herausfinden, um das ZIP später so nennen zu können
		String dirName = path.substring(path.lastIndexOf(File.separatorChar) + 1, path.length());
		File[] files = Paths.get(path.substring(0, path.lastIndexOf(File.separatorChar) + 1)).toFile().listFiles();
		boolean zipExists = false;
		File zipFile = Paths.get(path).toFile();
		for (File file : files) {
			if (file.getName().equals(dirName + ".zip")) {
				zipExists = true;
				zipFile = file;
			}
		}
		if (!zipExists) {
			zipFile = new File(dirName + ".zip");
		}
		zip(path.toString(), zipFile, fileList);
		byte[] output = readAllBytes(Paths.get(zipFile.getAbsolutePath()));
		return output;
	}

	public void zip(String source, File zipFile, List<String> fileList) throws RuntimeException {
		ZipEntry ze = null;
		try {
			// jede Datei wird einzeln zu dem ZIP hinzugefügt
			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);
			for (String filePath : fileList) {
				System.out.println("Zipping " + filePath);
				ze = new ZipEntry(filePath.substring(source.length() + 1, filePath.length()));
				if (!ze.getName().equals(zipFile.getName())) {
					zos.putNextEntry(ze);
				}

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
			zos.close();
			fos.close();
		} catch (Exception e) {
			throw new RuntimeException("msg.ZipError %" + ze.getName());
		}
	}

}