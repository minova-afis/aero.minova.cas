package aero.minova.trac.integration;

/**
 * interne Klasse, die eine Wiki-Seite verwaltet
 * 
 * @author wild
 * @since 12.4.0
 */
public class Wiki {
	private String address;
	private String content;

	/**
	 * @param address
	 *            die (interne) Adresse der Wiki-Seite, z.B. "Module/ch.minova.sap.sales"
	 */
	Wiki(String address) {
		this.address = address;
	}

	/**
	 * @return den aktuellen Inhalt der Wiki-Seite
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            der Inhalt, der den der aktuellen Wiki-Seite ersetzen soll (falls vorhanden)
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
}