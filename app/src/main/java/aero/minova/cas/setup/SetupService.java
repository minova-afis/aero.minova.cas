package aero.minova.cas.setup;

import static aero.minova.cas.resources.ResourceFileSystemProvider.FILE_SYSTEM_PROVIDER;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.SqlProcedureResult;
import aero.minova.cas.controller.SqlProcedureController;
import aero.minova.cas.controller.SqlViewController;
import aero.minova.cas.resources.ResourcePath;
import aero.minova.cas.service.FilesService;
import aero.minova.cas.setup.dependency.DependencyOrder;
import aero.minova.cas.sql.SystemDatabase;
import jakarta.annotation.PostConstruct;
import lombok.val;

/**
 * Installiert sämtliche Komponenten und Abhängigkeiten des APP-Servers (aero.minova.app.parent) anhand der "Setup.xml"s aus
 * "{@link FilesService#rootPath}/setup/**". Dabei werden zuerst die Abhängigkeiten aus "{@link FilesService#rootPath}/setup/dependency-graph.json" ausgelesen
 * und die entsprechenden "Setup.xml"s in "{@link FilesService#rootPath}/setup/**" installiert. Anschließend wird die Hauptkomponente anhand der
 * "{@link FilesService#rootPath}/setup/Setup.xml" installiert. TODO SQL-Code (nicht Schema) wird doppelt ausgeführt.
 */
@Service
public class SetupService {

	private static final String PROCEDURE_NAME = "setup";

	@Autowired
	InstallToolIntegration installToolIntegration;

	@Autowired
	public SystemDatabase database;

	@Autowired
	public FilesService service;

	@Autowired
	SqlProcedureController spc;

	@Autowired
	SqlViewController svc;

	@Autowired
	CustomLogger logger;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostConstruct
	private void setup() {
		spc.registerExtension(PROCEDURE_NAME, inputTable -> {
			try {
				SqlProcedureResult result = new SqlProcedureResult();
				// ANSI_WARNINGS OFF ignoriert Warnung bei zu langen Datensätzen und schneidet stattdessen diese direkt ab.
				// So können auch längere SQL Benutzernamen genutzt werden, ohne die Tabellen anzupasssen (Siehe Azure SKY).
				try (final var connection = database.getConnection()) {
					setAnsiWarnings(connection, false);
					final Path nativeRoot = service.getSystemFolder();
					readSetups(nativeRoot.resolve("setup").resolve("Setup.xml")//
							, nativeRoot.resolve("setup").resolve("dependency-graph.json")//
							, nativeRoot.resolve("setup")//
							, true, nativeRoot, true);

					// Abwärtskompatibilität zu CAS 12: Ist ein Legacy-Verzeichnis konfiguriert, wird dessen Setup zusätzlich
					// ausgeführt (nicht statt des CAS-13-eigenen), damit sichergestellt ist, dass sowohl native als auch aus
					// CAS 12 übernommene Schemas/Prozeduren installiert werden. Fehlt dort ein Setup (z.B. weil der Kunde
					// keine eigene DB-relevante Legacy-Konfiguration hat), wird das nur geloggt, nicht als Fehler behandelt.
					// Siehe doc/md/CAS12Compatibility.md.
					if (service.hasLegacyFallback()) {
						final Path legacyRoot = service.getLegacyRoot();
						logger.logSetup("Legacy-Verzeichnis konfiguriert (" + legacyRoot + ") -- führe zusätzliches Setup aus.");
						readSetups(legacyRoot.resolve("setup").resolve("Setup.xml")//
								, legacyRoot.resolve("setup").resolve("dependency-graph.json")//
								, legacyRoot.resolve("setup")//
								, true, legacyRoot, false);
					}

					svc.setupExtensions();
					spc.setupExtensions();

					// Diese Methode darf erst ganz zum Schluss ausgeführt werden, damit sichergestellt werden kann, dass der Admin tatsächlich ALLE Rechte bekommt.
					spc.setupPrivileges();
					setAnsiWarnings(connection, true);
				}
				return new ResponseEntity(result, HttpStatus.ACCEPTED);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		spc.registerExtensionBootstrapCheck(PROCEDURE_NAME, inputTable -> true);
	}

	/**
	 * Sets ANSI_WARNINGS on or off for the connection.
	 * ANSI_WARNINGS OFF ignores warnings for data truncation and allows longer SQL usernames.
	 *
	 * @param connection The database connection
	 * @param enabled true to enable ANSI_WARNINGS, false to disable
	 */
	private void setAnsiWarnings(Connection connection, boolean enabled) {
		String sql = enabled ? "set ANSI_WARNINGS on" : "set ANSI_WARNINGS off";
		try (var statement = connection.createStatement()) {
			statement.execute(sql);
		} catch (Exception e) {
			logger.logError("Failed to set ANSI_WARNINGS to " + enabled, e);
			throw new RuntimeException("Failed to set ANSI_WARNINGS", e);
		}
	}

	/**
	 * Liest die setup-Dateien der Dependencies und gibt eine Liste an Strings mit den benötigten SQL-Dateien zurück. Wird eine Setup.xml einer Dependency
	 * nicht gefunden läuft das Setup trotzdem weiter.
	 *
	 * @param setupPath
	 *            Pfad zur Haupt-"Setup.xml".
	 * @param dependencyList
	 *            Pfad zur "dependency-graph.json".
	 * @param dependencySetupsDir
	 *            Verzeichnis, in dem nach den "Setup.xml"s der Dependencies gesucht wird.
	 * @param setupTableSchemas
	 *            Ob Tabellen-Schemas installiert werden sollen.
	 * @param filesRoot
	 *            Wurzelverzeichnis, unter dem sich "tables" und "sql" für diesen Lauf befinden (siehe {@link InstallToolIntegration#installSetup(Path, Path)}).
	 * @param required
	 *            Ob eine fehlende Haupt-"Setup.xml" das Setup abbrechen soll (true, CAS-13-eigenes Setup) oder nur geloggt wird (false, optionaler
	 *            CAS-12-Legacy-Lauf, siehe doc/md/CAS12Compatibility.md).
	 */
	List<String> readSetups(Path setupPath, Path dependencyList, Path dependencySetupsDir, boolean setupTableSchemas, Path filesRoot, boolean required)
			throws IOException {
		final List<String> procedures = new ArrayList<>();
		if (Files.exists(dependencyList)) {
			List<String> dependencies = DependencyOrder.determineDependencyOrder(Files.readString(dependencyList));
			logger.logSetup("Dependency Installation Order: " + dependencies);
			for (String dependency : dependencies) {
				logger.logSetup("Searching for setup.xml for dependency " + dependency);
				final Optional<Path> setupXml = findSetupXml(dependency, dependencySetupsDir);
				if (setupXml.isEmpty()) {
					logger.logError("No setup file found for dependency " + dependency + ". Continuing with setup.");
					continue;
				}
				logger.logSetup("Installing setup: " + setupXml + ", " + dependency + ", " + dependencySetupsDir);
				if (setupTableSchemas) {
					installToolIntegration.installSetup(setupXml.get(), filesRoot);
				}
				final List<String> newProcedures = readProceduresToList(setupXml.get());
				procedures.addAll(newProcedures);
			}
		} else {
			logger.logSetup("No dependency list found at " + dependencyList + " -- skipping dependency setups for this run.");
		}
		if (Files.exists(setupPath)) {
			if (setupTableSchemas) {
				installToolIntegration.installSetup(setupPath, filesRoot);
			}
			List<String> newProcedures = readProceduresToList(setupPath);
			procedures.addAll(newProcedures);
		} else if (required) {
			throw new NoSuchFileException("No main-setup file '" + setupPath + "' found!");
		} else {
			logger.logSetup("No main setup file '" + setupPath + "' found -- skipping this optional setup run.");
		}
		return procedures;
	}

	/**
	 * Das ist ein Hack, wil wir Probleme haben über Maven die gewünschte Ordnerstruktur zu bekommen. Wir wollen, dass es erstmal grundsätzlich läuft.
	 *
	 * @param dependency
	 *            Name der Abhängigkeit.
	 * @return Setup.xml der Abhängigkeit.
	 */
	private Optional<Path> findSetupXml(String dependency, Path dependencySetupsDir) throws IOException {
		String niceSetupFile = dependency + ".setup.xml";
		Path dependencySetupFile = dependencySetupsDir.resolve(niceSetupFile);
		if (Files.exists(dependencySetupFile)) {
			logger.logSetup("Reading Setup-File: " + niceSetupFile);
			return Optional.of(dependencySetupFile);
		}
		/*
		 * Immer ab dem zweiten Punkt die Dependency abschneiden. Bsp: aero.minova.cas.app --> cas.app oder com.minova.cas.app --> cas.app Wenn dies kein Hack
		 * wäre, würde es hiermit enden. else { throw new NoSuchFileException("No setup file found with the name " + setupFile); }
		 */
		final String adjustedDependency = dependency.substring(dependency.indexOf(".", dependency.indexOf(".") + 1) + 1);

		// dependencySetupsDir kann entweder ein virtueller ResourcePath (Fat-Jar-Classpath) oder ein echter Pfad auf der
		// Festplatte sein (Nicht-Fat-Jar-Modus, oder das CAS-12-Legacy-Verzeichnis, siehe doc/md/CAS12Compatibility.md).
		// FILE_SYSTEM_PROVIDER.walk(...) akzeptiert ausschliesslich ResourcePaths und wirft sonst eine IllegalArgumentException,
		// daher muss hier unterschieden werden (analog zu BaseSetup.readTableXml).
		if (dependencySetupsDir instanceof ResourcePath) {
			return FILE_SYSTEM_PROVIDER.walk(dependencySetupsDir).stream().map(dir -> {
				if ((dir.toString().startsWith("setup/" + adjustedDependency + "-") || dir.getFileName().toString().equals(adjustedDependency))
						&& dir.getFileName().toString().equalsIgnoreCase("Setup.xml")) {
					return Optional.of(dir);
				}
				return Optional.<Path> empty();
			}).filter(Optional::isPresent).map(Optional::get).findFirst();
		}

		if (!Files.isDirectory(dependencySetupsDir)) {
			return Optional.empty();
		}
		try (var files = Files.walk(dependencySetupsDir)) {
			return files.map(dir -> {
				String relative = "setup/" + dependencySetupsDir.relativize(dir).toString().replace('\\', '/');
				if ((relative.startsWith("setup/" + adjustedDependency + "-") || dir.getFileName().toString().equals(adjustedDependency))
						&& dir.getFileName().toString().equalsIgnoreCase("Setup.xml")) {
					return Optional.of(dir);
				}
				return Optional.<Path> empty();
			}).filter(Optional::isPresent).map(Optional::get).findFirst();
		}
	}

	/**
	 * Liest ein einzelnes Setup-File und gibt eine Liste von benötigten SQl-Dateien zurück.
	 *
	 * @param dependencySetupFile
	 *            Das Setup-File, welches gescannt werden soll.
	 * @return Die Liste an SQL-Dateinamen für das gescannte Setup-File.
	 */
	List<String> readProceduresToList(Path dependencySetupFile) {
		List<String> procedures = new ArrayList<>();
		// Dokument auslesen
		try {
			Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(Files.newInputStream(dependencySetupFile));
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
	 * TODO Entfernen Liest die übergebene Liste an SQL-Dateinamen und installiert die jeweils dazugehörige Datei.
	 *
	 * @param procedures
	 *            Die Liste an SQL-Dateinamen.
	 * @throws NoSuchFileException
	 *             Falls die Datei passend zum Namen nicht existiert.
	 */
	@Deprecated
	void runScripts(List<String> procedures) throws NoSuchFileException {
		Path dependencySqlDir = service.getSystemFolder().resolve("sql");
		for (String procedureName : procedures) {
			Path sqlFile = dependencySqlDir.resolve(procedureName);
			if (sqlFile.toFile().exists()) {
				val connection = database.getConnection();
				try {
					// Den Sql-Code aus der Datei auslesen und ausführen.
					String procedure = Files.readString(sqlFile);

					logger.logSetup("Executing Script " + procedureName);
					try {
						try (var stmt = connection.prepareCall(procedure)) {
							stmt.execute();
						}
						connection.commit();
					} catch (Exception e) {
						logger.logSetup("Script " + procedureName + " is being executed.");
						// Falls das beim ersten Versuch die Prozedur/View noch nicht existiert, wird sie hier angelegt.
						if (procedure.startsWith("alter ")) {
							procedure = procedure.substring(5);
							procedure = "create" + procedure;
						}

						try (var stmt = connection.prepareCall(procedure)) {
							stmt.execute();
						}
						connection.commit();
					}
				} catch (Exception e) {
					throw new RuntimeException("Error in " + procedureName + ". The file could not be red.", e);
				} finally {
					database.closeConnection(connection);
				}

			} else {
				throw new NoSuchFileException("No File found with the name " + procedureName);
			}
		}
	}
}
