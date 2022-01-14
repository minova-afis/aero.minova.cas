package aero.minova.cas.domain;

import java.util.List;

import lombok.Data;

@Data
public class XProcedureException extends Exception {

	List<XTable> xTables;
	List<XSqlProcedureResult> results;

	public XProcedureException(List<XTable> xTables, List<XSqlProcedureResult> results, Throwable e) {
		super(e);
		this.xTables = xTables;
		this.results = results;
	}

	public XProcedureException(List<XTable> xTables, List<XSqlProcedureResult> results, String s) {
		super(s);
		this.xTables = xTables;
		this.results = results;
	}
}
