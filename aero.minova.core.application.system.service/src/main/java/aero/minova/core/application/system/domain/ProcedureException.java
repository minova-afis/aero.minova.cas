package aero.minova.core.application.system.domain;

public class ProcedureException extends Exception {

	public ProcedureException(Exception e) {
		super(e);
	}

	public ProcedureException(String s) {
		super(s);
	}
}
