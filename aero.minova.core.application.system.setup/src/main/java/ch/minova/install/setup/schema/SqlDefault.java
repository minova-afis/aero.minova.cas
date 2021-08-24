package ch.minova.install.setup.schema;

public class SqlDefault {
	private String name;
	private String value;
	private SqlDatabaseColumn column;

	public SqlDefault(final SqlDatabaseColumn column, final String name, final String value) {
		setColumn(column);
		setName(name);
		setValue(value);
		column.setColumnDefault(this);
	}

	/**
	 * @return the column
	 */
	public SqlDatabaseColumn getColumn() {
		return this.column;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	public void setColumn(final SqlDatabaseColumn column) {
		this.column = column;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(final String value) {
		this.value = value;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}
}