package aero.minova.core.application.system.setup;

@SuppressWarnings("serial")
public class ModuleNotFoundException extends Exception {
	public ModuleNotFoundException(final String format) {
		super(format);
	}

	public ModuleNotFoundException(final String name, final String message) {
		this(name + "|" + message);
	}
}