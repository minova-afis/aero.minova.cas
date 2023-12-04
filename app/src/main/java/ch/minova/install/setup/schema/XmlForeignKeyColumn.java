package ch.minova.install.setup.schema;

public class XmlForeignKeyColumn {
	private final String referencedColumnName;
	private final String localColumnName;

	/**
	 * @param name
	 *            Name der lokalen Spalte in der Aufgangstabelle
	 * @param refid
	 *            Name der entfernten Spalte in der referenzierten Tabelle
	 */
	public XmlForeignKeyColumn(final String name, final String refid) {
		this.localColumnName = name;
		this.referencedColumnName = refid;
	}

	public String getReferencedColumnName() {
		return this.referencedColumnName;
	}

	public String getLocalColumnName() {
		return this.localColumnName;
	}
}