package aero.minova.core.application.system.domain;

import lombok.Data;

@Data
public class SqlProcedureResult {
	private Table resultSet;
	private Table outputParameters;
	private Integer returnCode;
	private Exception returnErrorMessage;
}