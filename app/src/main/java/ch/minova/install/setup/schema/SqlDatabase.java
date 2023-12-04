package ch.minova.install.setup.schema;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;

public class SqlDatabase {
	private static final String SELECT_COLUMNS = "select b.name tablename, a.name columnname, a.is_nullable, t.name datatype, case a.user_type_id when 231 then a.max_length / 2 else a.max_length end as Length, c.name DefaultName, c.definition DefaultValue, a.collation_name from sys.all_columns a inner join sys.tables b on a.object_id = b.object_id left join sys.default_constraints c on a.default_object_id = c.object_id left join sys.types t on t.user_type_id = a.user_type_id order by b.name";
	private static final String SELECT_CONSTRAINTS = "select kcu.CONSTRAINT_NAME, kcu.TABLE_NAME, kcu.COLUMN_NAME, kcu.ORDINAL_POSITION, rc.UNIQUE_CONSTRAINT_NAME, tc.CONSTRAINT_TYPE from INFORMATION_SCHEMA.KEY_COLUMN_USAGE kcu left join INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS rc on rc.CONSTRAINT_NAME = kcu.CONSTRAINT_NAME left join INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc on tc.CONSTRAINT_NAME = kcu.CONSTRAINT_NAME order by kcu.TABLE_NAME, kcu.CONSTRAINT_NAME, kcu.ORDINAL_POSITION";

	public static void main(final String[] args) {
		final SqlDatabase me = new SqlDatabase();
		try {
			me.readDatabase();
		} catch (final SQLException e) {
			e.printStackTrace();
		} catch (final InstantiationException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private Connection connection;

	private void readDatabase() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		this.connection = DriverManager.getConnection("jdbc:jtds:sqlserver://10.211.55.4/data", "sa", "Minova+0");
		readDataBase(true);
	}

	public HashMap<String, String> readDataBase(final Connection con)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		this.connection = con; // DriverManager.getConnection(connectionstring);
		readDataBase(false);
		return null;
	}

	/**
	 * die beiden Funktionen readDataBase machen fast das selbe, das habe ich vereinheitlicht<br>
	 * diese Funktion setzt voraus, dass connection bereits gesetzt ist
	 * 
	 * @param debug
	 *            ob zus��tzliche Ausgaben gemacht werden sollen
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	private void readDataBase(final boolean debug) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		String tablename;
		String columnname;
		boolean nullable;
		String type;
		int length;
		int pos;
		String contraintname;
		String uniqueconstraintname;
		String defaultName;
		String defaultValue;
		@SuppressWarnings("unused")
		SqlDatabaseTable table;
		SqlDatabaseColumn column;
		SqlDefault columnDefault;
		SqlConstraint constraint = null;
		String collation = null;

		long start = 0, end;

		final Object d = Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
		System.out.println(MessageFormat.format("Driver:{0}", d.toString()));
		System.out.println("Test: " + this.connection.toString());

		System.out.println("Read table columns");
		if (debug) {
			start = System.currentTimeMillis();
		}
		ResultSet rst = this.connection.createStatement().executeQuery(SELECT_COLUMNS);
		int i = 0;
		while (rst.next()) {
			tablename = rst.getString("tablename");
			columnname = rst.getString("columnname");
			nullable = rst.getBoolean("is_nullable");
			type = rst.getString("datatype");
			length = rst.getInt("Length");
			if (rst.getObject("DefaultName") != null) {
				defaultName = rst.getString("DefaultName");
				defaultValue = rst.getString("DefaultValue");
			} else {
				defaultName = null;
				defaultValue = null;
			}
			collation = rst.getString("collation_name");

			table = SqlDatabaseTable.getTable(tablename);

			// An dieser Stelle wird die Spalte definiert!
			column = new SqlDatabaseColumn(tablename, columnname, nullable, type, length, collation);
			/* Früher wurde SqlDatabase#getColumnDefault verwendet, um festzustellen, ob sich der DefaultValue
			 * geändert hat. In diesem Fall wurde früher ein komplett anderer SQl-Update-Code ausgeführt.
			 * Inzwischen werden die Constraints immer gelöscht und anschliessend neu erstellt.
			 *
			 * Wir haben "defaultValue != null" eingebaut, da der defaultValue beim Auslesen manchmal null ist und
			 * der Grund dafür nicht bekannt ist.
			 * Dadurch wird eine NullPointerException verhindert.
			 */
			if (defaultName != null && defaultValue != null) {
				columnDefault = new SqlDefault(column, defaultName, defaultValue);
				column.setColumnDefault(columnDefault);
			}
			if (collation != null) {
				column.setCollation(collation);
			}
			i++;
		}
		rst.close();

		if (debug) {
			end = System.currentTimeMillis();
			System.out.println("Tablecolumns Columns " + i + " (" + (end - start) + "ms)");
		} else {
			System.out.println("Tablecolumns Columns " + i);
		}

		System.out.println("Read table constraints");
		if (debug) {
			start = System.currentTimeMillis();
		}
		rst = this.connection.createStatement().executeQuery(SELECT_CONSTRAINTS);
		// Spalten:
		// CONSTRAINT_NAME: Name der Einschr��nkung
		// TABLE_NAME: Tabelle
		// COLUMN_NAME: Spalte
		// ORDINAL_POSITION: Reihenfolge; neue Contraints haben hier eine 1
		// UNIQUE_CONSTRAINT_NAME: Name des Primary Keys der referenzierten
		// Tabelle
		// CONSTRAINT_TYPE: Art des Constraints (UNIQUE, PRIMARY KEY, FOREIGN
		// KEY
		i = 0;
		while (rst.next()) {
			tablename = rst.getString("TABLE_NAME");
			columnname = rst.getString("COLUMN_NAME");
			pos = rst.getInt("ORDINAL_POSITION");
			contraintname = rst.getString("CONSTRAINT_NAME");
			uniqueconstraintname = rst.getString("UNIQUE_CONSTRAINT_NAME");
			type = rst.getString("CONSTRAINT_TYPE");
			if (pos == 1) {
				// wir haben einen neuen Constraint
				if (type.equals("UNIQUE")) {
					constraint = new SqlUniqueKeyConstraint(contraintname, tablename, columnname);
				} else if (type.equals("PRIMARY KEY")) {
					constraint = new SqlPrimaryKeyConstraint(contraintname, tablename, columnname);
				} else if (type.equals("FOREIGN KEY")) {
					constraint = new SqlForeignKeyConstraint(contraintname, tablename, uniqueconstraintname, columnname);
				} else {
					System.err.println("Unknown CONSTRAINT_TYPE: " + type);
					constraint = null;
				}
			} else {
				if (constraint != null) {
					constraint.addColumn(columnname);
				}
			}
			i++;
		}
		rst.close();

		// hier findet der Geschwindigkeitstest statt
		if (debug) {
			end = System.currentTimeMillis();
			System.out.println("Tablecolumns Constraints " + i + " (" + (end - start) + "ms)");
		} else {
			System.out.println("Tablecolumns Constraints " + i);
		}
		/*
		 * table = SqlDatabaseTable.getTable("tAdditive"); System.out.println("tAdditive"); for (Iterator<SqlDatabaseColumn> c = table.getColumns().iterator();
		 * c.hasNext();) { SqlDatabaseColumn cc = (SqlDatabaseColumn) c.next(); System.out.println(" " + cc.getName() + ", " + cc.getType() + ", " +
		 * cc.listConstraints()); }
		 */
	}
}