package aero.minova.core.application.system.setup;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilderFactory;

import aero.minova.core.application.system.service.FilesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import aero.minova.core.application.system.controller.SqlProcedureController;
import aero.minova.core.application.system.domain.SqlProcedureResult;
import aero.minova.core.application.system.sql.SystemDatabase;
import lombok.val;

/**
 * Installiert sämtliche Komponenten und Abhängigkeiten des APP-Servers (aero.minova.app.parent)
 * anhand der "Setup.xml"s aus "{@link FilesService#rootPath}/setup/**".
 * Dabei werden zuerst die Abhängigkeiten aus "{@link FilesService#rootPath}/setup/dependencyList.txt"
 * ausgelesen und die entsprechenden "Setup.xml"s in "{@link FilesService#rootPath}/setup/**" installiert.
 * Anschließend wird die Hauptkomponente anhand der "{@link FilesService#rootPath}/setup/Setup.xml" installiert.
 *
 * TODO SQL-Code (nicht Schema) wird doppelt ausgeführt.
 */
@Service
public class SetupService {

	@Autowired InstallToolIntegration installToolIntegration;

	@Autowired
	public SystemDatabase database;

	@Autowired
	public FilesService service;

	@Autowired
	SqlProcedureController spc;

	Logger logger = LoggerFactory.getLogger(SetupService.class);

	@PostConstruct
	private void setup() {
		// Fügt Extension hinzu.
		spc.registerExctension("setup", inputTable -> {
			try {
				SqlProcedureResult result = new SqlProcedureResult();
				Path dependencyList = service.getSystemFolder().resolve("setup").resolve("dependencyList.txt");
				if (dependencyList.toFile().exists()) {
					List<String> procedures = readSetups(Files.readString(dependencyList), true);
					runDependencyProcedures(procedures);
				} else {
					throw new NoSuchFileException("No dependencyList.txt found!");
				}
				return new ResponseEntity(result, HttpStatus.ACCEPTED);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

	/**
	 * Die Methode überspringt die erste Zeile (bis nach \n) und schneidet alles ab dem String "The following files have NOT been resolved:" ab. Danach wird
	 * jede Zeile als ein Eintrag in der Rückgabe-Liste gesetzt. Dabei wird noch die Endung :jar entfernt und alle : durch . ersetzt.
	 *
	 * @param arg Ein String, in welchem die benötigten Dependencies stehen.
	 * @return Gibt eine Liste an String zurück, welche die benötigten Prozedur-Dateinamen beinhalten: prozedur.sql.
	 */
	List<String> parseDependencyList(String arg) {
		// Die obere Zeile und die die Zeilen mit den nicht resolvten Dateien abschneiden.
		String filteredArg = arg.substring(0, arg.indexOf("The following files have NOT been resolved:")).substring(arg.indexOf("\n") + 1);

		// Am Zeilenumbruch trennen und störende Leerzeichen entfernen.
		List<String> dependencies = Stream.of(filteredArg.split("\\R"))//
				.filter(s -> !s.contains("The following files have been resolved:"))//
				.filter(s -> !s.isBlank())//
				.filter(s -> s.contains("jar"))//
				.map(s -> s.substring(0 + 1, s.indexOf(":jar")).replace(":", ".").strip())//
				.collect(Collectors.toList());
		Collections.reverse(dependencies);
		return dependencies;
	}

	/**
	 * Liest die setup-Dateien der Dependencies und gibt eine Liste an Strings mit den benötigten SQL-Dateien zurück.
	 *
	 * @param arg Ein String, in welchem die benötigten Dependencies stehen.
	 * @return Die Liste an SQL-Dateien als Strings.
	 * @throws IOException Wenn kein Setup-File für eine benötigte Dependency gefunden werden kann.
	 */
	List<String> readSetups(String arg, boolean setupTableSchemas) throws IOException {
		List<String> dependencies = parseDependencyList(arg);
		Path dependencySetupsDir = service.getSystemFolder().resolve("setup");
		
		List<String> procedures = new ArrayList<>();

		// Zuerst durch alle Dependencies durchgehen.
		for (String dependency : dependencies) {
			final Path setupXml = findSetupXml(dependency, dependencySetupsDir);
			if (setupTableSchemas) {
				installToolIntegration.installSetup(setupXml);
			}
			procedures.addAll(readProceduresToList(setupXml.toFile()));
		}

		// Danach muss das Hauptsetup-File ausgelesen werden.
		final Path mainSetupXml = dependencySetupsDir.resolve("Setup.xml");
		File mainSetupFile = mainSetupXml.toFile();
		if (setupTableSchemas) {
			installToolIntegration.installSetup(mainSetupXml);
		}
		if (mainSetupFile.exists()) {
			procedures.addAll(readProceduresToList(mainSetupFile));
		} else {
			throw new NoSuchFileException("No main-setup file found!");
		}

		return procedures;
	}

	/**
	 * Das ist ein Hack, wil wir Probleme haben über Maven die gewünschte Ordnerstruktur zu bekommen.
	 * Wir wollen, dass es erstmal grundsätzlich läuft.
	 *
	 * @param dependency Name der Abhängigkeit.
	 * @return Setup.xml der Abhängigkeit.
	 */
	private Path findSetupXml(String dependency, Path dependencySetupsDir) {
		String niceSetupFile = dependency + ".setup.xml";
		Path dependencySetupFile = dependencySetupsDir.resolve(niceSetupFile);
		if (Files.exists(dependencySetupFile)) {
			logger.info("Reading Setup-File: " + niceSetupFile);
			return dependencySetupFile;
		}
		/* Wenn dies kein Hack wäre, würde es hiermit enden.
		 * else {
		 * throw new NoSuchFileException("No setup file found with the name " + setupFile);
		 *}
		 */
		final String adjustedDependency = dependency.substring("aero.minova.".length());
		try {
			final Optional<Path> result = Files.walk(dependencySetupsDir, 1)
					.map(dir -> {
						if (dir.getFileName().toString().startsWith(adjustedDependency)) {
							try {
								Optional<Path> setup = Files.walk(dir)//
										.filter(f -> f.getFileName().toString().equals("Setup.xml"))//
										.findFirst();
								return setup;
							} catch (IOException e) {
								throw new RuntimeException(e);
							}
						}
						return Optional.<Path>empty();
					})
					.filter(setup -> !setup.isEmpty())
					.map(setup -> setup.get())
					.findFirst();
			if (result.isEmpty()) {
				throw new NoSuchFileException("No setup file found with the name " + niceSetupFile);
			}
			return result.get();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Liest ein einzelnes Setup-File und gibt eine Liste von benötigten SQl-Dateien zurück.
	 *
	 * @param dependencySetupFile Das Setup-File, welches gescannt werden soll.
	 * @return Die Liste an SQL-Dateinamen für das gescannte Setup-File.
	 */
	List<String> readProceduresToList(File dependencySetupFile) {
		List<String> procedures = new ArrayList<>();
		// Dokument auslesen
		try {
			Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(dependencySetupFile);
			Node n = d.getDocumentElement().getElementsByTagName("sql-code").item(0);

			if (n == null) {
				return procedures;
			}

			// Jeden Eintrag im sql-code-Tag auslesen und in eine List packen.
			for (int i = 0; n.getChildNodes().getLength() > i; i++) {
				Element s = (Element) ((Element) n).getElementsByTagName("script").item(i);
				if (s != null) {
					String procedureName = s.getAttribute("name") + ".sql";
					procedures.add(procedureName);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error in  " + dependencySetupFile + ". The file could not be red.", e);
		}
		return procedures;
	}

	/**
	 * Liest die übergebene Liste an SQL-Dateinamen und installiert die jeweils dazugehörige Datei.
	 *
	 * @param procedures Die Liste an SQL-Dateinamen.
	 * @throws NoSuchFileException Falls die Datei passend zum Namen nicht existiert.
	 */
	void runDependencyProcedures(List<String> procedures) throws NoSuchFileException {
		Path dependencySqlDir = service.getSystemFolder().resolve("sql");
		for (String procedureName : procedures) {
			Path sqlFile = dependencySqlDir.resolve(procedureName);
			if (sqlFile.toFile().exists()) {
				final val connection = database.getConnection();
				try {
					// Den Sql-Code aus der Datei auslesen und ausführen.
					String procedure = Files.readString(sqlFile);

					logger.info("Executing Procedure/View " + procedureName);
					try {
						connection.prepareCall(procedure).execute();
						connection.commit();
					} catch (Exception e) {
						logger.info("Prozedur/View " + procedureName + " is being installed.");
						// Falls das beim ersten Versuch die Prozedur/View noch nicht existiert, wird sie hier angelegt.
						procedure = procedure.substring(5);
						procedure = "create" + procedure;

						connection.prepareCall(procedure).execute();
						connection.commit();
					}
				} catch (Exception e) {
					throw new RuntimeException("Error in " + procedureName + ". The file could not be red.", e);
				} finally {
					database.freeUpConnection(connection);
				}

			} else {
				throw new NoSuchFileException("No File found with the name " + procedureName);
			}
		}
	}
}
