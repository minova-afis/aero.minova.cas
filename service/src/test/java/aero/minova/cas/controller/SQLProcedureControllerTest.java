package aero.minova.cas.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import aero.minova.cas.CoreApplicationSystemApplication;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.service.AuthorizationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

//benötigt, damit JUnit-Tests nicht abbrechen
@SpringBootTest(classes = CoreApplicationSystemApplication.class, properties = { "application.runner.enabled=false" })
@ActiveProfiles("test")
@WithMockUser(username = "admin", password = "rqgzxTf71EAx8chvchMi", authorities = { "admin" }) // Wichtig ist "authorities" statt "roles" zu nutzen, da die
																								// Authority ansonsten "ROLE_admin" heißt
public class SQLProcedureControllerTest extends BaseTest {

	@Autowired
	SqlProcedureController spc;

	@Autowired
	AuthorizationService authorizationService;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private DataSource dataSource;

	@DisplayName("Keine doppelten Extensionnamen erlauben.")
	@Test
	void testDoubleExtensionWithSameName() throws Exception {

		spc.registerExtension("test", null);

		Throwable exception = assertThrows(IllegalArgumentException.class, () -> spc.registerExtension("test", null));
		assertThat(exception).hasMessage("Cannot register two extensions with the same name: test");

	}

	public static Integer test(Connection connection, int KeyLong) throws SQLException {
		// Translate your T-SQL statements to jOOQ statements
		return DSL.using(connection, SQLDialect.H2).selectCount().from("xvcasusersecurity").fetchOne(0, int.class);
	}

	@Test
	@DisplayName("Prozedurenaufruf testen")
	@Sql({ "/xpcasTestProcedure.sql" })
	void testProcedure() throws Exception {

		loadData();

		// Recht und Admin-Nutzer erstellen
		authorizationService.findOrCreateUserPrivilege("xvcasUserSecurity");
		authorizationService.findOrCreateUserPrivilege("xpcasTestProcedure");
		authorizationService.createOrUpdateAdminUser("admin", "$2a$10$l6uLtEVvQAOI7hOXutd7Ye0FtlaL7/npwGu/8YN31EhkHT0wjdtIq");

		// Tabelle für Index-Anfrage erstellen
		Table procedure = new Table();
		procedure.setName("xpcasTestProcedure");
		procedure.addColumn(new Column("KeyLong", DataType.INTEGER));
		Row r = new Row();
		r.addValue(new Value(1, null));
		procedure.addRow(r);

		ResponseEntity procedureResult = spc.executeProcedure(procedure);

		// Es muss mindestens das eine eben erstellte Recht geben
		assertNotNull(procedureResult.getBody());
	}

	public void loadData() {
		String filename = "xvcasUserSecurityForTest.sql";

		ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(false, false, "UTF-8", new ClassPathResource(filename));
		resourceDatabasePopulator.execute(dataSource);
	}

}
