package aero.minova.cas.controller;

import static org.junit.Assert.assertFalse;

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
@Sql({ "/xvcasUserSecurity.sql" }) // Die View muss erstellt/eingespielt werden. In diesem Fall ohne LastAction-Filter, da die LastAction-Spalte beim Erstellen
									// über JPA nicht gesetzt wird
class SQLViewControllerTest {

	@Autowired
	SqlViewController testSubject;

	@Autowired
	AuthorizationService authorizationService;

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
