package aero.minova.core.application.system.controller;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static java.nio.file.Files.readAllBytes;

@RestController
public class FilesController {

	@Autowired
	FilesService files;

	@RequestMapping(value = "files/read", produces= {MediaType.APPLICATION_OCTET_STREAM_VALUE})
	public @ResponseBody byte[] getFile(@RequestParam String path) throws Exception {
		val inputPath = files.getSystemFolder().resolve(path).toAbsolutePath().normalize();
		if (!inputPath.startsWith(files.getSystemFolder())) {
			throw new IllegalAccessException("Path variable with value " + path + " points outside the shared data folder of the system.");
		}
		return readAllBytes(inputPath);
	}
}