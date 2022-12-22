package aero.minova.cas.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import org.junit.AfterClass;
import org.springframework.boot.test.context.SpringBootTest;

import aero.minova.cas.service.FilesService;

public abstract class BaseTest {

	@AfterClass
	public static void cleanInternalFolder() throws Exception {
		Path internalFolder = Paths.get(new File("Internal").getAbsolutePath());
		if (internalFolder.toFile().exists()) {
			FilesService fs = new FilesService();
			// Den gesamten Inhalt des Internal-Ordners zusammen sammeln und löschen
			List<Path> internalContent = fs.populateFilesList(internalFolder);
			// Liste an Paths einmal umdrehen, damit erst der Inhalt der Unterordner und zum Schluss der Ordner gelöscht wird,
			// da dieser nur gelöscht werden kann, wenn sein Verzeichnis leer ist
			Collections.reverse(internalContent);
			for (Path p : internalContent) {
				p.toFile().delete();
			}

			// Den leeren Internal Ordner zum Schluss löschen
			internalFolder.toFile().delete();
		}
	}
}
