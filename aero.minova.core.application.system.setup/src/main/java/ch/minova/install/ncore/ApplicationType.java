package ch.minova.install.ncore;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Beschreibt vordefinierte Applikationstypen
 * 
 * @author dombrovski
 */
public class ApplicationType implements IApplicationType {
	public static final ApplicationType DISPO = new ApplicationType("DISPO", "Disposition", "Disposition");
	public static final ApplicationType TTA = new ApplicationType("TTA", "TankTerminalAutomation", "Tank Terminal Automation");
	public static final ApplicationType OZD = new ApplicationType("OZD", "OZDServer", "OZD-Server");
	public static final ApplicationType AFIS = new ApplicationType("AFIS", "AFIS", "Aircraft Fuelling Information System");
	public static final ApplicationType SIS = new ApplicationType("SIS", "ServicesInvoicingSystem", "Services Invoicing System");
	public static final ApplicationType TTS = new ApplicationType("TTS", "TruckTariffSystem", "Truck Tariff System");
	public static final ApplicationType MINOVA = new ApplicationType("MINOVA", "MinovaGereric", "MINOVA Generic Application", "minova.xbs", "minova_MDI.XML");
	public static final ApplicationType TMS = new ApplicationType("TMS", "TruckManagementSystem", "Truck Management System");
	public static final ApplicationType TOOLS = new ApplicationType("TOOLS", "MINOVADevelopmentTools", "Software Tools für MINOVA");
	public static final ApplicationType DVS = new ApplicationType("DVS", "DepotVisualisationSystem", "Visualisation of Depotsites");
	public static final ApplicationType BUNKER = new ApplicationType("BUNKER", "Bunkerboat", "Bunkerboat Automation");
	public static final ApplicationType UNKNOWN = new ApplicationType("UNKNOWN", "Unknown", "Unknown", null, null);
	public static final ApplicationType JUNIT = new ApplicationType("JUNIT", "JUnit", "Applikation für JUnit Tests", null, null);
	public static final List<IApplicationType> STANDARD_APPLICATIONS = new ArrayList<IApplicationType>();

	// Applikationen eintragen
	static {
		try {
			for (final Field f : ApplicationType.class.getFields()) {
				if (IApplicationType.class.isAssignableFrom(f.getType()) && (f.getModifiers() & Modifier.STATIC) != 0) {
					STANDARD_APPLICATIONS.add((IApplicationType) f.get(null));
				}
			}
		} catch (final Exception ex) {
			System.err.println("Error occured during the discovery of available application types: " + ex.getMessage());
		}
	}

	/** Kürzel, z.B. 'DISPO' */
	private final String SHORT_NAME;

	/** Standardname ohne Leerzeichen, z.B. 'Disposition' */
	private final String NAME;

	/** Langer Applikationsname */
	private final String LONG_NAME;

	/** XBS-Dateiname mit Endung */
	private final String XBS_FILE;

	/** Standard-MDI Datei. Sollte nur ausgewertet werden wenn in der XBS nichts drin steht */
	private final String MDI_FILE;

	public ApplicationType(final String shortName, final String name, final String longName) {
		this(shortName, name, longName, name + ".xbs", name + "_MDI.xml");
	}

	public ApplicationType(final String shortName, final String name, final String longName, final String xbsName, final String mdiFile) {
		this.SHORT_NAME = shortName;
		this.NAME = name;
		this.LONG_NAME = longName;
		this.XBS_FILE = xbsName;
		this.MDI_FILE = mdiFile;
	}

	@Override
	public String getName() {
		return this.NAME;
	}

	@Override
	public String getShortName() {
		return this.SHORT_NAME;
	}

	@Override
	public String getLongName() {
		return this.LONG_NAME;
	}

	/**
	 * Liefert alle verfügbaren Kürzel als Liste
	 */
	public static final String getApplicationList() {
		final StringBuffer sb = new StringBuffer();
		for (final IApplicationType a : STANDARD_APPLICATIONS) {
			sb.append(a.getShortName()).append(" ");
		}
		return sb.toString();
	}

	/**
	 * Liefert uns eine Application vom String oder null
	 */
	public static final IApplicationType getApplication(final String name) {
		for (final IApplicationType a : STANDARD_APPLICATIONS) {
			if (a.getName().equalsIgnoreCase(name)) {
				return a;
			} else if (a.getLongName().equalsIgnoreCase(name)) {
				return a;
			} else if (a.getShortName().equalsIgnoreCase(name)) {
				return a;
			}
		}
		return null;
	}

	/**
	 * Liefert den Typ ausgehend vom eingegebenen Kurznamen (z.B. TTA, AFIS, DISPO)
	 * 
	 * @param name
	 */
	public static IApplicationType valueOfShortName(final String name) {
		if (name != null) {
			for (final IApplicationType at : STANDARD_APPLICATIONS) {
				if (name.equalsIgnoreCase(at.getShortName())) {
					return at;
				}
			}
		}
		return UNKNOWN;
	}

	@Override
	public String getXBSFileName() {
		return this.XBS_FILE;
	}

	@Override
	public String getMDIName() {
		return this.MDI_FILE;
	}

	@Override
	public String toString() {
		return this.SHORT_NAME;
	}

	/**
	 * Den DefaultType benötigen wir, damit wir die das Application-Object GLOBAL als JUNIT ApplicationType für Unit-Tests erstellen können. <br/>
	 * Damit erreichen wir u.a., dass es keinen Log-Output von der Application bei Unit-Tests gibt.
	 */
	private static ApplicationType defaultApplicationType = UNKNOWN;

	/**
	 * Wir können mit dieser Funktion der ApplicationType setzen. Dies hat jedoch nur Auswirkung, wenn wir noch keine Application erstellt haben. <br/>
	 * Mit Aufruf einer statischen Funktion in der Klasse Application wird auch die erste Application gesetzt. Dies ist in der Regel UNKNOWN - Es sei denn man
	 * ändert es zuvor mit Aufruf dieser Funktion.
	 * 
	 * @param type
	 *            neuer Standardanwendungstyp für den ersten Konstruktoraufruf von Application
	 * @author saak
	 * @since 10.25.0
	 */
	public static void setDefault(final ApplicationType type) {
		defaultApplicationType = type;
	}

	/**
	 * Setzt den Standard-ApplicationType anhand des übergebenen Namens
	 * 
	 * @param type
	 * @author wild
	 * @since 11.0.0
	 * @see #setDefault(ApplicationType)
	 */
	public static void setDefault(final String type) {
		final IApplicationType applicationType = valueOfShortName(type);
		if (applicationType != null && applicationType instanceof ApplicationType) {
			setDefault((ApplicationType) applicationType);
		}
	}

	/**
	 * Wir können mit dieser Funktion der ApplicationType abfragen.<br/>
	 * Mit Aufruf einer statischen Funktion in der Klasse Application, wird auch die erste Applcation gesetzt. Dies ist in der Regel UNKNOWN - Es sei denn man
	 * ändert es zuvor mit {@link #setDefault(ApplicationType)}.
	 * 
	 * @return Standardanwendungstyp für den ersten Konstruktoraufruf von Application
	 * @author saak
	 * @since 10.25.0
	 */
	public static ApplicationType getDefault() {
		return defaultApplicationType;
	}
}