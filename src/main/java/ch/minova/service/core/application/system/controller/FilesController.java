package ch.minova.service.core.application.system.controller;

import static ch.minova.ncore.util.MessageFormat.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.readAllBytes;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ch.minova.ncore.util.MessageFormat;

@RestController
public class FilesController {

	@Autowired
	FilesService files;

	@RequestMapping(value = "/files/read")
	@ResponseBody
	public String getFile(@RequestParam String path) throws Exception {
		if (Paths.get(path).startsWith(files.sharedDataFolder())) {
			throw new IllegalArgumentException(format("Path variable with value \"{0}\" points outside the shared data folder of the system.", path));
		}
		return new String(readAllBytes(files.sharedDataFolder().resolve(path)), UTF_8);
	}

}
