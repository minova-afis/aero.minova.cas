package aero.minova.core.application.system.controller;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Files.write;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import aero.minova.core.application.system.CustomLogger;
import aero.minova.core.application.system.service.FilesService;
import lombok.val;

@SpringBootTest
@ActiveProfiles("test")
public class FilesControllerTest {

	@Test
	public void testLegal() throws Exception {
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Authentication authentication = Mockito.mock(Authentication.class);
		CustomLogger logger = Mockito.mock(CustomLogger.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("test");

		final val testSubject = new FilesController();
		testSubject.customLogger = logger;

		final val rootPath = new TemporaryFolder();
		rootPath.create();
		final val rootFolder = rootPath.getRoot().toPath();
		final val internalFolder = rootPath.newFolder("Internal").toPath();
		final val md5Folder = internalFolder.resolve("MD5");
		final val zipsFolder = internalFolder.resolve("Zips");
		final val logsFolder = internalFolder.resolve("UserLogs");
		final val sharedDataFolder = rootPath.newFolder("Shared Data").toPath();
		final val programFilesFolder = sharedDataFolder.resolve("Program Files");
		final val serviceFolder = programFilesFolder.resolve("AFIS");
		createDirectories(serviceFolder);
		createDirectories(md5Folder);
		createDirectories(zipsFolder);

		testSubject.files = new FilesService(rootFolder.toString());
		testSubject.files.setUp();

		write(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs"), new String("<preferences></preferences>").getBytes(StandardCharsets.UTF_8));
		assertThat(testSubject.getFile("Shared Data/Program Files/AFIS/AFIS.xbs")).isEqualTo("<preferences></preferences>".getBytes(StandardCharsets.UTF_8));
	}

	@Test
	public void testLegalHash() throws Exception {
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Authentication authentication = Mockito.mock(Authentication.class);
		CustomLogger logger = Mockito.mock(CustomLogger.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("test");

		final val testSubject = new FilesController();
		testSubject.customLogger = logger;

		final val rootPath = new TemporaryFolder();
		rootPath.create();
		final val rootFolder = rootPath.getRoot().toPath();
		final val internalFolder = rootPath.newFolder("Internal").toPath();
		final val md5Folder = internalFolder.resolve("MD5");
		final val zipsFolder = internalFolder.resolve("Zips");
		final val logsFolder = internalFolder.resolve("UserLogs");
		final val sharedDataFolder = rootPath.newFolder("Shared Data").toPath();
		final val programFilesFolder = sharedDataFolder.resolve("Program Files");
		final val serviceFolder = programFilesFolder.resolve("AFIS");
		createDirectories(serviceFolder);
		createDirectories(md5Folder);
		createDirectories(zipsFolder);

		testSubject.files = new FilesService(rootFolder.toString());
		testSubject.files.setUp();

		write(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs"), new String("<preferences></preferences>").getBytes(StandardCharsets.UTF_8));
		testSubject.hashFile(Paths.get("Shared Data/Program Files/AFIS/AFIS.xbs"));
		assertThat(testSubject.getHash("Shared Data/Program Files/AFIS/AFIS.xbs"))
				.isEqualTo("093544245ba5b8739014ac4e5a273520".getBytes(StandardCharsets.UTF_8));
	}

	@Test
	public void testLegalLog() throws Exception {
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Authentication authentication = Mockito.mock(Authentication.class);
		CustomLogger logger = Mockito.mock(CustomLogger.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("test");

		final val testSubject = new FilesController();
		testSubject.customLogger = logger;

		final val rootPath = new TemporaryFolder();
		rootPath.create();
		final val rootFolder = rootPath.getRoot().toPath();
		final val internalFolder = rootPath.newFolder("Internal").toPath();
		final val md5Folder = internalFolder.resolve("MD5");
		final val zipsFolder = internalFolder.resolve("Zips");
		final val logsFolder = internalFolder.resolve("UserLogs");
		final val sharedDataFolder = rootPath.newFolder("Shared Data").toPath();
		final val programFilesFolder = sharedDataFolder.resolve("Program Files");
		final val serviceFolder = programFilesFolder.resolve(".metadata");
		createDirectories(serviceFolder);
		createDirectories(md5Folder);
		createDirectories(zipsFolder);

		testSubject.files = new FilesService(rootFolder.toString());
		testSubject.files.setUp();

		write(serviceFolder.resolve("beispielLog.log"), new String("<text>Oh nein!Ein Fehler in der Anwendung!</text>").getBytes(StandardCharsets.UTF_8));
		testSubject.createZip(Paths.get("Shared Data/Program Files/.metadata"));

		// dabei wird der Logs Ordner erzeugt
		testSubject.getLogs(readAllBytes(zipsFolder.resolve("Shared Data").resolve("Program Files").toFile().listFiles()[0].toPath()));

		File found = findFile("beispielLog.log", internalFolder.resolve("UserLogs").toFile());
		assertThat(found).isNotEqualTo(null);
		assertThat(Files.readAllBytes(found.toPath()))
				.isEqualTo(new String("<text>Oh nein!Ein Fehler in der Anwendung!</text>").getBytes(StandardCharsets.UTF_8));
	}

	@Test
	public void testIllegal() throws Exception {
		final val rootPath = new TemporaryFolder();
		rootPath.create();
		final val rootFolder = rootPath.getRoot().toPath();
		final val internalFolder = rootPath.newFolder("Internal").toPath();
		final val md5Folder = internalFolder.resolve("MD5");
		final val zipsFolder = internalFolder.resolve("Zips");
		final val logsFolder = internalFolder.resolve("UserLogs");
		final val sharedDataFolder = rootPath.newFolder("Shared Data").toPath();
		final val programFilesFolder = sharedDataFolder.resolve("Program Files");
		final val serviceFolder = programFilesFolder.resolve("AFIS");
		createDirectories(serviceFolder);
		createDirectories(md5Folder);
		createDirectories(zipsFolder);

		final val testSubject = new FilesController();
		testSubject.files = new FilesService(rootFolder.toString());
		testSubject.files.setUp();

		Assertions.assertThrows(IllegalAccessException.class, () -> assertThat(testSubject.getFile("../Shared Data/Program Files/AFIS/AFIS.xbs")));
	}

	@Test
	public void testIllegalHash() throws Exception {
		final val rootPath = new TemporaryFolder();
		rootPath.create();
		final val rootFolder = rootPath.getRoot().toPath();
		final val internalFolder = rootPath.newFolder("Internal").toPath();
		final val md5Folder = internalFolder.resolve("MD5");
		final val zipsFolder = internalFolder.resolve("Zips");
		final val logsFolder = internalFolder.resolve("UserLogs");
		final val sharedDataFolder = rootPath.newFolder("Shared Data").toPath();
		final val programFilesFolder = sharedDataFolder.resolve("Program Files");
		final val serviceFolder = programFilesFolder.resolve("AFIS");
		createDirectories(serviceFolder);
		createDirectories(md5Folder);
		createDirectories(zipsFolder);

		final val testSubject = new FilesController();
		testSubject.files = new FilesService(rootFolder.toString());
		testSubject.files.setUp();

		Assertions.assertThrows(NoSuchFileException.class, () -> testSubject.hashFile(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs")));
	}

	@Test
	public void testLegalZip() throws Exception {
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Authentication authentication = Mockito.mock(Authentication.class);
		CustomLogger logger = Mockito.mock(CustomLogger.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("test");

		final val testSubject = new FilesController();
		testSubject.customLogger = logger;

		final val rootPath = new TemporaryFolder();
		rootPath.create();
		final val rootFolder = rootPath.getRoot().toPath();
		final val internalFolder = rootPath.newFolder("Internal").toPath();
		final val md5Folder = internalFolder.resolve("MD5");
		final val zipsFolder = internalFolder.resolve("Zips");
		final val logsFolder = internalFolder.resolve("UserLogs");
		final val sharedDataFolder = rootPath.newFolder("Shared Data").toPath();
		final val programFilesFolder = sharedDataFolder.resolve("Program Files");
		final val serviceFolder = programFilesFolder.resolve("AFIS");
		createDirectories(serviceFolder);
		createDirectories(md5Folder);
		createDirectories(zipsFolder);

		testSubject.files = new FilesService(rootFolder.toString());
		testSubject.files.setUp();

		write(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs"), new String("<preferences></preferences>").getBytes(StandardCharsets.UTF_8));
		testSubject.createZip(Paths.get("Shared Data/Program Files/AFIS"));

		final val tempFolder = programFilesFolder.resolve("temp");
		createDirectories(tempFolder);
		assertThat(Files.exists(tempFolder.resolve("AFIS"))).isFalse();

		File tempFile = tempFolder.resolve("tempZipFile.zip").toFile();
		Files.write(tempFile.toPath(), testSubject.getZip("Shared Data/Program Files/AFIS.zip"));
		testSubject.unzipFile(tempFile, tempFolder);

		assertThat(Files.exists(tempFolder.resolve("Shared Data").resolve("Program Files").resolve("AFIS"))).isTrue();
		assertThat(Files.exists(tempFolder.resolve("Shared Data").resolve("Program Files").resolve("AFIS").resolve("AFIS.xbs"))).isTrue();
		assertThat(readAllBytes(tempFolder.resolve("Shared Data").resolve("Program Files").resolve("AFIS").resolve("AFIS.xbs")))
				.isEqualTo(readAllBytes(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs")));
	}

	@Test
	public void testLegalZipExists() throws Exception {
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Authentication authentication = Mockito.mock(Authentication.class);
		CustomLogger logger = Mockito.mock(CustomLogger.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("test");

		final val testSubject = new FilesController();
		testSubject.customLogger = logger;

		final val rootPath = new TemporaryFolder();
		rootPath.create();
		final val rootFolder = rootPath.getRoot().toPath();
		final val internalFolder = rootPath.newFolder("Internal").toPath();
		final val md5Folder = internalFolder.resolve("MD5");
		final val zipsFolder = internalFolder.resolve("Zips");
		final val logsFolder = internalFolder.resolve("UserLogs");
		final val sharedDataFolder = rootPath.newFolder("Shared Data").toPath();
		final val programFilesFolder = sharedDataFolder.resolve("Program Files");
		final val serviceFolder = programFilesFolder.resolve("AFIS");
		createDirectories(serviceFolder);
		createDirectories(md5Folder);
		createDirectories(zipsFolder);

		testSubject.files = new FilesService(rootFolder.toString());
		testSubject.files.setUp();

		write(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs"), new String("<preferences></preferences>").getBytes(StandardCharsets.UTF_8));
		write(programFilesFolder.resolve("AFIS.zip"), new String("").getBytes(StandardCharsets.UTF_8));

		testSubject.createZip(Paths.get("Shared Data/Program Files/AFIS"));

		final val tempFolder = programFilesFolder.resolve("temp");
		createDirectories(tempFolder);

		File tempFile = tempFolder.resolve("tempZipFile.zip").toFile();
		Files.write(tempFile.toPath(), testSubject.getZip("Shared Data/Program Files/AFIS.zip"));
		testSubject.unzipFile(tempFile, tempFolder);

		assertThat(tempFolder.resolve("Shared Data").resolve("Program Files").resolve("AFIS").toFile().exists()).isTrue();

		byte[] unzipped = readAllBytes(findFile("AFIS.xbs", tempFolder.toFile()).toPath());
		assertThat(readAllBytes(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs"))).isEqualTo(unzipped);
		assertThat(readAllBytes(zipsFolder.resolve("Shared Data").resolve("Program Files").resolve("AFIS.zip")))
				.isNotEqualTo(new String("").getBytes(StandardCharsets.UTF_8));
		assertThat(unzipped).isEqualTo("<preferences></preferences>".getBytes(StandardCharsets.UTF_8));
	}

	@Test
	public void testIllegalZip() throws Exception {
		final val rootPath = new TemporaryFolder();
		rootPath.create();
		final val rootFolder = rootPath.getRoot().toPath();
		final val internalFolder = rootPath.newFolder("Internal").toPath();
		final val md5Folder = internalFolder.resolve("MD5");
		final val zipsFolder = internalFolder.resolve("Zips");
		final val logsFolder = internalFolder.resolve("UserLogs");
		final val sharedDataFolder = rootPath.newFolder("Shared Data").toPath();
		final val programFilesFolder = sharedDataFolder.resolve("Program Files");
		final val serviceFolder = programFilesFolder.resolve("AFIS");
		createDirectories(serviceFolder);
		createDirectories(md5Folder);
		createDirectories(zipsFolder);

		final val testSubject = new FilesController();
		testSubject.files = new FilesService(rootFolder.toString());
		testSubject.files.setUp();

		assertThrows(java.io.FileNotFoundException.class, () -> testSubject.createZip(serviceFolder.resolve("AFIS.xbs")));
	}

	@Test
	public void testLegalZipAll() throws Exception {
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Authentication authentication = Mockito.mock(Authentication.class);
		CustomLogger logger = Mockito.mock(CustomLogger.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("test");

		final val testSubject = new FilesController();
		testSubject.customLogger = logger;

		final val rootPath = new TemporaryFolder();
		rootPath.create();
		final val rootFolder = rootPath.getRoot().toPath();
		final val internalFolder = rootPath.newFolder("Internal").toPath();
		final val md5Folder = internalFolder.resolve("MD5");
		final val zipsFolder = internalFolder.resolve("Zips");
		final val logsFolder = internalFolder.resolve("UserLogs");
		final val sharedDataFolder = rootPath.newFolder("Shared Data").toPath();
		final val programFilesFolder = sharedDataFolder.resolve("Program Files");
		final val serviceFolder = programFilesFolder.resolve("AFIS");
		createDirectories(serviceFolder);
		createDirectories(md5Folder);
		createDirectories(zipsFolder);

		testSubject.files = new FilesService(rootFolder.toString());
		testSubject.files.setUp();

		write(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs"), new String("<preferences></preferences>").getBytes(StandardCharsets.UTF_8));
		write(programFilesFolder.resolve("AFIS.zip"), new String("").getBytes(StandardCharsets.UTF_8));
		int hexOld = readAllBytes(programFilesFolder.resolve("AFIS.zip")).hashCode();

		testSubject.zipAll();

		assertThat(Files.exists(programFilesFolder.resolve("AFIS.zip"))).isTrue();
		assertThat(readAllBytes(programFilesFolder.resolve("AFIS.zip")).hashCode()).isNotEqualTo(hexOld);
	}

	@Test
	public void testZipAllAndHashAll() throws Exception {
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Authentication authentication = Mockito.mock(Authentication.class);
		CustomLogger logger = Mockito.mock(CustomLogger.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("test");

		final val testSubject = new FilesController();
		testSubject.customLogger = logger;

		final val rootPath = new TemporaryFolder();
		rootPath.create();
		final val rootFolder = rootPath.getRoot().toPath();
		final val internalFolder = rootPath.newFolder("Internal").toPath();
		final val md5Folder = internalFolder.resolve("MD5");
		final val zipsFolder = internalFolder.resolve("Zips");
		final val logsFolder = internalFolder.resolve("UserLogs");
		final val sharedDataFolder = rootPath.newFolder("Shared Data").toPath();
		final val programFilesFolder = sharedDataFolder.resolve("Program Files");
		final val serviceFolder = programFilesFolder.resolve("AFIS");
		createDirectories(serviceFolder);
		createDirectories(md5Folder);
		createDirectories(zipsFolder);

		testSubject.files = new FilesService(rootFolder.toString());
		testSubject.files.setUp();

		write(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs"), new String("<preferences></preferences>").getBytes(StandardCharsets.UTF_8));
		write(serviceFolder.resolve("AFIS.zip"), new String("").getBytes(StandardCharsets.UTF_8));
		write(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs.md5"), new String("").getBytes(StandardCharsets.UTF_8));
		byte[] old = readAllBytes(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs.md5"));

		testSubject.zipAll();
		testSubject.hashAll();

		assertThat(readAllBytes(md5Folder.resolve("Shared Data").resolve("Program Files").resolve("AFIS").resolve("AFIS.xbs.md5"))).isNotEqualTo(old);
		assertThat(Files.exists(md5Folder.resolve("Internal").resolve("Zips").resolve("Shared Data").resolve("Program Files").resolve("AFIS.zip.md5")))
				.isTrue();
		assertThat(readAllBytes(md5Folder.resolve("Internal").resolve("Zips").resolve("Shared Data").resolve("Program Files").resolve("AFIS.zip.md5")))
				.isNotEmpty();
		assertThat(readAllBytes(md5Folder.resolve("Internal").resolve("Zips").resolve("Shared Data").resolve("Program Files").resolve("AFIS.zip.md5")))
				.isEqualTo(testSubject.getHash("Shared Data/Program Files/AFIS.zip"));

		// das zippen ist nicht deterministisch und würde auf github dazu führen, dass der Test abbricht, obwohl er local funktioniert
		// assertThat(readAllBytes(programFilesFolder.resolve("AFIS.zip.md5"))).isEqualTo("51a1713197b136586344905c9847daff".getBytes(StandardCharsets.UTF_8));
		assertThat(readAllBytes(md5Folder.resolve("Shared Data").resolve("Program Files").resolve("AFIS").resolve("AFIS.xbs.md5")))
				.isEqualTo("093544245ba5b8739014ac4e5a273520".getBytes(StandardCharsets.UTF_8));

		testSubject.hashAll();
	}

	@Test
	public void getZipBackCompatability() throws Exception {
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Authentication authentication = Mockito.mock(Authentication.class);
		CustomLogger logger = Mockito.mock(CustomLogger.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(authentication.getName()).thenReturn("test");

		final val testSubject = new FilesController();
		testSubject.customLogger = logger;

		final val rootPath = new TemporaryFolder();
		rootPath.create();
		final val rootFolder = rootPath.getRoot().toPath();
		final val internalFolder = rootPath.newFolder("Internal").toPath();
		final val md5Folder = internalFolder.resolve("MD5");
		final val zipsFolder = internalFolder.resolve("Zips");
		final val logsFolder = internalFolder.resolve("UserLogs");
		final val sharedDataFolder = rootPath.newFolder("Shared Data").toPath();
		final val programFilesFolder = sharedDataFolder.resolve("Program Files");
		final val serviceFolder = programFilesFolder.resolve("AFIS");
		createDirectories(serviceFolder);
		createDirectories(md5Folder);
		createDirectories(zipsFolder);

		testSubject.files = new FilesService(rootFolder.toString());
		testSubject.files.setUp();

		write(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs"), new String("<preferences></preferences>").getBytes(StandardCharsets.UTF_8));
		testSubject.createZip(Paths.get("Shared Data/Program Files/AFIS"));

		assertThat(testSubject.getFile("Shared Data/Program Files/AFIS.zip")).isEqualTo(testSubject.getZip("Shared Data/Program Files/AFIS"));

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
