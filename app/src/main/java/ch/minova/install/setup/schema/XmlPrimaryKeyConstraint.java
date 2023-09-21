package ch.minova.install.setup.schema;

import aero.minova.cas.setup.xml.table.Table;

import java.util.Vector;

public class XmlPrimaryKeyConstraint {
	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	public Vector<String> getColumnNames() {
		return this.columnNames;
	}

	public void setColumnNames(final Vector<String> columnNames) {
		this.columnNames = columnNames;
	}

	private String tableName;
	private Vector<String> columnNames = new Vector<String>();

	public XmlPrimaryKeyConstraint(final String tableName) {
		this.tableName = tableName;
	}

	public XmlPrimaryKeyConstraint(final Table table) {
		this(table.getName());
	}

	public void addColumnName(final String name) {
		this.columnNames.add(name);
	}

	public String getName() {
		return "PK_" + this.tableName;
	}

	@Override
	public String toString() {
		return getSQLCode();
	}

	public String getSQLCode() {
		boolean firstColumn = true;
		String sqlCode = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name = N'" + "PK_" + this.tableName
				+ "' AND user_name(uid) = N'dbo') BEGIN ALTER TABLE dbo." + "[" + this.tableName + "]" + " ADD";
		sqlCode += " constraint " + "[" + "PK_" + this.tableName + "]" + " primary key (";
		for (final String colName : this.columnNames) {
			if (!firstColumn) {
				sqlCode += ", ";
			}
			sqlCode += "[" + colName + "]";
			firstColumn = false;
		}
		sqlCode += ") END";
		return sqlCode;
	}
}