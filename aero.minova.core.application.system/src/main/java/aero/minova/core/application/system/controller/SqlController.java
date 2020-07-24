package aero.minova.core.application.system.controller;

import java.sql.Connection;
import java.sql.DriverManager;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.Table;
import lombok.val;

@RestController
public class SqlController {

	private Connection sqlConnection = msSqlConnection();

	private Connection msSqlConnection() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			return DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=AFIS_HAM", "sa", "Minova+0");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GetMapping(value = "data/index", produces = "application/json")
	public Table getIndexView() {
		Table movementTable = new Table();
		return movementTable;
	}
}