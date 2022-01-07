package domain;

import java.io.Serializable;

public class Column implements Serializable {
	private static final long serialVersionUID = 202106161636L;
	public static final String AND_FIELD_NAME = "&";
	public static final Column AND_FIELD = new Column(Column.AND_FIELD_NAME, DataType.BOOLEAN);
	private String name;
	private DataType type;
	private OutputType outputType;

	public Column() {

	}

	public Column(String name, DataType type) {
		setName(name);
		setType(type);
	}

	public Column(String name, DataType type, OutputType outputType) {
		setName(name);
		setType(type);
		this.outputType = outputType;
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

	public OutputType getOutputType() {
		return outputType;
	}

	public void setOutputType(OutputType outputType) {
		this.outputType = outputType;
	}
}