package aero.minova.core.application.system.controller;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Files.write;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.rules.TemporaryFolder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import aero.minova.core.application.system.FilesService;
import lombok.val;

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
		byte[] zipped = testSubject.getZip(serviceFolder);
		File zippedFile = new File(serviceFolder + ".zip");
		Files.write(Paths.get(zippedFile.getAbsolutePath()), zipped);

		String fileName = programFilesFolder + File.separator + "AFIS2";

		File dir = new File(fileName);
		FileInputStream fis;
		byte[] buffer = new byte[1024];
		try {
			fis = new FileInputStream(zippedFile);
			ZipInputStream zis = new ZipInputStream(fis);
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				String zippedFileEntry = ze.getName();
				File newFile = new File(fileName + File.separator + zippedFileEntry);
				System.out.println("Unzipping to " + newFile.getAbsolutePath());
				// create directories for sub directories in zip
				new File(newFile.getParent()).mkdirs();
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				// close this ZipEntry
				zis.closeEntry();
				ze = zis.getNextEntry();
			}
			// close last ZipEntry
			zis.closeEntry();
			zis.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertThat(Files.exists(programFilesFolder.resolve("AFIS2"))).isTrue();
		assertThat(Files.exists(programFilesFolder.resolve("AFIS2").resolve("AFIS"))).isTrue();
		assertThat(Files.exists(programFilesFolder.resolve("AFIS2").resolve("AFIS").resolve("AFIS.xbs"))).isTrue();
		assertThat(readAllBytes(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs")))
				.isEqualTo(readAllBytes(programFilesFolder.resolve("AFIS2").resolve("AFIS").resolve("AFIS.xbs")));
	}

	@Test
	public void testLegalZipExists() throws Exception {
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
		write(programFilesFolder.resolve("AFIS.zip"), new String("").getBytes(StandardCharsets.UTF_8));

		byte[] zipped = testSubject.getZip(serviceFolder);

		final byte[] buffer = new byte[1024];
		ByteArrayInputStream bais = new ByteArrayInputStream(zipped);
		int i = zipped.length;
		final ZipInputStream zip = new ZipInputStream(bais);
		final ByteArrayOutputStream out = new ByteArrayOutputStream(zipped.length);
		ZipEntry ze = null;
		int len = 0;
		if ((ze = zip.getNextEntry()) != null) {
			while ((len = zip.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
		}
		zip.close();
		out.close();
		byte[] unzipped = out.toByteArray();
		assertThat(readAllBytes(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs"))).isEqualTo(unzipped);
		assertThat(readAllBytes(programFilesFolder.resolve("AFIS.zip"))).isNotEqualTo(new String("").getBytes(StandardCharsets.UTF_8));
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

		Assertions.assertThrows(IllegalAccessException.class, () -> assertThat(testSubject.getZip(Paths.get("../Shared Data/Program Files/AFIS/AFIS.xbs"))));
	}

	@Test
	public void testLegalZipAll() throws Exception {
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
		write(programFilesFolder.resolve("AFIS.zip"), new String("").getBytes(StandardCharsets.UTF_8));
		int hexOld = readAllBytes(programFilesFolder.resolve("AFIS.zip")).hashCode();

		testSubject.zipAll();

		assertThat(Files.exists(programFilesFolder.resolve("AFIS.zip"))).isTrue();
		assertThat(readAllBytes(programFilesFolder.resolve("AFIS.zip")).hashCode()).isNotEqualTo(hexOld);
	}

	@Test
	public void testZipAllAndHashAll() throws Exception {
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
		write(programFilesFolder.resolve("AFIS.zip"), new String("").getBytes(StandardCharsets.UTF_8));
		write(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs.md5"), new String("").getBytes(StandardCharsets.UTF_8));
		byte[] old = readAllBytes(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs.md5"));

		testSubject.zipAll();
		testSubject.hashAll();

		assertThat(readAllBytes(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs.md5"))).isNotEqualTo(old);
		assertThat(Files.exists(programFilesFolder.resolve("AFIS.zip.md5"))).isTrue();
		assertThat(readAllBytes(programFilesFolder.resolve("AFIS.zip.md5"))).isNotEmpty();
		assertThat(readAllBytes(programFilesFolder.resolve("AFIS.zip.md5"))).isEqualTo(testSubject.getHash(programFilesFolder.resolve("AFIS.zip").toString()));
		/*
		 * assertThat(readAllBytes(programFilesFolder.resolve("AFIS.zip.md5"))).isEqualTo("51a1713197b136586344905c9847daff".getBytes(StandardCharsets.UTF_8));
		 * assertThat(readAllBytes(programFilesFolder.resolve("AFIS").resolve("AFIS.xbs.md5")))
		 * .isEqualTo("093544245ba5b8739014ac4e5a273520".getBytes(StandardCharsets.UTF_8));
		 */
		testSubject.hashAll();
	}

}
