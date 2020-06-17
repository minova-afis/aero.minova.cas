package ch.minova.service.core.application.system.controller;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.createDirectory;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.rules.TemporaryFolder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import lombok.val;

@TestConfiguration
@Profile("test")
public class FilesControllerTestConfiguration {

	private final Path programFilesFolder;
	private final Path serviceFolder;
	private final Path sharedDataFolder;

	public FilesControllerTestConfiguration() throws IOException {
		val folder = new TemporaryFolder();
		folder.create();
		sharedDataFolder = folder.newFolder("Shared Data").toPath();
		programFilesFolder = sharedDataFolder.resolve("Program Files");
		serviceFolder = programFilesFolder.resolve("core.application.system");
		createDirectories(serviceFolder);
	}

	@Bean
	@Primary
	public FilesService filesServices() {
		return new FilesService(serviceFolder);
	}

	@Bean
	@Primary
	public FilesController filesController() {
		return new FilesController();
	}
}
