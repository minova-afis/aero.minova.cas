package ch.minova.install.setup.schema;

import java.util.HashMap;

public class SqlDatabaseColumn {
	private String name;
	private boolean nullable;
	private String type;
	private int length;
	private SqlDatabaseTable table;
	private SqlDefault columnDefault;
	private final HashMap<String, SqlConstraint> constraintMap = new HashMap<String, SqlConstraint>();
	private String collation;

	public SqlDatabaseColumn(final String tablename, final String columnname) {
		this(tablename, columnname, true, "int", 0);
	}

	public String getCollation() {
		if (this.collation == null) {
			return "null";
		}
		return this.collation;
	}

	public SqlDatabaseColumn(final String tablename, final String columnname, final boolean nullable, final String type, final int length) {
		this.name = columnname;
		if (SqlDatabaseTable.getTable(tablename) == null) {
			this.table = new SqlDatabaseTable(tablename);
			SqlDatabaseTable.putTable(this.table);
		} else {
			this.table = SqlDatabaseTable.getTable(tablename);
		}
		this.nullable = nullable;
		this.type = type;
		this.length = length;
		this.table.addColumn(this);
	}

	public SqlDatabaseColumn(final String tablename, final String columnname, final boolean nullable2, final String type2, final int length2,
			final String collation2) {
		this.name = columnname;
		if (SqlDatabaseTable.getTable(tablename) == null) {
			this.table = new SqlDatabaseTable(tablename);
			SqlDatabaseTable.putTable(this.table);
		} else {
			this.table = SqlDatabaseTable.getTable(tablename);
		}
		this.nullable = nullable2;
		this.type = type2;
		this.length = length2;
		this.table.addColumn(this);
		this.collation = collation2;
	}

	public void addConstraint(final SqlConstraint constraint) {
		this.constraintMap.put(constraint.getName().toLowerCase(), constraint);
	}

	private String deletebrackets(String old) {
		if (old.startsWith("(") && old.endsWith(")")) {
			old = old.substring(1, old.length() - 1);
			old = deletebrackets(old);
		}
		return old;
	}

	/**
	 * @return the columnDefault
	 */
	public SqlDefault getColumnDefault() {
		if (this.columnDefault == null) {
			return new SqlDefault(new SqlDatabaseColumn("null", "null"), "null", "null");
		}
		return this.columnDefault;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return this.length;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the table
	 */
	public SqlDatabaseTable getTable() {
		return this.table;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * @return the nullable
	 */
	public boolean isNullable() {
		return this.nullable;
	}

	public String listConstraints() {
		final StringBuffer sb = new StringBuffer();
		for (final SqlConstraint constraint : this.constraintMap.values()) {
			if (sb.length() != 0) {
				sb.append(", ");
			}
			sb.append(constraint.toString());
		}
		return sb.toString();
	}

	/**
	 * @param columnDefault
	 *            the columnDefault to set
	 */
	public void setColumnDefault(final SqlDefault columnDefault) {
		columnDefault.setValue(deletebrackets(columnDefault.getValue()));
		this.columnDefault = columnDefault;
	}

	/**
	 * @param length
	 *            the length to set
	 */
	public void setLength(final int length) {
		this.length = length;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @param nullable
	 *            the nullable to set
	 */
	public void setNullable(final boolean nullable) {
		this.nullable = nullable;
	}

	/**
	 * @param table
	 *            the table to set
	 */
	public void setTable(final SqlDatabaseTable table) {
		this.table = table;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(final String type) {
		this.type = type;
	}

	public void setCollation(final String collation) {
		this.collation = collation;
	}
}