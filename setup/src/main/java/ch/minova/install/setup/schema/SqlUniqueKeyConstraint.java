package ch.minova.install.setup.schema;

public class SqlUniqueKeyConstraint extends SqlConstraint {
	public SqlUniqueKeyConstraint(final String name, final String tableName, final String columnName) {
		super(name, tableName, columnName);
	}
}