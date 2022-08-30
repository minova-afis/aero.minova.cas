package ch.minova.install.setup.schema;

import aero.minova.cas.setup.xml.table.Column;
import aero.minova.cas.setup.xml.table.ForeignKey;
import aero.minova.cas.setup.xml.table.Table;

import java.util.List;

public class XmlForeignKeyContraint {
	private String tableName;
	private String columnName;
	private String foreignTableName;
	private XmlForeignKeyColumn[] foreignKeyColumns;

	public XmlForeignKeyContraint(final Table table, final ForeignKey foreignkey) {
		this(table, foreignkey, null);
	}

	public XmlForeignKeyContraint(final String tableName, final String columnName, final String foreignTableName) {
		this.tableName = tableName;
		this.columnName = columnName;
		this.foreignTableName = foreignTableName;
	}

	public XmlForeignKeyContraint(final String tableName, final String columnName, final String foreignTableName,
			final XmlForeignKeyColumn[] foreignKeyColumns) {
		this.tableName = tableName;
		this.columnName = columnName;
		this.foreignTableName = foreignTableName;
		this.foreignKeyColumns = foreignKeyColumns;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return this.columnName;
	}

	public void setColumnName(final String columnName) {
		this.columnName = columnName;
	}

	public String getForeignTableName() {
		return this.foreignTableName;
	}

	public void setForeignTableName(final String foreignTableName) {
		this.foreignTableName = foreignTableName;
	}

	public XmlForeignKeyColumn[] getForeignKeyColumns() {
		return this.foreignKeyColumns;
	}

	public void setForeignKeyColumns(final XmlForeignKeyColumn[] foreignKeyColumns) {
		this.foreignKeyColumns = foreignKeyColumns;
	}

	public XmlForeignKeyContraint(final Table table, final ForeignKey foreignkey,
			final List<Column> columnArray) {
		this.tableName = table.getName();
		this.columnName = foreignkey.getRefid();
		this.foreignTableName = foreignkey.getTable();
		if (columnArray != null) {
			this.foreignKeyColumns = new XmlForeignKeyColumn[columnArray.size()];
			for (int i = 0; i < columnArray.size(); i++) {
				this.foreignKeyColumns[i] = new XmlForeignKeyColumn(columnArray.get(i).getName(), columnArray.get(i).getRefid());
			}
		}
	}

	public String getName() {
		return "FK_" + this.tableName + "_" + this.columnName;
	}

	public String getNameOfConstriant() {
		String sqlCode = "FK_" + this.tableName + "_" + this.columnName + "(";
		if (this.foreignKeyColumns == null || this.foreignKeyColumns.length == 0) {
			sqlCode += this.columnName;
		} else {
			boolean firstColumn = true;
			for (final XmlForeignKeyColumn fkc : this.foreignKeyColumns) {
				if (!firstColumn) {
					sqlCode += ", ";
				}
				sqlCode += "[" + fkc.getLocalColumnName() + "]";
				firstColumn = false;
			}
		}
		sqlCode += ")";
		return sqlCode;
	}

	@Override
	public String toString() {
		return getSQLCode();
	}

	public String getSQLCode() {
		String sqlCode = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name = N'" + "FK_" + this.tableName + "_" + this.columnName
				+ "' AND user_name(uid) = N'dbo') BEGIN ALTER TABLE dbo." + "[" + this.tableName + "]" + " ADD";
		sqlCode += " constraint " + "[" + "FK_" + this.tableName + "_" + this.columnName + "]" + " foreign key (";
		if (this.foreignKeyColumns == null || this.foreignKeyColumns.length == 0) {
			sqlCode += "[" + this.columnName + "]";
		} else {
			boolean firstColumn = true;
			for (final XmlForeignKeyColumn fkc : this.foreignKeyColumns) {
				if (!firstColumn) {
					sqlCode += ", ";
				}
				sqlCode += "[" + fkc.getLocalColumnName() + "]";
				firstColumn = false;
			}
		}
		sqlCode += ") references " + "[" + this.foreignTableName + "]" + " (";
		if (this.foreignKeyColumns == null || this.foreignKeyColumns.length == 0) {
			sqlCode += "[" + "KeyLong" + "]";
		} else {
			boolean firstColumn = true;
			for (final XmlForeignKeyColumn fdc1 : this.foreignKeyColumns) {
				if (!firstColumn) {
					sqlCode += ", ";
				}
				//
				if (fdc1.getReferencedColumnName() == null) {
					sqlCode += "[" + fdc1.getLocalColumnName() + "]";
				} else {
					sqlCode += "[" + fdc1.getReferencedColumnName() + "]";
				}
				firstColumn = false;
			}
		}
		sqlCode += ") END";
		return sqlCode;
	}
}