package ch.minova.install;

/**
 * Klassen, die dieses Interface implementieren, können während der Installation aufgerufen werden.
 * 
 * @author saak
 */
public interface IInstaller {
	/**
	 * Setzt einen Parameter für die Installation
	 * 
	 * @param name
	 * @param value
	 */
	public void setParameter(String name, String value);

	/**
	 * führt die Installation aus, die die jeweilige Klasse definiert
	 */
	public void execute();
}