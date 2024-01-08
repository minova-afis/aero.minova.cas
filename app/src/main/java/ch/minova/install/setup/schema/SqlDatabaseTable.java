package ch.minova.install.setup.schema;

import java.util.HashMap;
import java.util.Vector;

public class SqlDatabaseTable {
	private static HashMap<String, SqlDatabaseTable> tableMap = new HashMap<String, SqlDatabaseTable>();
	private final Vector<SqlDatabaseColumn> columns = new Vector<SqlDatabaseColumn>();
	private final HashMap<String, SqlDatabaseColumn> columnMap = new HashMap<String, SqlDatabaseColumn>();
	private final String name;
	private HashMap<String, SqlConstraint> constraintMap = new HashMap<String, SqlConstraint>();
	private Vector<SqlConstraint> constraints = new Vector<SqlConstraint>();

	public SqlDatabaseTable(final String tablename) {
		this.name = tablename;
		tableMap.put(tablename.toLowerCase(), this);
	}

	public static SqlDatabaseTable getTable(final String tablename) {
		return tableMap.get(tablename.toLowerCase());
	}

	public static void putTable(final SqlDatabaseTable table) {
		tableMap.put(table.getName().toLowerCase(), table);
	}

	public HashMap<String, SqlConstraint> getConstraintMap() {
		return this.constraintMap;
	}

	public void setConstraintMap(final HashMap<String, SqlConstraint> constraintMap) {
		this.constraintMap = constraintMap;
	}

	public String getName() {
		return this.name;
	}

	public void addColumn(final SqlDatabaseColumn column) {
		this.columns.add(column);
		this.columnMap.put(column.getName().toLowerCase(), column);
	}

	public Vector<SqlDatabaseColumn> getColumns() {
		return this.columns;
	}

	public void addConstraint(final SqlConstraint constraint) {
		if (!this.constraintMap.containsKey(constraint.getName().toLowerCase())) {
			this.constraints.add(constraint);
		}
		this.constraintMap.put(constraint.getName().toLowerCase(), constraint);
	}

	public Vector<SqlConstraint> getConstraints() {
		return this.constraints;
	}

	public void setConstraints(final Vector<SqlConstraint> constraints) {
		this.constraints = constraints;
	}

	public SqlDatabaseColumn getColumn(final String columnName) {
		return this.columnMap.get(columnName.toLowerCase());
	}
}