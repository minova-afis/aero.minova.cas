package ch.minova.install.ncore;

/**
 * Beschreibt einen Applikationstyp. Die Schnittstelle erlaubt dynamische Beschreibung eines Applikationstyps
 * 
 * @author dombrovski
 */
public interface IApplicationType {
	/**
	 * Liefert den Namen ohne Leerzeichen. Wird u.a. für die Auflösung der XBS-Dateien verwendet
	 */
	public String getName();

	/**
	 * Applikationskürzel
	 */
	public String getShortName();

	/**
	 * Langer Applikationsname (Leerzeichen erlaubt)
	 */
	public String getLongName();

	/**
	 * Liefert den XBS-Dateinamen
	 */
	public String getXBSFileName();

	/**
	 * Liefert den Standard-MDI Namen. Sollte nur ausgewertet werden wenn in der XBS nichts drin steht
	 */
	public String getMDIName();
}