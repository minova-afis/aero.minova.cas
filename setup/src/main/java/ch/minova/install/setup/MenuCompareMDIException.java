package ch.minova.install.setup;

@SuppressWarnings("serial")
public class MenuCompareMDIException extends Exception {
	public MenuCompareMDIException(final String format) {
		super("MenuCompareMDIException: " + format);
	}
}