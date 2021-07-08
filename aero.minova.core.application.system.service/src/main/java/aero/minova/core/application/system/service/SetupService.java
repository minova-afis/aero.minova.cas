package aero.minova.core.application.system.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import aero.minova.core.application.system.sql.SystemDatabase;
import lombok.val;

@Service
public class SetupService {

	@Autowired
	public SystemDatabase database;

	@Autowired
	public FilesService service;

	Logger logger = LoggerFactory.getLogger(SetupService.class);

	public List<String> parseDependencyList(String arg) {
		List<String> dependencies = new ArrayList<>();
		// Die obere Zeile und die die Zeilen mit den nicht resolvten Dateien abschneiden.
		arg = arg.substring(arg.indexOf("\n") + 1);
		arg = arg.substring(0, arg.indexOf("The following files have NOT been resolved:"));

		// Am Zeilenumbruch trennen und störende Leerzeichen entfernen.
		dependencies = Stream.of(arg.split("\n"))//
				.map(s -> s.substring(0 + 1, s.indexOf(":jar")).replace(":", ".").strip())//
				.collect(Collectors.toList());
		Collections.reverse(dependencies);
		return dependencies;
	}

	public void readSetups(List<String> dependencies) throws IOException {
		Path dependencySetupsDir = service.getSystemFolder().resolve("setup");

		// Zuerst muss das Hauptsetup-File ausgelesen werden.
		File mainSetupFile = dependencySetupsDir.resolve("Setup.xml").toFile();
		runProcedures(mainSetupFile);

		// Danach durch alle Dependencies durchgehen.
		for (String dependency : dependencies) {
			String setupFile = dependency + ".setup.xml";
			logger.info("Lese Setup-File: " + setupFile);

			File dependencySetupFile = dependencySetupsDir.resolve(setupFile).toFile();
			if (dependencySetupFile.exists()) {
				runProcedures(dependencySetupFile);
			} else {
				logger.error("Kein Setup-File mit dem Namen " + setupFile + " gefunden!");
			}
		}
	}

	public void runProcedures(File dependencySetupFile) {
		Path dependencySqlDir = service.getSystemFolder().resolve("sql");
		// Dokument auslesen
		try {
			Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(dependencySetupFile);
			Node n = d.getDocumentElement().getElementsByTagName("sql-code").item(0);

			// Jeden Eintrag im sql-code-Tag auslesen und ausführen.
			for (int i = 0; n.getChildNodes().getLength() > i; i++) {
				Element s = (Element) ((Element) n).getElementsByTagName("script").item(i);
				if (s != null) {
					String procedureName = s.getAttribute("name") + ".sql";

					Path sqlFile = dependencySqlDir.resolve(procedureName);
					if (sqlFile.toFile().exists()) {
						// Den Sql-Code aus der Datei auslesen und ausführen.
						String procedure = Files.readString(sqlFile);
						final val connection = database.getConnection();

						logger.info("Ausführen der Prozedur/View " + procedureName);
						try {
							connection.prepareCall(procedure).execute();
						} catch (Exception e) {
							logger.info("Prozedur/View " + procedureName + " existiert noch nicht und wird angelegt.");
							// Falls das beim ersten Versuch die Prozedur/View noch nicht existiert, wird sie hier angelegt.
							procedure.replace("alter", "create");
							connection.prepareCall(procedure).execute();
						}
					} else {
						logger.error("Keine Datei mit dem Namen " + procedureName + " gefunden!");
					}
				}
			}

		} catch (Exception e) {
			logger.error("Fehler in einer der Prozeduren in " + dependencySetupFile);
			e.getStackTrace();
		}
	}
}
