package ch.minova.install.setup.schema;

public class SqlPrimaryKeyConstraint extends SqlConstraint {
	public SqlPrimaryKeyConstraint(final String name, final String tableName, final String columnName) {
		super(name, tableName, columnName);
	}
}