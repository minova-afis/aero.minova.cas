package aero.minova.core.application.system.controller;

import static java.nio.file.Files.readAllBytes;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.val;

@RestController
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

	@RequestMapping(value = "files/zip", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public @ResponseBody byte[] getZip(@RequestParam String path) throws Exception {
		val inputPath = files.getSystemFolder().resolve(path).toAbsolutePath().normalize();
		if (!inputPath.startsWith(files.getSystemFolder())) {
			throw new IllegalAccessException("msg.PathError %" + path + " %" + inputPath);
		}
		List<String> fileList = populateFilesList(new File(path));

		String dirName = path.substring(path.length() + 1, path.length());
		File[] files = Paths.get(path).toFile().listFiles();
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
		zipFile.delete();
		return output;
	}

	public void zip(String source, File zipFile, List<String> fileList) {
		byte[] buffer = new byte[1024];
		try {
			// now zip files one by one
			// create ZipOutputStream to write to the byte array
			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);
			for (String filePath : fileList) {
				System.out.println("Zipping " + filePath);
				// for ZipEntry we need to keep only relative file path, so we used substring on absolute path
				ZipEntry ze = new ZipEntry(filePath.substring(source.length() + 1, filePath.length()));
				zos.putNextEntry(ze);

				// read the file and write to ZipOutputStream
				FileInputStream fis = new FileInputStream(filePath);
				int len;
				BufferedInputStream entryStream = new BufferedInputStream(fis, 2048);
				while ((len = entryStream.read(buffer, 0, 1024)) != -1) {
					zos.write(buffer, 0, len);
				}
				zos.closeEntry();
				fis.close();
			}
			zos.close();
			fos.close();
		} catch (

		IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method populates all the files in a directory to a List
	 * 
	 * @param dir
	 * @throws IOException
	 */
	private List<String> populateFilesList(File dir) throws IOException {
		List<String> filesListInDir = new ArrayList<String>();
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				filesListInDir.add(file.getAbsolutePath());
			} else {
				populateFilesList(file);
			}
		}
		return filesListInDir;
	}
}