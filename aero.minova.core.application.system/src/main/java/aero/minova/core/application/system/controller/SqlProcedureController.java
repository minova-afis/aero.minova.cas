package aero.minova.core.application.system.controller;

import java.sql.Connection;
import java.util.Set;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import aero.minova.core.application.system.domain.Table;
import aero.minova.core.application.system.sql.SystemDatabase;

public class SqlProcedureController {
	Connection sqlConnection;

	@PostMapping(value = "data/procedure", produces = "application/json")
	public Table executeProcedure(@RequestBody Table inputTable) {
		if (sqlConnection == null) {
			sqlConnection = SystemDatabase.connection();
		}
		throw new UnsupportedOperationException();
	}
	
}
