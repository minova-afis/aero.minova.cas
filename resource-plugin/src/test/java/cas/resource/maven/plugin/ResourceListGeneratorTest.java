package cas.resource.maven.plugin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import aero.minova.cas.resource.maven.plugin.ResourceListGenerator;

public class ResourceListGeneratorTest {

	@Test
	void testMergeI18n() throws FileNotFoundException, IOException {
		Path resourcePath = Paths.get("src/test/resources");
		File result = resourcePath.resolve("i18n").resolve("messages.properties").toFile();

		// Wir wollen überprüfen, ob unser Ergebnis richtig ist, deshalb vorherige Ergebnisse löschen.
		if (result.exists()) {
			result.delete();
		}

		ResourceListGenerator resourceListGenerator = new ResourceListGenerator();

		resourceListGenerator.mergeI18n(resourcePath.resolve("test"), resourcePath);

		List<String> lines = new ArrayList<String>();
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(result))) {
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
		}
//		xtcasUserGroup=Benutzergruppe
//				xtcasUser=Benutzer

		assertEquals("xtcasUserGroup=Benutzergruppe", lines.get(0));
		assertEquals("xtcasUser=Benutzer", lines.get(1));
	}

}
