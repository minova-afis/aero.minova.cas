package ch.minova.install.setup.schema;

public class SqlForeignKeyConstraint extends SqlConstraint {
	private String uniqueKeyConstraintName;

	public SqlForeignKeyConstraint(final String contraintname, final String tablename, final String uniqueConstraintName, final String columnname) {
		super(contraintname, tablename, columnname);
		this.uniqueKeyConstraintName = uniqueConstraintName;
	}

	/**
	 * @return the uniqueKeyConstraintName
	 */
	public String getUniqueKeyConstraintName() {
		return this.uniqueKeyConstraintName;
	}

	/**
	 * @param uniqueKeyConstraintName
	 *            the uniqueKeyConstraintName to set
	 */
	public void setUniqueKeyConstraintName(final String uniqueKeyConstraintName) {
		this.uniqueKeyConstraintName = uniqueKeyConstraintName;
	}

	@Override
	public String toString() {
		return super.toString() + "->" + this.uniqueKeyConstraintName;
	}
}