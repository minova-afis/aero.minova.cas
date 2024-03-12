package aero.minova.cas.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;

import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import aero.minova.cas.CoreApplicationSystemApplication;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.SqlProcedureResult;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.service.AuthorizationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

//benötigt, damit JUnit-Tests nicht abbrechen
@SpringBootTest(classes = CoreApplicationSystemApplication.class, properties = { "application.runner.enabled=false" })
@ActiveProfiles("test")
@WithMockUser(username = "admin", password = "rqgzxTf71EAx8chvchMi", authorities = { "admin" }) // Wichtig ist "authorities" statt "roles" zu nutzen, da die

@Sql({ "/xvcasUserSecurityForTest.sql", "/xpcasTestProcedures.sql" })
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class SQLProcedureControllerTest extends BaseTest {

	@Autowired
	SqlProcedureController spc;

	@Autowired
	AuthorizationService authorizationService;

	@PersistenceContext
	private EntityManager entityManager;

	private static final String SECURITYVIEW = "xvcasusersecurity";

	@DisplayName("Keine doppelten Extensionnamen erlauben.")
	@Test
	void testDoubleExtensionWithSameName() throws Exception {

		spc.registerExtension("test", null);

		Throwable exception = assertThrows(IllegalArgumentException.class, () -> spc.registerExtension("test", null));
		assertThat(exception).hasMessage("Cannot register two extensions with the same name: test");

	}

	public static String testInt(Connection connection, int keyLong) throws SQLException {
		// Translate your T-SQL statements to jOOQ statements
		return DSL.using(connection, SQLDialect.H2).select(DSL.field("PrivilegeKeyText")).from(SECURITYVIEW).where(DSL.field("KeyLong").eq(keyLong)).fetchOne(0,
				String.class);
	}

	public static Integer testString(Connection connection, String keyText) throws SQLException {
		// Translate your T-SQL statements to jOOQ statements
		return DSL.using(connection, SQLDialect.H2).select(DSL.field("KeyLong")).from(SECURITYVIEW).where(DSL.field("PrivilegeKeyText").eq(keyText)).fetchOne(0,
				Integer.class);
	}

	public static Integer testBoolean(Connection connection, Boolean rowLevelSecurity, String keyText) throws SQLException {
		// Translate your T-SQL statements to jOOQ statements
		return DSL.using(connection, SQLDialect.H2).selectCount().from(SECURITYVIEW)
				.where(DSL.field("RowLevelSecurity").eq(rowLevelSecurity).and(DSL.field("PrivilegeKeyText").eq(keyText))).fetchOne(0, int.class);
	}

	public static Boolean testBoolean2(Connection connection, String keyText) throws SQLException {
		// Translate your T-SQL statements to jOOQ statements
		return DSL.using(connection, SQLDialect.H2).select(DSL.field("RowLevelSecurity")).from(SECURITYVIEW).where(DSL.field("PrivilegeKeyText").eq(keyText))
				.fetchOne(0, Boolean.class);
	}

	public static java.sql.Timestamp testInstant(Connection connection, String keyText) throws SQLException {
		// Translate your T-SQL statements to jOOQ statements
		return DSL.using(connection, SQLDialect.H2).select(DSL.field("LastDate")).from(SECURITYVIEW).where(DSL.field("PrivilegeKeyText").eq(keyText))
				.fetchOne(0, java.sql.Timestamp.class);
	}

	public static Integer testInstant2(Connection connection, Instant lastDate) throws SQLException {
		// Translate your T-SQL statements to jOOQ statements
		return DSL.using(connection, SQLDialect.H2).selectCount().from(SECURITYVIEW).where(DSL.field("LastDate").lessOrEqual(lastDate)).fetchOne(0,
				Integer.class);
	}

	@BeforeEach
	public void init() {
		// Recht und Admin-Nutzer erstellen
		authorizationService.findOrCreateUserPrivilege(SECURITYVIEW);
		authorizationService.findOrCreateUserPrivilege("xpcasTestProcedureInt");
		authorizationService.findOrCreateUserPrivilege("xpcasTestProcedureString");
		authorizationService.findOrCreateUserPrivilege("xpcasTestProcedureBoolean");
		authorizationService.findOrCreateUserPrivilege("xpcasTestProcedureBoolean2");

		authorizationService.findOrCreateUserPrivilege("xpcasTestProcedureInstant");
		authorizationService.findOrCreateUserPrivilege("xpcasTestProcedureInstant2");
		authorizationService.createOrUpdateAdminUser("admin", "$2a$10$l6uLtEVvQAOI7hOXutd7Ye0FtlaL7/npwGu/8YN31EhkHT0wjdtIq");
	}

	@Test
	@DisplayName("Prozedurenaufruf für Integer testen")
	void testProcedure() throws Exception {
		// Tabelle für Index-Anfrage erstellen
		Table procedure = new Table();
		procedure.setName("xpcasTestProcedureInt");
		procedure.addColumn(new Column("KeyLong", DataType.INTEGER));
		Row r = new Row();
		r.addValue(new Value(1, null));
		procedure.addRow(r);

		ResponseEntity procedureResult = spc.executeProcedure(procedure);

		// Es muss mindestens das eine eben erstellte Recht geben
		assertNotNull(procedureResult.getBody());

		SqlProcedureResult result = (SqlProcedureResult) procedureResult.getBody();
		assertEquals(SECURITYVIEW, result.getResultSet().getRows().get(0).getValues().get(0).getStringValue());
	}

	@Test
	@DisplayName("Prozedurenaufruf für Boolean testen")
	void testProcedureBoolean() throws Exception {
		// Tabelle für Index-Anfrage erstellen
		Table procedure = new Table();
		procedure.setName("xpcasTestProcedureBoolean");
		procedure.addColumn(new Column("RowLevelSecurity", DataType.BOOLEAN));
		procedure.addColumn(new Column("PrivilegeKeyText", DataType.STRING));
		Row r = new Row();
		r.addValue(new Value(false, null));
		r.addValue(new Value(SECURITYVIEW, null));
		procedure.addRow(r);

		ResponseEntity procedureResult = spc.executeProcedure(procedure);

		// Es muss mindestens das eine eben erstellte Recht geben
		assertNotNull(procedureResult.getBody());
		SqlProcedureResult result = (SqlProcedureResult) procedureResult.getBody();
		assertEquals(1, result.getResultSet().getRows().get(0).getValues().get(0).getIntegerValue());
	}

	@Test
	@DisplayName("Prozedurenaufruf für Strings testen")
	void testProcedureString() throws Exception {

		// Tabelle für Index-Anfrage erstellen
		Table procedure = new Table();
		procedure.setName("xpcasTestProcedureString");
		procedure.addColumn(new Column("KeyText", DataType.STRING));
		Row r = new Row();
		r.addValue(new Value(SECURITYVIEW, null));
		procedure.addRow(r);

		ResponseEntity procedureResult = spc.executeProcedure(procedure);

		// Es muss mindestens das eine eben erstellte Recht geben
		assertNotNull(procedureResult.getBody());
		SqlProcedureResult result = (SqlProcedureResult) procedureResult.getBody();
		assertEquals(1, result.getResultSet().getRows().get(0).getValues().get(0).getIntegerValue());
	}

	@Test
	@DisplayName("Prozedurenaufruf für Boolean als Rückgabewert testen")
	void testProcedureBoolean2() throws Exception {
		// Tabelle für Index-Anfrage erstellen
		Table procedure = new Table();
		procedure.setName("xpcasTestProcedureBoolean2");
		procedure.addColumn(new Column("KeyText", DataType.STRING));
		Row r = new Row();
		r.addValue(new Value(SECURITYVIEW, null));
		procedure.addRow(r);

		ResponseEntity procedureResult = spc.executeProcedure(procedure);

		// Es muss mindestens das eine eben erstellte Recht geben
		assertNotNull(procedureResult.getBody());
		SqlProcedureResult result = (SqlProcedureResult) procedureResult.getBody();
		assertFalse(result.getResultSet().getRows().get(0).getValues().get(0).getBooleanValue());
	}

	@Test
	@DisplayName("Prozedurenaufruf für Instant testen")
	void testProcedureInstant() throws Exception {

		// Tabelle für Index-Anfrage erstellen
		Table procedure = new Table();
		procedure.setName("xpcasTestProcedureInstant2");
		procedure.addColumn(new Column("LastDate", DataType.INSTANT));
		Row r = new Row();
		r.addValue(new Value(Instant.now(), null));
		procedure.addRow(r);

		ResponseEntity procedureResult = spc.executeProcedure(procedure);

		// Es muss mindestens das eine eben erstellte Recht geben
		assertNotNull(procedureResult.getBody());
		SqlProcedureResult result = (SqlProcedureResult) procedureResult.getBody();
		assertEquals(7, result.getResultSet().getRows().get(0).getValues().get(0).getIntegerValue());
	}

	@Test
	@DisplayName("Prozedurenaufruf für Instant als Rückgabewert testen")
	void testProcedureInstant2() throws Exception {

		// Tabelle für Index-Anfrage erstellen
		Table procedure = new Table();
		procedure.setName("xpcasTestProcedureInstant");
		procedure.addColumn(new Column("KeyText", DataType.STRING));
		Row r = new Row();
		r.addValue(new Value(SECURITYVIEW, null));
		procedure.addRow(r);

		ResponseEntity procedureResult = spc.executeProcedure(procedure);

		// Es muss mindestens das eine eben erstellte Recht geben
		assertNotNull(procedureResult.getBody());
		SqlProcedureResult result = (SqlProcedureResult) procedureResult.getBody();
		assertTrue(Instant.now().isAfter(result.getResultSet().getRows().get(0).getValues().get(0).getInstantValue()));
	}

}