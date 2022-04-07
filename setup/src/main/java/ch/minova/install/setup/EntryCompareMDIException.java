package ch.minova.install.setup;

@SuppressWarnings("serial")
public class EntryCompareMDIException extends Exception {
	public EntryCompareMDIException(final String format) {
		super("EntryCompareMDIException" + format);
	}
}