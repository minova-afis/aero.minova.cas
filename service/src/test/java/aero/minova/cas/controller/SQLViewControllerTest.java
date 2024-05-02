package aero.minova.cas.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import aero.minova.cas.CoreApplicationSystemApplication;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.service.AuthorizationService;

@SpringBootTest(classes = CoreApplicationSystemApplication.class)
@ActiveProfiles("test")
@WithMockUser(username = "admin", password = "rqgzxTf71EAx8chvchMi", authorities = { "admin" }) // Wichtig ist "authorities" statt "roles" zu nutzen, da die
																								// Authority ansonsten "ROLE_admin" heißt

@Sql({ "/xvcasUserSecurityForTest.sql" }) // Die View muss erstellt/eingespielt werden. In diesem Fall ohne LastAction-Filter, da die LastAction-Spalte beim
											// Erstellen über JPA nicht gesetzt wird
class SQLViewControllerTest {

	@Autowired
	SqlViewController testSubject;

	@Autowired
	AuthorizationService authorizationService;

	@DisplayName("Keine doppelten Extensionnamen erlauben.")
	@Test
	void testDoubleExtensionWithSameName() throws Exception {

		testSubject.registerExtension("test", null);

		Throwable exception = assertThrows(IllegalArgumentException.class, () -> testSubject.registerExtension("test", null));
		assertThat(exception).hasMessage("Cannot register two extensions with the same name: test");

	}

	@Test
	@DisplayName("Methode getIndexView() testen")
	void getIndexView() throws Exception {

		// Recht und Admin-Nutzer erstellen
		authorizationService.findOrCreateUserPrivilege("xvcasUserSecurity");
		authorizationService.createOrUpdateAdminUser("admin", "$2a$10$l6uLtEVvQAOI7hOXutd7Ye0FtlaL7/npwGu/8YN31EhkHT0wjdtIq");

		// Tabelle für Index-Anfrage erstellen
		Table indexView = new Table();
		indexView.setName("xvcasUserSecurity");
		indexView.addColumn(new Column("KeyLong", DataType.INTEGER));
		indexView.addColumn(new Column("PrivilegeKeyText", DataType.STRING));
		indexView.addColumn(new Column("SecurityToken", DataType.STRING));
		Row r = new Row();
		r.addValue(null);
		r.addValue(null);
		r.addValue(null);
		indexView.addRow(r);

		Table indexViewResult = testSubject.getIndexView(indexView);

		// Es muss mindestens das eine eben erstellte Recht geben
		assertFalse(indexViewResult.getRows().isEmpty());
	}

}
