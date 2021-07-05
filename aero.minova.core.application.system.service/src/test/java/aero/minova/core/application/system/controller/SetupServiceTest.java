package aero.minova.core.application.system.controller;

import aero.minova.core.application.system.service.SetupService;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Scanner;

public class SetupServiceTest {
	@Test
	public void testExampleProject() {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("exampleDependencyList.txt");
		String text = null;
		try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
			text = scanner.useDelimiter("\\A").next();
		}
		new SetupService().parseDependencyList(text);
	}
}
