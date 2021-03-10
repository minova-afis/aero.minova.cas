package aero.minova.core.application.system.controller;

import static java.nio.file.Files.readAllBytes;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
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
}