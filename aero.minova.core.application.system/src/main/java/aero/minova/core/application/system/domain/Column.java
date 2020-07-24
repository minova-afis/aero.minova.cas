package aero.minova.core.application.system.domain;

public class Column {
	public static final String AND_FIELD_NAME = "&";
	private String name;
	private DataType type;

	public Column(String name, DataType type) {
		setName(name);
		setType(type);
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
}