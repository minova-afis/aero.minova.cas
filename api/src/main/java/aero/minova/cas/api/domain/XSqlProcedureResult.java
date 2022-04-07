package aero.minova.cas.api.domain;

import lombok.Data;

@Data
public class XSqlProcedureResult {

	public XSqlProcedureResult(String id, SqlProcedureResult resultSet) {
		this.id = id;
		this.resultSet = resultSet;
	}

	private String id;
	private SqlProcedureResult resultSet;
}
