package aero.minova.core.application.system.controller;

import java.sql.Connection;
import java.sql.DriverManager;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import aero.minova.core.application.system.domain.Table;

public class SqlProcedureController {
	Connection sqlConnection;

	private Connection msSqlConnection() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			return DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=AFIS_HAM", "sa", "Minova+0");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@PostMapping(value = "data/procedure", produces = "application/json")
	public Table executeProcedure(@RequestBody Table inputTable) {
		if (sqlConnection == null) {
			sqlConnection = msSqlConnection();
		}
		throw new UnsupportedOperationException();
	}
}
