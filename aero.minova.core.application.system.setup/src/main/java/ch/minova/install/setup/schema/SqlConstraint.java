package ch.minova.install.setup.schema;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class SqlConstraint {
	private static HashMap<String, SqlConstraint> constraintMap = new HashMap<String, SqlConstraint>();
	private final Vector<String> columnNames = new Vector<String>();
	private SqlDatabaseTable table;
	private String name;

	public SqlConstraint(final String name, final String tableName, final String columnName) {
		this.name = name;
		this.table = SqlDatabaseTable.getTable(tableName);
		this.table.addConstraint(this);
		addColumn(columnName);
		putConstraint(this);
	}

	public void addColumn(final String columnName) {
		this.table.getColumn(columnName).addConstraint(this);
		this.columnNames.add(columnName);
	}

	public static SqlConstraint getConstraint(final String constraintName) {
		return constraintMap.get(constraintName.toLowerCase());
	}

	public static void putConstraint(final SqlConstraint constraint) {
		constraintMap.put(constraint.getName().toLowerCase(), constraint);
	}

	/**
	 * @return the table
	 */
	public SqlDatabaseTable getTable() {
		return this.table;
	}

	/**
	 * @param table
	 *            the table to set
	 */
	public void setTable(final SqlDatabaseTable table) {
		this.table = table;
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

	@Override
	public String toString() {
		String columnList = "";
		for (final Iterator<String> i = this.columnNames.iterator(); i.hasNext();) {
			final String name = i.next();
			if (columnList.length() != 0) {
				columnList += ", ";
			}
			columnList += name;
		}
		return this.name + "(" + columnList + ")";
	}
}