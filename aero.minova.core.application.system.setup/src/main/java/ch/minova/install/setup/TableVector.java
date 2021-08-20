package ch.minova.install.setup;

public class TableVector {
	private String name;
	private String type;

	public TableVector(final String name2, final ch.minova.core.install.TableschemaDocument.Tableschema.Type.Enum type2) {
		setName(name2);
		if (type2 == null) {
			setType("table");
		} else {
			setType(type2.toString());
		}
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(final String type) {
		this.type = type;
	}
}