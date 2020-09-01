package aero.minova.core.application.system.controller;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

@Service
public class FilesService {

	private final Path programFilesFolder;
	private final Path sharedDataFolder;
	private final Path systemFolder;

	public FilesService() {
		this(Paths.get(".").toAbsolutePath());
	}

	public FilesService(Path serviceFolder) {
		// Mit toAbsolutePath werden die Pfade so einfach und eindeutig wie möglich.
		serviceFolder = serviceFolder.toAbsolutePath();
		programFilesFolder = serviceFolder.resolve("..").toAbsolutePath().normalize();
		sharedDataFolder = programFilesFolder.resolve("..").toAbsolutePath().normalize();
		systemFolder = sharedDataFolder.resolve("..").toAbsolutePath().normalize();
	}

	public Path applicationFolder(String application) {
		return programFilesFolder.resolve(application);
	}

	public Path getSystemFolder() {
		return systemFolder;
	}

}
