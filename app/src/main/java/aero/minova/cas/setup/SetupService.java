package aero.minova.cas.setup;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilderFactory;

import jakarta.annotation.PostConstruct;
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
import aero.minova.cas.service.FilesService;
import aero.minova.cas.setup.dependency.DependencyOrder;
import aero.minova.cas.sql.SystemDatabase;
import lombok.val;

import static aero.minova.cas.resources.ResourceFileSystemProvider.FILE_SYSTEM_PROVIDER;

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

    @PostConstruct
    private void setup() {
        spc.registerExtension(PROCEDURE_NAME, inputTable -> {
            try {
                SqlProcedureResult result = new SqlProcedureResult();
                // ANSI_WARNINGS OFF ignoriert Warnung bei zu langen Datensätzen und schneidet stattdessen diese direkt ab.
                // So können auch längere SQL Benutzernamen genutzt werden, ohne die Tabellen anzupasssen (Siehe Azure SKY).
                database.getConnection().createStatement().execute("set ANSI_WARNINGS off");
                readSetups(service.getSystemFolder().resolve("setup").resolve("Setup.xml")//
                        , service.getSystemFolder().resolve("setup").resolve("dependency-graph.json")//
                        , service.getSystemFolder().resolve("setup")//
                        , true);
                svc.setupExtensions();
                spc.setupExtensions();

                // Diese Methode darf erst ganz zum Schluss ausgeführt werden, damit sichergestellt werden kann, dass der Admin tatsächlich ALLE Rechte bekommt.
                spc.setupPrivileges();
                database.getConnection().createStatement().execute("set ANSI_WARNINGS on");
                return new ResponseEntity(result, HttpStatus.ACCEPTED);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        spc.registerExtensionBootstrapCheck(PROCEDURE_NAME, inputTable -> true);
    }

    /**
     * Liest die setup-Dateien der Dependencies und gibt eine Liste an Strings mit den benötigten SQL-Dateien zurück.
     *
     * @param arg Ein String, in welchem die benötigten Dependencies stehen.
     * @throws IOException Wenn kein Setup-File für eine benötigte Dependency gefunden werden kann.
     */
    List<String> readSetups(Path setupPath, Path dependencyList, Path dependencySetupsDir, boolean setupTableSchemas) throws IOException {
        List<String> dependencies = DependencyOrder.determineDependencyOrder(Files.readString(dependencyList));
        logger.logSetup("Dependency Installation Order: " + dependencies);
        final List<String> procedures = new ArrayList<>();
        for (String dependency : dependencies) {
            logger.logSetup("Searching for setup.xml for dependency " + dependency);
            final Path setupXml = findSetupXml(dependency, dependencySetupsDir);
            logger.logSetup("Installing setup: " + setupXml + ", " + dependency + ", " + dependencySetupsDir);
            if (setupTableSchemas) {
                installToolIntegration.installSetup(setupXml);
            }
            final List<String> newProcedures = readProceduresToList(setupXml);
            procedures.addAll(newProcedures);
        }
        if (Files.exists(setupPath)) {
            if (setupTableSchemas) {
                installToolIntegration.installSetup(setupPath);
            }
            List<String> newProcedures = readProceduresToList(setupPath);
            procedures.addAll(newProcedures);
        } else {
            throw new NoSuchFileException("No main-setup file '" + setupPath + "' found!");
        }
        return procedures;
    }

    /**
     * Das ist ein Hack, wil wir Probleme haben über Maven die gewünschte Ordnerstruktur zu bekommen. Wir wollen, dass es erstmal grundsätzlich läuft.
     *
     * @param dependency Name der Abhängigkeit.
     * @return Setup.xml der Abhängigkeit.
     */
    private Path findSetupXml(String dependency, Path dependencySetupsDir) {
        String niceSetupFile = dependency + ".setup.xml";
        Path dependencySetupFile = dependencySetupsDir.resolve(niceSetupFile);
        if (Files.exists(dependencySetupFile)) {
            logger.logSetup("Reading Setup-File: " + niceSetupFile);
            return dependencySetupFile;
        }
        /*
         * Immer ab dem zweiten Punkt die Dependency abschneiden. Bsp: aero.minova.cas.app --> cas.app oder com.minova.cas.app --> cas.app Wenn dies kein Hack
         * wäre, würde es hiermit enden. else { throw new NoSuchFileException("No setup file found with the name " + setupFile); }
         */
        final String adjustedDependency = dependency.substring(dependency.indexOf(".", dependency.indexOf(".") + 1) + 1);
        try {
            if (true) {
                final Optional<Path> result = FILE_SYSTEM_PROVIDER.walk(dependencySetupsDir).stream().map(dir -> {
                    if (dir.toString().startsWith("setup/" + adjustedDependency + "-") || dir.getFileName().toString().equals(adjustedDependency)) {
                        if (dir.getFileName().toString().equalsIgnoreCase("Setup.xml")) {
                            return Optional.of(dir);
                        }
                    }
                    return Optional.<Path>empty();
                }).filter(Optional::isPresent).map(Optional::get).findFirst();
                if (result.isEmpty()) {
                    throw new NoSuchFileException("No setup file found with the name " + niceSetupFile);
                }
                return result.get();
            } else {
                final Optional<Path> result = Files.walk(dependencySetupsDir, 1).map(dir -> {
                    if (dir.getFileName().toString().startsWith(adjustedDependency + "-") || dir.getFileName().toString().equals(adjustedDependency)) {
                        try {
                            return Files.walk(dir)//
                                    .filter(f -> f.getFileName().toString().equalsIgnoreCase("setup.xml"))//
                                    .findFirst();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return Optional.<Path>empty();
                }).filter(Optional::isPresent).map(Optional::get).findFirst();
                if (result.isEmpty()) {
                    throw new NoSuchFileException("No setup file found with the name " + niceSetupFile);
                }
                return result.get();
            }
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
     * @param procedures Die Liste an SQL-Dateinamen.
     * @throws NoSuchFileException Falls die Datei passend zum Namen nicht existiert.
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
                        connection.prepareCall(procedure).execute();
                        connection.commit();
                    } catch (Exception e) {
                        logger.logSetup("Script " + procedureName + " is being executed.");
                        // Falls das beim ersten Versuch die Prozedur/View noch nicht existiert, wird sie hier angelegt.
                        if (procedure.startsWith("alter ")) {
                            procedure = procedure.substring(5);
                            procedure = "create" + procedure;
                        }

                        connection.prepareCall(procedure).execute();
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
