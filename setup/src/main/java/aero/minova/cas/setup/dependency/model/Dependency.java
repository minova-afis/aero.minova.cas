package aero.minova.cas.setup.dependency.model;

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
