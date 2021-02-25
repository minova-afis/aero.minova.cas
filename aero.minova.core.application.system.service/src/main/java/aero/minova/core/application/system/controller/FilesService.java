package aero.minova.core.application.system.controller;

import static java.nio.file.Files.isDirectory;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FilesService {

	@Value("${aero_minova_core_application_root_path:../../..}")
	private String rootPath;

	private Path programFilesFolder;
	private Path sharedDataFolder;
	private Path systemFolder;
	private final Logger logger = LoggerFactory.getLogger(FilesService.class);

	public FilesService() {}

	public FilesService(String rootPath) {
		this.rootPath = rootPath;
	}

	/**
	 * Mit {@link Path#toAbsolutePath()} und {@link Path#normalize} werden die Pfade so eindeutig wie möglich.
	 */
	@PostConstruct
	public void setUp() {
		if (rootPath == null || rootPath.isEmpty()) {
			rootPath = Paths.get(".").toAbsolutePath().normalize().toString();
		}
		systemFolder = Paths.get(rootPath).toAbsolutePath().normalize();
		sharedDataFolder = systemFolder.resolve("Shared Data").toAbsolutePath().normalize();
		programFilesFolder = sharedDataFolder.resolve("Program Files").toAbsolutePath().normalize();
		if (!isDirectory(systemFolder)) {
			logger.error("System folder does not exist (see aero_minova_core_application_root_path): " + systemFolder);
		}
		if (!isDirectory(sharedDataFolder)) {
			logger.error("Shared data folder does not exist (see aero_minova_core_application_root_path): " + sharedDataFolder);
		}
		if (!isDirectory(programFilesFolder)) {
			logger.error("Program files folder does not exist (see aero_minova_core_application_root_path): " + programFilesFolder);
		}
	}

	public Path applicationFolder(String application) {
		return programFilesFolder.resolve(application);
	}

	public Path getSystemFolder() {
		return systemFolder;
	}

}
