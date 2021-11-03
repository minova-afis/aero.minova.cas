package aero.minova.core.application.system.setup.dependency;

import java.io.Serializable;

public class Dependency implements Serializable {
	private String from;
	private String to;

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}
}
