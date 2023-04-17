package aero.minova.cas.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.junit.AfterClass;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.restapi.ClientRestAPI;
import aero.minova.cas.service.FilesService;

public abstract class BaseTest {

	Gson gson;

	@Autowired
	ClientRestAPI clientRestAPI;

	@AfterClass
	protected static void cleanInternalFolder() throws Exception {
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

	@SuppressWarnings("resource")
	public Table readTableFromExampleJson(String fileName) {

		gson = clientRestAPI.getGson();
		return gson.fromJson(new Scanner(getClass()//
				.getClassLoader()//
				.getResourceAsStream(fileName + ".json"), "UTF-8")//
						.useDelimiter("\\A")//
						.next(),
				Table.class);
	}

}
