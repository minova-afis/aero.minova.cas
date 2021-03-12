package aero.minova.core.application.system.controller;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Files.write;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.val;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FileControllerTest {

	@Test
	public void testLegal() throws Exception {
		final val rootPath = new TemporaryFolder();
		rootPath.create();
		final val rootFolder = rootPath.getRoot().toPath();
		final val sharedDataFolder = rootPath.newFolder("Shared Data").toPath();
		final val programFilesFolder = sharedDataFolder.resolve("Program Files");
		final val serviceFolder = programFilesFolder.resolve("AFIS");
		createDirectories(serviceFolder);

		final val testSubject = new FilesController();
		testSubject.files = new FilesService(rootFolder.toString());
		testSubject.files.setUp();

		write(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs"), new String("<preferences></preferences>").getBytes(StandardCharsets.UTF_8));
		assertThat(testSubject.getFile("Shared Data/Program Files/AFIS/AFIS.xbs")).isEqualTo("<preferences></preferences>".getBytes(StandardCharsets.UTF_8));
	}

	@Test
	public void testLegalHash() throws Exception {
		final val rootPath = new TemporaryFolder();
		rootPath.create();
		final val rootFolder = rootPath.getRoot().toPath();
		final val sharedDataFolder = rootPath.newFolder("Shared Data").toPath();
		final val programFilesFolder = sharedDataFolder.resolve("Program Files");
		final val serviceFolder = programFilesFolder.resolve("AFIS");
		createDirectories(serviceFolder);

		final val testSubject = new FilesController();
		testSubject.files = new FilesService(rootFolder.toString());
		testSubject.files.setUp();

		write(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs"), new String("<preferences></preferences>").getBytes(StandardCharsets.UTF_8));
		assertThat(testSubject.getHash("Shared Data/Program Files/AFIS/AFIS.xbs"))
				.isEqualTo("093544245ba5b8739014ac4e5a273520".getBytes(StandardCharsets.UTF_8));
	}

	@Test
	public void testIllegal() throws Exception {
		final val rootPath = new TemporaryFolder();
		rootPath.create();
		final val rootFolder = rootPath.getRoot().toPath();
		final val sharedDataFolder = rootPath.newFolder("Shared Data").toPath();
		final val programFilesFolder = sharedDataFolder.resolve("Program Files");
		final val serviceFolder = programFilesFolder.resolve("AFIS");
		createDirectories(serviceFolder);

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
		final val sharedDataFolder = rootPath.newFolder("Shared Data").toPath();
		final val programFilesFolder = sharedDataFolder.resolve("Program Files");
		final val serviceFolder = programFilesFolder.resolve("AFIS");
		createDirectories(serviceFolder);

		final val testSubject = new FilesController();
		testSubject.files = new FilesService(rootFolder.toString());
		testSubject.files.setUp();

		Assertions.assertThrows(IllegalAccessException.class, () -> assertThat(testSubject.getHash("../Shared Data/Program Files/AFIS/AFIS.xbs")));
	}

	@Test
	public void testLegalZip() throws Exception {
		final val rootPath = new TemporaryFolder();
		rootPath.create();
		final val rootFolder = rootPath.getRoot().toPath();
		final val sharedDataFolder = rootPath.newFolder("Shared Data").toPath();
		final val programFilesFolder = sharedDataFolder.resolve("Program Files");
		final val serviceFolder = programFilesFolder.resolve("AFIS");
		createDirectories(serviceFolder);

		final val testSubject = new FilesController();
		testSubject.files = new FilesService(rootFolder.toString());
		testSubject.files.setUp();

		write(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs"), new String("<preferences></preferences>").getBytes(StandardCharsets.UTF_8));
		byte[] zipped = testSubject.getZip(serviceFolder.toString());

		final byte[] buffer = new byte[1024];
		ByteArrayInputStream bais = new ByteArrayInputStream(zipped);
		int i = zipped.length;
		final ZipInputStream zip = new ZipInputStream(bais);
		final ByteArrayOutputStream out = new ByteArrayOutputStream(zipped.length);
		ZipEntry ze = null;
		int anzahl = 0;
		if ((ze = zip.getNextEntry()) != null) {
			while ((anzahl = zip.read(buffer)) != -1) {
				out.write(buffer, 0, anzahl);
			}
		}
		zip.close();
		out.close();
		byte[] unzipped = out.toByteArray();
		assertThat(readAllBytes(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs"))).isEqualTo(unzipped);
		assertThat(unzipped).isEqualTo("<preferences></preferences>".getBytes(StandardCharsets.UTF_8));
	}

	@Test
	public void testIllegalZip() throws Exception {
		final val rootPath = new TemporaryFolder();
		rootPath.create();
		final val rootFolder = rootPath.getRoot().toPath();
		final val sharedDataFolder = rootPath.newFolder("Shared Data").toPath();
		final val programFilesFolder = sharedDataFolder.resolve("Program Files");
		final val serviceFolder = programFilesFolder.resolve("AFIS");
		createDirectories(serviceFolder);

		final val testSubject = new FilesController();
		testSubject.files = new FilesService(rootFolder.toString());
		testSubject.files.setUp();

		Assertions.assertThrows(IllegalAccessException.class, () -> assertThat(testSubject.getZip("../Shared Data/Program Files/AFIS/AFIS.xbs")));
	}

}
