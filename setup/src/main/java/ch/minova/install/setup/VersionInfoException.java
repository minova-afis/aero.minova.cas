package ch.minova.install.setup;

@SuppressWarnings("serial")
public class VersionInfoException extends Exception {
	public VersionInfoException(final String format) {
		super(format);
	}
}