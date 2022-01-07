package domain;

public class CovidException extends Exception {

	public CovidException(Exception e) {
		super(e);
	}

	public CovidException(String s) {
		super(s);
	}
}
