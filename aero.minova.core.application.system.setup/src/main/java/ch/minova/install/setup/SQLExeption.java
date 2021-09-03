package ch.minova.install.setup;

@SuppressWarnings("serial")
public class SQLExeption extends Exception {
	public SQLExeption(final String format) {
		super(format);
	}
}