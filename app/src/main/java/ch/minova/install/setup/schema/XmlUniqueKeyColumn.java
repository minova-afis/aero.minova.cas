package ch.minova.install.setup.schema;

public class XmlUniqueKeyColumn {
	private final String localColumnName;

	/**
	 * @param name
	 *            Name der lokalen Spalte in der Aufgangstabelle
	 * @param refid
	 *            Name der entfernten Spalte in der referenzierten Tabelle
	 */
	public XmlUniqueKeyColumn(final String name) {
		this.localColumnName = name;
	}

	public String getLocalColumnName() {
		return this.localColumnName;
	}
}