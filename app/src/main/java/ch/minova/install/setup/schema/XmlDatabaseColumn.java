/**
 * public String getSQLCode() { String sqlCode = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name = N'"+"["+"UK_" + tableName + "_" + columnName + "]"+
 * "' AND user_name(uid) = N'dbo') BEGIN ALTER TABLE dbo." +"["+ tableName +"]"+ " ADD constraint "+"["+"UK_" + tableName + "_" + columnName +"]"+
 * " unique nonclustered ("; for(int i=0; i<uniqueKeyColumns.length; i++){ if(i>0){ sqlCode +=","; } sqlCode +=
 * "["+uniqueKeyColumns[i].getLocalColumnName()+"]"; } sqlCode +=") END"; return sqlCode; }
 */

package ch.minova.install.setup.schema;

import aero.minova.cas.setup.xml.table.*;

import java.io.StringWriter;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

public class XmlDatabaseColumn {
	public static final int BIGINT = 1;
	public static final int BOOLEAN = 2;
	public static final int DATETIME = 3;
	public static final int FLOAT = 4;
	public static final int INT = 5;
	public static final int MONEY = 6;
	public static final int VARCHAR = 0;
	private final String tableName;
	private String columnName;
	private int type;
	private String collation;
	private String defaultValue;

	private final String dropTableConstraints1 = "select 'if exists (select 1 from INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE where"
			+ " Constraint_name = ''' + coalesce(icc2.Constraint_name,icc.Constraint_name) + ''') begin"
			+ " alter table ' + coalesce(icc2.Table_Name,icc.Table_Name) + '"
			+ " drop constraint ' + coalesce(icc2.Constraint_name,icc.Constraint_name) + ' end"
			+ " ' as Script from INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE icc"
			+ " left join INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS ic on ic.UNIQUE_CONSTRAINT_NAME= icc.CONSTRAINT_NAME"
			+ " left join INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE icc2 on icc2.CONSTRAINT_NAME = ic.CONSTRAINT_NAME" + " where icc.TABLE_NAME = '";
	private final String dropTableConstraints2 = "' union select 'if exists (select 1 from INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE "
			+ "where Constraint_name = ''' + CONSTRAINT_NAME + ''')"
			+ " begin alter table ' + TABLE_NAME + ' drop constraint ' + CONSTRAINT_NAME + ' end' as Script "
			+ "from INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE where TABLE_NAME = '";
	private final String dropTableConstraints3 = "' order by Script";
	// private String ifnotexits = "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = N'";
	// private String ifnotexits2 = "' AND TABLE_SCHEMA = N'dbo' AND COLUMN_NAME = '";
	// private String constraintSQL = " IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE WHERE CONSTRAINT_NAME= '";
	// private String simicolonbracketbeginspace = "') BEGIN ";
	// private String EndSQL = " END ";
	private final String bracketaltercolumn = "] ALTER COLUMN ";
	private final String NullableSQL = " null";
	private final String NotNullableSQL = " not null";
	private final String AlterTabledbo = " ALTER TABLE dbo.[";
	// private String bracketdropconstrint = "] DROP CONSTRAINT ";
	private final String collate = " collate Database_default";
	@SuppressWarnings("unused")
	private final String AddConstriantSQl1 = "] ADD CONSTRAINT [";
	@SuppressWarnings("unused")
	private final String DEFAULT = "DF_";

	private final String FOR = "FOR";
	@SuppressWarnings("unused")
	private final String defaultvalue1 = "default('";
	@SuppressWarnings("unused")
	private final String defaultvalue2 = "') ";

	public String getColumnName() {
		return this.columnName;
	}

	public void setColumnName(final String columnName) {
		this.columnName = columnName;
	}

	public String getCollation() {
		if (this.collation == null) {
			return "null";
		}
		return this.collation;
	}

	public void setCollation(final String collation) {
		this.collation = collation;
	}

	// Rückgabe ist ein String mit dem Typen
	public String getType() {
		return getType(0);
	}

	public String getType(final int original) {
		switch (this.type) {
		case BIGINT:
			return "bigint";
		case BOOLEAN:
			return "bit";
		case DATETIME:
			return "datetime";
		case FLOAT:
			return "float";
		case INT:
			return "int";
		case MONEY:
			return "money";
		case VARCHAR: {
			if (original == 1) {
				return "varchar";
			}
			return "nvarchar";
		}
		default:
			return "null";
		}
	}

	/*
	 * Dies wi
	 */
	public String getTypeText() {
		switch (this.type) {
		case BIGINT:
			return "bigint";
		case BOOLEAN:
			return "bit";
		case DATETIME:
			return "datetime";
		case FLOAT:
			return "float";
		case INT:
			return "int";
		case MONEY:
			return "money";
		case VARCHAR: {
			return "nvarchar (" + this.length + ")";
		}
		default:
			return "null";
		}
	}

	public void setType(final int type) {
		this.type = type;
	}

	public int getLength() {
		return this.length;
	}

	public void setLength(final int length) {
		this.length = length;
	}

	public boolean isNullable() {
		return this.nullable;
	}

	public void setNullable(final boolean nullable) {
		this.nullable = nullable;
	}

	private int length;
	private boolean nullable = false;
	private int decimals;
	private boolean identity;

	public boolean isIdentity() {
		return this.identity;
	}

	public void setIdentity(final boolean identity) {
		this.identity = identity;
	}

	public XmlDatabaseColumn(final Table table, final Column column) {
		this.columnName = column.getName();
		if (column.getBigint() != null) {
			this.type = BIGINT;
			this.nullable = column.getBigint().getNullable();
		} else if (column.getBoolean() != null) {
			this.type = BOOLEAN;
			this.nullable = column.getBoolean().getNullable();
		} else if (column.getDatetime() != null) {
			this.type = DATETIME;
			this.nullable = column.getDatetime().getNullable();
		} else if (column.getFloat() != null) {
			this.type = FLOAT;
			this.nullable = column.getFloat().getNullable();
			this.decimals = column.getFloat().getDecimals();
		} else if (column.getInteger() != null) {
			this.type = INT;
			this.nullable = column.getInteger().getNullable();
			this.identity = column.getInteger().getIdentity();
		} else if (column.getMoney() != null) {
			this.type = MONEY;
			this.nullable = column.getMoney().getNullable();
		} else if (column.getVarchar() != null) {
			this.type = VARCHAR;
			this.nullable = column.getVarchar().getNullable();
			this.length = column.getVarchar().getLength().intValue();
		}
		this.defaultValue = column.getDefault();
		this.tableName = table.getName();
	}

	public String getDefaultValue() {
		if (this.defaultValue == null) {
			return "null";
		}
		return this.defaultValue;
	}

	public void setDefaultValue(final String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public XmlDatabaseColumn(final String tableName, final String columnName, final int type, final boolean nullable, final boolean identity) {
		this.tableName = tableName;
		this.columnName = columnName;
		this.type = type;
		this.nullable = nullable;
		this.identity = identity;
		this.collation = "database_default";
	}

	public XmlDatabaseColumn(final String tableName, final String columnName, final int type, final boolean nullable, final int length) {
		this.tableName = tableName;
		this.columnName = columnName;
		this.type = type;
		this.nullable = nullable;
		this.length = length;
		this.identity = false;
		this.collation = "database_default";
	}

	public XmlDatabaseColumn(final String tableName, final String columnName, final int type, final boolean nullable, final int length,
			final String defaultValue) {
		this.tableName = tableName;
		this.columnName = columnName;
		this.type = type;
		this.nullable = nullable;
		this.length = length;
		this.identity = false;
		this.defaultValue = defaultValue;
		this.collation = "database_default";
	}

	public XmlDatabaseColumn(final String tableName, final String columnName, final int type, final boolean nullable, final int length, final int decimals) {
		this.tableName = tableName;
		this.columnName = columnName;
		this.type = type;
		this.nullable = nullable;
		this.length = length;
		this.identity = false;
		this.defaultValue = null;
		this.decimals = decimals;
		this.collation = "database_default";
	}

	public XmlDatabaseColumn(final Table t, final Column column, final String dbCollation) {
		this.columnName = column.getName();
		if (column.getBigint() != null) {
			this.type = BIGINT;
			this.nullable = column.getBigint().getNullable();
		} else if (column.getBoolean() != null) {
			this.type = BOOLEAN;
			this.nullable = column.getBoolean().getNullable();
		} else if (column.getDatetime() != null) {
			this.type = DATETIME;
			this.nullable = column.getDatetime().getNullable();
		} else if (column.getFloat() != null) {
			this.type = FLOAT;
			this.nullable = column.getFloat().getNullable();
			this.decimals = column.getFloat().getDecimals();
		} else if (column.getInteger() != null) {
			this.type = INT;
			this.nullable = column.getInteger().getNullable();
			this.identity = column.getInteger().getIdentity();
		} else if (column.getMoney() != null) {
			this.type = MONEY;
			this.nullable = column.getMoney().getNullable();
		} else if (column.getVarchar() != null) {
			this.type = VARCHAR;
			this.nullable = column.getVarchar().getNullable();
			this.length = column.getVarchar().getLength().intValue();
		}
		this.defaultValue = column.getDefault();
		this.tableName = t.getName();
		this.collation = dbCollation;
	}

	@Override
	public String toString() {
		return getSqlCode();
	}

	public String getSqlCode() {
		String colString = "[" + this.columnName + "]";
		switch (this.type) {
		case BIGINT:
			colString += " bigint";
			break;
		case BOOLEAN:
			colString += " bit";
			break;
		case DATETIME:
			colString += " datetime";
			break;
		case FLOAT:
			colString += " float /* " + this.decimals + " decimals */";
			break;
		case INT:
			colString += " int";
			if (this.identity) {
				colString += " identity(1, 1)";
			}
			break;
		case MONEY:
			colString += " money";
			break;
		case VARCHAR:
			colString += " nvarchar(" + this.length + ")" + this.collate;
			break;
		default:
			break;
		}

		if (!this.nullable || this.identity) {
			colString += " not";
		}
		colString += " null";
		if (this.defaultValue != null && this.defaultValue.length() > 0) {
			colString += " constraint " + "[" + "DF_" + this.tableName + "_" + this.columnName + "]" + " default(" + this.defaultValue + ")";
		}
		return colString;
	}

	public String getName() {
		return this.columnName;
	}

	public void generateXml(final Table table) {
		final Column col = table.addNewColumn();
		col.setName(this.columnName);
		switch (this.type) {
		case BIGINT:
			col.addNewBigint().setNullable(this.nullable);
			break;
		case BOOLEAN:
			col.addNewBoolean().setNullable(this.nullable);
			break;
		case DATETIME:
			col.addNewDatetime().setNullable(this.nullable);
			break;
		case FLOAT:
			final ColumnFloat f = col.addNewFloat();
			f.setNullable(this.nullable);
			f.setDecimals(this.decimals);
			break;
		case INT:
			final ColumnInteger i = col.addNewInteger();
			i.setIdentity(this.identity);
			i.setNullable(this.nullable);
			break;
		case MONEY:
			col.addNewMoney().setNullable(this.nullable);
			break;
		case VARCHAR:
			final ColumnVarchar v = col.addNewVarchar();
			v.setLength(this.length);
			v.setNullable(this.nullable);
			break;
		default:
			break;
		}
	}

	public void generateSql(final StringWriter sw, final boolean firstColumn) {
		if (!firstColumn) {
			sw.write(", ");
		}
		sw.write(getSqlCode());
	}

	/**
	 * @return String mit SQL-Anweisung zum die Tabellenspalte zu bearbeiten.
	 * @throws SQLException
	 */
	private String getChangeTableColumn(final Connection connection) throws SQLException {
		ResultSet rs;
		String returncode = null;
		try {
			rs = connection.createStatement()
					.executeQuery(this.dropTableConstraints1 + this.tableName + this.dropTableConstraints2 + this.tableName + this.dropTableConstraints3);
			while (rs.next()) {
				connection.createStatement().execute(rs.getString("Script"));
			}
		} catch (final SQLException e) {
			throw new SQLException(e.getMessage() + "getChangeTableColumn() - Error drop Constraints");
		}

		if (isNullable()) {
			if (getType().equalsIgnoreCase("nvarchar")) {

				returncode = this.AlterTabledbo + this.tableName + this.bracketaltercolumn + "[" + this.columnName + "]" + " " + getType() + " (" + getLength()
						+ ")" + this.collate + this.NullableSQL;
			} else {
				returncode = this.AlterTabledbo + this.tableName + this.bracketaltercolumn + "[" + this.columnName + "]" + " " + getType() + this.NullableSQL;
			}
		} else {
			if (getType().equalsIgnoreCase("nvarchar")) {

				returncode = this.AlterTabledbo + this.tableName + this.bracketaltercolumn + "[" + this.columnName + "]" + " " + getType() + " (" + getLength()
						+ ")" + this.collate + this.NotNullableSQL;
			} else {
				returncode = this.AlterTabledbo + this.tableName + this.bracketaltercolumn + "[" + this.columnName + "]" + " " + getType()
						+ this.NotNullableSQL;
			}

		}

		return returncode;
	}

	/**
	 * derzeit werden nur Änderungen am Spaltenname und an Constraints gemacht
	 * 
	 * @param sqlColumn
	 * @param sdt
	 * @return
	 * @throws SQLException
	 */
	public String getSqlUpdateCode(final SqlDatabaseColumn sqlColumn, final SqlDatabaseTable sdt, final Connection connection, final boolean LogDB)
			throws SQLException {
		if (sqlColumn == null) {
			for (final SqlDatabaseColumn sqlcolumn : sdt.getColumns()) {
				// Hier wird Überprüft ob der Name der Spalte ein Leerzeichen
				// enthält
				if (getName().trim().equalsIgnoreCase(sqlcolumn.getName().trim())) {
					if (!sqlcolumn.getName().equalsIgnoreCase(sqlcolumn.getName().trim())) {
						// Erstellen des Befehls um die Spalte umzubenennen!
						System.out.println(MessageFormat.format("Ändern des Spaltennamen =: -{0}- aus Tabelle: -{2}- nach -{1}-", sqlcolumn.getName(),
								getName().trim(), this.tableName));
						return "exec sp_rename '" + this.tableName + "." + sqlcolumn.getName() + "','" + getName().trim() + "','COLUMN'";
					}
				}
			}
			return "alter table " + "[" + this.tableName + "]" + " add " + getSqlCode();
		}

		// Wenn die Collation nicht gleich ist, wird diese extra geändert.
		if (getType() == "nvarchar") {
			if (!sqlColumn.getCollation().toString().equalsIgnoreCase(getCollation().toString())) {
				return getChangeTableColumn(connection);
			}
		}

		// abfrage der nullable Eigenschaft
		if ((sqlColumn.isNullable() == isNullable())) {
			// wenn der Type gleich ist getType(0) gint nvarchar zurück
			if (sqlColumn.getType().equalsIgnoreCase(getType(0))) {
				// wenn die Lännge stimmt
				if (sqlColumn.getLength() == getLength()) {
					if (sqlColumn.getType().equalsIgnoreCase("bit")) {
						if (sqlColumn.getLength() == 1 && getLength() == 0) {
							return null;
						}
					} else {
						return null;
					}
				} else {
					// TODO
					// Änderungen in Bezug auf die Länge des Feldes
					if (sqlColumn.getLength() < getLength()) {
						return getChangeTableColumn(connection);
					}
				}
			} else if (sqlColumn.getType() == "varchar") {
				return getChangeTableColumn(connection);
			}
		}
		return null;
	}
}