package ch.minova.service.core.application.system.domain;

public class Column_ {
	public Column_(String name, DataType type) {
		this.setName(name);
		this.setType(type);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	private String name;
	private DataType type;
}
