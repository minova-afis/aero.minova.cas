package ch.minova.install.setup.schema;

import aero.minova.cas.setup.xml.table.Column;
import aero.minova.cas.setup.xml.table.Table;
import aero.minova.cas.setup.xml.table.UniqueKey;

import java.util.List;

public class XmlUniqueKeyConstraint {
	private String columnName;
	private String tableName;
	private XmlUniqueKeyColumn[] uniqueKeyColumns;

	public String getColumnName() {
		return this.columnName;
	}

	public void setColumnName(final String columnName) {
		this.columnName = columnName;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	public XmlUniqueKeyConstraint(final Table t, final UniqueKey uniqueKey, final List<String> columns) {
		this.tableName = t.getName();
		this.columnName = uniqueKey.getName();
		final XmlUniqueKeyColumn uniKeyColumns[] = new XmlUniqueKeyColumn[columns.size()];
		for (int i = 0; i < columns.size(); i++) {
			final XmlUniqueKeyColumn xmlc = new XmlUniqueKeyColumn(columns.get(i));
			uniKeyColumns[i] = xmlc;
		}
		this.uniqueKeyColumns = uniKeyColumns;
	}

	public XmlUniqueKeyConstraint(final String name, final XmlUniqueKeyColumn[] uniqueKeyColumns) {
		this.tableName = name;
		this.columnName = null;
		this.uniqueKeyColumns = uniqueKeyColumns;
	}

	public XmlUniqueKeyConstraint(final String tableName, final String columnName) {
		this.tableName = tableName;
		this.columnName = columnName;
		this.uniqueKeyColumns = null;
	}

	public void setUniqueKeyColumns(final XmlUniqueKeyColumn[] uniqueKeyColumns) {
		this.uniqueKeyColumns = uniqueKeyColumns;
	}

	public XmlUniqueKeyColumn[] getUniqueKeyColumns() {
		return this.uniqueKeyColumns;
	}

	@Override
	public String toString() {
		return getSQLCode();
	}

	public String getName() {
		return getNameOfConstriant();
	}

	public String getNameOfConstriant() {
		String sqlCode = "UK_" + this.tableName;
		for (int i = 0; i < this.uniqueKeyColumns.length; i++) {
			sqlCode += "_" + this.uniqueKeyColumns[i].getLocalColumnName();
		}
		return sqlCode;
	}

	private String getUKcolumns() {
		String uk_columns = "";
		for (int i = 0; i < this.uniqueKeyColumns.length; i++) {
			if (i > 0) {
				uk_columns += "_";
			}
			uk_columns += this.uniqueKeyColumns[i].getLocalColumnName();
		}
		return uk_columns;
	}

	public String getSQLCode() {
		String uk_columns = "";
		if (this.uniqueKeyColumns == null) {
			uk_columns = this.columnName;
		} else {
			uk_columns = getUKcolumns();
		}
		String sqlCode = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name = N'" + "UK_" + this.tableName + "_" + uk_columns
				+ "' AND user_name(uid) = N'dbo') BEGIN ALTER TABLE dbo." + "[" + this.tableName + "]" + " ADD constraint " + "[" + "UK_" + this.tableName + "_"
				+ uk_columns + "]" + " unique nonclustered (";
		if (this.uniqueKeyColumns == null) {
			sqlCode += "[" + this.columnName + "]";
			sqlCode += ") END";
			return sqlCode;
		}
		for (int i = 0; i < this.uniqueKeyColumns.length; i++) {
			if (i > 0) {
				sqlCode += ",";
			}
			sqlCode += "[" + this.uniqueKeyColumns[i].getLocalColumnName() + "]";

		}
		sqlCode += ") END";
		return sqlCode;
	}
}