package aero.minova.core.application.system.domain;

import java.util.List;

import lombok.Data;

@Data
public class SqlProcedureResult {
	private Table resultSet;
	private Table outputParameters;
	private List<Integer> returnCodes;
	private int returnCode;
}