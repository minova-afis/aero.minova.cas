package aero.minova.core.application.system.setup;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.mysql.cj.jdbc.CallableStatement;

import aero.minova.core.application.system.service.FilesService;
import aero.minova.core.application.system.sql.SystemDatabase;

public class SetupServiceTest{

	@Mock
	private SystemDatabase database;
	@Mock
	private Connection connection;
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
		MockitoAnnotations.initMocks(this);

		// Die Setup-Dateien liegen im setup-Ordner.
		String url = getClass().getClassLoader().getResource("setup").toURI().getPath();
		Path setupPath = new File(url.substring(0, url.lastIndexOf("/"))).toPath();
		Mockito.when(service.getSystemFolder()).thenReturn(setupPath);
		Mockito.when(database.getConnection()).thenReturn(connection);
		Mockito.when(connection.prepareCall(Mockito.any())).thenReturn(Mockito.mock(CallableStatement.class));

		SetupService testSubject = new SetupService();
		testSubject.service = service;
		testSubject.database = database;
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("exampleDependencyList.txt");
		String text = null;
		try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
			text = scanner.useDelimiter("\\A").next();
		}

		List<String> procedureList = testSubject.readSetups(text, false);

		assertThat(procedureList).hasSize(1);
		assertThat(procedureList).contains("xvxmpExampleIndex.sql");
	}
}
