package aero.minova.cas.api.domain;

public class ProcedureException extends Exception {

	public ProcedureException(Throwable e) {
		super(e);
	}

	public ProcedureException(String s) {
		super(s);
	}
}
