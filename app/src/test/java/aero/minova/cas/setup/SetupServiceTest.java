package aero.minova.cas.setup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import aero.minova.cas.CustomLogger;

/**
 * Testet die neue Mehrfach-Lauf-Logik von {@link SetupService#readSetups} isoliert, ohne Spring-Kontext oder echte Datenbank (siehe
 * doc/md/CAS12Compatibility.md): ein zweiter, optionaler Lauf gegen ein CAS-12-Legacy-Verzeichnis darf das Setup nicht abbrechen, wenn dort keine
 * "dependency-graph.json"/"Setup.xml" existiert -- der native Lauf bleibt dagegen verpflichtend.
 */
class SetupServiceTest {

	@TempDir
	Path tmp;

	SetupService testSubject;
	InstallToolIntegration installToolIntegration;

	@BeforeEach
	void setUp() {
		testSubject = new SetupService();
		installToolIntegration = mock(InstallToolIntegration.class);
		testSubject.installToolIntegration = installToolIntegration;
		testSubject.logger = mock(CustomLogger.class);
	}

	private Path writeMinimalSetupXml(Path dir) throws IOException {
		Files.createDirectories(dir);
		Path setupXml = dir.resolve("Setup.xml");
		Files.writeString(setupXml, "<setup></setup>");
		return setupXml;
	}

	@DisplayName("Fehlende dependency-graph.json wird übersprungen, Haupt-Setup läuft trotzdem")
	@Test
	void missingDependencyListSkipsDependencyProcessing() throws Exception {
		Path filesRoot = tmp.resolve("native");
		Path setupPath = writeMinimalSetupXml(filesRoot.resolve("setup"));
		Path missingDependencyList = filesRoot.resolve("setup").resolve("dependency-graph.json");

		testSubject.readSetups(setupPath, missingDependencyList, filesRoot.resolve("setup"), true, filesRoot, true);

		verify(installToolIntegration).installSetup(eq(setupPath), eq(filesRoot));
	}

	@DisplayName("Fehlendes Haupt-Setup bricht ab, wenn required=true")
	@Test
	void missingRequiredSetupPathThrows() {
		Path filesRoot = tmp.resolve("native");
		Path missingSetupPath = filesRoot.resolve("setup").resolve("Setup.xml");
		Path missingDependencyList = filesRoot.resolve("setup").resolve("dependency-graph.json");

		assertThrows(NoSuchFileException.class,
				() -> testSubject.readSetups(missingSetupPath, missingDependencyList, filesRoot.resolve("setup"), true, filesRoot, true));
	}

	@DisplayName("Fehlendes Haupt-Setup wird nur geloggt, wenn required=false (optionaler Legacy-Lauf)")
	@Test
	void missingOptionalSetupPathDoesNotThrow() throws Exception {
		Path legacyRoot = tmp.resolve("legacy");
		Path missingSetupPath = legacyRoot.resolve("setup").resolve("Setup.xml");
		Path missingDependencyList = legacyRoot.resolve("setup").resolve("dependency-graph.json");

		var procedures = testSubject.readSetups(missingSetupPath, missingDependencyList, legacyRoot.resolve("setup"), true, legacyRoot, false);

		assertThat(procedures).isEmpty();
		verify(installToolIntegration, never()).installSetup(any(), any());
	}

	@DisplayName("installSetup wird mit dem übergebenen filesRoot aufgerufen, nicht mit einem anderen Verzeichnis")
	@Test
	void installSetupUsesGivenFilesRoot() throws Exception {
		Path legacyRoot = tmp.resolve("legacy");
		Path setupPath = writeMinimalSetupXml(legacyRoot.resolve("setup"));
		Path missingDependencyList = legacyRoot.resolve("setup").resolve("dependency-graph.json");

		testSubject.readSetups(setupPath, missingDependencyList, legacyRoot.resolve("setup"), true, legacyRoot, false);

		verify(installToolIntegration).installSetup(eq(setupPath), eq(legacyRoot));
	}

	@DisplayName("setupTableSchemas=false ruft installSetup gar nicht auf")
	@Test
	void setupTableSchemasFalseSkipsInstall() throws Exception {
		Path filesRoot = tmp.resolve("native");
		Path setupPath = writeMinimalSetupXml(filesRoot.resolve("setup"));
		Path missingDependencyList = filesRoot.resolve("setup").resolve("dependency-graph.json");

		testSubject.readSetups(setupPath, missingDependencyList, filesRoot.resolve("setup"), false, filesRoot, true);

		verify(installToolIntegration, never()).installSetup(any(), any());
	}

	@DisplayName("Regressionstest: Fuzzy-Fallback in findSetupXml darf bei echtem Disk-Pfad nicht mit IllegalArgumentException crashen")
	@Test
	void fuzzyFallbackWorksAgainstRealDiskDirectory() throws Exception {
		// Minimaler Abhängigkeitsgraph mit genau einer Dependency, deren exakter Dateiname
		// ("aero.minova.legacytestdep.setup.xml") bewusst NICHT existiert, sodass findSetupXml auf den
		// Fuzzy-Fallback (vormals FILE_SYSTEM_PROVIDER.walk, das bei echten Disk-Pfaden mit
		// IllegalArgumentException crashte) zurückfallen muss.
		Path legacyRoot = tmp.resolve("legacy");
		Path setupDir = legacyRoot.resolve("setup");
		Path dependencyList = setupDir.resolve("dependency-graph.json");
		Files.createDirectories(setupDir);
		Files.writeString(dependencyList,
				"{\"dependencies\":[{\"from\":\"com.minova:mytestmodule:jar\",\"to\":\"aero.minova:legacytestdep:jar:app\"}]}");

		Path fuzzyMatch = writeMinimalSetupXml(setupDir.resolve("legacytestdep-1.2.3"));
		Path mainSetupPath = writeMinimalSetupXml(setupDir);

		testSubject.readSetups(mainSetupPath, dependencyList, setupDir, true, legacyRoot, false);

		verify(installToolIntegration).installSetup(eq(fuzzyMatch), eq(legacyRoot));
		verify(installToolIntegration).installSetup(eq(mainSetupPath), eq(legacyRoot));
	}
}
