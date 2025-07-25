package aero.minova.cas.service;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import aero.minova.cas.BaseTest;
import aero.minova.cas.CoreApplicationSystemApplication;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.TableMetaData;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.controller.SqlViewController;
import aero.minova.cas.service.model.Authorities;
import aero.minova.cas.service.model.ColumnSecurity;
import aero.minova.cas.service.model.LuUserPrivilegeUserGroup;
import aero.minova.cas.service.repository.AuthoritiesRepository;
import aero.minova.cas.service.repository.ColumnSecurityRepository;
import jakarta.annotation.PostConstruct;

@SpringBootTest(classes = CoreApplicationSystemApplication.class)
@ActiveProfiles("test")
@WithMockUser(username = "admin", password = "rqgzxTf71EAx8chvchMi", authorities = { "admin" }) // Wichtig ist "authorities" statt "roles" zu nutzen, da die
																								// Authority ansonsten "ROLE_admin" heißt
@Sql({ "/xvcasUserSecurityForTest.sql" }) // Die View muss erstellt/eingespielt werden. In diesem Fall ohne LastAction-Filter, da die LastAction-Spalte beim
											// Erstellen über JPA nicht gesetzt wird. Wird vor JEDEM test aufgerufen
public abstract class ViewServiceBaseTest<T extends ViewServiceInterface> extends BaseTest {

	@Autowired
	T testSubject;

	@Autowired
	ViewService viewService;

	@Autowired
	SqlViewController viewController;

	@Autowired
	AuthorizationService authorizationService;

	@Autowired
	AuthoritiesRepository authoritiesRepository;

	@Autowired
	LuUserPrivilegeUserGroupService luUserPrivilegeUserGroupService;

	@Autowired
	UserGroupService userGroupService;

	@Autowired
	ColumnSecurityRepository columnSecurityRepository;

	private List<Integer> authorityKeys = Arrays.asList(1, 2, 3, 4, 5);

	@PostConstruct
	void setupViewServiceTest() {

		// Erlaubnis auf Tabelle geben
		authorizationService.findOrCreateUserPrivilege("xtcasAuthorities");
		authorizationService.createOrUpdateAdminUser("admin", "$2a$10$l6uLtEVvQAOI7hOXutd7Ye0FtlaL7/npwGu/8YN31EhkHT0wjdtIq");

		// Erlaubnis auf ColumnSecurity Tabelle mit RowLevelSecurity aktiviert
		LuUserPrivilegeUserGroup privilege = authorizationService.findOrCreateLuUserPrivilegeUserGroup(userGroupService.findEntitiesByKeyText("admin").get(0),
				authorizationService.findOrCreateUserPrivilege("xtcasColumnSecurity"));
		privilege.setRowLevelSecurity(true);
		luUserPrivilegeUserGroupService.save(privilege);

		// Ein paar Daten erstellen
		authoritiesRepository.save(new Authorities(1, "User1", "test", "user", Timestamp.valueOf("2023-06-18 00:00:00.0").toLocalDateTime(), 1));
		authoritiesRepository.save(new Authorities(2, "User2", "TEST", "user", Timestamp.valueOf("2023-06-19 00:00:00.0").toLocalDateTime(), 2));
		authoritiesRepository.save(new Authorities(3, "User3", "not test", "user", Timestamp.valueOf("2023-06-19 08:00:00.0").toLocalDateTime(), 1));
		authoritiesRepository.save(new Authorities(4, "User4", "testtest", "user", Timestamp.valueOf("2023-06-19 16:00:00.0").toLocalDateTime(), 1));
		authoritiesRepository.save(new Authorities(5, "User5", "te", null, Timestamp.valueOf("2023-06-20 08:00:00.0").toLocalDateTime(), -1));

		columnSecurityRepository.save(new ColumnSecurity(1, "cs1", "cs1", "cs1", null));
		columnSecurityRepository.save(new ColumnSecurity(2, "cs2", "cs2", "cs2", "admin"));
		columnSecurityRepository.save(new ColumnSecurity(3, "cs3", "cs3", "cs3", "niemand"));
	}

	@Test
	@DisplayName("RowLevelSecurity ohne weitere Bedingungen")
	void rowLevelNoConditions() throws Exception {
		Table indexView = getTableForPrivilegeRequest();

		Table indexViewResult = viewController.getIndexView(indexView);
		assertTrue(indexViewResult.getRows().size() == 2);
	}

	@Test
	@DisplayName("RowLevelSecurity mit weitere Bedingungen")
	void rowLevelWithConditions() throws Exception {
		Table indexView = getTableForPrivilegeRequest();
		indexView.setValue(new Value("cs2", "like"), "KeyText", 0);

		Table indexViewResult = viewController.getIndexView(indexView);
		assertTrue(indexViewResult.getRows().size() == 1);
	}

	@Test
	@DisplayName("Und Verknüpfung von mehreren Zeilen überprüfen")
	void testAndMultipleRows() throws Exception {
		Table indexView = getTableForRequest();
		indexView.setValue(new Value("test", null), "Authority", 0);
		indexView.addRow(getEmptyRow(7));
		indexView.setValue(new Value(true, null), Column.AND_FIELD_NAME, 1);
		indexView.setValue(new Value(2, ">="), "KeyLong", 1);

		Table indexViewResult = viewController.getIndexView(indexView);
		assertTrue(indexViewResult.getRows().size() >= 1);
		for (Row r : indexViewResult.getRows()) {
			assertTrue("test".equalsIgnoreCase(indexViewResult.getValue("Authority", r).getStringValue()));
			assertTrue(indexViewResult.getValue("KeyLong", r).getIntegerValue() >= 2);
		}
	}

	@Test
	@DisplayName("Und Verknüpfung von einer Zeile überprüfen")
	void testAndOneRow() throws Exception {
		Table indexView = getTableForRequest();
		indexView.setValue(new Value("test", "~"), "Authority", 0);
		indexView.setValue(new Value(2, ">="), "Keylong", 0);

		Table indexViewResult = viewController.getIndexView(indexView);
		assertTrue(indexViewResult.getRows().size() >= 1);
		for (Row r : indexViewResult.getRows()) {
			assertTrue("test".equalsIgnoreCase(indexViewResult.getValue("Authority", r).getStringValue()));
			assertTrue(indexViewResult.getValue("KeyLong", r).getIntegerValue() >= 2);
		}
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(booleans = { false })
	@DisplayName("Oder Verknüpfung überprüfen")
	void testOr(Boolean andValue) throws Exception {
		Table indexView = getTableForRequest();
		indexView.setValue(new Value("User1", "="), "username", 0);
		indexView.addRow(getEmptyRow(7));
		indexView.setValue(new Value(andValue, null), Column.AND_FIELD_NAME, 1);
		indexView.setValue(new Value("User2", "="), "username", 1);

		Table indexViewResult = viewController.getIndexView(indexView);
		assertTrue(indexViewResult.getRows().size() >= 2);
		for (Row r : indexViewResult.getRows()) {
			assertTrue("User1".equalsIgnoreCase(indexViewResult.getValue("username", r).getStringValue()) || //
					"User2".equalsIgnoreCase(indexViewResult.getValue("username", r).getStringValue()));
		}
	}

	@Test
	@DisplayName("Verknüpfung ohne &-Spalte überprüfen")
	void testNoAndColumn() throws Exception {
		Table indexView = getTableForRequestNoAndColumn();
		indexView.setValue(new Value("User1", "="), "username", 0);
		indexView.addRow(getEmptyRow(6));
		indexView.setValue(new Value("User2", "="), "username", 1);

		Table indexViewResult = viewController.getIndexView(indexView);
		assertTrue(indexViewResult.getRows().size() >= 2);
		for (Row r : indexViewResult.getRows()) {
			assertTrue("User1".equalsIgnoreCase(indexViewResult.getValue("username", r).getStringValue()) || //
					"User2".equalsIgnoreCase(indexViewResult.getValue("username", r).getStringValue()));
		}
	}

	@ParameterizedTest
	@DisplayName("Like/Equal-String Operationen (ohne wildcards) überprüfen")
	@NullSource
	@ValueSource(strings = { "", "~", "like", "=" })
	void testStringOperationsNoWildcards(String rule) throws Exception {
		Table indexView = getTableForRequest();
		indexView.setValue(new Value("test", rule), "Authority", 0);
		Table indexViewResult = viewController.getIndexView(indexView);
		assertTrue(indexViewResult.getRows().size() >= 2); // Mindestens "test" und "TEST"
		for (Row r : indexViewResult.getRows()) {
			assertTrue("test".equalsIgnoreCase(indexViewResult.getValue("Authority", r).getStringValue()));
		}
	}

	@ParameterizedTest
	@DisplayName("Like-String Operationen (mit wildcards) überprüfen")
	@NullSource
	@ValueSource(strings = { "", "~", "like" })
	void testStringOperationsWithWildcards(String rule) throws Exception {
		Table indexView = getTableForRequest();
		indexView.setValue(new Value("test%", rule), "Authority", 0);
		Table indexViewResult = viewController.getIndexView(indexView);
		assertTrue(indexViewResult.getRows().size() >= 3);// Mindestens "test", "TEST" und "testtest"
		for (Row r : indexViewResult.getRows()) {
			assertTrue(indexViewResult.getValue("Authority", r).getStringValue().toLowerCase().startsWith("test"));
		}

		indexView = getTableForRequest();
		indexView.setValue(new Value("t_st", rule), "Authority", 0);
		indexViewResult = viewController.getIndexView(indexView);
		assertTrue(indexViewResult.getRows().size() >= 2);// Mindestens "test" und "TEST"
		for (Row r : indexViewResult.getRows()) {
			assertThat(indexViewResult.getValue("Authority", r).getStringValue().toLowerCase(), matchesPattern("t.st"));
		}
	}

	@ParameterizedTest
	@DisplayName("Not Like String Operationen (ohne wildcards) überprüfen")
	@ValueSource(strings = { "<>", "!~", "not like" })
	void testStringOperationsNotLikeWithoutWildcards(String rule) throws Exception {
		Table indexView = getTableForRequest();
		indexView.setValue(new Value("test", rule), "Authority", 0);
		Table indexViewResult = viewController.getIndexView(indexView);
		assertTrue(indexViewResult.getRows().size() >= 3);
		for (Row r : indexViewResult.getRows()) {
			assertNotEquals("test", indexViewResult.getValue("Authority", r).getStringValue().toLowerCase());
		}
	}

	@ParameterizedTest
	@DisplayName("Not Like String Operationen (mit wildcards) überprüfen")
	@ValueSource(strings = { "!~", "not like" })
	void testStringOperationsNotLikeWithWildcards(String rule) throws Exception {
		Table indexView = getTableForRequest();
		indexView.setValue(new Value("test%", rule), "Authority", 0);
		Table indexViewResult = viewController.getIndexView(indexView);
		assertTrue(indexViewResult.getRows().size() >= 2);
		for (Row r : indexViewResult.getRows()) {
			assertFalse(indexViewResult.getValue("Authority", r).getStringValue().toLowerCase().startsWith("test"));
		}

		indexView = getTableForRequest();
		indexView.setValue(new Value("t_st", rule), "Authority", 0);
		indexViewResult = viewController.getIndexView(indexView);
		assertTrue(indexViewResult.getRows().size() >= 3);
		for (Row r : indexViewResult.getRows()) {
			assertNotEquals("test", indexViewResult.getValue("Authority", r).getStringValue().toLowerCase());
		}
	}

	@Test
	@DisplayName("Weitere String Operationen überprüfen")
	void testStringOperations() throws Exception {

		Table indexView = getTableForRequest();
		indexView.setValue(new Value("test%", "<>"), "Authority", 0);
		Table indexViewResult = viewController.getIndexView(indexView);
		assertTrue(indexViewResult.getRows().size() >= 5);
		for (Row r : indexViewResult.getRows()) {
			assertFalse("test%".equalsIgnoreCase(indexViewResult.getValue("Authority", r).getStringValue()));
		}

		indexView = getTableForRequest();
		indexView.setValue(new Value("test%", "="), "Authority", 0);
		indexViewResult = viewController.getIndexView(indexView);
		assertEquals(0, indexViewResult.getRows().size());

	}

	@Test
	@DisplayName("Timestamp Operationen überprüfen")
	void testTimestampOperations() throws Exception {
		Table indexView = getTableForRequest();
		indexView.setValue(new Value(Timestamp.valueOf("2023-06-19 00:00:00.0").toInstant(), "<"), "Lastdate", 0);
		Table indexViewResult = viewController.getIndexView(indexView);
		assertFalse(indexViewResult.getRows().isEmpty());
		for (Row r : indexViewResult.getRows()) {
			assertTrue(Timestamp.valueOf("2023-06-19 00:00:00.0").toInstant().isAfter(indexViewResult.getValue("Lastdate", r).getInstantValue()));
		}

		indexView = getTableForRequest();
		indexView.setValue(new Value(Timestamp.valueOf("2023-06-19 00:00:00.0").toInstant(), "<="), "Lastdate", 0);
		indexViewResult = viewController.getIndexView(indexView);
		assertFalse(indexViewResult.getRows().isEmpty());
		for (Row r : indexViewResult.getRows()) {
			assertTrue(Timestamp.valueOf("2023-06-19 00:00:00.0").toInstant().isAfter(indexViewResult.getValue("Lastdate", r).getInstantValue()) || //
					Timestamp.valueOf("2023-06-19 00:00:00.0").toInstant().equals(indexViewResult.getValue("Lastdate", r).getInstantValue()));
		}

		indexView = getTableForRequest();
		indexView.setValue(new Value(Timestamp.valueOf("2023-06-19 00:00:00.0").toInstant(), ">"), "Lastdate", 0);
		indexViewResult = viewController.getIndexView(indexView);
		assertFalse(indexViewResult.getRows().isEmpty());
		for (Row r : indexViewResult.getRows()) {
			assertTrue(Timestamp.valueOf("2023-06-19 00:00:00.0").toInstant().isBefore(indexViewResult.getValue("Lastdate", r).getInstantValue()));
		}

		indexView = getTableForRequest();
		indexView.setValue(new Value(Timestamp.valueOf("2023-06-19 00:00:00.0").toInstant(), ">="), "Lastdate", 0);
		indexViewResult = viewController.getIndexView(indexView);
		assertFalse(indexViewResult.getRows().isEmpty());
		for (Row r : indexViewResult.getRows()) {
			assertTrue(Timestamp.valueOf("2023-06-19 00:00:00.0").toInstant().isBefore(indexViewResult.getValue("Lastdate", r).getInstantValue()) || //
					Timestamp.valueOf("2023-06-19 00:00:00.0").toInstant().equals(indexViewResult.getValue("Lastdate", r).getInstantValue()));
		}

		indexView = getTableForRequest();
		indexView.setValue(new Value(Timestamp.valueOf("2023-06-19 00:00:00.0").toInstant(), "<>"), "Lastdate", 0);
		indexViewResult = viewController.getIndexView(indexView);
		assertFalse(indexViewResult.getRows().isEmpty());
		for (Row r : indexViewResult.getRows()) {
			assertNotEquals(Timestamp.valueOf("2023-06-19 00:00:00.0").toInstant(), indexViewResult.getValue("Lastdate", r).getInstantValue());
		}

		indexView = getTableForRequest();
		indexView.setValue(new Value(Timestamp.valueOf("2023-06-19 00:00:00.0").toInstant(), "="), "Lastdate", 0);
		indexViewResult = viewController.getIndexView(indexView);
		assertFalse(indexViewResult.getRows().isEmpty());
		for (Row r : indexViewResult.getRows()) {
			assertEquals(Timestamp.valueOf("2023-06-19 00:00:00.0").toInstant(), indexViewResult.getValue("Lastdate", r).getInstantValue());
		}

		indexView = getTableForRequest();
		indexView.setValue(new Value(Timestamp.valueOf("2023-06-19 00:00:00.0").toInstant(), null), "Lastdate", 0);
		indexViewResult = viewController.getIndexView(indexView);
		assertFalse(indexViewResult.getRows().isEmpty());
		for (Row r : indexViewResult.getRows()) {
			assertEquals(Timestamp.valueOf("2023-06-19 00:00:00.0").toInstant(), indexViewResult.getValue("Lastdate", r).getInstantValue());
		}

	}

	@Test
	@DisplayName("Integer Operationen überprüfen")
	void testIntOperations() throws Exception {
		Table indexView = getTableForRequest();
		indexView.setValue(new Value(3, "<"), "KeyLong", 0);
		Table indexViewResult = viewController.getIndexView(indexView);
		assertFalse(indexViewResult.getRows().isEmpty());
		for (Row r : indexViewResult.getRows()) {
			assertTrue(indexViewResult.getValue("KeyLong", r).getIntegerValue() < 3);
		}

		indexView = getTableForRequest();
		indexView.setValue(new Value(3, "<="), "KeyLong", 0);
		indexViewResult = viewController.getIndexView(indexView);
		assertFalse(indexViewResult.getRows().isEmpty());
		for (Row r : indexViewResult.getRows()) {
			assertTrue(indexViewResult.getValue("KeyLong", r).getIntegerValue() <= 3);
		}

		indexView = getTableForRequest();
		indexView.setValue(new Value(3, ">"), "KeyLong", 0);
		indexViewResult = viewController.getIndexView(indexView);
		assertFalse(indexViewResult.getRows().isEmpty());
		for (Row r : indexViewResult.getRows()) {
			assertTrue(indexViewResult.getValue("KeyLong", r).getIntegerValue() > 3);
		}

		indexView = getTableForRequest();
		indexView.setValue(new Value(3, ">="), "KeyLong", 0);
		indexViewResult = viewController.getIndexView(indexView);
		assertFalse(indexViewResult.getRows().isEmpty());
		for (Row r : indexViewResult.getRows()) {
			assertTrue(indexViewResult.getValue("KeyLong", r).getIntegerValue() >= 3);
		}

		indexView = getTableForRequest();
		indexView.setValue(new Value(3, "<>"), "KeyLong", 0);
		indexViewResult = viewController.getIndexView(indexView);
		assertFalse(indexViewResult.getRows().isEmpty());
		for (Row r : indexViewResult.getRows()) {
			assertNotEquals(3, indexViewResult.getValue("KeyLong", r).getIntegerValue());
		}

		indexView = getTableForRequest();
		indexView.setValue(new Value(3, "="), "KeyLong", 0);
		indexViewResult = viewController.getIndexView(indexView);
		assertFalse(indexViewResult.getRows().isEmpty());
		for (Row r : indexViewResult.getRows()) {
			assertEquals(3, indexViewResult.getValue("KeyLong", r).getIntegerValue());
		}

		indexView = getTableForRequest();
		indexView.setValue(new Value(3, null), "KeyLong", 0);
		indexViewResult = viewController.getIndexView(indexView);
		assertFalse(indexViewResult.getRows().isEmpty());
		for (Row r : indexViewResult.getRows()) {
			assertEquals(3, indexViewResult.getValue("KeyLong", r).getIntegerValue());
		}
	}

	@Test
	@DisplayName("null und !null Operationen überprüfen")
	void testNullOperations() throws Exception {
		Table indexView = getTableForRequest();
		indexView.setValue(new Value("", "null"), "Lastuser", 0);
		Table indexViewResult = viewController.getIndexView(indexView);
		assertFalse(indexViewResult.getRows().isEmpty());
		for (Row r : indexViewResult.getRows()) {
			assertNull(indexViewResult.getValue("Lastuser", r));
		}

		indexView = getTableForRequest();
		indexView.setValue(new Value("", "!null"), "Lastuser", 0);
		indexViewResult = viewController.getIndexView(indexView);
		assertFalse(indexViewResult.getRows().isEmpty());
		for (Row r : indexViewResult.getRows()) {
			assertNotNull(indexViewResult.getValue("Lastuser", r));
		}
	}

	@Test
	@DisplayName("Limit testen")
	void testLimit() throws Exception {
		Table indexView = getTableForRequestWithLimitedRows(1);
		Table indexViewResult = viewController.getIndexView(indexView);
		assertEquals(2, indexViewResult.getRows().size());
		final var freeKeys = authorityKeys.stream().collect(toList());
		assertTrue(freeKeys.remove(indexViewResult.getRows().get(0).getValues().get(0).getIntegerValue()));
		assertTrue(freeKeys.remove(indexViewResult.getRows().get(1).getValues().get(0).getIntegerValue()));
		assertEquals(2, indexViewResult.getMetaData().getLimited());
		assertEquals(1, indexViewResult.getMetaData().getPage());
		assertTrue(indexViewResult.getMetaData().getTotalResults() > 4);
		assertTrue(indexViewResult.getMetaData().getResultsLeft() >= 2);

		indexView = getTableForRequestWithLimitedRows(2);
		indexViewResult = viewController.getIndexView(indexView);
		assertEquals(2, indexViewResult.getRows().size());
		assertTrue(freeKeys.remove(indexViewResult.getRows().get(0).getValues().get(0).getIntegerValue()));
		assertTrue(freeKeys.remove(indexViewResult.getRows().get(1).getValues().get(0).getIntegerValue()));
		assertEquals(2, indexViewResult.getMetaData().getLimited());
		assertEquals(2, indexViewResult.getMetaData().getPage());
		assertTrue(indexViewResult.getMetaData().getTotalResults() > 4);
		assertTrue(indexViewResult.getMetaData().getResultsLeft() >= 0);
	}

	private Table getTableForRequest() {
		Table indexView = new Table();
		indexView.setName("xtcasAuthorities");
		indexView.addColumn(new Column(Column.AND_FIELD_NAME, DataType.BOOLEAN));
		indexView.addColumn(new Column("KeyLong", DataType.INTEGER));
		indexView.addColumn(new Column("Username", DataType.STRING));
		indexView.addColumn(new Column("Authority", DataType.STRING));
		indexView.addColumn(new Column("Lastuser", DataType.STRING));
		indexView.addColumn(new Column("Lastdate", DataType.INSTANT));
		indexView.addColumn(new Column("lastaction", DataType.INTEGER));

		indexView.addRow(getEmptyRow(7));
		return indexView;
	}

	private Table getTableForPrivilegeRequest() {
		Table indexView = new Table();
		indexView.setName("xtcasColumnSecurity");
		indexView.addColumn(new Column("KeyLong", DataType.INTEGER));
		indexView.addColumn(new Column("KeyText", DataType.STRING));

		indexView.addRow(getEmptyRow(2));
		return indexView;
	}

	private Table getTableForRequestNoAndColumn() {
		Table indexView = new Table();
		indexView.setName("xtcasAuthorities");
		indexView.addColumn(new Column("KeyLong", DataType.INTEGER));
		indexView.addColumn(new Column("Username", DataType.STRING));
		indexView.addColumn(new Column("Authority", DataType.STRING));
		indexView.addColumn(new Column("Lastuser", DataType.STRING));
		indexView.addColumn(new Column("Lastdate", DataType.INSTANT));
		indexView.addColumn(new Column("lastaction", DataType.INTEGER));

		indexView.addRow(getEmptyRow(6));
		return indexView;
	}

	private Table getTableForRequestWithLimitedRows(int page) {
		Table indexView = new Table();
		indexView.setName("xtcasAuthorities");
		indexView.addColumn(new Column("KeyLong", DataType.INTEGER));
		indexView.addColumn(new Column("Username", DataType.STRING));
		indexView.addColumn(new Column("Authority", DataType.STRING));
		indexView.addColumn(new Column("Lastuser", DataType.STRING));
		indexView.addColumn(new Column("Lastdate", DataType.INSTANT));
		indexView.addColumn(new Column("lastaction", DataType.INTEGER));

		indexView.addRow(getEmptyRow(6));

		TableMetaData tableMetaData = new TableMetaData();
		tableMetaData.setLimited(2);
		tableMetaData.setPage(page);
		indexView.setMetaData(tableMetaData);

		return indexView;
	}

	private Row getEmptyRow(int numberOfValues) {
		Row r = new Row();
		for (int i = 0; i < numberOfValues; i++) {
			r.addValue(null);
		}
		return r;
	}
}
