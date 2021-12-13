package aero.minova.core.application.system.domain;

public class ProcedureException extends Exception {

	public ProcedureException(Throwable e) {
		super(e);
	}

	public ProcedureException(String s) {
		super(s);
	}
}
