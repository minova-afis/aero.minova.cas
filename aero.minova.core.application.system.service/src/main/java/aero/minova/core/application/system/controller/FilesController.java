package aero.minova.core.application.system.controller;

import static java.nio.file.Files.readAllBytes;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
			throw new IllegalAccessException("Path variable with value " + path + " points outside the shared data folder of the system: " + inputPath);
		}
		return readAllBytes(inputPath);
	}

	@RequestMapping(value = "files/hash", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public @ResponseBody String getHash(@RequestParam String path) throws Exception {
		val inputPath = files.getSystemFolder().resolve(path).toAbsolutePath().normalize();
		if (!inputPath.startsWith(files.getSystemFolder())) {
			throw new IllegalAccessException("Path variable with value " + path + " points outside the shared data folder of the system: " + inputPath);
		}
		return hashFile(inputPath);
	}

	private static String hashFile(Path p) throws IOException {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 is not available, check Java installation");
		}
		md.update(readAllBytes(p));

		String fx = "%0" + (md.getDigestLength() * 2) + "x";
		return String.format(fx, new BigInteger(1, md.digest()));
	}
}