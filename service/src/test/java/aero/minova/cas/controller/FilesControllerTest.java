package aero.minova.cas.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.TemporaryFolder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import aero.minova.cas.BaseTest;
import aero.minova.cas.CustomLogger;
import aero.minova.cas.service.FilesService;
import lombok.val;

@Slf4j
@ExtendWith(MockitoExtension.class)
class FilesControllerTest extends BaseTest {
	@Mock
	private CustomLogger customLoggerMock;

	@InjectMocks
	private FilesController filesController;

	Path rootFolder;
	Path internalFolder;
	Path md5Folder;
	Path zipsFolder;
	Path logsFolder;
	Path sharedDataFolder;
	Path programFilesFolder;
	Path serviceFolder;

	@BeforeEach
	void setup() throws IOException {
		val rootPath = new TemporaryFolder();
		rootPath.create();
		rootFolder = rootPath.getRoot().toPath();

		internalFolder = rootPath.newFolder("Internal").toPath();
		md5Folder = internalFolder.resolve("MD5");
		zipsFolder = internalFolder.resolve("Zips");
		logsFolder = internalFolder.resolve("UserLogs");
		sharedDataFolder = rootPath.newFolder("Shared Data").toPath();
		programFilesFolder = sharedDataFolder.resolve("Program Files");
		serviceFolder = programFilesFolder.resolve("AFIS");
		Files.createDirectories(serviceFolder);
		Files.createDirectories(md5Folder);
		Files.createDirectories(zipsFolder);
		Files.createDirectories(logsFolder);

		// TODO Rainer: should be mocked!
		filesController.fileService = new FilesService(rootFolder.toString());
		filesController.fileService.customLogger = customLoggerMock;
		filesController.fileService.setUp();
	}

	@Test
	void testLegal() throws Exception {
		Files.write(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs"), "<preferences></preferences>".getBytes(StandardCharsets.UTF_8));
		assertThat(filesController.getFile("Shared Data/Program Files/AFIS/AFIS.xbs")).isEqualTo(
				"<preferences></preferences>".getBytes(StandardCharsets.UTF_8));
	}

	@Test
	void testLegalHash() throws Exception {
		Files.write(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs"), "<preferences></preferences>".getBytes(StandardCharsets.UTF_8));
		filesController.hashFile(Paths.get("Shared Data/Program Files/AFIS/AFIS.xbs"));
		assertThat(filesController.getHash("Shared Data/Program Files/AFIS/AFIS.xbs"))
				.isEqualTo("093544245ba5b8739014ac4e5a273520".getBytes(StandardCharsets.UTF_8));
	}

	@Test
	void testLegalLog() throws Exception {
		val metaDataFolder = programFilesFolder.resolve(".metadata");
		Files.createDirectories(metaDataFolder);

		Files.write(metaDataFolder.resolve("beispielLog.log"),
				"<text>Oh nein!Ein Fehler in der Anwendung!</text>".getBytes(StandardCharsets.UTF_8));
		filesController.createZip(Paths.get("Shared Data/Program Files/.metadata"));

		// dabei wird der Logs Ordner erzeugt
		filesController.getLogs(Files.readAllBytes(zipsFolder.resolve("Shared Data").resolve("Program Files").toFile().listFiles()[0].toPath()));

		File found = findFile("beispielLog.log", internalFolder.resolve("UserLogs").toFile());
		assertThat(found).isNotNull();
		assertThat(Files.readAllBytes(found.toPath()))
				.isEqualTo("<text>Oh nein!Ein Fehler in der Anwendung!</text>".getBytes(StandardCharsets.UTF_8));
	}

	@Test
	void testIllegal() {
		Assertions.assertThrows(IllegalAccessException.class, () -> filesController.getFile("../Shared Data/Program Files/AFIS/AFIS.xbs"));
	}

	@Test
	void testIllegalHash() {
		Assertions.assertThrows(NoSuchFileException.class, () -> filesController.hashFile(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs")));
	}

	@Test
	void testLegalZip() throws Exception {
		Files.write(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs"), "<preferences></preferences>".getBytes(StandardCharsets.UTF_8));
		filesController.createZip(Paths.get("Shared Data/Program Files/AFIS"));

		val tempFolder = programFilesFolder.resolve("temp");
		Files.createDirectories(tempFolder);
		assertThat(Files.exists(tempFolder.resolve("AFIS"))).isFalse();

		File tempFile = tempFolder.resolve("tempZipFile.zip").toFile();
		Files.write(tempFile.toPath(), filesController.getZip("Shared Data/Program Files/AFIS.zip"));
		filesController.fileService.unzipFile(tempFile, tempFolder);

		assertThat(Files.exists(tempFolder.resolve("Shared Data").resolve("Program Files").resolve("AFIS"))).isTrue();
		assertThat(Files.exists(tempFolder.resolve("Shared Data").resolve("Program Files").resolve("AFIS").resolve("AFIS.xbs"))).isTrue();
		assertThat(Files.readAllBytes(tempFolder.resolve("Shared Data").resolve("Program Files").resolve("AFIS").resolve("AFIS.xbs")))
				.isEqualTo(Files.readAllBytes(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs")));
	}

	@Test
	void testLegalZipExists() throws Exception {
		Files.write(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs"), "<preferences></preferences>".getBytes(StandardCharsets.UTF_8));
		Files.write(programFilesFolder.resolve("AFIS.zip"), "".getBytes(StandardCharsets.UTF_8));

		filesController.createZip(Paths.get("Shared Data/Program Files/AFIS"));

		val tempFolder = programFilesFolder.resolve("temp");
		Files.createDirectories(tempFolder);

		File tempFile = tempFolder.resolve("tempZipFile.zip").toFile();
		Files.write(tempFile.toPath(), filesController.getZip("Shared Data/Program Files/AFIS.zip"));
		filesController.fileService.unzipFile(tempFile, tempFolder);

		assertThat(tempFolder.resolve("Shared Data").resolve("Program Files").resolve("AFIS").toFile().exists()).isTrue();

		byte[] unzipped = Files.readAllBytes(findFile("AFIS.xbs", tempFolder.toFile()).toPath());
		assertThat(Files.readAllBytes(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs"))).isEqualTo(unzipped);
		assertThat(Files.readAllBytes(zipsFolder.resolve("Shared Data").resolve("Program Files").resolve("AFIS.zip")))
				.isNotEqualTo("".getBytes(StandardCharsets.UTF_8));
		assertThat(unzipped).isEqualTo("<preferences></preferences>".getBytes(StandardCharsets.UTF_8));
	}

	@Test
	void testIllegalZip() {
		assertThrows(java.io.FileNotFoundException.class, () -> filesController.createZip(serviceFolder.resolve("AFIS.xbs")));
	}

	@Test
	void testLegalZipAll() throws Exception {
		Files.write(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs"), "<preferences></preferences>".getBytes(StandardCharsets.UTF_8));
		Files.write(programFilesFolder.resolve("AFIS.zip"), "".getBytes(StandardCharsets.UTF_8));
		int hexOld = Files.readAllBytes(programFilesFolder.resolve("AFIS.zip")).hashCode();

		filesController.zipAll();

		assertThat(Files.exists(programFilesFolder.resolve("AFIS.zip"))).isTrue();
		assertThat(Files.readAllBytes(programFilesFolder.resolve("AFIS.zip")).hashCode()).isNotEqualTo(hexOld);
	}

	@Test
	void testZipAllAndHashAll() throws Exception {
		filesController.fileService = new FilesService(rootFolder.toString());
		filesController.fileService.setUp();

		Files.write(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs"), "<preferences></preferences>".getBytes(StandardCharsets.UTF_8));
		Files.write(serviceFolder.resolve("AFIS.zip"), "".getBytes(StandardCharsets.UTF_8));
		Files.write(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs.md5"), "".getBytes(StandardCharsets.UTF_8));
		byte[] old = Files.readAllBytes(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs.md5"));

		filesController.zipAll();
		filesController.hashAll();

		assertThat(Files.readAllBytes(md5Folder.resolve("Shared Data").resolve("Program Files").resolve("AFIS").resolve("AFIS.xbs.md5"))).isNotEqualTo(old);
		assertThat(Files.exists(md5Folder.resolve("Internal").resolve("Zips").resolve("Shared Data").resolve("Program Files").resolve("AFIS.zip.md5")))
				.isTrue();
		assertThat(Files.readAllBytes(md5Folder.resolve("Internal").resolve("Zips").resolve("Shared Data").resolve("Program Files").resolve("AFIS.zip.md5")))
				.isNotEmpty();
		assertThat(Files.readAllBytes(md5Folder.resolve("Internal").resolve("Zips").resolve("Shared Data").resolve("Program Files").resolve("AFIS.zip.md5")))
				.isEqualTo(filesController.getHash("Shared Data/Program Files/AFIS.zip"));

		// das zippen ist nicht deterministisch und würde auf github dazu führen, dass der Test abbricht, obwohl er local funktioniert
		// assertThat(readAllBytes(programFilesFolder.resolve("AFIS.zip.md5"))).isEqualTo("51a1713197b136586344905c9847daff".getBytes(StandardCharsets.UTF_8));
		assertThat(Files.readAllBytes(md5Folder.resolve("Shared Data").resolve("Program Files").resolve("AFIS").resolve("AFIS.xbs.md5")))
				.isEqualTo("093544245ba5b8739014ac4e5a273520".getBytes(StandardCharsets.UTF_8));

		filesController.hashAll();
	}

	@Test
	void getZipBackCompatability() throws Exception {
		Files.write(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs"), "<preferences></preferences>".getBytes(StandardCharsets.UTF_8));
		filesController.createZip(Paths.get("Shared Data/Program Files/AFIS"));

		assertThat(filesController.getFile("Shared Data/Program Files/AFIS.zip")).isEqualTo(filesController.getZip("Shared Data/Program Files/AFIS"));
	}

	// Hilfsmethode
	private File findFile(String file, File directory) {
		File[] list = directory.listFiles();
		File found = null;
		if (list != null) {
			for (File fil : list) {
				if (fil.isDirectory()) {
					found = findFile(file, fil);
				} else if (file.equalsIgnoreCase(fil.getName())) {
					found = fil;
				}
				if (found != null) {
					return found;
				}
			}
		}
		return found;
	}
}
