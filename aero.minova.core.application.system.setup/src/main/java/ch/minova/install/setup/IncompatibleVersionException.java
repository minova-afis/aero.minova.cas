package ch.minova.install.setup;

public class IncompatibleVersionException extends BaseSetupException {
	private static final long serialVersionUID = 3237339248015000491L;

	public IncompatibleVersionException(final String format) {
		super(format);
	}
}