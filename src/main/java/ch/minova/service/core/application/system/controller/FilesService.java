package ch.minova.service.core.application.system.controller;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

@Service
public class FilesService {

	// Der Dienst wird im Bin Ordner gestartet.
	private final Path serviceFolder;
	private final Path programFilesFolder;
	private final Path sharedDataFolder;

	public FilesService() {
		this(Paths.get("..").toAbsolutePath());
	}

	public FilesService(Path serviceFolder) {
		// Mit toAbsolutePath werden die Pfade so einfach und eindeutig wie möglich.
		this.serviceFolder = serviceFolder.toAbsolutePath();
		programFilesFolder = serviceFolder.resolve("..").toAbsolutePath();
		sharedDataFolder = programFilesFolder.resolve("..").toAbsolutePath();
	}

	public Path applicationFolder(String application) {
		return programFilesFolder.resolve(application);
	}

	public Path sharedDataFolder() {
		return sharedDataFolder;
	}

}
