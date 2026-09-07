package aero.minova.cas.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Testet den CAS-12-Legacy-Fallback (siehe doc/md/CAS12Compatibility.md) isoliert, ohne Spring-Kontext, da die getesteten Methoden keine anderen Beans
 * benötigen.
 */
class FilesServiceLegacyFallbackTest {

	@TempDir
	Path legacyRoot;

	FilesService testSubject;

	@BeforeEach
	void setUp() throws IOException {
		testSubject = new FilesService();

		Files.createDirectories(legacyRoot.resolve("tables"));
		Files.writeString(legacyRoot.resolve("tables/foo.table.xml"), "<table/>");
		Files.createDirectories(legacyRoot.resolve("sql"));
		Files.writeString(legacyRoot.resolve("sql/bar.sql"), "select 1");
	}

	@DisplayName("Ohne konfigurierten Legacy-Pfad ist der Fallback deaktiviert")
	@Test
	void noFallbackConfigured() {
		assertThat(testSubject.hasLegacyFallback()).isFalse();
		assertThat(testSubject.resolveLegacyFile("/tables/foo.table.xml")).isEmpty();
	}

	@DisplayName("Existierende Datei wird über den Legacy-Pfad gefunden")
	@Test
	void resolvesExistingFile() {
		testSubject.setLegacySystemFilesPath(legacyRoot.toString());

		assertThat(testSubject.hasLegacyFallback()).isTrue();
		assertThat(testSubject.resolveLegacyFile("/tables/foo.table.xml")).contains(legacyRoot.resolve("tables/foo.table.xml"));
		// Auch ohne führenden Slash muss die Datei gefunden werden.
		assertThat(testSubject.resolveLegacyFile("tables/foo.table.xml")).isPresent();
	}

	@DisplayName("Nicht existierende Datei wird nicht gefunden")
	@Test
	void missingFileIsEmpty() {
		testSubject.setLegacySystemFilesPath(legacyRoot.toString());

		assertThat(testSubject.resolveLegacyFile("/tables/does-not-exist.table.xml")).isEmpty();
	}

	@DisplayName("Pfad-Escape aus dem Legacy-Verzeichnis wird verhindert")
	@Test
	void blocksPathEscape() {
		testSubject.setLegacySystemFilesPath(legacyRoot.toString());

		assertThat(testSubject.resolveLegacyFile("/../outside.txt")).isEmpty();
	}

	@DisplayName("listLegacyFiles listet alle Dateien unterhalb eines Präfixes")
	@Test
	void listsFilesUnderPrefix() throws Exception {
		testSubject.setLegacySystemFilesPath(legacyRoot.toString());

		assertThat(testSubject.listLegacyFiles("/tables")).containsExactly(legacyRoot.resolve("tables/foo.table.xml"));
	}

	@DisplayName("listLegacyFiles liefert eine leere Liste für ein nicht existierendes Präfix")
	@Test
	void listReturnsEmptyForMissingPrefix() throws Exception {
		testSubject.setLegacySystemFilesPath(legacyRoot.toString());

		assertThat(testSubject.listLegacyFiles("/does-not-exist")).isEmpty();
	}
}
