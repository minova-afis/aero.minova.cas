package aero.minova.core.application.system.controller;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import aero.minova.core.application.system.service.FilesService;
import aero.minova.core.application.system.service.SetupService;
import aero.minova.core.application.system.sql.SystemDatabase;

public class SetupServiceTest {

	@Mock
	private SystemDatabase database;

	@Mock
	private FilesService service;

	@Test
	public void testExampleProject() {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("exampleDependencyList.txt");
		String text = null;
		try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
			text = scanner.useDelimiter("\\A").next();
		}
		new SetupService().parseDependencyList(text);
	}

	@Test
	public void testReadSetups() throws Exception {
		Path testPath = Paths.get(getClass().getClassLoader().getResource("Setup.xml").toURI());
		Mockito.when(service.getSystemFolder()).thenReturn(testPath);

		SetupService testSubject = new SetupService();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("exampleDependencyList.txt");
		String text = null;
		try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
			text = scanner.useDelimiter("\\A").next();
		}
		System.out.println(text);
		List<String> dependencies = testSubject.parseDependencyList(text);
		testSubject.readSetups(dependencies);
	}
}
