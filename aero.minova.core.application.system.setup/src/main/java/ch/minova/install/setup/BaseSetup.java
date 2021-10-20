package ch.minova.install.setup;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Optional;
import java.util.Properties;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.management.RuntimeErrorException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.FileUtils;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;

import ch.minova.core.install.DircopyDocument.Dircopy;
import ch.minova.core.install.ExecuteJavaDocument.ExecuteJava;
import ch.minova.core.install.FilecopyDocument.Filecopy;
import ch.minova.core.install.Log4JConfDocument.Log4JConf;
import ch.minova.core.install.MainDocument;
import ch.minova.core.install.MainDocument.Main;
import ch.minova.core.install.Map;
import ch.minova.core.install.ModuleDocument.Module;
import ch.minova.core.install.NodeDocument.Node;
import ch.minova.core.install.OneOfDocument.OneOf;
import ch.minova.core.install.PreferencesDocument;
import ch.minova.core.install.PreferencesDocument.Preferences;
import ch.minova.core.install.PreferencesDocument.Preferences.Root;
import ch.minova.core.install.ScriptDocument.Script;
import ch.minova.core.install.ScriptDocument.Script.Type;
import ch.minova.core.install.ScriptDocument.Script.Type.Enum;
import ch.minova.core.install.ServiceDocument.Service;
import ch.minova.core.install.SetupDocument;
import ch.minova.core.install.TableschemaDocument.Tableschema;
import ch.minova.core.install.VersionDocument.Version;
import ch.minova.core.install.WrapperConfDocument.WrapperConf;
import ch.minova.install.IInstaller;
import ch.minova.install.ncore.ApplicationType;
import ch.minova.install.ncore.IApplicationType;
import ch.minova.install.setup.copyfile.DirCopy;
import ch.minova.install.setup.copyfile.FileCopy;
import ch.minova.install.setup.mdi.Entry;
import ch.minova.install.setup.mdi.Menu;
import ch.minova.install.setup.schema.SqlDatabase;
import ch.minova.install.setup.schema.SqlDatabaseTable;
import ch.minova.install.setup.schema.XmlDatabaseColumn;
import ch.minova.install.setup.schema.XmlDatabaseTable;
import ch.minova.install.setup.xbs.NodeException;
import ch.minova.install.sql.TVersion;
import net.sourceforge.jtds.util.SSPIJNIClient;

/**
 * Basisklasse für die Installer der Module
 *
 * @author erlanger
 */
public class BaseSetup {
	public static final String MISSINGFILE = "Die Datei {0} wurde nicht gefunden.";
	public static final String MISSINGMODULE = "Folgendes Modul wurde nicht gefunden: {0}";
	public static final String MODULENOVERSION = "Das Modul {0} ist nicht in der erforderlichen Minimal-Version {1}.{2}.{3} vorhanden!";
	public static final String REQUIREDMODULES = "Erforderliches Modul: {0}";
	public static final String REQUIREDVERSION = "Erforderliche Minimal-Version : {0}.{1}.{2} fuer Modul {3} muss bereitgestellt werden!";
	public static final String MODULEISVERSION = "Das Modul ist in der erforderlichen Minimal-Version vorhanden: {0}";
	public static final String MODULE_PATCHLEVEL = "Das Modul {0} ist nicht in dem erforderlichen Patchlevel vorhanden: {1}";
	public static final String MODULE_MINORLEVEL = "Das Modul {0} ist nicht in der erforderlichen Minorversion vorhanden: {1}";
	public static final String MODULE_MAJORLEVEL = "Das Modul {0} ist nicht in der erforderlichen Majorversion vorhanden: {1}";
	public static final String MODULEMISSING = "Das folgende Modul konnte nicht gefunden werden: ";
	public static final String BUILDPROPMISSING = "Die Datei buildnumber.properties wurde nicht gefunden: {0}";
	public static final String LOADFILEERROR = "Fehler beim Laden der Datei: {0}, Fehlermeldung: {1}";
	public static final String MODULEARRAYOK = "Aus der Liste von moeglichgen Modulen wurde mindestens eins gefunden! - OK";
	public static final String MODULEARRAYFAIL = "Aus der Liste von moeglichen Modulen wurde keins gefunden! - FAIL";
	public static final String MODULEONEOFNOTFOUND = "Das Modul {0} ist in dem OneOf-Kontext nicht vorhanden!";
	public static final String MODULEONEOFWRONGVERSION = "Das Modul {0} ist in dem OneOf-Kontext mit der falschen Version vorhanden!";
	public static final String MODULEONEOFFOUND = "Das Modul {0} ist in dem OneOf-Kontext vorhanden!";
	public static final String ONEOFFAIL = "Kein Modul aus den OneOf-Modulen ist verfuegbar: {0}";
	public static final String CHECKMODULEOK = "Erforderliche Module wurden gefunden, das Paket ist bereit zur Installation! CHECKMODULES OK!";
	public static final String CIRCULATORYREQUIREMENT = "Ein Kreislauf wurde in den Abhaengigkeiten von {0} gefunden!";
	public static final String MISSINGXMLDATA = "Keine der erforderlichen XML-Dateien ist im xbs-Code verfuegbar!";
	public static final String MULTIPLEXMLINXBS = "Die xml-Datei {0} ist bereits in der xbs-Datei {1} vorhanden!";
	public static final String FILEFAILTOCREATE = "Die Datei konnte nicht erstellt werden: {0}!";
	public static final String FAILTOLOADXBS = "Die XBS-Datei {0} konnte nicht geladen werden!";
	public static final String FAILSAVETOHASH = "Das Modul konnte nicht gespeichert werden!";
	public static final String FAILTOWRITENODETODOCUMENT = "Error: Der Knoten {0} konnte nicht in die Datei eingetragen werden!";
	public static final String FAILSAVEDATA = "Error: die Datei {0} konnte nicht gespeichert werden!";

	public static ch.minova.install.setup.xbs.Node rootNode = new ch.minova.install.setup.xbs.Node("minova");
	public static ch.minova.install.setup.xbs.Root rootNodeRoot = new ch.minova.install.setup.xbs.Root("system");
	public static ch.minova.install.setup.mdi.Main mainmdi = new ch.minova.install.setup.mdi.Main();

	/**
	 * In dieser Tabelle werden die VersionInfo-Objekte alle geladenen Module gespeichert. Als Schlüssel wird der Modulename verwendet (z.B. ch.minova.install)
	 */
	private static Hashtable<String, VersionInfo> hashModules = new Hashtable<String, VersionInfo>();
	private static Hashtable<String, String> hashtables = new Hashtable<String, String>();
	private static Vector<TableVector> tablevector = new Vector<TableVector>();

	/**
	 * Modulname des Moduls welches im Abhängigkeitsbaum zum Schluss
	 */
	private static String LastMdiModu;
	// key sollte der Name der Maske oder Optionpage sein
	// private static Hashtable<String, String> ModuleInfoHash = new
	// Hashtable<String, String>();
	// private static Vector<String> ModuleIndoVector = new Vector<String>();
	/**
	 * Dieser Vector enthält alle Setup-Objekte der geladenen Module in der Reihenfolge, in der sie abgearbeitet werden müssen. Wenn Module ch.minova.install
	 * von Module ch.minova.database benötigt wird, steht ch.minova.install vor ch.minova.database im Vector. Der Vector wird beim Einlesen und Prüfen der
	 * Module einmalig aufgebaut.
	 */
	private static Vector<BaseSetup> orderedDependingModules = new Vector<BaseSetup>();
	private static Vector<BaseSetup> orderedlocalModules = new Vector<BaseSetup>();
	private static Hashtable<String, Integer> hashModulesXbs = new Hashtable<String, Integer>();
	private static Hashtable<String, Integer> hashModulesMdi = new Hashtable<String, Integer>();
	// Für die Module die nicht da sind
	private static Hashtable<String, Boolean> modulesnotfound = new Hashtable<String, Boolean>();
	private static Vector<String> modulesnotfoundvector = new Vector<String>();
	/**
	 * um bereits untersuchte Module zu speichern
	 */
	private static Hashtable<String, Integer> hashModulesActive = new Hashtable<String, Integer>();
	// verwendet für das Abfangen zirkulärer Abhängigkeiten

	public static Properties parameter = null;
	private static String sqldialect = "sql";
	private static String xbsName;
	private static String connectiondb = "";

	// #22872
	private String Language = "";

	private SetupDocument setupDocument;

	/**
	 * eine Enum für die Parameter der Ausführung von Javacode in der Setup.xml
	 */
	public enum ExecuteJavaType {
		COPYFILES, WRITEMDI, WRITEXBS, UPDATESCHEMA, UPDATEDATABASE, INSTALLSERVICE
	}

	ExecuteJavaType executejava;

	/**
	 * Ueberpruefung der Versionsnummern des Modules
	 *
	 * @param name
	 * @return boolean <br>
	 * sobald eine der Versionen uebereinstimmt, wird true zurueckgegeben.
	 */
	private static boolean checkActive(final String name) {
		if (hashModulesActive.isEmpty()) {
			return true;
		}
		try {
			@SuppressWarnings("unused") final int i = hashModulesActive.get(name);
		} catch (final NullPointerException e) {
			return true;
		}
		return false;
	}

	/**
	 * @param moduleName  Name des zu prüfenden Moduls (z.B.:"ch.minova.data")
	 * @param versionInfo Die ???
	 * @return
	 * @function Ueberpruefung des angegebenen Strings und der angegebenen VersionInfo von dem Module() StringnameModule ob dieser in der gegebenen version
	 * vorhanden ist bzw. kompatiblen Version vorhanden
	 */
	public static boolean checkKey(final String moduleName, final VersionInfo versionInfo) {
		final VersionInfo verinfo = hashModules.get(moduleName);
		return versionInfo.compatibleTo(verinfo);
	}

	/**
	 * @param module
	 * @return
	 * @throws IncompatibleVersionException
	 * @function gibt mit dem Rueckgabeparameter an, ob das angegebene Module bereits in dem Hashtable aufgenommen, und somit bereits ueberprueft worden ist
	 */
	public static boolean checkKeyModule(final Module module) throws IncompatibleVersionException {
		VersionInfo verinfo;
		try {
			verinfo = hashModules.get(module.getName());
		} catch (final Exception e) {
			return false;
		}
		boolean versionIsOk = false;
		final Version[] viarray = module.getVersionArray();
		if (verinfo != null) {
			if (viarray.length > 0) {
				// mehrere Versionen eines Moduls sind vorhanden
				for (int i = 0; i < viarray.length; i++) {
					if (verinfo.getMajorVersion() == Integer.parseInt(viarray[i].getMajor().toString())) {
						if (verinfo.getMinorVersion() > Integer.parseInt(viarray[i].getMinor().toString())) {
							versionIsOk = true;
							break;
						} else if (verinfo.getMinorVersion() == Integer.parseInt(viarray[i].getMinor().toString())
								&& verinfo.getPatchLevel() >= Integer.parseInt(viarray[i].getPatch().toString())) {
							versionIsOk = true;
							break;
						}
					}
				}
				if (!versionIsOk) {
					throw new IncompatibleVersionException(
							MessageFormat.format(MODULENOVERSION, module.getName(), getMajorVersion(module), getMinorVersion(module), getPatchVersion(module)));
				}
			} else {
				if (verinfo.getMajorVersion() == getMajorVersion(module)) {
					if (verinfo.getMinorVersion() > getMinorVersion(module)) {
						versionIsOk = true;
					} else if (verinfo.getMinorVersion() == getMinorVersion(module) && verinfo.getPatchLevel() >= getPatchVersion(module)) {
						versionIsOk = true;
					}
				}
				if (!versionIsOk) {
					throw new IncompatibleVersionException(
							MessageFormat.format(MODULENOVERSION, module.getName(), getMajorVersion(module), getMinorVersion(module), getPatchVersion(module)));
				}
			}
		} else {
			// Modul nicht in Hashtable vorhanden
		}

		return versionIsOk;
	}

	private static int getMajorVersion(final Module module) {
		try {
			return module.getMajor().intValue();
		} catch (final NullPointerException npe) {
			return 10;
		}
	}

	private static int getMinorVersion(final Module module) {
		try {
			return module.getMinor().intValue();
		} catch (final NullPointerException npe) {
			return 0;
		}
	}

	private static int getPatchVersion(final Module module) {
		try {
			return module.getPatch().intValue();
		} catch (final NullPointerException npe) {
			return 0;
		}
	}

	/**
	 * @param nameModul
	 * @return boolean
	 * @function entfernt einen Eintrag aus dem Hashtable
	 */
	public static boolean deleteModuleActive(final String nameModul) {
		try {
			hashModulesActive.remove(nameModul);

			if (hashModulesActive.get(nameModul) == null) {
				return true;
			}
		} catch (final NullPointerException e) {
			return true;
		}

		return false;
	}

	/**
	 * Auslesen des Modulenamen inlusive der zugehoerigen Versionsnummer
	 *
	 * @param clazz BaseSetup
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getModuleName(final Class clazz) {
		final String className = clazz.getName();
		if (className.endsWith(".setup.Setup")) {
			return className.substring(0, className.length() - 12);
		}
		return "invalid Class " + className;
	}

	public static String getXbsName() {
		return xbsName;
	}

	/**
	 * verarbeitet die Übergebenen argumente und speichert diese als Parameter
	 *
	 * @param args
	 */
	public static void analyseArgs(final String[] args) {
		if (parameter == null) {
			parameter = new Properties();
		}

		if (args != null) {
			String argName, argValue, arg;
			for (int i = 0; i < args.length; i++) {
				arg = args[i];
				if (arg.startsWith("-")) {
					final int posEquals = arg.indexOf("=");
					if (posEquals < 0) {
						argName = arg.substring(1);
						argValue = "true";
					} else {
						argName = arg.substring(1, posEquals);
						argValue = arg.substring(posEquals + 1);
					}
					setParameter(argName, argValue);
				}
			}
		}

		if (!parameter.containsKey("basedir")) {
			setParameter("basedir", "../");
		}
		if (parameter.containsKey("basedir")) {
			final String getBasedir = parameter.getProperty("basedir");
			if (!getBasedir.endsWith("/")) {
				setParameter("basedir", getBasedir + "/", true);
			}
		}
		if (!parameter.containsKey("outdir")) {
			setParameter("outdir", parameter.getProperty("basedir"));
		}
		if (parameter.containsKey("outdir")) {
			final String getOutdir = parameter.getProperty("basedir");
			if (!getOutdir.endsWith("/")) {
				setParameter("outdir", getOutdir + "/");
			}
		}
		if (!parameter.containsKey("app")) {
			setParameter("app", "disposition");
		}
		if (!parameter.containsKey("file")) {
			setParameter("file", "");
		}
		if (!parameter.containsKey("u")) {
			setParameter("u", "sa");
		}
		if (!parameter.containsKey("p")) {
			setParameter("p", "Minova+0");
		}
		if (!parameter.containsKey("override")) {
			setParameter("override", "false");
		}
		if (parameter.containsKey("installallwithoutsql") || parameter.containsKey("iaws")) {
			setParameter("cf", "true", true);
			setParameter("writemdi", "true", true);
			setParameter("writexbs", "true", true);
			setParameter("calljava", "true", true);
			setParameter("cl", "true", true);
		}
		if (parameter.containsKey("installservicewithoutxbs") || parameter.containsKey("iswx")) {
			setParameter("ud", "true", true);
		}
		if (parameter.containsKey("installservice") || parameter.containsKey("is")) {
			setParameter("cf", "true", true);
			setParameter("writexbs", "true", true);
			setParameter("ud", "true", true);
		}
		if (parameter.containsKey("installall") || parameter.containsKey("ia")) {
			setParameter("us", "true", true);
			setParameter("ud", "true", true);
			setParameter("cf", "true", true);
			setParameter("writemdi", "true", true);
			setParameter("writexbs", "true", true);
			setParameter("calljava", "true", true);
			setParameter("cl", "true", true);
		}
	}

	private static void printHelp() {
		System.out.println("usage:");
		System.out.println("java -jar install.jar");
		System.out.println(" -nodb    			   Es wird keine Datenbankverbindung aufgebaut.");
		System.out.println(" -app=<application>    welche Anwendung wird installiert (muss).");
		System.out.println(" -file=<file>          zu schreibende Datei");
		System.out.println(" -checkconnection (cc) geht eine Verbindung mit der Datenbank ein.");
		System.out.println("                       -u und -p muessen mit eingegeben werden.");
		System.out.println(" -checkmodules (cm)    Ueberprueft die Abhaengigkeiten des angegebnen Modules");
		System.out.println(" -cm -list             Wird nur zusammen mit (-cm) ausgewertet. Gibt eine Liste");
		System.out.println("                       mit den fehlenden Modulen aus.");
		System.out.println(" -checkmodules -list   Wird nur zusammen mit (-cm) ausgewertet. Gibt eine Liste");
		System.out.println("                       mit den fehlenden Modulen aus.");
		System.out.println(" -checkxbscode (cxbs)  Ueberprueft den xbs-code und legt gegebenenfalls");
		System.out.println("                       entsprechende Felder an");
		System.out.println(" -copyfile (cf)        Kopiert die in der Setup.xml angegebnen Dateien");
		System.out.println("                       in das /Program File/${application} Verzeichnis");
		System.out.println(" -fs                   erzwingt die Ausfuehrung aller SQL-Skripte");
		System.out.println(" -in=<filename>        setzt das Eingangspaket. (muss)");
		System.out.println(" -basedir=<dir>        Verzeichnis, in dem sich die Verzeichnisse");
		System.out.println("                       \"deploy\" und \"Program Files\" befinden");
		System.out.println(" -versioninfo (vi)     liefert die Versionsinformationen zum angegebenen Paket");
		System.out.println("                       (optional)");
		System.out.println(" -info                 liefert Informationen zum Eingangspaket (optional)");
		System.out.println("                       Installiert den gesamten Abhaengigkeitsbaum der Pakete");
		System.out.println("                       disposition, afis, tankterminalautomation, ohne die ");
		System.out.println("                       SQL-Scripte auszuführen oder die Datenbank upzudaten");
		/*
		 * System.out.println( " -installservice (is)  Installiert den einen ausgewählten Dienst/Service" ); System.out.println(
		 * "                 (ias) Installiert alle benoetigten Dienste/Service" ); System.out.println(
		 * "                       , die in der Setup.xml des Kunden angegeben sind!" ); System.out.println(" -installwithoutsql (iaws) ");
		 */
		System.out.println(" -execjava (ej)        Führt die in der Setup.xml angegebene Klassen mit");
		System.out.println("                       Parametern aus: ch.minova.test.setup.Setup(a,b)");
		System.out.println(" -installall (ia)      Installiert den gesamten Abhaengigkeitsbaum der Pakete");
		System.out.println("                       disposition, afis, tankterminalautomation");
		System.out.println(" -installservice (is)  Installiert einen Dienst der angegeben wird");
		System.out.println("                       es werden alle Dienste der Setup.xml des aufzurufenden");
		System.out.println("                       es werden alle Prozedurenausgeführt");
		System.out.println("                       Modules installiert. WICHTIG ist die Angabe der <app>");
		System.out.println("                       Zusaetzlich kann die gewueschte Arch. angegeben werden");
		System.out.println("                       z.B.: -arch=32 ");
		System.out.println(" -installservicelite (isl)  Installiert einen Dienst der angegeben wird");
		System.out.println("                       es werden alle Dienste der Setup.xml des aufzurufenden");
		System.out.println("                       es werden kein Prozeduren durchgeführt");
		System.out.println("                       Modules installiert. WICHTIG ist die Angabe der <app>");
		System.out.println("                       Zusaetzlich kann die gewueschte Arch. angegeben werden");
		System.out.println("                       z.B.: -arch=32 ");
		System.out.println(" -installservicewithoutxbs (iswx)  Installiert einen Dienst der angegeben wird");
		System.out.println("                       es werden alle Dienste der Setup.xml des aufzurufenden");
		System.out.println("                       es wird kein xbs-Code geschreiben");
		System.out.println("                       Modules installiert. WICHTIG ist die Angabe der <app>");
		System.out.println("                       Zusaetzlich kann die gewueschte Arch. angegeben werden");
		System.out.println("                       z.B.: -arch=32 ");
		System.out.println(" -outdir=<dir>         Verzeichnis, in dass der output geschreiben wird");
		System.out.println(" -app=<Application>    Application die Installiert wird");
		System.out.println("                       default: Disposition");
		System.out.println(" -override             aktiviert das Überschreibung von MDI Dateien (optional)");
		System.out.println("                       es werden nur MDI-Eintraege ueberschreiben bei denen ");
		System.out.println("                       das Override-Flag auf 'true' gesetzt ist! ");
		System.out.println(" -c=<Customer>         Kunden-Name der installierten /zu Installierten Anwendung");
		System.out.println(" -p=<SQL-Passwort>     default: Minova Testpasswort 2013");
		System.out.println(" -u=<SQL-Benutzer>     default: \"sa\"");
		System.out.println(" -arch=(32/64)         Die Einagbe von 64 oder 32 bewirkt das Installieren eines");
		System.out.println("                       Dienstes in der Entsprechende 32 oder 64 Bit Version");
		System.out.println(" -cl -language=<DE,..> Nachdem die reports bereits auf dem System installiert");
		System.out.println("                       wurden, kann die XBS dahingehend geändert werden, dass");
		System.out.println("                       der Name der übersetzende DTD Datei geändert wird. ");
		/*
		 * System.out.println( " -readxbsfile          Liest eine angegebene xbs-Datei ein die über" ); System.out.println(
		 * "                       -basedir<dir> und -app<application> übergeben wurde" ); System.out.println(
		 * " -readmdifile          Liest eine angegebene mdi-Datei ein die über" ); System.out.println(
		 * "                       -basedir<dir> und -app<application> uebergeben wurde" ); System.out.println(
		 * " -readxbs              Liest den xbs-Code der Setup.xml Datein rekursiv aus!" ); System.out.println(
		 * " -readmdi              Liest den mdi-Code der Setup.xml Datein rekursiv aus!" );
		 */
		System.out.println(" -writexbs             Hintereinanderausführung von readxbsfile, readxbs.");
		System.out.println("                       -basedir<dir> (muss) und -app<application>  (muss)");
		System.out.println("                       werden verwendet um die xbs-Datei anzugeben");
		System.out.println("                       die ausgelesen werden sollte.");
		System.out.println(" -writemdi             Hintereinanderausführung von readmdifile, readmdi.");
		System.out.println("                       -basedir<dir> (muss) und -app<application> (muss)");
		System.out.println("                       werden verwendet um die mdi-Datei anzugeben");
		System.out.println("                       die ausgelesen werden sollte.");
		System.out.println(" -updateschema (us)    Laesst die Tabellen des aufgerufenen Modules gegen die DB");
		System.out.println("                       laufen, dabei werden die Tabellen auf den aktuellsten ");
		System.out.println("                       Stand gebracht");
		System.out.println(" -updatedatabase (ud)  fuehrt die in der Setup.xml aufgelisteten SQl-Skripte aus");
		/*
		 * System.out.println( " -forcesql (fs)        fuehrt die SQl-Skripte immer aus");
		 */
		System.out.println(" -truckdb (tdb)        mit diesem Parameter wird die Verbuindung zur TruckDB");
		System.out.println("                       ausgewaehlt, falls diese in connection.xbs vorhanden ist");
		System.out.println("                       ausgelesen wird der Eintrag 'truckconnection'");
		System.out.println(" -verbose (v)          mit diesem Parameter lassen sich zusaetzliche ");
		System.out.println("                       Informationen anzeigen.");
	}

	/**
	 * Übergeben wird der String mit dem Pfad der zu der Setup.xml Datei die ausgelesen werden muss. Ausgelesene erforderliche Mudle werden mit einem ".jar"
	 * erweiteret und in einem String, mit Leerzeichen getrennt zurückgegebn.
	 *
	 * @param pathsetup
	 * @return
	 */
	private String setMetaInfClasspath() {
		String metainfclass = "";
		final String jar = ".jar";
		SetupDocument doc = null;
		try {
			try {
				doc = getSetupDocument();
			} catch (final BaseSetupException e) {
				System.out.println(MessageFormat.format("Error BaseSetupException: {0}", e.getMessage()));
				// e.printStackTrace();
			}
			// Auslesend der erforderlichen Module
			if (doc.getSetup().getRequiredModules() != null && !doc.getSetup().getRequiredModules().isNil()) {
				for (int i = 0; i < doc.getSetup().getRequiredModules().getModuleArray().length; i++) {
					metainfclass = metainfclass + " " + doc.getSetup().getRequiredModules().getModuleArray(i).getName() + jar;
				}
				// Auslesend der erforderlichen One-Module
				for (int i = 0; i < doc.getSetup().getRequiredModules().getOneOfArray().length; i++) {
					// Auslesend der erforderlichen Module aus den
					// One-Of-Modulen
					for (int k = 0; k < doc.getSetup().getRequiredModules().getOneOfArray(i).getModuleArray().length; k++) {
						metainfclass = metainfclass + " " + doc.getSetup().getRequiredModules().getOneOfArray(i).getModuleArray(k).getName() + jar;
					}
				}
			}
		} catch (final XmlException e) {
			System.out.println(MessageFormat.format("Error XmlException: {0}", e.getMessage()));
			// e.printStackTrace();
		} catch (final IOException e) {
			System.out.println(MessageFormat.format("Error IOException: {0}", e.getMessage()));
			// e.printStackTrace();
		}
		return metainfclass;
	}

	public static void setHashModules(final Hashtable<String, VersionInfo> hashModules) {
		BaseSetup.hashModules = hashModules;
	}

	public static void setHashModulesActive(final Hashtable<String, Integer> hashModules) {
		BaseSetup.hashModulesActive = hashModules;
	}

	public static void setHashModulesMdi(final Hashtable<String, Integer> hashModulesMdi) {
		BaseSetup.hashModulesMdi = hashModulesMdi;
	}

	public static void setHashModulesXbs(final Hashtable<String, Integer> hashModulesXbs) {
		BaseSetup.hashModulesXbs = hashModulesXbs;
	}

	public static void setParameter(final String key, final String value) {
		setParameter(key, value, false);
	}

	/**
	 * Verarbeitung des Übergebenen strings in einen Parameter
	 *
	 * @param key
	 * @param value
	 * @param overwrite
	 */
	public static void setParameter(String key, final String value, final boolean overwrite) {
		key = key.toLowerCase();
		if (parameter.containsKey(key) && !overwrite) {
			final String oldValue = (String) parameter.get(key);
			final RuntimeException re = new RuntimeException("Duplicate parameter key '" + key + "'.\n" + "Old value '" + oldValue + "'");
			re.fillInStackTrace();
			throw re;
		} else {
			if (value != null) {
				parameter.put(key, value);
			}
		}
	}

	/**
	 * @param moduleName
	 * @param vinfo
	 * @param setup      (wird derzeit nicht verwendet)
	 */
	public static void setValue(final String moduleName, final VersionInfo vinfo, final BaseSetup setup) {
		hashModules.put(moduleName, vinfo);
	}

	public static void setValueActive(final String nameModule, final Integer i) {
		hashModulesActive.put(nameModule, i);
	}

	public static void setXbsName(final String xbsName) {
		BaseSetup.xbsName = xbsName;
	}

	protected VersionInfo versionInfo;
	// private long time;
	private HashMap<String, String> mymap;

	public BaseSetup() {
		initModuleInfo();
	}

	/**
	 * Mapeinträge mit in die Knoten unter den Reiter Map schreiben
	 *
	 * @param node
	 * @param map
	 */
	public void addEntriesToNode(final ch.minova.install.setup.xbs.Node node, final Map map) {
		if (map != null) {
			final Map.Entry[] entries = map.getEntryArray();
			// Entry[] entries = map.getEntryArray();
			if (entries.length > 0) {
				// log(MessageFormat.format("add entries to node: <{0}>",
				// node.getName()));
				for (int i = 0; i < entries.length; i++) {
					if (!entries[i].getKey().endsWith("-")) {
						log(MessageFormat.format(" add Entry <Entry: Key={0} Value={1}> ", entries[i].getKey(), entries[i].getValue()));
						node.addEntry(new ch.minova.install.setup.xbs.Entry(entries[i].getKey(), entries[i].getValue()));
					}
				}
			}
		}
	}

	/**
	 * schreibt Einträge in den RootNode root-Map-Entry
	 *
	 * @param root
	 * @param map
	 */
	private void addEntriesToRoot(final ch.minova.install.setup.xbs.Root root, final Map map) {
		if (map != null) {
			final Map.Entry[] entries = map.getEntryArray();
			if (entries.length > 0) {
				for (int i = 0; i < entries.length; i++) {
					if (entries[i].getKey().equalsIgnoreCase("CustomerID")) {
						setParameter("c", entries[i].getValue());
					}
					if (entries[i].getKey().equalsIgnoreCase("locale")) {
						setParameter("language", entries[i].getValue());
						this.Language = entries[i].getValue();
						log(MessageFormat.format("add Entry <Entry: Key={0} Value={1}> ", entries[i].getKey(), entries[i].getValue()));
					}
					if (!entries[i].getKey().endsWith("-")) {
						log(MessageFormat.format("add Entry <Entry: Key={0} Value={1}> ", entries[i].getKey(), entries[i].getValue()));
						root.addEntry(new ch.minova.install.setup.xbs.Entry(entries[i].getKey(), entries[i].getValue()));
					}
				}
			}
		}
	}

	/**
	 * Node eintragen in das Object rootNode, rekursiv wird dann der Reihe nach
	 *
	 * @param xbsnode
	 * @param node
	 */
	public void addNodeToNode(final ch.minova.install.setup.xbs.Node xbsnode, final Node[] node) {
		if (node.length > 0) {
			for (int i = 0; i < node.length; i++) {
				// hinzufuegen eines neuen Nodes zu der rootNode-Struktur
				if (node[i].getName() != null) {
					if (!node[i].getName().endsWith("-")) {
						log(MessageFormat.format("add node to node: <node ={0}>", node[i].getName()));
						xbsnode.addNode(new ch.minova.install.setup.xbs.Node(node[i].getName()));
						// hinzufuegen der Map eintraege in der zuletzt
						// hinzugefuegten
						// Node-Struktur
						addEntriesToNode(xbsnode.getNode(node[i].getName(), false), node[i].getMap());
						// Erstellen eines referenz Objektes zu der zuletzt
						// hinzugefuegten Node-Struktur
						final ch.minova.install.setup.xbs.Node refnode = xbsnode.getNode(node[i].getName(), false);
						// rekursiver Aufruf mit dem Referenzobjekt, an welches
						// neue
						// Knoten angefuegt werden, und dem Array vom Array
						addNodeToNode(refnode, node[i].getNodeArray());
					}
				}
			}
		}
	}

	/**
	 * Fügt einen neuen Knoten zu der Xbs-KnotenStruktur hinzu. Speziell wir ein Knoten zu dem Wurzelelement hinzugefügt.
	 *
	 * @param root
	 * @param node
	 */
	private void addNodeToRoot(final ch.minova.install.setup.xbs.Root root, final Node[] node) {
		if (node.length > 0) {
			for (int i = 0; i < node.length; i++) {
				if (!node[i].getName().endsWith("-")) {
					log(MessageFormat.format("add node to root: <node ={0}>", node[i].getName()));
					// hinzufuegen eines neuen Nodes zu der rootNode-Struktur
					root.addNode(new ch.minova.install.setup.xbs.Node(node[i].getName()));
					// hinzufuegen der Map eintraege in der zuletzt
					// hinzugefuegten
					// Node-Struktur
					addEntriesToNode(root.getNode(node[i].getName(), false), node[i].getMap());
					// Erstellen eines referenz Objektes zu der zuletzt
					// hinzugefuegten Node-Struktur
					final ch.minova.install.setup.xbs.Node refnode = root.getNode(node[i].getName(), false);
					// rekursiver Aufruf mit dem Referenzobjekt, an welches neue
					// Knoten angefuegt werden, und dem Array vom Array
					addNodeToNode(refnode, node[i].getNodeArray());
				}
			}
		}
	}

	public boolean writemodulesxml() throws ModuleNotFoundException, BaseSetupException, VersionInfoException, URISyntaxException, CircleFoundException,
			XmlException, IOException, IncompatibleVersionException {
		if (hashModules.containsKey(this.versionInfo.getModulName())) {
			return true;
		}
		try {
			if (getSetupDocument().getSetup().getRequiredModules() != null) {
				final SetupDocument doc = getSetupDocument();
				final Module[] module = doc.getSetup().getRequiredModules().getModuleArray();
				if (module.length > 0) {
					handleModules(module);
				}
				boolean bool_handleOneOf = false;
				Module[] oneofmodules = null;
				final OneOf[] oneof_module = doc.getSetup().getRequiredModules().getOneOfArray();
				if (oneof_module.length > 0) {
					bool_handleOneOf = false;
					for (int l = 0; l < oneof_module.length; l++) {
						oneofmodules = oneof_module[l].getModuleArray();
						System.out.println(MessageFormat.format("\t-- OneOfModule:{0}", l));
						// Abfangen der Exception aus handleOneofModules
						// weitergeben von einem OneOfModule
						// handleOneOfModule
						try {
							bool_handleOneOf = handleOneOfModule(oneofmodules);
						} catch (final ModuleNotFoundException e) {
							// One-Of Module nicht gefunden
							// System.out.println(MessageFormat.format("MissingModuleException von compareRequirementsEquals: {0}",
							// e.getMessage()));
						}
						if (!bool_handleOneOf) {
							final String Modulename = getSetupDocument().getSetup().getName();
							throw new BaseSetupException(MessageFormat.format(
									"In dem Modul: " + Modulename.toUpperCase() + ", wird folgender Fehler geworfen: \n" + MODULEONEOFNOTFOUND,
									oneofmodules[l].getName()));
						}
					}
				}
			}
		} catch (final FileNotFoundException e) {
			throw new BaseSetupException(MessageFormat.format(e.toString(), e.getMessage()));
		} catch (final ModuleNotFoundException e) {
			final String Modulename = getSetupDocument().getSetup().getName();
			throw new ModuleNotFoundException("\nIn dem Modul \"" + Modulename + "\" wird folgende Exception geworfen: \n" + e.getMessage());
		} catch (final XmlException e) {
			throw new BaseSetupException(MessageFormat.format(e.toString(), e.getMessage()));
		} catch (final IOException e) {
			throw new BaseSetupException(MessageFormat.format(e.toString(), e.getMessage()));
		} catch (final NullPointerException e) {
			log(MessageFormat.format("Error NullPointerException: {0}", e.getMessage()));
		}
		if (!orderedDependingModules.contains(this)) {
			orderedDependingModules.add(this);
			log(MessageFormat.format("Module: {0},  version: {1}", new Object[] { getVersionInfo(this).getModulName(), getVersionInfo(this).toString() }),
					true);
		}
		return true;
	}

	/**
	 * Auslesen der erforderlichen Module innerhalb der Setup.xml Datei des Moduls. Diese Methode wird rekursiv aufgerufen, für jedes Modul aus der Liste von
	 * erforderlichen Modulen, wird diese Methode aufgerufen um die Abhängigkeiten zu überprüfen.
	 *
	 * @return
	 * @throws ModuleNotFoundException
	 * @throws BaseSetupException
	 * @throws VersionInfoException
	 * @throws URISyntaxException
	 * @throws CircleFoundException
	 * @throws IOException
	 * @throws XmlException
	 */
	public boolean checkModules() throws ModuleNotFoundException, BaseSetupException, VersionInfoException, URISyntaxException, CircleFoundException,
			XmlException, IOException, IncompatibleVersionException {
		if (hashModules.containsKey(this.versionInfo.getModulName())) {
			return true;
		}
		try {
			if (getSetupDocument().getSetup().getRequiredModules() != null) {
				final SetupDocument doc = getSetupDocument();
				final Module[] module = doc.getSetup().getRequiredModules().getModuleArray();
				if (module.length > 0) {
					handleModules(module);
				}
				boolean bool_handleOneOf = false;
				Module[] oneofmodules = null;
				final OneOf[] oneof_module = doc.getSetup().getRequiredModules().getOneOfArray();
				if (oneof_module.length > 0) {
					bool_handleOneOf = false;
					for (int l = 0; l < oneof_module.length; l++) {
						oneofmodules = oneof_module[l].getModuleArray();
						System.out.println(MessageFormat.format("\t-- OneOfModule:{0}", l));
						// Abfangen der Exception aus handleOneofModules
						// weitergeben von einem OneOfModule
						// handleOneOfModule
						try {
							bool_handleOneOf = handleOneOfModule(oneofmodules);
						} catch (final ModuleNotFoundException e) {
							// One-Of Module nicht gefunden

							// System.out.println(MessageFormat.format("MissingModuleException von compareRequirementsEquals: {0}",
							// e.getMessage()));
						}
						if (!bool_handleOneOf) {
							final String Modulename = getSetupDocument().getSetup().getName();
							throw new BaseSetupException(MessageFormat.format("In dem Modul: " + Modulename.toUpperCase()
									+ ", wird folgender Fehler geworfen: \n" + MODULEONEOFNOTFOUND + "benötigt wird: {0}", oneofmodules[l].getName()));
						}
					}
				}
			}
		} catch (final FileNotFoundException e) {
			throw new BaseSetupException(MessageFormat.format(e.toString(), e.getMessage()));
		} catch (final ModuleNotFoundException e) {
			final String Modulename = getSetupDocument().getSetup().getName();
			throw new ModuleNotFoundException("\nIn dem Modul \"" + Modulename + "\" wird folgende Exception geworfen: \n" + e.getMessage());
		} catch (final XmlException e) {
			throw new BaseSetupException(MessageFormat.format(e.toString(), e.getMessage()));
		} catch (final IOException e) {
			throw new BaseSetupException(MessageFormat.format(e.toString(), e.getMessage()));
		} catch (final NullPointerException e) {
			log(MessageFormat.format("Error NullPointerException: {0}", e.getMessage()));
			// e.printStackTrace();
		}
		if (!orderedlocalModules.contains(this)) {

			orderedlocalModules.add(this);
		}
		if (!orderedDependingModules.contains(this)) {

			orderedDependingModules.add(this);
			log(MessageFormat.format("Module: {0},  version: {1}", new Object[] { getVersionInfo(this).getModulName(), getVersionInfo(this).toString() }),
					true);
		}
		return true;
	}

	/**
	 * Auslesen der erforderlichen Module für die Services innerhalb der Setup.xml Datei des Moduls. Diese Methode wird rekursiv aufgerufen, für jedes Modul aus
	 * der Liste von erforderlichen Modulen, wird diese Methode aufgerufen um die Abhängigkeiten zu überprüfen.
	 *
	 * @return
	 * @throws ModuleNotFoundException
	 * @throws BaseSetupException
	 * @throws VersionInfoException
	 * @throws URISyntaxException
	 * @throws CircleFoundException
	 * @throws IOException
	 * @throws XmlException
	 */
	public boolean checkServices(final String servicename)
			throws ModuleNotFoundException, BaseSetupException, VersionInfoException, URISyntaxException, CircleFoundException, XmlException, IOException {
		// Überprüfung ob dieses Module bereits in HashModules steht.
		// if (hashModules.containsKey(this.versionInfo.getModulName())) {
		// return true;
		// }
		orderedlocalModules.clear();
		try {
			if (servicename != null) {
				final SetupDocument doc = getSetupDocument();
				final Module[] smodule = doc.getSetup().getRequiredModules().getModuleArray();
				if (smodule.length > 0) {
					try {
						handleModules(smodule);
					} catch (IncompatibleVersionException e) {
						throw new RuntimeException(e);
					}
				}
				/*
				 * boolean bool_handleOneOf = false; Module[] oneofmodules = null; OneOf[] oneof_module = doc.getSetup().getRequiredModules().getOneOfArray();
				 * if (oneof_module.length > 0) { bool_handleOneOf = false; for (int l = 0; l < oneof_module.length; l++) { oneofmodules =
				 * oneof_module[l].getModuleArray(); System.out.println(MessageFormat .format("\t-- OneOfModule:{0}", l)); // Abfangen der Exception aus
				 * handleOneofModules // weitergeben von einem OneOfModule // handleOneOfModule try { bool_handleOneOf = handleOneOfModule(oneofmodules); }
				 * catch (ModuleNotFoundException e) { // One-Of Module nicht gefunden // System.out.println(MessageFormat.format(
				 * "MissingModuleException von compareRequirementsEquals: {0}", // e.getMessage())); } if (!bool_handleOneOf) { String Modulename =
				 * getSetupDocument().getSetup().getName(); throw new BaseSetupException(MessageFormat.format("In dem Modul: " + Modulename.toUpperCase() +
				 * ", wird folgender Fehler geworfen: \n" + MODULEONEOFNOTFOUND, oneofmodules[l].getName())); } } }
				 */
			}
		} catch (final FileNotFoundException e) {
			throw new BaseSetupException(MessageFormat.format(e.toString(), e.getMessage()));
		} catch (final ModuleNotFoundException e) {
			final String Modulename = getSetupDocument().getSetup().getName();
			throw new ModuleNotFoundException("\nIn dem Modul \"" + Modulename + "\" wird folgende Exception geworfen: \n" + e.getMessage());
		} catch (final XmlException e) {
			throw new BaseSetupException(MessageFormat.format(e.toString(), e.getMessage()));
		} catch (final IOException e) {
			throw new BaseSetupException(MessageFormat.format(e.toString(), e.getMessage()));
		} catch (final NullPointerException e) {
			log(MessageFormat.format("Error NullPointerException: {0}", e.getMessage()));
			// e.printStackTrace();
		}
		if (!orderedlocalModules.contains(this)) {
			orderedlocalModules.add(this);
		}

		/*
		 * if (!orderedDependingModules.contains(this)) { orderedDependingModules.add(this); log(MessageFormat.format("Module: {0},  version: {1}", new Object[]
		 * { getVersionInfo(this).getModulName(), getVersionInfo(this).toString() }), true); }
		 */
		return true;
	}

	/*
	 * Diese Methde übernimmt das kopieren des Jarfiles in einnen angegebenen Ordern
	 */
	public boolean copyJar(final String srcfile, String destfile, final String FileName) {
		// private static void copyfile(String srcfile, String destfile) {
		if (destfile.contains("${service}")) {
			if (parameter.getProperty("service") != null) {
				destfile = destfile.replace("${service}", parameter.getProperty("service"));
			} else {
				return false;
			}
		}

		File dirprogramfile;
		if (parameter.containsKey("basedir")) {
			dirprogramfile = new File(parameter.getProperty("basedir") + "Program Files");
		} else {
			dirprogramfile = new File("../Program Files/");
		}
		if (!dirprogramfile.exists()) {
			dirprogramfile.mkdirs();
		} else {
			log(MessageFormat.format("Directory besteht: {0}", dirprogramfile.getAbsolutePath()));
		}

		/*
		 * jetzt befindet man sich in ../Program Files/
		 */
		// String filename = srcfile;
		File srcFile = new File("../" + srcfile);
		log("Pfad zu der Jardatei: " + srcFile.getAbsolutePath());
		destfile = destfile.replace("${application}", getAppNameShort());
		destfile = replacestring(destfile);
		File destFile = new File(dirprogramfile + "/" + destfile);
		if (!destFile.exists()) {
			destFile.mkdirs();
		} else {
			log(MessageFormat.format("Directory besteht: {0}", destFile.getAbsolutePath()));
		}
		if (!srcFile.exists()) {
			// srcFile = new
			// File(Setup.class.getProtectionDomain().getCodeSource().getLocation().getPath()
			// + "/../" + FileName);
			srcFile = new File(new File(".").getAbsolutePath() + "/" + FileName);

			if (!srcFile.exists()) {
				log(MessageFormat.format("File gibt es nicht.: {0}", srcFile.getAbsolutePath()), true);
			}
		}
		destFile = new File(dirprogramfile + "/" + destfile + "/" + FileName);
		try {
			FileUtils.copyFile(srcFile, destFile);
			log(MessageFormat.format("srcfile: {0} to {1}", srcFile.getAbsoluteFile(), destFile.getAbsolutePath()), true);
		} catch (final IOException e) {
			log(MessageFormat.format("Error IOException: {0}", e.getMessage()));
			// e.printStackTrace();
		}
		return false;
	}

	/**
	 * kopiert die in der Jar_Datei liegenden *.xml und *.xbs Dateien in den Ordner Program Files. Program Files liegt auf der gleichen Ebene wie das deploy
	 * Verzeichnis in dem die Jar_Datei liegt. Falls Das basedir verzeichnis wird ausgelesen und kann mit den Übergabeparametern in der Konsole gesetzt werden.
	 * java -jar ch.minova.xxxx.jar -copyfile -basedir=../
	 *
	 * @param setup
	 * @param cf
	 * @return
	 * @throws BaseSetupException
	 */
	public boolean copyFiles(final BaseSetup setup, final ch.minova.install.setup.copyfile.CopyFile cf) throws BaseSetupException {
		String path = getModuleName(setup.getClass());

		log(MessageFormat.format("Anzahl der Verzeichnisse die kopiert werden sollen: {0}", cf.getDirCopyCount()));
		log(MessageFormat.format("Anzahl der Dateien die kopiert werden sollen: {0}", cf.getFileCopyCount()));
		final String jarpath = path + ".jar";
		log(MessageFormat.format("Pfad zur Jar_Datei: {0}", jarpath));
		path = path.replace(".", "/") + "/";
		log(MessageFormat.format("Pfad zur den Xml-Datein: {0}", path));

		JarFile jFile = null;
		JarEntry jEntry = null;
		final byte[] buffer = new byte[16384];
		int len;
		File dirprogramfile;
		if (parameter.containsKey("basedir")) {
			dirprogramfile = new File(parameter.getProperty("basedir") + "Program Files");
		} else {
			dirprogramfile = new File("../Program Files");
		}
		if (!dirprogramfile.exists()) {
			dirprogramfile.mkdirs();
		} else {
			log(MessageFormat.format("Directory besteht: {0}", dirprogramfile.getAbsolutePath()));
		}
		try {
			log(MessageFormat.format("Output Dir: {0}", dirprogramfile.getAbsolutePath()), true);
			jFile = new JarFile(jarpath);
			String dataName = "";
			final Enumeration<JarEntry> jEntries = jFile.entries();
			// Ausgabeverzeichnis
			log(MessageFormat.format("+-- {0}", dirprogramfile.getAbsolutePath()), true);
			String directory = null;
			while (jEntries.hasMoreElements()) {
				jEntry = jEntries.nextElement();
				dataName = jEntry.getName().replace(path, "");
				// hier muss der slash eingefügt werden, weil die datein mit "/"
				// gespeichert werden z.B.: "/tversion.class"
				dataName = "/" + dataName.toLowerCase();
				log(MessageFormat.format("Datei die kopiert wird {0}", dataName));
				// schreibe file in Direktory

				if (cf.existsFileCopy(dataName)) {
					directory = cf.getFileCopy(dataName, "", "", false).getTodir();
					directory = directory.replace("${application}", getAppNameShort());
					directory = directory.replace("${service}", getAppNameShort());
					directory = replacestring(directory);
					log(MessageFormat.format("Verzeichnis: {0}", directory));
					if (directory == null) {
						return true;
					}

					if (!jEntry.isDirectory()) {
						// Erstellen der Datei

						final File dir = new File(dirprogramfile.getAbsolutePath() + "/" + directory);
						log(MessageFormat.format("File dir: {0}", dir.getAbsolutePath()));
						if (!dir.getParentFile().exists()) {
							dir.getParentFile().mkdirs();
						} else {
							log(MessageFormat.format("Parent Directory besteht: {0}", dir.getParentFile().getAbsolutePath()));
						}
						if (!dir.exists()) {
							dir.mkdirs();
						} else {
							log(MessageFormat.format("Directory besteht: {0}", dir.getAbsolutePath()));
						}
						log(MessageFormat.format("|-- {0}", dir.getAbsolutePath() + "/" + cf.getFileCopy(dataName, "", "", false).getFilename()), true);
						log(MessageFormat.format("|-quelle- {0}", jEntry.getName()));
						final BufferedOutputStream bos = new BufferedOutputStream(
								new FileOutputStream(new File(dir.getPath(), cf.getFileCopy(dataName, "", "", false).getFilename())));
						// Datei aus dem JEntry nehmen
						final BufferedInputStream bis = new BufferedInputStream(jFile.getInputStream(jEntry));
						// einlesen der Datei
						while ((len = bis.read(buffer)) > 0) {
							bos.write(buffer, 0, len);
						}
						bos.flush();
						bos.close();
						bis.close();
					}
				}
				dataName = jEntry.getName().replace(path, "");
				log(MessageFormat.format("Verzeichnis in das kopiert wird {0}", dataName));
				if (cf.existsDirCopy(dataName)) {
					final Enumeration<JarEntry> jEntries_1 = jFile.entries();
					JarEntry jEntry_1 = null;
					directory = cf.getDirCopy(dataName, "", false).getTodir();
					directory = directory.replace("${application}", getAppNameShort());
					directory = replacestring(directory);
					while (jEntries_1.hasMoreElements()) {
						jEntry_1 = jEntries_1.nextElement();
						if (jEntry_1.getName().startsWith(path + dataName)) {
							if (!jEntry_1.isDirectory()) {
								final File dir = new File(dirprogramfile.getAbsolutePath() + "/"
										+ jEntry_1.getName().replace(path + cf.getDirCopy(dataName, "", false).getFromdir(), directory + "/"));
								if (!dir.getParentFile().exists()) {
									dir.getParentFile().mkdirs();
								} else {
									log(MessageFormat.format("Parent Directory besteht: {0}", dir.getParentFile().getPath()));
								}
								log(MessageFormat.format("|-- {0}", dir.getAbsolutePath()), true);
								log(MessageFormat.format("|-quelle- {0}", jEntry_1.getName()));
								final BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(dir.getPath())));
								// Datei aus dem JEntry nehmen
								final BufferedInputStream bis = new BufferedInputStream(jFile.getInputStream(jEntry_1));
								// einlesen der Datei
								while ((len = bis.read(buffer)) > 0) {
									bos.write(buffer, 0, len);
								}
								bos.flush();
								bos.close();
								bis.close();
							}
						}
					}
					log("keine Elemente die zu dem Namen passt gefunden");
				}
			}
		} catch (final IOException e) {
			log(MessageFormat.format("Error IOException: {0}", e.getMessage()));
			// e.printStackTrace();
		}

		return true;
	}

	/**
	 * liest die orderedDependingModules liste aus und führt für jedes modul die Setup.xml aus bzw liest den Mdi-Code
	 *
	 * @throws XmlException
	 * @throws IOException
	 * @throws BaseSetupException
	 */
	public void readCopyFileOfOrderedModules(final BaseSetup[] bsetup) throws XmlException, IOException, BaseSetupException {
		SetupDocument setup = null;
		BaseSetup[] a = null;
		final Connection conb = checkConnection(null);
		if (bsetup != null) {
			a = bsetup;
		} else {
			a = orderedDependingModules.toArray(new BaseSetup[0]);
		}
		for (final BaseSetup bs : a) {
			try {
				log(MessageFormat.format("Datei die ausgelesen wird: {0}", bs.getVersionInfo().getModulName() + "  -  setup.xml"));
				setup = bs.getSetupDocument();
				if (setup.getSetup().getCopyFile() != null) {
					final ch.minova.install.setup.copyfile.CopyFile cf = new ch.minova.install.setup.copyfile.CopyFile();
					log(MessageFormat.format("Auslesen der Dateien", ""));

					try {
						for (final Filecopy fc : setup.getSetup().getCopyFile().getFilecopyArray()) {
							FileCopy n = null;
							// Für den fall, dass das einzelne Module kopiert
							// werden soll.
							if (fc.getFilename().equalsIgnoreCase("this")) {
								// Wenn angegeben wird, dass es keine
								// Datenbankverbindung gibt, werden keine Masken
								// in die TVersion10 eingetragen andernfalls
								// schon
								if (parameter.getProperty("nodb") == null) {
									try {
										bs.executeAddData("", fc.getFilename(), true, conb);
									} catch (final SQLException e) {
										log(MessageFormat.format("zu kopierende Datei konnte nicht in tVersion10 eingetragen werden: {0}",
												bs.getVersionInfo().getModulName()));
									}
								}
								if (fc.getFromdir() == null) {
									if (fc.getTodir().contains("${service}")) {
										if (parameter.getProperty("service") != null) {
											copyJar("/" + setup.getSetup().getName() + ".jar", fc.getTodir() + "/", setup.getSetup().getName() + ".jar");
											continue;
										}
									}
									if (fc.getTodir().contains("${application}")) {
										if (parameter.getProperty("app") != null) {
											copyJar("/" + setup.getSetup().getName() + ".jar", fc.getTodir() + "/", setup.getSetup().getName() + ".jar");
											continue;
										}
									} else {
										if (parameter.getProperty("app") != null) {
											copyJar("/" + setup.getSetup().getName() + ".jar", fc.getTodir() + "/", setup.getSetup().getName() + ".jar");
											continue;
										}
									}
								} else {
									if (parameter.getProperty("service") != null) {
										copyJar(fc.getFromdir() + "/" + setup.getSetup().getName() + ".jar", fc.getTodir() + "/",
												setup.getSetup().getName() + ".jar");
										continue;
									}
								}
							} else {
								// für den Fall, dass es spezielle Dateien aus
								// dem Jarfile sind

								// Wenn angegeben wird, dass es keine
								// Datenbankverbindung gibt, werden keine Masken
								// in die TVersion10 eingetragen andernfalls
								// schon
								if (parameter.getProperty("nodb") == null) {
									try {
										bs.executeAddData("", fc.getFilename(), true, conb);
									} catch (final SQLException e) {
										log(MessageFormat.format("zu kopierende Datei konnte nicht in tVersion10 eingetragen werden: {0}",
												bs.getVersionInfo().getModulName()), true);
									}
								}
								n = cf.getFileCopy(fc.getFilename(), fc.getTodir(), fc.getFromdir(), true);
							}
							n = cf.getFileCopy(fc.getFilename(), fc.getTodir(), fc.getFromdir(), true);
							log(MessageFormat.format("copy file {0} {1} -> {2}", n.getFromdir(), n.getFilename(), n.getTodir()));
						}

						log(MessageFormat.format("Anzahl der Dateien die kopiert werden sollen: {0}", cf.getFileCopyCount()));
						log(MessageFormat.format("Auslesen der Verzeichnisse", " "));
						for (final Dircopy dc : setup.getSetup().getCopyFile().getDircopyArray()) {
							if (dc.getTodir().contains("${service}") || dc.getFromdir().contains("${service}")) {
								if (parameter.getProperty("service") != null) {
									final DirCopy d = cf.getDirCopy(dc.getFromdir(), dc.getTodir(), true);
									log(MessageFormat.format("copy file {0} -> {1}", d.getFromdir(), d.getTodir()));
								}
							} else {
								final DirCopy d = cf.getDirCopy(dc.getFromdir(), dc.getTodir(), true);
								log(MessageFormat.format("copy file {0} -> {1}", d.getFromdir(), d.getTodir()));
							}
						}
						log(MessageFormat.format("Anzahl der Verzeichnisse die kopiert werden sollen: {0}", cf.getDirCopyCount()));
					} catch (final Exception e) {
						log(MessageFormat.format("Anzahl der Dateien die kopiert werden sollen: {0}", cf.getFileCopyCount()));
						log(MessageFormat.format("Anzahl der Verzeichnisse die kopiert werden sollen: {0}", cf.getDirCopyCount()));
						log(MessageFormat.format("Exception: {0}", e.getMessage()), true);
						throw new BaseSetupException(e.getMessage() + setup.getClass().getName());
					}
					if (cf.getDirCopyCount() != 0 || cf.getFileCopyCount() != 0) {
						bs.copyFiles(bs, cf);
					}
				} else {
					log(MessageFormat.format("Keine Dateien zum Kopieren angegeben", ""), true);
				}
			} catch (final BaseSetupException e) {
				throw new BaseSetupException(e.getMessage() + setup.getClass().getName());
			}
		}
	}

	/**
	 * liest die orderedDependingModules liste aus und führt für jedes modul die Setup.xml aus bzw liest den Mdi-Code
	 *
	 * @throws XmlException
	 * @throws IOException
	 * @throws BaseSetupException
	 */
	@SuppressWarnings("static-access")
	public void readCopyFileOfOrderedModules() throws XmlException, IOException, BaseSetupException {
		SetupDocument setup = null;
		final BaseSetup[] a = orderedDependingModules.toArray(new BaseSetup[0]);

		for (final BaseSetup bs : a) {
			try {
				log(MessageFormat.format("Datei die ausgelesen wird: {0}", bs.getVersionInfo().getModulName() + "  -  setup.xml"));
				setup = bs.getSetupDocument();
				if (setup.getSetup().getCopyFile() != null) {
					final ch.minova.install.setup.copyfile.CopyFile cf = new ch.minova.install.setup.copyfile.CopyFile();
					log(MessageFormat.format("Auslesen der Dateien", ""));

					try {
						for (final Filecopy fc : setup.getSetup().getCopyFile().getFilecopyArray()) {
							FileCopy n = null;
							// Für den fall, dass das einzelne Module kopiert
							// werden soll.
							if (fc.getFilename().equalsIgnoreCase("this")) {
								if (fc.getFromdir() == null) {
									if (fc.getTodir().contains("${service}")) {
										if (parameter.getProperty("service") != null) {
											copyJar("/" + setup.getSetup().getName() + ".jar", fc.getTodir() + "/", setup.getSetup().getName() + ".jar");
											continue;
										}
									}
									if (fc.getTodir().contains("${application}")) {
										if (parameter.getProperty("app") != null) {
											copyJar("/" + setup.getSetup().getName() + ".jar", fc.getTodir() + "/", setup.getSetup().getName() + ".jar");
											continue;
										}
									}
								}
							}

							if (fc.getFromdir() == null) {
								copyJar("/" + setup.getSetup().getName() + ".jar", fc.getTodir() + "/", setup.getSetup().getName() + ".jar");
							} else {
								copyJar(fc.getFromdir() + "/" + bs.getModuleName(this.getClass()) + ".jar", fc.getTodir() + "/",
										bs.getModuleName(this.getClass()) + ".jar");
							}
							// für den Fall, dass es spezielle Dateien aus
							// dem Jarfile sind
							n = cf.getFileCopy(fc.getFilename(), fc.getTodir(), fc.getFromdir(), true);
							log(MessageFormat.format("copy file {0} {1} -> {2}", n.getFromdir(), n.getFilename(), n.getTodir()));
							continue;
						}

						log(MessageFormat.format("Anzahl der Dateien die kopiert werden sollen: {0}", cf.getFileCopyCount()));
						log(MessageFormat.format("Auslesen der Verzeichnisse", " "));
						for (final Dircopy dc : setup.getSetup().getCopyFile().getDircopyArray()) {
							if (dc.getTodir().contains("${service}") || dc.getFromdir().contains("${service}")) {
								if (parameter.getProperty("service") != null) {
									final DirCopy d = cf.getDirCopy(dc.getFromdir(), dc.getTodir(), true);
									log(MessageFormat.format("copy file {0} -> {1}", d.getFromdir(), d.getTodir()));
								}
							} else {
								final DirCopy d = cf.getDirCopy(dc.getFromdir(), dc.getTodir(), true);
								log(MessageFormat.format("copy file {0} -> {1}", d.getFromdir(), d.getTodir()));
							}
						}
						log(MessageFormat.format("Anzahl der Verzeichnisse die kopiert werden sollen: {0}", cf.getDirCopyCount()));
					} catch (final Exception e) {
						log(MessageFormat.format("Anzahl der Dateien die kopiert werden sollen: {0}", cf.getFileCopyCount()));
						log(MessageFormat.format("Anzahl der Verzeichnisse die kopiert werden sollen: {0}", cf.getDirCopyCount()));
						log(MessageFormat.format("Exception: {0}", e.getMessage()), true);
						throw new BaseSetupException(e.getMessage() + setup.getClass().getName());
					}
					if (cf.getDirCopyCount() != 0 || cf.getFileCopyCount() != 0) {
						bs.copyFiles(bs, cf);
					}
				} else {
					log(MessageFormat.format("Keine Dateien zum Kopieren angegeben", ""), true);
				}
			} catch (final BaseSetupException e) {
				throw new BaseSetupException(e.getMessage() + setup.getClass().getName());
			}
		}
	}

	/**
	 * Ausführen und Einlesen der Sql-Scripts, dabei werden zwischen 4 unterschiedlichen Typen unterschieden. View, Script, Procedure und Function
	 *
	 * @param con
	 * @throws XmlException
	 * @throws IOException
	 * @throws BaseSetupException
	 * @throws SQLException
	 * @throws SQLExeption
	 */
	private void handleSqlScripts(final Connection con) throws XmlException, IOException, BaseSetupException, SQLException, SQLExeption {
		SetupDocument doc;
		final String table = "tVersion10";
		final boolean forceSql = parameter.containsKey("fs");
		doc = getSetupDocument();

		final Connection connection = con;
		log(MessageFormat.format("Module wird bearbeitet: {0}", this.getVersionInfo().getModulName() + this.getVersionInfo().toString()), true);
		Hashtable<String, TVersion> tVersionHash = new Hashtable<String, TVersion>();
		tVersionHash = getTVersion(connection, table);
		checktVersion10(con, Optional.empty());
		tVersionHash = getTVersion(connection, table);
		// Einlesen der Daten aus tVersion
		if (doc.getSetup().getSqlCode() != null) {
			final Script scripts[] = doc.getSetup().getSqlCode().getScriptArray();
			for (int i = 0; i < scripts.length; i++) {
				final Script scp = scripts[i];
				final String name = scp.getName();
				final Enum type = scp.getType();
				log(MessageFormat.format("Script: {0}, Type= {1}", name, type.toString()));
				// connection = checkConnection(null);
				// falls es Versions gibt wird überprüft!
				final String sqlScript = readSqlFromJarFileToString(getVersionInfo().getModulName(), sqldialect, name + ".sql");
				log(sqlScript, false);
				if (type == Type.SCRIPT) {
					executeSqlScript(sqlScript);
				} else if (type == Type.TABLE) {
					readSQLOfOrderedModules();
				} else if (type == Type.PROCEDURE || type == Type.VIEW || type == Type.FUNCTION) {
					if (!tVersionHash.containsKey(name)) {
						// Prozedur /View nicht in tVersion10 vorhanden
						runScript(sqlScript, name, this.versionInfo, type, con);
						log(MessageFormat.format("{0} wurde ausgeführt :-)", name), true);
					} else if (compareToTVersion(tVersionHash.get(scp.getName()))) {
						// Prozedur/View nicht in der korrekten Version vorhanden
						runScript(sqlScript, name, this.versionInfo, type, con);
						log(MessageFormat.format("{0} wurde ausgeführt ;-)", name), true);
					} else if (forceSql) {
						runScript(sqlScript, name, this.versionInfo, type, con);
						log(MessageFormat.format("{0} wurde erneut ausgeführt :-D", name), true);
					} else {
						log(MessageFormat.format("{0} ist aktuell und wurde nicht ausgeführt :-(, -fs?", name), true);
					}
				} else {
					throw new BaseSetupException(MessageFormat.format("Unknown SQL script type {0}", type.toString()));
				}
			}
		}
	}

	/**
	 * @param sqlScript
	 * @param name
	 * @param versionInfo2 (wird derzeit nicht verwendet)
	 * @param type
	 * @param con
	 * @throws SQLException
	 */
	private void runScript(final String sqlScript, final String name, final VersionInfo versionInfo2, final Enum type, final Connection con)
			throws SQLException {
		if (type == Type.FUNCTION) {
			executeSqlFunction(sqlScript, name, true, con);
		} else if (type == Type.PROCEDURE) {
			executeSqlProcedure(sqlScript, name, true, con);
		} else if (type == Type.VIEW) {
			executeSqlView(sqlScript, name, true, con);
		}
	}

	// hier wirde die aktuelle Datenbank (Tabelle TVersion10 ) ausgelesen!
	private Hashtable<String, TVersion> getTVersion(final Connection connection, final String table) {
		ResultSet results = null;
		final Hashtable<String, TVersion> tVersionHash = new Hashtable<String, TVersion>();
		try {
			results = connection.createStatement().executeQuery("select * from " + table);
			while (results.next()) {
				tVersionHash.put(results.getString("Keytext"),
						new TVersion(results.getInt("KeyLong"), results.getString("Keytext"), results.getString("ModuleName"), results.getInt("MajorVersion"),
								results.getInt("MinorVersion"), results.getInt("PatchLevel"), results.getInt("BuildNumber"), results.getString("LastUser"),
								results.getString("LastDate"), results.getInt("LastAction")));
			}
		} catch (final SQLException e) {
			// TODO Tabelle tVersion10 muss wenn nicht vorhanden noch erstellt werden
			log(MessageFormat.format("Tabelle {0} nicht vorhanden", table), true);
			return null;
		}
		return tVersionHash;
	}

	/**
	 * Überprüfung der Version mit den Verion des aktuellen Projekts return true bedeutet ausführen, false = nicht ausführen
	 *
	 * @param tversion
	 * @return
	 */
	private boolean compareToTVersion(final TVersion tversion) {
		if (tversion.getMajorversion() == getVersionInfo().getMajorVersion()) {
			if (tversion.getMinorversion() >= getVersionInfo().getMinorVersion()) {
				if (tversion.getPatchlevel() >= getVersionInfo().getPatchLevel()) {
					if (tversion.getBuildnumber() >= getVersionInfo().getBuildNumber()) {
						return false;
					}
					return true;
				}
				return true;
			}
			return true;
		}
		return true;
	}

	private void executeAddData(final String sqlScript, final String name, final boolean force, final Connection con) throws SQLException {
		executeSqlMethod(sqlScript, name, "@Dataname", "spMinovaCheckData", force, con);
	}

	private void executeSqlFunction(final String sqlScript, final String name, final boolean force, final Connection con) throws SQLException {
		executeSqlMethod(sqlScript, name, "@FunctionName", "spMinovaCheckFunction", force, con);
	}

	private void executeSqlView(final String sqlScript, final String name, final boolean force, final Connection con) throws SQLException {
		executeSqlMethod(sqlScript, name, "@ViewName", "spMinovaCheckView", force, con);
	}

	private void executeSqlTable(final String sqlScript, final String name) throws SQLException {
		executeSqlTableMethod(sqlScript, "@TableName", "spMinovaCheckTable",
				new VersionInfo(name.substring(name.indexOf(".") + 1, name.length()) + "-0", "", name.substring(0, name.indexOf("."))));
	}

	private void executeSqlProcedure(final String sqlScript, final String name, final boolean force, final Connection con) throws SQLException {
		executeSqlMethod(sqlScript, name, "@ProcedureName", "spMinovaCheckProcedure", force, con);
	}

	private void executeSqlTableMethod(final String sqlScript, final String firstParameterName, final String checkProcedureName, final VersionInfo vi)
			throws SQLException {
		log(sqlScript);
		try {
			connect();
			final CallableStatement pscp = this.connection.prepareCall("{? = call " + checkProcedureName + "(?,?,?,?,?,?)}");
			pscp.registerOutParameter(1, Types.INTEGER);
			pscp.setObject(firstParameterName, vi.getModulName());
			pscp.setObject("@ModuleName", getVersionInfo().getModulName());
			pscp.setObject("@MajorVersion", vi.getMajorVersion());
			pscp.setObject("@MinorVersion", vi.getMinorVersion());
			pscp.setObject("@PatchLevel", vi.getPatchLevel());
			pscp.setObject("@BuildNumber", getVersionInfo().getBuildNumber());
			pscp.executeUpdate();
			log(MessageFormat.format("Buildnumber = {0}", getVersionInfo().getBuildNumber()));
			final int result = pscp.getInt(1); // 1 -> muss ausgeführt werden, 2 ->
			// kann ausgeführt werden
			if (result == 1) {
				log(" script must be executed");
			} else if (result == 2) {
				log(" script can be executed");
			} else {
				log("result != 1 or != 2");
			}
			pscp.close();

			final PreparedStatement ps = this.connection.prepareStatement(sqlScript);
			ps.execute();
			ps.close();
			log(MessageFormat.format("Versionsprüfung für {0} wurde ausgeführt.", checkProcedureName));
		} catch (final SQLException e) {
			throw new SQLException(e.getMessage());
		}
	}

	private void executeSqlMethod(final String sqlScript, final String name, final String firstParameterName, final String checkProcedureName,
			final boolean force, final Connection con) throws SQLException {
		log(sqlScript);
		try {
			this.connection = con;
			// connect();
			if (this.connection != null) {
				CallableStatement pscp = null;
				boolean executeSql;
				if (checkProcedureName.equalsIgnoreCase("spMinovaCheckData")) {
					pscp = this.connection.prepareCall("{? = call " + checkProcedureName + "(?,?,?,?,?,?,?)}");
					pscp.registerOutParameter(1, Types.INTEGER);
					pscp.setObject("@ProcedureName", "DATA");
					pscp.setObject("@ModuleName", getVersionInfo().getModulName());
					pscp.setObject("@MajorVersion", getVersionInfo().getMajorVersion());
					pscp.setObject("@MinorVersion", getVersionInfo().getMinorVersion());
					pscp.setObject("@PatchLevel", getVersionInfo().getPatchLevel());
					log(MessageFormat.format("Buildnumber = {0}", getVersionInfo().getBuildNumber()));
					pscp.setObject("@BuildNumber", getVersionInfo().getBuildNumber());
					pscp.setObject(firstParameterName, name);
				} else {
					pscp = this.connection.prepareCall("{? = call " + checkProcedureName + "(?,?,?,?,?,?)}");
					pscp.registerOutParameter(1, Types.INTEGER);
					pscp.setObject(firstParameterName, name);
					pscp.setObject("@ModuleName", getVersionInfo().getModulName());
					pscp.setObject("@MajorVersion", getVersionInfo().getMajorVersion());
					pscp.setObject("@MinorVersion", getVersionInfo().getMinorVersion());
					pscp.setObject("@PatchLevel", getVersionInfo().getPatchLevel());
					log(MessageFormat.format("Buildnumber = {0}", getVersionInfo().getBuildNumber()));
					pscp.setObject("@BuildNumber", getVersionInfo().getBuildNumber());
				}
				try {
					pscp.executeUpdate();
				} catch (final SQLException e) {
					log(MessageFormat.format("{0}", e.getMessage()), true);
					executeSql = true;
				}
				final int result = pscp.getInt(1); // 1 -> muss ausgeführt werden, 2
				// ->0
				// kann ausgeführt werden
				if (force) {
					executeSql = true;
				}
				if (result == 1) {
					log(MessageFormat.format("script {0} must be executed", name));
					executeSql = true;
				} else if (result == 2) {
					log(MessageFormat.format("script {0} can be executed", name));
					executeSql = force;
					log("forcesql = " + force);
					// Wenn daten in das Version10 geschrieben werden.
				} else if (result == 5) {
					executeSql = false;
				} else {
					log("alles komisch", true);
					executeSql = true;
				}
				pscp.close();

				if (executeSql) {
					final PreparedStatement ps = this.connection.prepareStatement(sqlScript);
					ps.execute();
					ps.close();
					log(MessageFormat.format("Versionsprüfung für {0} wurde in der tVersion10 ausgeführt", name));
				}

				// Updaten der buildnummer und der tVersion10 einträge
				if (result != 5) {
					pscp = this.connection.prepareCall("{? = call spMinovaUpdateVersion(?,?,?,?,?,?)}");
					pscp.registerOutParameter(1, Types.INTEGER);
					pscp.setObject("@Name", name);
				} else {
					pscp = this.connection.prepareCall("{? = call spMinovaUpdateVersion(?,?,?,?,?,?,?)}");
					pscp.registerOutParameter(1, Types.INTEGER);
					pscp.setObject("@Name", "DATA");
				}
				pscp.setObject("@ModuleName", getVersionInfo().getModulName());
				pscp.setObject("@MajorVersion", getVersionInfo().getMajorVersion());
				pscp.setObject("@MinorVersion", getVersionInfo().getMinorVersion());
				pscp.setObject("@PatchLevel", getVersionInfo().getPatchLevel());
				pscp.setObject("@BuildNumber", getVersionInfo().getBuildNumber());
				if (result == 5) {
					pscp.setObject("@Dataname", name);
				}
				log(MessageFormat.format("Buildnumber = {0}", getVersionInfo().getBuildNumber()));
				pscp.executeUpdate();
				pscp.close();
			}
		} catch (final SQLException e) {
			log(sqlScript, true);
			log("Fehler beim Ausfuehren des Skripts: " + name + " in dem Modul: " + getVersionInfo().getModulName(), true);
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		}
	}

	/**
	 * Ausführen eines SQL-Scripts, auf die connenction, die zuvor erstellt worden ist.
	 *
	 * @param sqlScript
	 * @throws SQLException
	 */
	private void executeSqlScript(final String sqlScript) throws SQLException {
		try {
			final PreparedStatement ps = this.connection.prepareStatement(sqlScript);
			ps.execute();
			ps.close();
			log(MessageFormat.format(" ... ausgeführt", ""), true);
		} catch (final SQLException e) {
			log(MessageFormat.format("ERROR", ""), true);
			log(sqlScript, true);
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		}
	}

	public void execScriptTest(final String name) {
		this.connection = checkConnection(null);
		String script = null;
		try {
			final File file = new File(name);
			System.out.println(file.getAbsolutePath());
			final FileInputStream sqlIn = new FileInputStream(file);
			final StringBuffer sb = new StringBuffer();
			while (sqlIn.available() > 0) {
				final byte[] input = new byte[sqlIn.available()];
				sqlIn.read(input);
				sb.append(new String(input));
			}
			sqlIn.close();
			script = sb.toString();
		} catch (final IOException e) {
			log(MessageFormat.format("Fehler beim auslesen der Datei {0}", name), true);
			log(MessageFormat.format("Error IOException: {0}", e.getMessage()));
			// e.printStackTrace();
		}
		try {
			executeSqlScript(script);
		} catch (final SQLException e) {
			log(MessageFormat.format("Error SQLException: {0}", e.getMessage()));
			e.printStackTrace();
		}
	}

	private String readSqlScript(String name, Path sqlLibrary) {
		try {
			return new String(Files.readAllBytes(sqlLibrary.resolve(name + ".sql")), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Diese Methode liest aus einem übergebenen JarFile eine bestimmte SQL-Datei aus! Die ausgelesene Datei muss mit Ordner angegeben werden, unter dem sie zu
	 * finden ist. Vorgesehen sind 3 Übergabeparameter!
	 *
	 * @param moduleName
	 * @param folderPath
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private String readSqlFromJarFileToString(final String moduleName, final String folderPath, final String fileName) throws IOException {
		final String completeName = moduleName.replaceAll("\\.", "/") + "/" + folderPath + "/" + fileName;
		try {
			final InputStream inputStream = readFromJarFileToInputStream(moduleName, folderPath, fileName);
			if (inputStream == null) {
				// Fehler wurde schon abgefangen und behandelt - gib einfach null zurück
			} else {
				final InputStreamReader sqlIn = new InputStreamReader(inputStream, "UTF-8");
				StringBuffer sb = new StringBuffer();
				int read = sqlIn.read();
				while (read > 0) {
					sb.append((char) read);
					read = sqlIn.read();
				}
				sqlIn.close();
				final String sbvalue = sb.toString();
				sb = null;
				return sbvalue;
			}
		} catch (final NullPointerException npe) {
			log(MessageFormat.format("NullPointerException beim Lesen der Datei '{0}'!", completeName), true);
			// npe.printStackTrace();
		} catch (final IOException e) {
			log(MessageFormat.format("Error IOException: {0}", e.getMessage()));
			// e.printStackTrace();
		}
		return null;
	}

	/**
	 * Diese Methode liest aus einem übergebenen JarFile eine bestimmte Datei aus! Die ausgelesene Datei muss mit Ordner angegeben werden, unter dem sie zu
	 * finden ist. Vorgesehen sind 3 Übergabeparameter!
	 *
	 * @param moduleName
	 * @param folderPath
	 * @param fileName
	 * @return
	 */
	private InputStream readFromJarFileToInputStream(final String moduleName, final String folderPath, final String fileName) {
		InputStream sqlIn = null;
		final File basedir = new File("");
		log(MessageFormat.format("aktuelles Verzeichnis: {0}", basedir.getAbsolutePath()));
		log(MessageFormat.format("Dateiname: {0}", moduleName + ".jar"));
		final String completeName = moduleName.replaceAll("\\.", "/") + "/" + folderPath + "/" + fileName;
		log(MessageFormat.format("Auszulesende Datei: {0}", completeName));
		JarFile jFile = null;
		try {
			jFile = new JarFile(moduleName + ".jar");
			final JarEntry jEntry = new JarEntry(completeName);
			log(jFile + ", " + jEntry);
			final InputStream inputStream = jFile.getInputStream(jEntry);
			// damit das jarFile (und damit alle geöffneten Streams) geschlossen werden kann
			sqlIn = copyStream(inputStream);
		} catch (final Exception e) {
			sqlIn = null;
			// e.printStackTrace();
		} finally {
			if (jFile != null) {
				try {
					// schließt auch alle selbst geöffneten InputStreams
					jFile.close();
				} catch (final IOException e) {
					// e.printStackTrace();
				}
			}
		}

		if (sqlIn == null) {
			// Dann versuchen wir es über das Dateisystem (eher für Debugging)
			try {
				sqlIn = getClass().getResourceAsStream("../" + folderPath + "/" + fileName);
			} catch (final NullPointerException npe) {
				sqlIn = null;
				// e.printStackTrace();
			}
		}

		if (sqlIn == null) {
			log(MessageFormat.format("Die Datei: {0} ist nicht in {1} zu finden!", completeName, moduleName + ".jar"), true);
		}
		return sqlIn;
	}

	/**
	 * Auslesen einers SQL-Scripts. Einlesen des Scriptes aus der Jar-Datei Einlesen in einen String,
	 *
	 * @param name
	 * @return String - String mit dem Inhalt des Scripts wird zurückgegeben.
	 */
	/*
	 * private String readSqlScript(String name) { log(MessageFormat.format("read SQL-script: ../sql/{0}.sql", name), true); InputStream sqlIn = null; try {
	 * File pfad = new File(""); log(MessageFormat.format("aktuelles Verzeichnis: {0}", pfad.getAbsolutePath())); log(MessageFormat.format("Dateiname: {0}",
	 * getVersionInfo().getModulName() + ".jar")); JarFile jFile = new JarFile(getVersionInfo().getModulName() + ".jar"); log(MessageFormat.format(
	 * "Datei JFile: {0}", jFile.getName())); JarEntry jEntry = new JarEntry(getVersionInfo().getModulName().replaceAll("\\.", "/") + "/sql/" + name + ".sql");
	 * log(jFile + ", " + jEntry); sqlIn = jFile.getInputStream(jEntry); } catch (IOException e2) { // Dann versuchen wir es über das Dateisystem (eher für
	 * Debugging) sqlIn = getClass().getResourceAsStream("../sql/" + name + ".sql"); } try { StringBuffer sb = new StringBuffer(); while (sqlIn.available() > 0)
	 * { byte[] input = new byte[sqlIn.available()]; sqlIn.read(input); sb.append(new String(input)); } sqlIn.close(); log(sb.toString(), false); return
	 * sb.toString(); } catch (NullPointerException npe) { log(" error reading file!!", true); return "select 0"; // ein gültiger Befehl, damit es weitergeht }
	 * catch (IOException e) { e.printStackTrace(); } return null; }
	 */

	/**
	 * Verarbeitet den xbs-code und liest dabei die Maps und verschiedene knoten ab.
	 *
	 * @return
	 * @throws BaseSetupException
	 * @throws XmlException
	 * @throws IOException
	 */
	public boolean compareRequirementsEquals(final BaseSetup setup, final Module module) throws IncompatibleVersionException {
		final Version[] version = module.getVersionArray();
		VersionInfo vi;

		if (version.length == 0) {
			String major;
			String minor;
			String patch;
			try {
				major = module.getMajor().toString();
			} catch (final NullPointerException npe) {
				major = "10";
			}
			try {
				minor = module.getMinor().toString();
			} catch (final NullPointerException npe) {
				minor = "0";
			}
			try {
				patch = module.getPatch().toString();
			} catch (final NullPointerException npe) {
				patch = "0";
			}

			vi = new VersionInfo(major, minor, patch, "0", "2013-01-01 00:00:00", module.getName());

			if (setup.getVersionInfo().compatibleTo(vi)) {
				setValue(module.getName(), setup.getVersionInfo(), setup);
				return true;
			} else {
				log(MessageFormat.format("In dem Modul: {0} wird folgende Meldung geworfen", setup.versionInfo.getModulName()), true);
				throw new IncompatibleVersionException(
						MessageFormat.format(REQUIREDVERSION, vi.getMajorVersion(), vi.getMinorVersion(), vi.getPatchLevel(), vi.getModulName()));
			}
		} else {
			for (int i = 0; i < version.length; i++) {
				vi = new VersionInfo(version[i].getMajor().toString(), version[i].getMinor().toString(), version[i].getPatch().toString(), "0",
						"2013-01-01 00:00:00", module.getName());
				if (setup.getVersionInfo().compatibleTo(vi)) {
					setValue(module.getName(), setup.getVersionInfo(), setup);
					return true;
				}
			}
			log(MessageFormat.format("In dem Modul: {0} wird folgende Meldung geworfen", setup.versionInfo.getModulName()), true);
			throw new IncompatibleVersionException(
					MessageFormat.format(REQUIREDVERSION, version[0].getMajor(), version[0].getMinor(), version[0].getPatch(), module.getName()));
		}
	}

	/**
	 * Die Methode oeffnet die uebergebene xbs-Datei und lengt in dieser Datei (file) den Node "minova" und das Entsprechende Kind an. Das Kind hat den namen
	 * der xbs Datei in der es steht. disposition.xbs --> node name="disposition" Das uebergebene Object PreferencesDocument wird erweitert und dann in (file)
	 * gespeichert Der uebergebene String liefert den Namen des child Knotens unterhalb von "minova" z.B.: disposition.xbs, tankterminalautomation.xbs,
	 * aircraftfuelinginformationsystem.xbs, servicesinvoicingsystem.xbs
	 *
	 * @param xbsFile
	 * @throws BaseSetupException
	 * @throws XmlException
	 * @throws IOException
	 */
	public void createXbsMinova(final String xbsFile, final PreferencesDocument pref, final File file) throws BaseSetupException, XmlException, IOException {
		final String substringxml = xbsFile.substring(0, xbsFile.indexOf("."));

		final Root root = pref.getPreferences().getRoot();
		Node node = null;
		root.insertNewNode(0);
		node = root.getNodeArray(0);
		node.setName("minova");
		node.addNewMap();
		node.insertNewNode(0);
		node.getNodeArray(0).setName(substringxml);
		savepref(pref, file);
	}

	public void savepref(final PreferencesDocument pref, final File file) throws IOException {
		pref.documentProperties().setEncoding("UTF-8");
		final XmlOptions opts = new XmlOptions();
		opts.setCharacterEncoding("UTF-8");
		opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(4);
		pref.save(file, opts);
	}

	/**
	 * Auslesen einer xbs-Datei das dabei entstandene PreferenceDocument wird zurückgegen.
	 *
	 * @param file
	 * @return
	 * @throws BaseSetupException
	 */
	public PreferencesDocument getPreferencesDocument(final File file) throws BaseSetupException {
		PreferencesDocument xbsdocument = null;
		try {
			final XMLStreamReader xmlsr = XMLInputFactory.newFactory().createXMLStreamReader(new FileInputStream(file), "UTF-8");
			final XmlOptions opts = new XmlOptions();
			opts.setCharacterEncoding("UTF-8");
			opts.setSavePrettyPrint();
			opts.setSavePrettyPrintIndent(4);
			xbsdocument = PreferencesDocument.Factory.parse(xmlsr, opts);
			xbsdocument.documentProperties().setEncoding("UTF-8");
		} catch (final XmlException e) {
			throw new BaseSetupException(MessageFormat.format(MISSINGFILE, file.getAbsolutePath()));
		} catch (final IOException e) {
			throw new BaseSetupException(MessageFormat.format(MISSINGFILE, file.getAbsolutePath()));
		} catch (final XMLStreamException e) {
			log(MessageFormat.format("Error XMLStreamException: {0}", e.getMessage()));
			// e.printStackTrace();
		} catch (final FactoryConfigurationError e) {
			log(MessageFormat.format("Error FactoryConfigurationError: {0}", e.getMessage()));
			// e.printStackTrace();
		}
		return xbsdocument;
	}

	/**
	 * Aus einem übergebnenen File wird ein MainDocument erstellt. Mit diesem MainDocument ist es möglich die MDI Daten auszulesen.
	 *
	 * @param file
	 * @return
	 * @throws BaseSetupException
	 */
	public MainDocument getMainDocument(final File file) throws BaseSetupException {
		MainDocument xbsdocument = null;
		try {
			final XMLStreamReader xmlsr = XMLInputFactory.newFactory().createXMLStreamReader(new FileInputStream(file), "UTF-8");
			final XmlOptions opts = new XmlOptions();
			opts.setCharacterEncoding("UTF-8");
			opts.setSavePrettyPrint();
			opts.setSavePrettyPrintIndent(4);
			xbsdocument = MainDocument.Factory.parse(xmlsr, opts);
			xbsdocument.documentProperties().setEncoding("UTF-8");
		} catch (final XmlException e) {
			throw new BaseSetupException(MessageFormat.format(MISSINGFILE, file.getAbsolutePath()));
		} catch (final IOException e) {
			throw new BaseSetupException(MessageFormat.format(MISSINGFILE, file.getAbsolutePath()));
		} catch (final XMLStreamException e) {
			log(MessageFormat.format("Error XMLStreamException: {0}", e.getMessage()));
			// e.printStackTrace();
		} catch (final FactoryConfigurationError e) {
			log(MessageFormat.format("Error FactoryConfigurationError: {0}", e.getMessage()));
			// e.printStackTrace();
		}
		return xbsdocument;
	}

	/**
	 * Auslesen/Erstellen des PreferenceDocument Objektes aus der angegebenen xbs-Datei
	 *
	 * @param xbsfile
	 * @return
	 * @throws BaseSetupException
	 * @throws XmlException
	 * @throws IOException
	 */
	public PreferencesDocument getPreferencesDocument(final String xbsfile) throws BaseSetupException, XmlException, IOException {
		PreferencesDocument xbsdocument = null;
		final InputStream isXbsPfad = this.getClass().getResourceAsStream(xbsfile);
		try {
			if (isXbsPfad == null) {
				throw new BaseSetupException(MessageFormat.format(MISSINGFILE, xbsfile));
			}
			final XMLStreamReader xmlsr = XMLInputFactory.newFactory().createXMLStreamReader(isXbsPfad, "UTF-8");
			final XmlOptions opts = new XmlOptions();
			opts.setCharacterEncoding("UTF-8");
			opts.setSavePrettyPrint();
			opts.setSavePrettyPrintIndent(4);
			xbsdocument = PreferencesDocument.Factory.parse(xmlsr, opts);
		} catch (final XMLStreamException e) {
			log(MessageFormat.format("Error XMLStreamException: {0}", e.getMessage()));
			// e.printStackTrace();
		} catch (final FactoryConfigurationError e) {
			log(MessageFormat.format("Error FactoryConfigurationError: {0}", e.getMessage()));
			// e.printStackTrace();
		}

		return xbsdocument;
	}

	public void saveMainDoc(final MainDocument maindoc, final File file) throws IOException {
		maindoc.documentProperties().setEncoding("UTF-8");
		final XmlOptions opts = new XmlOptions();
		opts.setSavePrettyPrint();
		opts.setCharacterEncoding("UTF-8");
		opts.setSavePrettyPrintIndent(4);
		maindoc.save(file, opts);
	}

	/**
	 * Instantiierung der Setup Klasse aus dem uebergebenen Module, ausfuehrung der run Methode mit dem Parameter, welcher der in der Konsole angefuegt worden
	 * ist Uebergabeparameter: Module module
	 *
	 * @param module
	 * @return
	 * @throws ModuleNotFoundException
	 * @throws BaseSetupException
	 * @throws VersionInfoException
	 * @throws URISyntaxException
	 */
	public BaseSetup getSetupClass(final Module module) throws ModuleNotFoundException {
		return getSetupClass(module.getName());
	}

	/**
	 * Instantiierung der Setup Klasse aus dem uebergebenen Module, ausfuehrung der run Methode mit dem Parameter, welcher der in der Konsole angefuegt worden
	 * ist Uebergabeparameter: Module module
	 *
	 * @param module
	 * @return
	 * @throws ModuleNotFoundException
	 * @throws BaseSetupException
	 * @throws VersionInfoException
	 * @throws URISyntaxException
	 */
	public BaseSetup getSetupClass(final Service service) throws ModuleNotFoundException {
		return getSetupClass(service.getName());
	}

	/**
	 * Instantiierung der Setup Klasse aus dem uebergebenen Module, ausfuehrung der run Methode mit dem Parameter, welcher der in der Konsole angefuegt worden
	 * ist Uebergabeparameter: String (Name des Modules)
	 *
	 * @param strKey
	 * @return
	 * @throws ModuleNotFoundException
	 */
	private BaseSetup getSetupClass(final String strKey) throws ModuleNotFoundException {
		BaseSetup reflectionclass = null;
		try {
			reflectionclass = (BaseSetup) Class.forName(strKey + ".setup.Setup").newInstance();
			reflectionclass.run();
		} catch (final Exception e) {

			if (parameter.containsKey("list") && (parameter.containsKey("cm") || parameter.containsKey("checkmodules"))) {

				if (!modulesnotfound.containsKey(strKey)) {
					modulesnotfound.put(strKey, true);
					modulesnotfoundvector.add(strKey);
				}
			} else {
				throw new ModuleNotFoundException(MessageFormat
						.format(" \u2620 \u2620 \u2620 \u2620  ModuleNotFoundException: " + MISSINGMODULE + " \u2620 \u2620  \u2620  \u2620 ", strKey));
			}
		}
		return reflectionclass;
	}

	/**
	 * Instantiierung der Setup Klasse aus dem uebergebenen Module, ausfuehrung der run Methode mit dem Parameter, welcher der in der Konsole angefuegt worden
	 * ist Uebergabeparameter: String (Name des Modules)
	 *
	 * @param strKey
	 * @return
	 * @throws ModuleNotFoundException
	 */
	private BaseSetup getSetupClass(final String strKey, final boolean oneof) throws ModuleNotFoundException {
		BaseSetup reflectionclass = null;
		try {
			reflectionclass = (BaseSetup) Class.forName(strKey + ".setup.Setup").newInstance();
			reflectionclass.run();
		} catch (final Exception e) {

			if (parameter.containsKey("list") && (parameter.containsKey("cm") || parameter.containsKey("checkmodules"))) {

				if (oneof == true) {
					if (!modulesnotfound.containsKey(strKey + " - one-of modul")) {
						modulesnotfound.put(strKey, true);
						modulesnotfoundvector.add(strKey + " - one-of modul");
					}
				}
			} else {
				throw new ModuleNotFoundException(MessageFormat
						.format(" \u2620 \u2620 \u2620 \u2620  ModuleNotFoundException: " + MISSINGMODULE + " \u2620 \u2620  \u2620  \u2620 ", strKey));
			}
		}
		return reflectionclass;
	}

	/**
	 * auslesen der Setup.xml Datei des Modul. Dabei wird eine SetupDocumet erstellt, welche auch zurückgegebn wird
	 *
	 * @return
	 * @throws XmlException
	 * @throws IOException
	 * @throws BaseSetupException
	 */
	private SetupDocument getSetupDocument() throws XmlException, IOException, BaseSetupException {
		if (setupDocument != null) {
			return setupDocument;
		}
		final String setupxml = "Setup.xml";
		final InputStream isSetupXmlpfad = this.getClass().getResourceAsStream(setupxml);
		if (isSetupXmlpfad == null) {
			throw new BaseSetupException(MessageFormat.format(MISSINGFILE, setupxml));
		}
		final SetupDocument doc = SetupDocument.Factory.parse(isSetupXmlpfad, null);
		// setjavacall(doc); 
		return doc;
	}

	public VersionInfo getVersionInfo() {
		return getVersionInfo(this);
	}

	public VersionInfo getVersionInfo(final BaseSetup setup) {
		if (this.versionInfo == null) {
			return new VersionInfo("0.0.0-0 2013-01-01 00:00:00", setup.getClass().getName());
		} else {
			return this.versionInfo;
		}
	}

	/**
	 * überprüfung der SQL-Einträge in der Setup.xml Datei
	 *
	 * @param bs  BaseSetup
	 * @param doc SetupDocument
	 * @throws IOException
	 * @throws SQLException
	 * @throws BaseSetupException
	 */
	public void checkSql(final BaseSetup bs, final SetupDocument doc) throws IOException, SQLException, BaseSetupException {
		final Script[] sc = doc.getSetup().getSqlCode().getScriptArray();
		String path = bs.getClass().getName().replace(".Setup", "").replace(".setup", "") + "/sql";
		log(MessageFormat.format("Pfad unter dem die sql Dateien zu finden sind: {0}", path));
		@SuppressWarnings("unused")
		boolean checkscripts = false;
		if (parameter.containsKey("basedir")) {
			path = parameter.getProperty("basedir") + path;
		} else {
			path = "../" + path;
		}
		for (int i = 0; i < sc.length; i++) {
			checkscripts = false;
			// Nur table Scripte
			log(MessageFormat.format("Name: {0}, Type:{1}", sc[i].getName(), sc[i].getType()));
			final String type = sc[i].getType().toString();
			if (type.equals("table")) {
				checkscripts = checkScriptVersion(sc[i], bs);
			}
		}
	}

	/**
	 * Verarbeitung der Scripte vom type "table", dabie werden die Versionen auf ihre existens überprüft!
	 *
	 * @param script
	 * @param bs
	 * @param path
	 * @param name
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 * @throws BaseSetupException
	 */
	private boolean checkScriptVersion(final Script script, final BaseSetup bs) throws IOException, SQLException, BaseSetupException {
		final Version[] varrayscript = script.getVersionArray();
		String path = getModuleName(bs.getClass());
		final String jarpath = path + ".jar";
		log(MessageFormat.format("Pfad zur Jar_Datei: {0}", jarpath));
		path = path.replace(".", "/") + "/";
		log(MessageFormat.format("Pfad zur den Xml-Datein: {0}", path));

		JarFile jFile = null;
		JarEntry jEntry = null;
		// java.util.Map.Entry<?, ?> entry = null;
		jFile = new JarFile(jarpath);
		String dataName = "";
		VersionInfo version = null;
		int countsql = 0;
		final Vector<String> sqltables = new Vector<String>();
		final Vector<String> sqlconstrains = new Vector<String>();
		final Vector<String> sqlvalues = new Vector<String>();
		// ein Array mit genau so vielen Einträgen wie verschiedene
		// Minorversions
		// die höchste Minorversion bestimmt die anzhal der Felder des Arrays
		for (int i = 0; i < varrayscript.length; i++) {
			int versionfound = 0;
			log(MessageFormat.format("Version der von {0} : {1}", script.getName(), varrayscript[i].toString()));
			final Enumeration<JarEntry> jEntries = jFile.entries();
			while (jEntries.hasMoreElements()) {
				jEntry = jEntries.nextElement();
				dataName = jEntry.getName().replace(path, "");
				if (dataName.startsWith("sql/" + script.getName())) {
					if (i == 0) {
						countsql++;
					}
					log(MessageFormat.format("Datei gelesen= {0}", dataName));
					version = getVersionFromString(dataName);
					// Version wurde gefunden!
					if (version.getMajorVersion() == varrayscript[i].getMajor().intValue()) {
						if (version.getMinorVersion() == varrayscript[i].getMinor().intValue()) {
							if (version.getPatchLevel() == varrayscript[i].getPatch().intValue()) {
								log(MessageFormat.format("Die ausgelesene Datei: {0} stimmt mit der Version ueberein",
										version.getModulName() + "-" + version.toString()));
								versionfound++;
								if (dataName.endsWith(".constraints.sql")) {
									sqlconstrains.add(dataName.replace(".sql", "").replace("sql/", ""));
								} else if (dataName.endsWith(".values.sql")) {
									sqlvalues.add(dataName.replace(".sql", "").replace("sql/", ""));
								} else if (dataName.endsWith(".sql")) {
									sqltables.add(dataName.replace(".sql", "").replace("sql/", ""));
								}
							}
						}
					}
				}
			}
			final String name = script.getName() + "-" + varrayscript[i].getMajor() + "." + varrayscript[i].getMinor() + "." + varrayscript[i].getPatch();
			if (versionfound > 0) {
				countsql = countsql - versionfound;
				log(MessageFormat.format("Es wurden {0}, gleichnamige Dateien gefunden: {1}", versionfound, name));
			} else {
				log(MessageFormat.format("Es wurden {0}, gleichnamige Dateien gefunden: {1} Error", versionfound, name), true);
				// keine Version gefunden die zu dieser Datei gehört, -->
				// Anlegen
				// und hinzufügen!
				// TODO
			}
		}

		final String table = "tVersion10";
		final Connection connection = checkConnection(null);
		final Hashtable<String, TVersion> tVersionHash = new Hashtable<String, TVersion>();
		ResultSet results = null;
		results = connection.createStatement().executeQuery("select * from " + table);
		TVersion tversion = null;
		// Einlesen der Daten aus tVersion
		while (results.next()) {
			tVersionHash.put(results.getString("Keytext"),
					new TVersion(results.getInt("KeyLong"), results.getString("Keytext"), results.getString("ModuleName"), results.getInt("MajorVersion"),
							results.getInt("MinorVersion"), results.getInt("PatchLevel"), results.getInt("BuildNumber"), results.getString("LastUser"),
							results.getString("LastDate"), results.getInt("LastAction")));
		}

		if (tVersionHash.containsKey(script.getName())) {
			tversion = tVersionHash.get(script.getName());
			log(MessageFormat.format("Tabelleneintrag: Keylong:{0} ,Modul:{6}, Name:{1}, {2}.{3}.{4}-{5}", tversion.getKeylong(), tversion.getKeytext(),
					tversion.getMajorversion(), tversion.getMinorversion(), tversion.getPatchlevel(), tversion.getBuildnumber(), tversion.getModulname()));
		}

		try {
			Collections.sort(sqltables);
			Collections.sort(sqlconstrains);
			Collections.sort(sqlvalues);
		} catch (final Exception e) {
			throw new BaseSetupException(MessageFormat.format("Sorting Vectors Error: {0}", e.getMessage()));
		}

		for (final String s : sqltables) {
			log(MessageFormat.format("SQL-Tables: {0}", s));
			if (tversion == null) {
				log(MessageFormat.format("SQL-Tables: {0} wurde ausgefuehrt", s), true);
				// executeSqlTable(readSqlScript(s), s);
				executeSqlTable(readSqlFromJarFileToString(getVersionInfo().getModulName(), sqldialect, s + ".sql"), s);
			} else {
				if (compareToTVersion(tversion, new VersionInfo(s.substring(s.indexOf(".") + 1, s.length()) + "-0", "", s.substring(0, s.indexOf("."))))) {
					log(MessageFormat.format("SQL-Tables: {0} wurde ausgefuehrt", s), true);
					executeSqlTable(readSqlFromJarFileToString(getVersionInfo().getModulName(), sqldialect, s + ".sql"), s);
				}
			}
		}
		for (final String s : sqlconstrains) {
			log(MessageFormat.format("SQL-Constrains: {0}", s));
			if (compareToTVersion(tversion, new VersionInfo(s.substring(s.indexOf(".") + 1, s.lastIndexOf(".")) + "-0", "", s.substring(0, s.indexOf("."))))) {
				log(MessageFormat.format("SQL-Constrains: {0} wurde ausgefuehrt", s), true);
				executeSqlScript(readSqlFromJarFileToString(getVersionInfo().getModulName(), sqldialect, s + ".sql"));
			}
		}
		for (final String s : sqlvalues) {
			log(MessageFormat.format("SQL-Values: {0}", s));
			if (compareToTVersion(tversion, new VersionInfo(s.substring(s.indexOf(".") + 1, s.lastIndexOf(".")) + "-0", "", s.substring(0, s.indexOf("."))))) {
				log(MessageFormat.format("SQL-Values: {0} wurde ausgefuehrt", s), true);
				executeSqlScript(readSqlFromJarFileToString(getVersionInfo().getModulName(), sqldialect, s + ".sql"));
			}
		}
		if (countsql == 0) {
			log(MessageFormat.format("Es wurden {0} Dateien zu viel in {1} gedunden", countsql, jarpath + "/sql"));
		} else {
			log(MessageFormat.format("Es wurden {0} Dateien zu viel in {1} gedunden Error", countsql, jarpath + "/sql"), true);
		}
		return false;
	}

	/**
	 * Vergleicht die verschiedenen Versionen untereinander, zum Vergleich wird der gleichnamige Eintrag in der Tabelle der Datenbank tVersion10 herangezogen.
	 * Verglichen wird dieser mit dem Namen der auszuführenden *.sql Datei z.B: tAircraft.10.22.4 und der Eintrag | tAircraft | ch.minova.install | 10 | 0 | 0 |
	 * 213 |
	 *
	 * @param tversion
	 * @param vi
	 * @return
	 */
	private boolean compareToTVersion(final TVersion tversion, final VersionInfo vi) {
		if (tversion == null) {
			return true;
		}
		if (tversion.getMajorversion() >= vi.getMajorVersion()) {
			if (tversion.getMinorversion() >= vi.getMinorVersion()) {
				if (tversion.getPatchlevel() >= vi.getPatchLevel()) {
					return false;
				}
				return true;
			}
			return true;
		}
		return true;
	}

	/**
	 * wandelt aus einem Dateinamen die Versionsnummer ab
	 *
	 * @param name
	 * @return
	 */
	private VersionInfo getVersionFromString(String name) {
		final String version = name;
		name = name.substring(name.indexOf(".") + 1, name.length());
		log(MessageFormat.format("Substring von Dateiname: {0}", name));
		final String major = name.substring(0, name.indexOf("."));
		name = name.substring(name.indexOf(".") + 1, name.length());
		final String minor = name.substring(0, name.indexOf("."));
		name = name.substring(name.indexOf(".") + 1, name.length());
		final String patchlevel = name.substring(0, name.indexOf("."));
		name = name.substring(name.indexOf(".") + 1, name.length());
		final String buildNumber = "0";
		final String buildDate = "null";
		final VersionInfo v = new VersionInfo(major, minor, patchlevel, buildNumber, buildDate, name);
		log(MessageFormat.format("Versioninfo von Dateiname: {0}", version.toString()));
		return v;
	}

	/**
	 * Verarbeitung eines Arrays aus Modulen, Module stammen von den RequirementModules, und m�ssen alle in der korrektenVersion vorhanden sein, damit die es
	 * nicht zu einem fehler kommt. anders als bei handleOneOf. Uebergabeparameter: Module[] modules
	 *
	 * @param modules
	 * @return
	 * @throws BaseSetupException
	 * @throws VersionInfoException
	 * @throws URISyntaxException
	 * @throws ModuleNotFoundException
	 * @throws CircleFoundException
	 * @throws IncompatibleVersionException
	 */
	public boolean handleModules(final Module[] modules)
			throws BaseSetupException, VersionInfoException, URISyntaxException, ModuleNotFoundException, CircleFoundException, IncompatibleVersionException {
		Boolean boolfinder = true;
		Boolean boolcheckcircle = true;
		BaseSetup reflectionclass = null;
		final Module[] module = modules;
		for (int k = 0; k < module.length; k++) {
			// Dynamischer Klassenaufruf, Reflection
			// Einspungspunkt fuer die "rekursiven" Aufrufe
			boolcheckcircle = checkActive(module[k].getName());
			/*
			 * ueberprueft ob das aktive Module einen Kreislauf besitzt (sich selbst aufruft) Sobald ein erforderliches Module bereits in hashModulesAvctive
			 * vorhanden ist wird eine BaseSetupException geworfen!
			 */
			if (!boolcheckcircle) {
				throw new CircleFoundException(MessageFormat.format(CIRCULATORYREQUIREMENT, module[k].getName()));
			}
			setValueActive(module[k].getName(), 1);
			/*
			 * actives Module wird gespeichert
			 */
			reflectionclass = getSetupClass(module[k]);
			deleteModuleActive(module[k].getName());

			// Module mit dem Hashtable vergleichen
			// Doppelte Aufrufe werden verhindert
			if (checkKeyModule(module[k])) {
				continue;
			}
			// Wenn das Modul nicht gefunden wird fahren wir fort.
			if (reflectionclass == null) {
				continue;
			}

			/*-----------------------------------------------------------
			Vergleich der verschiedenen Versionsnummern:
			habe: 10.0.0, brauche 10.8.1
			Ausgabe: Modul in der erforderlichen Version x.x.x nicht vorhanden.
			--> Abbruch
			-----------------------------------------------------------*/
			boolean isequal = false;
			if (module[k].getVersionArray().length < 1) {
				isequal = compareRequirementsEquals(reflectionclass, module[k]);
				if (isequal) {
					log(MessageFormat.format(MODULEISVERSION, module[k].getName()));
					boolfinder = true;
				} else {
					System.out.println(MessageFormat.format(MODULENOVERSION, module[k].getName()));
					boolfinder = false;
				}
			} else {
				if (compareRequirementsEquals(reflectionclass, module[k])) {
					System.out.println(MessageFormat.format(MODULEISVERSION, module[k].getName()));
					boolfinder = true;
				} else {
					System.out.println(MessageFormat.format(MODULENOVERSION, module[k].getName()));
					boolfinder = false;
				}
			}
			if (!boolfinder) {
				System.out.println(MessageFormat.format(MODULEMISSING, module[k].getName()));
				return boolfinder;
			}
			boolfinder = true;

		}
		return boolfinder;
	}

	/**
	 * liest die orderedDependingModules liste aus und führt für jedes modul die Setup.xml aus bzw liest den Xbs-Code
	 *
	 * @throws XmlException
	 * @throws IOException
	 * @throws BaseSetupException
	 */
	public void readXbsOfOrderedModules() throws XmlException, IOException, BaseSetupException {
		SetupDocument setup;
		final BaseSetup[] a = orderedDependingModules.toArray(new BaseSetup[0]);

		for (final BaseSetup bs : a) {
			setup = bs.getSetupDocument();
			log(MessageFormat.format("Aus der Setup.xml Datei wird der xbs-Code ausgelesen Modul:{0}_{1}", bs.getVersionInfo().getModulName(),
					bs.getVersionInfo().toString()));
			bs.readXbsCode(setup);
		}
	}

	/**
	 * liest aus dem übergebenen SetupDocument (Setup.xml) den xbs-code aus und schreibt diesen dann in rootNodeRoot (Objekt von root aus BaseSetup)
	 *
	 * @param doc
	 * @throws BaseSetupException
	 */
	private void readXbsCode(final SetupDocument doc) throws BaseSetupException {
		if (parameter.containsKey("file")) {
			String xbsfile = parameter.getProperty("file");
			if (!xbsfile.endsWith(".xbs")) {
				xbsfile = xbsfile + ".xbs";
			}
			// hier muss darauf geachtet werden, dass überprüft wird, ob die
			// Setup.xml Datei überhaupt xbs-code besitzt
			if (doc.getSetup().getXbsCode() != null) {

				if (doc.getSetup().getXbsCode().getMap() != null) {
					final Map m = doc.getSetup().getXbsCode().getMap();
					rootNodeRoot.getNode("minova", true).getNode(xbsfile.substring(0, xbsfile.indexOf(".")), true).readMap(m);
				}
				for (int i = 0; i < doc.getSetup().getXbsCode().getNodeArray().length; i++) {
					rootNodeRoot.getNode("minova", true).getNode(xbsfile.substring(0, xbsfile.indexOf(".")), true)
							.readNode(doc.getSetup().getXbsCode().getNodeArray(i));
				}
				final ch.minova.install.setup.xbs.Node node = rootNodeRoot.getNode("minova", true).getNode(getAppName(), true);
				final ArrayList<ch.minova.install.setup.xbs.Entry> entry = node.getEntries();
				for (final ch.minova.install.setup.xbs.Entry entry2 : entry) {
					if (entry2.getKey().equalsIgnoreCase("CustomerID")) {
						setParameter("c", entry2.getValue().toString(), true);
					}
				}
			}
		} else {
			throw new BaseSetupException(MessageFormat.format("Es wurde keine Application name in -file=<application> übergeben", ""));
		}
	}

	/**
	 * Verarbeitung eines Arrays aus Modulen, sobald ein Module in der richtigen Verison gefunden wurde, wird das OneOf-Module akzeptiert.
	 *
	 * @param module
	 * @return
	 * @throws ModuleNotFoundException
	 * @throws BaseSetupException
	 * @throws VersionInfoException
	 * @throws URISyntaxException
	 * @throws CircleFoundException
	 */
	public boolean handleOneOfModule(final Module[] module)
			throws ModuleNotFoundException, BaseSetupException, VersionInfoException, URISyntaxException, CircleFoundException, IncompatibleVersionException {
		boolean moduleStatus = false;
		int counterException = 0;
		for (int k = 0; k < module.length; k++) {
			try {
				moduleStatus = moduleStatus | handleOneOfModuleModul(module[k]);
			} catch (final ModuleNotFoundException e) {
				deleteModuleActive(module[k].getName());
				counterException++;
				System.out.println(MessageFormat.format(MODULEONEOFNOTFOUND, module[k].getName()));
				if (counterException == module.length) {
					System.out.println(MessageFormat.format(ONEOFFAIL, e.getMessage()));
					for (final Module module2 : module) {
						System.out.println(MessageFormat.format(REQUIREDMODULES, module2.getName()));
					}
					throw new ModuleNotFoundException(e.getMessage());
				}
			}
		}
		if (moduleStatus) {
			System.out.println(MessageFormat.format(MODULEARRAYOK, ""));
			return true;
		} else {
			// Exception: kein Module konnte erreicht werden!
			System.out.println(MessageFormat.format(MODULEARRAYFAIL, ""));
			return false;
		}
	}

	/**
	 * Bearbeitet ein Module aus einer Menge von erforderlichen OneOf-Modulen, ueberpruefung der Version des angegebenen Modules Uebergabeparameter: Module
	 * module Rueckgabe: boolean, fuer den Fall , dass das Module nicht gefunden wurde, wird eine IncompatibleVersionException geworfen
	 *
	 * @param module
	 * @return
	 * @throws ModuleNotFoundException
	 * @throws BaseSetupException
	 * @throws VersionInfoException
	 * @throws URISyntaxException
	 * @throws CircleFoundException
	 */
	public boolean handleOneOfModuleModul(final Module module)
			throws ModuleNotFoundException, BaseSetupException, VersionInfoException, URISyntaxException, CircleFoundException, IncompatibleVersionException {
		boolean checkVersion = false;
		BaseSetup reflectionclass = null;
		final boolean boolcheckcircle = checkActive(module.getName());
		/*
		 * ueberprueft ob das aktive Module einen Kreislauf besitzt (sich selbst aufruft) Sobald ein erforderliches Module bereits in hashModulesAvctive
		 * vorhanden ist wird eine BaseSetupException geworfen!
		 */
		if (!boolcheckcircle) {
			throw new CircleFoundException(MessageFormat.format(CIRCULATORYREQUIREMENT, module.getName()));
		}
		setValueActive(module.getName(), 1);
		reflectionclass = getSetupClass(module, true);
		deleteModuleActive(module.getName());

		if (checkKeyModule(module)) {
			return true;
		}
		try {
			checkVersion = compareRequirementsEquals(reflectionclass, module);
		} catch (final IncompatibleVersionException e) {
			log(MessageFormat.format("Aus dem Modul {0} wurde eine IncompatibleVersionException geworfen", getVersionInfo().getModulName()), true);
			throw new IncompatibleVersionException(MessageFormat.format(MODULEONEOFWRONGVERSION, module.getName()));
		}
		if (checkVersion) {
			System.out.println(MessageFormat.format(MODULEONEOFFOUND, module.getName()));
			return checkVersion;
		} else {
			return checkVersion;
		}
	}

	private BaseSetup getSetupClass(final Module module, final boolean b) throws ModuleNotFoundException {
		return getSetupClass(module.getName(), b);
	}

	/**
	 * initialisiert das Setup, dabei wird die buildnumber.properties Datei ausgelesen und die Daten werden in die VersionInfo geschrieben
	 *
	 * @param setup
	 */
	protected void initModuleInfo() {
		if (!this.getClass().getName().equals("ch.minova.install.setup.BaseSetup")) {
			initModuleInfo(this);
		}
	}

	/**
	 * initialisiert das Setup, dabei wird die buildnumber.properties Datei ausgelesen und die Daten werden in die VersionInfo geschrieben
	 *
	 * @param setup
	 */
	private void initModuleInfo(final BaseSetup setup) {
		String packageName = setup.getClass().getName();
		// durch die -6 wird das angehängte .setup nicht beachtet
		packageName = packageName.substring(0, packageName.length() - 6).replace(".", "/");
		final String buildnumberFilename = packageName + "/buildnumber.properties";
		final InputStream inBuildProps = Setup.class.getClassLoader().getResourceAsStream(buildnumberFilename);
		if (inBuildProps == null) {
			System.err.println(MessageFormat.format(MISSINGFILE, buildnumberFilename));
		}
		final Properties buildProps = new java.util.Properties();
		try {
			buildProps.load(inBuildProps);
		} catch (final IOException e) {
			System.err.println(MessageFormat.format(LOADFILEERROR, buildnumberFilename));
			log(MessageFormat.format("Error IOException: {0}", e.getMessage()));
			// e.printStackTrace();
		}
		final String setVersionInfo = buildProps.getProperty("major.version") + "." + buildProps.getProperty("minor.version") + "."
				+ buildProps.getProperty("patch.level") + "-" + buildProps.getProperty("build.number") + " " + buildProps.getProperty("build.date");
		this.versionInfo = new VersionInfo(setVersionInfo, getModuleName(setup.getClass()));
	}

	/**
	 * standard Logging, in diesem Fall werden die Nachrichten nur ausgegeben wenn der Parameter -verbose hinter die Nachricht geschrieben wird
	 *
	 * @param message
	 */
	protected void log(final String message) {
		log(message, false);
	}

	/**
	 * Logging falls true übergeben wird, werden die Logs auf der Konsole ausgegeben
	 *
	 * @param message
	 * @param forceOutput
	 */
	protected static void log(final String message, final boolean forceOutput) {
		if (forceOutput || (parameter.containsKey("verbose") || parameter.containsKey("v"))) {
			System.out.println(message);
		}
	}

	/**
	 * Auslesen einer übergebenen xbs- Datei.
	 *
	 * @param xbspath
	 * @param xbsfile
	 * @return
	 * @throws BaseSetupException
	 */
	public boolean readEveryXbsFile(final String xbspath, final String xbsfile) throws BaseSetupException {
		PreferencesDocument xbsdocument = null;
		final File file = new File(xbspath + "/" + xbsfile);
		try {
			log(MessageFormat.format("Absoluter Pfad der Datei: {0}", file.getAbsolutePath()), true);
			xbsdocument = getPreferencesDocument(file);
		} catch (final BaseSetupException e) {
			throw new BaseSetupException(MessageFormat.format(MISSINGFILE, xbspath + xbsfile));
		}
		/*
		 * Alle Entries die ind er Map aufgelistet werden, werden in das Hauptobjekt übernommen.
		 */
		addEntriesToRoot(rootNodeRoot, xbsdocument.getPreferences().getRoot().getMap());
		addNodeToRoot(rootNodeRoot, xbsdocument.getPreferences().getRoot().getNodeArray());
		/*
		 * hierachisch werden die unterschiedlichen knoten durch die 6 Objekte repräsentiert
		 */
		return true;
	}

	/**
	 * liest die orderedDependingModules liste aus und führt für jedes modul die Setup.xml aus bzw liest den Mdi-Code
	 *
	 * @throws XmlException
	 * @throws IOException
	 * @throws BaseSetupException
	 * @throws EntryCompareMDIException
	 * @throws MenuCompareMDIException
	 */
	public void readMdiOfOrderedModules() throws XmlException, IOException, BaseSetupException, MenuCompareMDIException, EntryCompareMDIException {
		SetupDocument setup = null;
		for (final BaseSetup bs1 : orderedDependingModules) {
			try {
				setup = bs1.getSetupDocument();
				if (bs1.hasMdi(setup)) {
					LastMdiModu = bs1.getVersionInfo().getModulName();
				}
			} catch (final BaseSetupException e) {
				throw new BaseSetupException(e.getMessage() + setup.getClass().getName());
			}
		}
		log(MessageFormat.format("Das letzte Modul mit MDI-Code ist: {0}", LastMdiModu), true);
		for (final BaseSetup bs : orderedDependingModules) {
			try {
				setup = bs.getSetupDocument();
				log(MessageFormat.format("Aus der Setup.xml Datei wird der mdi-Code ausgelesen Modul:{0}_{1}", bs.getVersionInfo().getModulName(),
						bs.getVersionInfo().toString()));
				bs.readMdi(setup);
			} catch (final EntryCompareMDIException en) {
				throw en;

			} catch (final BaseSetupException e) {
				throw new BaseSetupException(e.getMessage() + setup.getClass().getName());
			}
		}
	}

	/**
	 * liest die orderedDependingModules liste aus und führt für jedes modul die Setup.xml aus bzw liest den Mdi-Code
	 *
	 * @throws XmlException
	 * @throws IOException
	 * @throws BaseSetupException
	 */
	// public void readSetupInformation() throws XmlException, IOException,
	// BaseSetupException {
	// SetupDocument setup = null;
	//
	// // BaseSetup[] a = orderedDependingModules.toArray(new BaseSetup[0]);
	//
	// System.out.println("readSetupInformation Anzahl der orderedDependingModules: "
	// + orderedDependingModules.size());
	// for (BaseSetup bs : orderedDependingModules) {
	// try {
	// XMLForms xmlforms = new XMLForms();
	// ModuleInformation modulinfo = new ModuleInformation();
	// setup = bs.getSetupDocument();
	// xmlforms.setBasesetup(bs);
	// xmlforms.setSetup(setup);
	// xmlforms.ReadoutXMl();
	// } catch (BaseSetupException e) {
	// throw new BaseSetupException(e.getMessage() +
	// setup.getClass().getName());
	// }
	// }
	// }

	/**
	 * liest die orderedDependingModules liste aus und führt für jedes modul die Setup.xml aus bzw liest den Mdi-Code
	 *
	 * @throws XmlException
	 * @throws IOException
	 * @throws BaseSetupException
	 */
	public void execjava(final ExecuteJavaType execjava) throws XmlException, IOException, BaseSetupException, ClassNotFoundException {
		SetupDocument setup = null;
		// BaseSetup[] a = orderedDependingModules.toArray(new BaseSetup[0]);

		for (final BaseSetup bs : orderedDependingModules) {
			try {
				bs.executejava = execjava;
				setup = bs.getSetupDocument();

				try {
					bs.readjavacall(setup, execjava);
				} catch (final InstantiationException e) {
					log(MessageFormat.format("InstantiationException: {0}", e.getMessage()), true);
					e.printStackTrace();
				} catch (final IllegalAccessException e) {
					log(MessageFormat.format("IllegalAccessException: {0}", e.getMessage()), true);
					e.printStackTrace();
				}
			} catch (final BaseSetupException e) {
				throw new BaseSetupException(e.getMessage() + setup.getClass().getName());
			}
		}
	}

	/**
	 * Übergeben wird ein SetupDocument. Es wird der java-call Teil ausgelesen und aufgeführt.
	 *
	 * @param setup    ch.minova.core.install.SetupDocument
	 * @param execjava
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private void readjavacall(final SetupDocument setup, final ExecuteJavaType execjava) throws InstantiationException, IllegalAccessException {
		final ExecuteJava[] ej = setup.getSetup().getExecuteJavaArray();
		String systemenum = null;
		ch.minova.core.install.ExecuteJavaDocument.ExecuteJava.Parameter[] para = null;
		if (ej != null) {
			for (final ExecuteJava javac : ej) {
				para = null;
				Object obj = null;
				systemenum = getEnumJavaExecute(execjava);
				if (javac.getExecuteAfter().toString().equalsIgnoreCase(systemenum)) {
					try {
						obj = Class.forName(javac.getClassname()).newInstance();
					} catch (final ClassNotFoundException e) {
						// log(MessageFormat.format("Fehler beim Ausführen des Java-Codes: \n Die Klasse: {0} wurde nicht gefunden!",
						// javac.getClassname()),true);
						throw new RuntimeException(
								MessageFormat.format("Fehler beim Ausfuehren des Java-Codes: \n Die Klasse: {0} wurde nicht gefunden!", javac.getClassname()));
					}
					String pname = null;
					String pvalue = null;
					if (obj instanceof IInstaller) {
						para = javac.getParameterArray();
						if (para != null) {
							for (int i = 0; i < para.length; i++) {
								File dirprogramfile;
								if (parameter.containsKey("basedir")) {
									dirprogramfile = new File(parameter.getProperty("basedir") + "Program Files");
								} else {
									dirprogramfile = new File("../Program Files");
								}
								if (!dirprogramfile.exists()) {
									dirprogramfile.mkdirs();
								}
								pname = replacestring(para[i].getName());
								pvalue = replacestring(para[i].getValue());
								((IInstaller) obj).setParameter(pname, pvalue);
								log(MessageFormat.format("Aufruf: {0}({1},{2})", javac.getClassname(), pname, pvalue), true);
							}
						}
					} else {
						continue;
					}
					((IInstaller) obj).execute();
				}
			}
		}
	}

	/**
	 * Gibt den String zurück der mit dem Executeafter-Paramter aus der Setup.xml Datei vergleicht werden kann. executejava
	 *
	 * @param execjava
	 * @return string
	 */
	private String getEnumJavaExecute(final ExecuteJavaType execjava) {
		switch (this.executejava) {
		case COPYFILES:
			return "copy-files";
		case UPDATEDATABASE:
			return "update-database";
		case INSTALLSERVICE:
			return "install-service";
		case UPDATESCHEMA:
			return "update-schema";
		case WRITEMDI:
			return "write-mdi";
		case WRITEXBS:
			return "write-xbs";
		default:
			return null;
		}
	}

	/**
	 * Überprüft einen angegebenen String auf einen zu eretztenden Wert und gibt diesen wieder zurück Wenn
	 *
	 * @param appshort true steht für die Ausgabe eines Pfades
	 * @param name
	 * @return
	 */
	private String replacestring(final String value) {
		// Rückgabe des Pfades zur Applikation
		if (value.contains("${application}")) {
			File dirprogramfile;
			if (parameter.containsKey("basedir")) {
				dirprogramfile = new File(parameter.getProperty("basedir") + "Program Files");
			} else {
				dirprogramfile = new File("../Program Files/");
			}
			if (!dirprogramfile.exists()) {
				dirprogramfile.mkdirs();
			} else {
				log(MessageFormat.format("Directory besteht: {0}", dirprogramfile.getAbsolutePath()));
			}
			/*
			 * jetzt befindet man sich in ../Program Files/
			 */
			final File destFile = new File(dirprogramfile + "/" + value);
			return destFile.getAbsolutePath().replace("${application}", getAppNameShort().toString());
		} else if (value.contains("${service}")) {
			if (parameter.getProperty("service") != null) {
				return value.replace("${service}", parameter.getProperty("service").toString());
			} else {
				return null;
			}
		} else if (value.contains("${customer-id}")) {
			if (parameter.getProperty("c") != null) {
				return value.replace("${customer-id}", parameter.getProperty("c").toString());
			} else {
				return "";
			}
		} else if (value.contains("${app-name}")) {
			return value.replace("${app-name}", getAppNameShort().toString());
		} else if (value.contains("${application-long}")) {
			return value.replace("${application-long}", getAppName().toString());
		}
		return value;
	}

	private void readTabelsOfOrderedModules() throws XmlException, IOException, BaseSetupException, SQLException, ModuleNotFoundException {
		checkConnection(null);
		final Connection con = this.connection;
		final BaseSetup[] a = orderedDependingModules.toArray(new BaseSetup[0]);

		for (final BaseSetup bs : a) {
			try {
				log(MessageFormat.format("Module: {1}, {0}", bs.getVersionInfo(), bs.getVersionInfo().getModulName()), true);
				bs.readSchema();
				if (bs.getSetupDocument().getSetup().getSchema() == null) {
					continue;
				}
				bs.readoutSchema(con);
			} catch (final BaseSetupException e) {
				log(MessageFormat.format("Error BaseSetupException: {0}", e.getMessage()));
				throw e;
			} catch (final ModuleNotFoundException m) {
				log(MessageFormat.format("Error ModuleNotFoundException: {0}", m.getMessage()));
				throw m;
			} catch (final RuntimeException rm) {
				log(MessageFormat.format("Error ModuleNotFoundException: {0}", rm.getMessage()));
				throw rm;
			}
		}
	}

	private void readTabelsOfOrderedModulesLogDB() throws XmlException, IOException, BaseSetupException, SQLException, ModuleNotFoundException {
		checkConnection(null);
		final Connection con = this.connection;
		final BaseSetup[] a = orderedDependingModules.toArray(new BaseSetup[0]);

		for (final BaseSetup bs : a) {
			try {
				log(MessageFormat.format("Module: {1}, {0}", bs.getVersionInfo(), bs.getVersionInfo().getModulName()), true);
				bs.readSchema();
				if (bs.getSetupDocument().getSetup().getSchema() == null) {
					continue;
				}
				bs.readoutSchemaCreateLogDB(con);
			} catch (final BaseSetupException e) {
				log(MessageFormat.format("Error BaseSetupException: {0}", e.getMessage()));
				throw e;
			} catch (final ModuleNotFoundException m) {
				log(MessageFormat.format("Error ModuleNotFoundException: {0}", m.getMessage()));
				throw m;
			} catch (final RuntimeException rm) {
				log(MessageFormat.format("Error ModuleNotFoundException: {0}", rm.getMessage()));
				throw rm;
			}
		}
	}

	private void readTabelsOfOrderedModulesCreate() throws XmlException, IOException, BaseSetupException, SQLException, ModuleNotFoundException {
		checkConnection(null);
		final Connection con = this.connection;
		/*
		 * String catalogname = connectioncheck(); log("catalogName = " + catalogname); connection.setCatalog(catalogname);
		 */
		final BaseSetup[] a = orderedDependingModules.toArray(new BaseSetup[0]);

		for (final BaseSetup bs : a) {
			try {
				log(MessageFormat.format("Module: {1}, {0}", bs.getVersionInfo(), bs.getVersionInfo().getModulName()), true);
				bs.readSchema();
				if (bs.getSetupDocument().getSetup().getSchema() == null) {
					continue;
				}
				bs.readoutSchemaCreate(con);
			} catch (final BaseSetupException e) {
				log(MessageFormat.format("Error BaseSetupException: {0}", e.getMessage()));
				throw e;
			} catch (final ModuleNotFoundException m) {
				log(MessageFormat.format("Error ModuleNotFoundException: {0}", m.getMessage()));
				throw m;
			} catch (final RuntimeException rm) {
				log(MessageFormat.format("Error ModuleNotFoundException: {0}", rm.getMessage()));
				throw rm;
			}
		}
	}

	private void readTabelsOfOrderedModulesCreateLogDB() throws XmlException, IOException, BaseSetupException, SQLException, ModuleNotFoundException {
		checkConnection(null);
		final Connection con = this.connection;
		/*
		 * String catalogname = connectioncheck(); log("catalogName = " + catalogname); connection.setCatalog(catalogname);
		 */
		final BaseSetup[] a = orderedDependingModules.toArray(new BaseSetup[0]);

		for (final BaseSetup bs : a) {
			try {
				log(MessageFormat.format("Module: {1}, {0}", bs.getVersionInfo(), bs.getVersionInfo().getModulName()), true);
				bs.readSchema();
				if (bs.getSetupDocument().getSetup().getSchema() == null) {
					continue;
				}
				bs.readoutSchemaCreateLogDB(con);
			} catch (final BaseSetupException e) {
				log(MessageFormat.format("Error BaseSetupException: {0}", e.getMessage()));
				throw e;
			} catch (final ModuleNotFoundException m) {
				log(MessageFormat.format("Error ModuleNotFoundException: {0}", m.getMessage()));
				throw m;
			} catch (final RuntimeException rm) {
				log(MessageFormat.format("Error ModuleNotFoundException: {0}", rm.getMessage()));
				throw rm;
			}
		}
	}

	private boolean readoutSchemaCreateLogDB(final Connection con) throws org.apache.xmlbeans.XmlException, IOException, BaseSetupException {
		checktVersion10(con, Optional.empty());
		final SqlDatabase sqldatabase = new SqlDatabase();
		XmlDatabaseTable xmlTable = null;
		SqlDatabaseTable sqlTable = null;
		String sqlCode = null;

		try {
			sqldatabase.readDataBase(this.connection);
			log(MessageFormat.format("Tabellen für LogDB werden neu erstellt. \n", ""), true);
			for (int i = 0; i < tablevector.size(); i++) {
				if (tablevector.get(i).getType().equalsIgnoreCase("table")) {
					if (!tablevector.get(i).getName().startsWith("prt") && !tablevector.get(i).getName().startsWith("TMP")
							&& !tablevector.get(i).getName().startsWith("tmp")) {
						sqlTable = SqlDatabaseTable.getTable(tablevector.get(i).getName());
						log(MessageFormat.format("Tabelle {0} aus der DB!", tablevector.get(i)));
						final InputStream is = readFromJarFileToInputStream(getVersionInfo().getModulName(), "tables",
								tablevector.get(i).getName() + ".table.xml");
						xmlTable = new XmlDatabaseTable(is, getCollation());
						is.close();
						sqlCode = generateUpsdateScriptTable(sqlTable, xmlTable, true);
						log(MessageFormat.format("SQLCODE für {0} sqlCode = {1}", xmlTable.getName(), sqlCode));
						// Tabelle wird neu erstellt falls diese nicht vorhanden
						if (sqlCode != null) {
							this.connection.createStatement().execute(sqlCode);
							log(MessageFormat.format("Die Tabelle {0} wurde erstellt", xmlTable.getName()), true);
						}
					}
				}
			}
			for (int i = 0; i < tablevector.size(); i++) {
				if (tablevector.get(i).getType().equalsIgnoreCase("table")) {
					if (!tablevector.get(i).getName().startsWith("prt") && !tablevector.get(i).getName().startsWith("TMP")
							&& !tablevector.get(i).getName().startsWith("tmp")) {
						sqlTable = SqlDatabaseTable.getTable(tablevector.get(i).getName());
						final InputStream is = readFromJarFileToInputStream(getVersionInfo().getModulName(), "tables",
								tablevector.get(i).getName() + ".table.xml");
						xmlTable = new XmlDatabaseTable(is, getCollation());
						is.close();
						sqlCode = generateProcedureScriptLogTable(xmlTable);
						if (sqlCode != null) {
							runScript(sqlCode, "xpLog" + xmlTable.getName(), null, Type.PROCEDURE, this.connection);
						}
					}
				}
			}
		} catch (final IOException e) {
			log(MessageFormat.format("IOException create tables \n {0}", e.getMessage()), true);
			throw new RuntimeException(e.getMessage());
		} catch (final org.apache.xmlbeans.XmlException e) {
			log(MessageFormat.format("Fehler beim einlesen der Tabelle: {0}", xmlTable.getName()), true);
			throw new RuntimeException(e.getMessage());
		} catch (final Exception e) {
			log(MessageFormat.format("Exception create tables \n {0}", e.getMessage()), true);
			throw new RuntimeException(e.getMessage());
		}
		return false;
	}

	/**
	 * Diese Methode erstellt den code für die Prozeuren die auf der Log-DB hinterlegt werden. Für jede Tabelle eine Prozedur.
	 *
	 * @param xmlTable
	 * @return String (Prozedur code)
	 */
	private String generateProcedureScriptLogTable(final XmlDatabaseTable xmlTable) {
		// Wir erstellen die Prozedur neu
		final StringWriter sw = new StringWriter();
		sw.write("alter procedure xpLog" + xmlTable.getName() + " as insert into " + xmlTable.getName() + "(");
		String datadbcolums = "";
		String logdbcolums = "";
		boolean firsttime = true;
		if (xmlTable.getPrimaryKeyConstraint() == null) {
			for (final XmlDatabaseColumn xmlc : xmlTable.getColumnVector()) {
				if (!firsttime) {
					logdbcolums += ", ";
					datadbcolums += ", ";
				}
				if (xmlc.getColumnName().equalsIgnoreCase("KeyLong")) {
					// wenn der Key genau der Key ist der auch als PK eingetragen wurden wird dieser dann als XXX_Originbal eingetragen
					logdbcolums += "[" + xmlc.getColumnName() + "_Original]";
					datadbcolums += "[" + xmlc.getColumnName() + "]";
					firsttime = false;
				} else {
					logdbcolums += "[" + xmlc.getColumnName() + "]";
					datadbcolums += "[" + xmlc.getColumnName() + "]";
					firsttime = false;
				}
			}
		} else {
			for (final String pkname : xmlTable.getPrimaryKeyConstraint().getColumnNames()) {
				for (final XmlDatabaseColumn xmlc : xmlTable.getColumnVector()) {
					if (!firsttime) {
						logdbcolums += ", ";
						datadbcolums += ", ";
					}
					if (pkname.equalsIgnoreCase(xmlc.getColumnName())) {
						// wenn der Key genau der Key ist der auch als PK eingetragen wurden wird dieser dann als XXX_Originbal eingetragen
						logdbcolums += "[" + xmlc.getColumnName() + "_Original]";
						datadbcolums += "[" + xmlc.getColumnName() + "]";
						firsttime = false;
					} else {
						logdbcolums += "[" + xmlc.getColumnName() + "]";
						datadbcolums += "[" + xmlc.getColumnName() + "]";
						firsttime = false;
					}
				}
			}
		}

		sw.write(logdbcolums);
		sw.write(") select ");
		sw.write(datadbcolums);
		sw.write(" from #" + xmlTable.getName());
		return sw.toString();
	}

	public boolean readoutSchemaCreate(final Connection con) throws org.apache.xmlbeans.XmlException, IOException, BaseSetupException {
		return readoutSchemaCreate(con, Optional.empty(), Optional.empty());
	}

	public boolean readoutSchemaCreate(final Connection con, Optional<Path> tableLibrary, Optional<Path> sqlLibrary) throws org.apache.xmlbeans.XmlException, IOException, BaseSetupException {
		checktVersion10(con, sqlLibrary);
		SqlDatabase sqldatabase = new SqlDatabase();
		XmlDatabaseTable xmlTable = null;
		SqlDatabaseTable sqlTable = null;
		String sqlCode = null;

		try {
			final String PK_UK_FKs = "select CONSTRAINT_NAME as constraintname,COLUMN_NAME as columnname from INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE where TABLE_CATALOG = '";
			final ResultSet rs = this.connection.createStatement().executeQuery(PK_UK_FKs + this.connection.getCatalog() + "'");
			this.mymap = new HashMap<String, String>();
			if (rs.next()) {
				this.mymap.put(rs.getString("constraintname"), rs.getString("constraintname"));
			}
			sqldatabase.readDataBase(this.connection);
			log(MessageFormat.format("Tabellen werden neu erstellt. \n", ""), true);
			for (int i = 0; i < tablevector.size(); i++) {
				if (tablevector.get(i).getType().equalsIgnoreCase("table")) {
					sqlTable = SqlDatabaseTable.getTable(tablevector.get(i).getName());
					log(MessageFormat.format("Tabelle {0} aus der DB!", tablevector.get(i)));
					final InputStream is = readTableXml(tablevector.get(i).getName(), tableLibrary);
					// hier wird die Datei aus der Setup.xml eingelesen und kann
					// mit
					// der ind der Datenbank vorhandenen Datei verglichen
					// werden.
					xmlTable = new XmlDatabaseTable(is, getCollation());
					is.close();
					sqlCode = generateUpsdateScriptTable(sqlTable, xmlTable);
					log(MessageFormat.format("SQLCODE für {0} sqlCode = {1}", xmlTable.getName(), sqlCode));
					// Tabelle wird neu erstellt falls diese nicht vorhanden
					if (sqlCode != null) {
						this.connection.createStatement().execute(sqlCode);
						log(MessageFormat.format("Die Tabelle {0} wurde erstellt", xmlTable.getName()), true);
					}
				}
			}
		} catch (final IOException e) {
			log(MessageFormat.format("IOException create tables \n {0}", e.getMessage()), true);
			throw new RuntimeException(e.getMessage());
		} catch (final org.apache.xmlbeans.XmlException e) {
			log(MessageFormat.format("Fehler beim einlesen der Tabelle: {0}", xmlTable.getName()), true);
			throw new RuntimeException(e.getMessage());
		} catch (final Exception e) {
			log(MessageFormat.format("Exception create tables \n {0}", e.getMessage()), true);
			throw new RuntimeException(e.getMessage());
		}
		sqldatabase = new SqlDatabase();
		// checkConnection(null);
		sqlTable = null;
		sqlCode = null;
		xmlTable = null;
		try {
			sqldatabase.readDataBase(this.connection);
		} catch (final SQLException e1) {
			System.out.println("help me SQLException");
			e1.printStackTrace();
		} catch (final InstantiationException e1) {
			System.out.println("help me InstantiationException");
			e1.printStackTrace();
		} catch (final IllegalAccessException e1) {
			System.out.println("help me IllegalAccessException");
			e1.printStackTrace();
		} catch (final ClassNotFoundException e1) {
			System.out.println("help me ClassNotFoundException");
			e1.printStackTrace();
		}
		// Table constraints für UN und PK
		log(MessageFormat.format("Tableconstrains erstellen (UK/PK):", ""), true);
		for (int i = 0; i < tablevector.size(); i++) {
			try {
				if (tablevector.get(i).getType().equalsIgnoreCase("table")) {
					// Table aus der ausgelesenen Datenbank
					log(MessageFormat.format("Tabelle {0} aus der DB!", tablevector.get(i)));
					sqlTable = SqlDatabaseTable.getTable(tablevector.get(i).getName());
					final InputStream is = readTableXml(tablevector.get(i).getName(), tableLibrary);
					xmlTable = new XmlDatabaseTable(is, getCollation());
					is.close();
					sqlCode = generateUpdateTableConstraintsPK_UK(sqlTable, xmlTable);
					log(MessageFormat.format("Die (UK/PK)_Constraints für {0} bearbeitet", xmlTable.getName()), true);
					if (sqlCode != null) {
						log(sqlCode);
						this.connection.createStatement().execute(sqlCode);
						sqlCode = null;
						log(MessageFormat.format("Die Constraints für {0} wurden ausgeführt", xmlTable.getName()));
					}
				}
			} catch (final Exception e) {
				log(MessageFormat.format("Exception write constrains UK/PK \n {0}", e.getMessage()), true);
				throw new RuntimeException(e.getMessage());
			}
		}
		// Values in die Tabellen einfügen
		log(MessageFormat.format("Values in die Tabellen schreiben", ""), true);
		for (int i = 0; i < tablevector.size(); i++) {
			if (tablevector.get(i).getType().equalsIgnoreCase("table")) {
				// Table aus der ausgelesenen Datenbank
				log(MessageFormat.format("Tabelle {0} aus der DB!", tablevector.get(i)));
				sqlTable = SqlDatabaseTable.getTable(tablevector.get(i).getName());
				try {
					final InputStream is = readTableXml(tablevector.get(i).getName(), tableLibrary);
					xmlTable = new XmlDatabaseTable(is, getCollation());
					is.close();
					sqlCode = xmlTable.generateUpdateValues();
					if (sqlCode != null) {
						log(sqlCode);
						this.connection.createStatement().execute(sqlCode);
						sqlCode = null;
						log(MessageFormat.format("Alle Values wurden für {0} eingepflegt", xmlTable.getName()), true);
					}
				} catch (final Exception e) {
					log(MessageFormat.format("Exception write values \n {0} , - {1}", e.getMessage(), tablevector.get(i).getName()), true);
					throw new RuntimeException(e.getMessage());
				}
			}
		}
		// Table constraints für FK
		log(MessageFormat.format("Tableconstrains erstellen (FK):", ""), true);
		for (int i = 0; i < tablevector.size(); i++) {
			if (tablevector.get(i).getType().equalsIgnoreCase("table")) {
				// Table aus der ausgelesenen Datenbank
				log(MessageFormat.format("Tabelle {0} aus der DB!", tablevector.get(i)));
				sqlTable = SqlDatabaseTable.getTable(tablevector.get(i).getName());
				try {
					final InputStream is = readTableXml(tablevector.get(i).getName(), tableLibrary);
					xmlTable = new XmlDatabaseTable(is, getCollation());
					is.close();
					sqlCode = generateUpdateTableConstraintsFK(sqlTable, xmlTable);
					log(MessageFormat.format("Die FK_Constraints für {0} bearbeitet", xmlTable.getName()), true);

					if (sqlCode != null) {
						log(sqlCode);
						this.connection.createStatement().execute(sqlCode);
						sqlCode = null;
						log(MessageFormat.format("Die Constraints für {0} wurden ausgeführt", xmlTable.getName()));
					}

				} catch (final Exception e) {
					log(MessageFormat.format("Exception write constrains FK \n {0}", e.getMessage()), true);
					log(MessageFormat.format("SQL_CODE: ", sqlCode));
					throw new RuntimeException(e.getMessage());
				}
			}
		}
		return false;
	}

	public void readSQLOfOrderedModules() throws XmlException, IOException, BaseSetupException, SQLException {
		SetupDocument setup = null;

		final BaseSetup[] a = orderedDependingModules.toArray(new BaseSetup[0]);

		for (final BaseSetup bs : a) {
			try {
				setup = bs.getSetupDocument();
				log(MessageFormat.format("Aus der Setup.xml Datei wird der sql-Code ausgelesen Modul:{0}_{1}", bs.getVersionInfo().getModulName(),
						bs.getVersionInfo().toString()));
				bs.checkSql(bs, setup);
			} catch (final BaseSetupException e) {
				e.printStackTrace();
				throw new BaseSetupException(e.getMessage() + setup.getClass().getName());
			} catch (final SQLException e) {
				e.printStackTrace();
				throw new SQLException(e.getMessage() + setup.getClass().getName());
			}
		}
	}

	/**
	 * Überprüfung ob die Tabelle tVersion10 besteht. Ansonsten wird diese erstellt.
	 */
	private void checktVersion10(final Connection con, Optional<Path> sqlLibrary) {
		final String tversion10 = "tVersion10";
		this.connection = con;
		Hashtable<String, TVersion> tVersionHash = getTVersion(this.connection, tversion10);
		if (tVersionHash == null) {
			try {
				final String sqlScript;
				if (sqlLibrary.isPresent()) {
					sqlScript = readSqlScript("initVersionTable", sqlLibrary.get());
				} else {
					sqlScript = readSqlFromJarFileToString("ch.minova.install", sqldialect, "initVersionTable" + ".sql");
				}
				log(MessageFormat.format("Die {0} Tabelle wurde nicht gefunden", tversion10));
				executeSqlScript(sqlScript);
				tVersionHash = getTVersion(this.connection, tversion10);
				if (tVersionHash != null) {
					log(MessageFormat.format("Die {0} Tabelle wurde neu erstellt", tversion10));
				}
			} catch (final IOException e) {
				log(MessageFormat.format("Error IOException: {0}", e.getMessage()));
				e.printStackTrace();
			} catch (final SQLException e) {
				log(MessageFormat.format("Error SQLException: {0}", e.getMessage()));
				e.printStackTrace();
			}
		}
	}

	private void execSqlScripts(final String tablename) throws IOException, SQLException {
		final String sqlScript = readSqlFromJarFileToString(getVersionInfo().getModulName(), sqldialect, tablename + ".sql");
		log(MessageFormat.format("{0} Ausführen des Scripts: \n {1}", tablename, sqlScript));
		executeSqlScript(sqlScript);
	}

	private String getCollation() {
		try {
			ResultSet rs;

			// rs =
			// connection.createStatement().executeQuery("select SERVERPROPERTY('Collation') as collation");
			rs = this.connection.createStatement().executeQuery("select DATABASEPROPERTYEX('" + this.connection.getCatalog() + "','collation') as collation");
			rs.next();
			return rs.getString("collation");
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "null";
	}

	public boolean readoutSchema(final Connection con) throws org.apache.xmlbeans.XmlException, IOException, BaseSetupException, SQLException {
		return readoutSchema(con, Optional.empty(), Optional.empty());
	}

	private InputStream readTableXml(String tableName, Optional<Path> tableLibrary) {
		if (tableLibrary.isEmpty()) {
			return readFromJarFileToInputStream(getVersionInfo().getModulName(), "tables", tableName + ".table.xml");
		} else {
			try {
				final Optional<Path> tableXml = Files.walk(tableLibrary.get())
						.map(path -> {
							// TODO Einheitliche XML-Namen verwenden.
							if (Files.isRegularFile(path) && path.getFileName().toString().equals(tableName + ".table.xml") ||path.getFileName().toString().equals(tableName + ".xml")) {
								return Optional.of(path);
							}
							return Optional.<Path>empty();
						})//
						.filter(path -> !path.isEmpty())
						.map(path -> path.get())
						.findFirst();
				if (tableXml.isEmpty()) {
					throw new RuntimeException("Table xml " + tableName + "not found.");
				}
				return Files.newInputStream(tableXml.get());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Diese methode liest die Tabelle aus der XML-Datei aus. Danach wird sie mit der Tabelle , falls diese bereits in der DB besteht, verglichen. Ansonsten
	 * wird die Tabelle neu angelegt. Im nächsten Schritt werden die Constraints der Tabelle vergliochen. Dabei werden zunächst alle PK und UK_Constraints
	 * bearbeitet und dann die FK_Constraints.
	 *
	 * @return
	 * @throws BaseSetupException
	 * @throws IOException
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public boolean readoutSchema(final Connection con, Optional<Path> tableLibrary, Optional<Path> sqlLibrary)
			throws org.apache.xmlbeans.XmlException, IOException, BaseSetupException, SQLException {
		checktVersion10(con, sqlLibrary);
		SqlDatabase sqldatabase = new SqlDatabase();
		XmlDatabaseTable xmlTable = null;
		SqlDatabaseTable sqlTable = null;
		String sqlCode = null;
		SetupDocument doc = getSetupDocument();
		if (doc.getSetup().getSchema() == null) {
			return false;
		}
		try {
			final String PK_UK_FKs = "select CONSTRAINT_NAME as constraintname,COLUMN_NAME as columnname from INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE where TABLE_CATALOG = '";
			final ResultSet rs = this.connection.createStatement().executeQuery(PK_UK_FKs + this.connection.getCatalog() + "'");
			this.mymap = new HashMap<String, String>();
			if (rs.next()) {
				this.mymap.put(rs.getString("constraintname"), rs.getString("columnname"));
			}
			sqldatabase.readDataBase(this.connection);
			log(MessageFormat.format("Tabellenspalten und Tabellen werden aktualisiert. \n", ""), true);
			for (int i = 0; i < tablevector.size(); i++) {
				// Table aus der ausgelesenen Datenbank
				if (tablevector.get(i).getType().equalsIgnoreCase("script")) {
					// ist das der gleiche Name
					if (doc.getSetup().getSchema().getTableschemaArray(i).getName().equalsIgnoreCase(tablevector.get(i).getName())) {
						// soll das script vorher ausgeführt werden
						if (doc.getSetup().getSchema().getTableschemaArray(i).getExecute().toString().equalsIgnoreCase("before")) {
							execSqlScripts(tablevector.get(i).getName());
						}
					}
				} else {
					sqlTable = SqlDatabaseTable.getTable(tablevector.get(i).getName());
					log(MessageFormat.format("{0} - Tabelle wird ueberprueft", tablevector.get(i).getName().toString()), true);

					final InputStream is = readTableXml(tablevector.get(i).getName(), tableLibrary);
					// hier wird die Datei aus der Setup.xml eingelesen und kann
					// mit
					// der in der Datenbank vorhandenen Datei verglichen
					// werden.

					xmlTable = new XmlDatabaseTable(is, getCollation());
					is.close();
					sqlCode = generateUpsdateScriptTable(sqlTable, xmlTable);
					// Tabelle wird neu erstellt falls diese nicht vorhanden
					if (sqlCode != null) {
						this.connection.createStatement().execute(sqlCode);
						log(MessageFormat.format("Tabelle {0} wurde mit Sql-Code: {1}", tablevector.get(i).getName(), sqlCode), true);
					}
				}
			}
		} catch (final RuntimeException re) {
			throw re;
		} catch (final IOException e) {
			throw new RuntimeException(e.getMessage());
		} catch (final org.apache.xmlbeans.XmlException e) {
			System.out.println("Fehler beim einlesen der Tabelle:" + xmlTable.getName());
			throw new RuntimeException(e.getMessage());
		} catch (final Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		sqldatabase = new SqlDatabase();
		sqlTable = null;
		sqlCode = null;
		xmlTable = null;
		try {
			sqldatabase.readDataBase(this.connection);
		} catch (final SQLException e1) {
			log(MessageFormat.format("Die Tabelle erstellt SQLException \n {0}", e1.getMessage()), true);
		} catch (final InstantiationException e1) {
			log(MessageFormat.format("Die Tabelle erstellt InstantiationException \n {0}", e1.getMessage()), true);
		} catch (final IllegalAccessException e1) {
			log(MessageFormat.format("Die Tabelle erstellt IllegalAccessException \n {0}", e1.getMessage()), true);
		} catch (final ClassNotFoundException e1) {
			log(MessageFormat.format("Die Tabelle erstellt ClassNotFoundException \n {0}", e1.getMessage()), true);
		}
		// Table constraints für UN und PK
		log(MessageFormat.format("Tableconstrains fuer UN und PK werden erstellt/ueberprueft. Einen Moment bitte \n", ""), true);

		for (int i = 0; i < tablevector.size(); i++) {
			// if((i*10)%getcountPercent(tablevector.size()*10)==0){
			// log(MessageFormat.format("{0} %", counter++),true);
			// }
			try {
				if (tablevector.get(i).getType().equalsIgnoreCase("table")) {
					// Table aus der ausgelesenen Datenbank
					log(MessageFormat.format("Tabelle {0} aus der DB!", tablevector.get(i)));
					sqlTable = SqlDatabaseTable.getTable(tablevector.get(i).getName());
					final InputStream is = readTableXml(tablevector.get(i).getName(), tableLibrary);
					xmlTable = new XmlDatabaseTable(is, getCollation());
					is.close();
					sqlCode = generateUpdateTableConstraintsPK_UK(sqlTable, xmlTable);
					// log(MessageFormat.format("Die UK_/PK_Constraints für {0} bearbeitet",
					// xmlTable.getName()), true);
					if (sqlCode != null) {
						log(sqlCode);
						this.connection.createStatement().execute(sqlCode);
						sqlCode = null;
						log(MessageFormat.format("Die UK_/PK_Constraints für {0} wurden ausgeführt", xmlTable.getName()), true);
					}
				}
			} catch (final Exception e) {
				log(MessageFormat.format("Exception in generateUpdatescript for UN/PK: \n" + e.getMessage(), ""), true);
				throw new RuntimeException(e.getMessage());
			}
		}
		// Table constraints für FK
		log(MessageFormat.format("Tableconstrains fuer FK wird erstellt/ueberprueft. Einen Moment bitte. \n.", ""), true);
		for (int i = 0; i < tablevector.size(); i++) {
			// if((i*10)%getcountPercent(tablevector.size()*10)==0){
			// log(MessageFormat.format("{0} %", counter++),true);
			// }
			// Table aus der ausgelesenen Datenbank
			if (tablevector.get(i).getType().equalsIgnoreCase("table")) {
				// Table aus der ausgelesenen Datenbank
				log(MessageFormat.format("Tabelle {0} aus der DB!", tablevector.get(i)));

				sqlTable = SqlDatabaseTable.getTable(tablevector.get(i).getName());

				try {
					final InputStream is = readTableXml(tablevector.get(i).getName(), tableLibrary);
					xmlTable = new XmlDatabaseTable(is, getCollation());
					is.close();
					sqlCode = generateUpdateTableConstraintsFK(sqlTable, xmlTable);
					if (sqlCode != null) {
						log(sqlCode);
						this.connection.createStatement().execute(sqlCode);
						sqlCode = null;
						log(MessageFormat.format("Die FK_Constraints für {0} wurden ausgeführt.", xmlTable.getName()), true);
					} else {
						log(MessageFormat.format("Die FK_Constraints für {0} bearbeitet", xmlTable.getName()), true);
					}

				} catch (final Exception e) {
					log(MessageFormat.format("Exception in generateUpdatescript for FK: \n" + e.getMessage(), ""), true);
					throw new RuntimeException(e.getMessage());
				}
			}
		}
		// Eintragen der Values in die Tabellen
		log(MessageFormat.format("Tabelle werden mit  Values gefuellt. \n", ""), true);
		for (int i = 0; i < tablevector.size(); i++) {
			if (tablevector.get(i).getType().equalsIgnoreCase("table")) {
				// Table aus der ausgelesenen Datenbank
				log(MessageFormat.format("Tabelle {0} aus der DB!", tablevector.get(i).getName()));
				// if(tablevector.get(i)){
				// execSqlScripts(xmlTable.getName());
				// }
				sqlTable = SqlDatabaseTable.getTable(tablevector.get(i).getName());
				try {
					final InputStream is = readTableXml(tablevector.get(i).getName(), tableLibrary);
					xmlTable = new XmlDatabaseTable(is, getCollation());
					is.close();
					sqlCode = xmlTable.generateUpdateValues();
					if (sqlCode != null) {
						log(sqlCode);
						this.connection.createStatement().execute(sqlCode);
						sqlCode = null;
						log(MessageFormat.format("Alle Values wurden für {0} eingepflegt", xmlTable.getName()));
					}
				} catch (final Exception e) {
					log(MessageFormat.format("Exception in generateUpdatescript for write Values: \n" + e.getMessage(), ""), true);
					throw new RuntimeException(e.getMessage());
				}
			}
		}

		// Ausführen aller Prozeduren mit after
		log(MessageFormat.format("Prozeduren werden auf die Tabelle ausgefuehrt. \n", ""), true);
		for (int i = 0; i < tablevector.size(); i++) {
			if (tablevector.get(i).getType().equalsIgnoreCase("script")) {
				doc = getSetupDocument();
				// ist das der gleiche Name
				if (doc.getSetup().getSchema().getTableschemaArray(i).getName().equalsIgnoreCase(tablevector.get(i).getName())) {
					// soll das script vorher ausgeführt werden
					if (doc.getSetup().getSchema().getTableschemaArray(i).getExecute().toString().equalsIgnoreCase("after")) {
						try {
							execSqlScripts(tablevector.get(i).getName());
						} catch (final SQLException e) {
							log(MessageFormat.format("SQLException execute Sqlprocedure: \n" + e.getMessage(), ""), true);
							throw new RuntimeException(e.getMessage());
						}
						i++;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Diese Methode überprüft die Verbindung und gibt nützliche Daten des Objektes aus.
	 */
	private boolean connectioncheck() {
		int key = 0;
		String zeile = null;
		while (key == 0) {
			try {
				log(MessageFormat.format("Aktuelle DatenbankPfad: {0}", this.connection.getCatalog()), true);
				System.out.println(MessageFormat.format("Soll mit der angebenen DB: - {0} - fortgefahren werden? YES(y)(enter) NO(n)", connectiondb));
				final BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
				try {
					zeile = console.readLine();
				} catch (final Exception e) {
					e.printStackTrace();
					return false;
				}
				if (zeile.isEmpty()) {
					log(MessageFormat.format("current Database: {0}", this.connection.getCatalog()), true);
					key = 1;
					return true;
				} else if (zeile.equalsIgnoreCase("no") || zeile.equalsIgnoreCase("n")) {
					return false;
				} else if (zeile.equalsIgnoreCase("yes") || zeile.equalsIgnoreCase("y")) {
					log(MessageFormat.format("current Database: {0}", this.connection.getCatalog()), true);
					return true;
				}
			} catch (final SQLException e) {
				System.err.println(e.getMessage());
				key = 0;
				return false;
			} catch (final NullPointerException e) {
				return false;
			}
		}
		return false;
	}

	private String generateUpdateTableConstraintsPK_UK(final SqlDatabaseTable sqlTable, final XmlDatabaseTable xmlTable) throws SQLException {
		return xmlTable.compareConstrainsPK_UK(sqlTable, this.connection, this.mymap);
	}

	private String generateUpdateTableConstraintsFK(final SqlDatabaseTable sqlTable, final XmlDatabaseTable xmlTable) throws SQLException {
		return xmlTable.compareConstrainsFK(sqlTable, this.connection, this.mymap);
	}

	/**
	 * Diese Methode überprüft die existenz der Tabellen auf der DB, ist eine Tabelle nicht vorhanden wird diese angelegt. Das entsprechende UpdateScropt wird
	 * erstellt und als Rückgabeparameter weitergegeben.
	 *
	 * @param sqlTable
	 * @param xmlTable
	 * @return
	 * @throws Exception
	 */
	private String generateUpsdateScriptTable(final SqlDatabaseTable sqlTable, final XmlDatabaseTable xmlTable, final boolean LogDB) throws Exception {
		if (LogDB == false) {
			return generateUpsdateScriptTable(sqlTable, xmlTable);
		} else {
			if (sqlTable == null && xmlTable != null) {
				log(MessageFormat.format("Tabelle -{0}- wird erstellt", xmlTable.getName()), true);
				// Wir erstellen die Tabelle neu
				final StringWriter sw = new StringWriter();
				xmlTable.saveSQLLogDB(sw, LogDB);
				return sw.toString();
			} else if (sqlTable != null && xmlTable != null) {
				log(MessageFormat.format("Tabellen vergleichen", ""));
				// wir vergleichen die beiden Tabellen
				if (compareschema(xmlTable, sqlTable, LogDB)) {
					log(MessageFormat.format("Tabellen sind gleich", ""));
					return null;
				}
			} else if (sqlTable == null && xmlTable == null) {
				log(MessageFormat.format("Table nicht in DB vorhanden! XML Table nicht vorhanden", ""), true);
			} else {
				log(MessageFormat.format("Table in DB vorhanden! ERROR", ""), true);
				throw new Exception();
			}
		}
		return null;
	}

	/**
	 * Diese Methode überprüft die existenz der Tabellen auf der DB, ist eine Tabelle nicht vorhanden wird diese angelegt. Das entsprechende UpdateScropt wird
	 * erstellt und als Rückgabeparameter weitergegeben.
	 *
	 * @param sqlTable
	 * @param xmlTable
	 * @return
	 * @throws Exception
	 */
	private String generateUpsdateScriptTable(final SqlDatabaseTable sqlTable, final XmlDatabaseTable xmlTable) throws Exception {
		if (sqlTable == null && xmlTable != null) {
			log(MessageFormat.format("Tabelle -{0}- wird erstellt", xmlTable.getName()), true);
			// Wir erstellen die Tabelle neu
			final StringWriter sw = new StringWriter();
			xmlTable.saveSQL(sw);
			return sw.toString();
		} else if (sqlTable != null && xmlTable != null) {
			log(MessageFormat.format("Tabellen vergleichen", ""));
			// wir vergleichen die beiden Tabellen
			if (compareschema(xmlTable, sqlTable)) {
				log(MessageFormat.format("Tabellen sind gleich", ""));
				return null;
			}
		} else if (sqlTable == null && xmlTable == null) {
			log(MessageFormat.format("Table nicht in DB vorhanden! XML Table nicht vorhanden", ""), true);
		} else {
			log(MessageFormat.format("Table in DB vorhanden! ERROR", ""), true);
			throw new Exception();
		}
		return null;
	}

	/**
	 * Überprüft ob die Tabellen (xml, sql) die gleich Anzahl von Spalten haben, sollte eine Fehlen wird diese aus dem xml-Table Objekt entnommen und in die
	 * Tabelle auf der DB gespielt.
	 *
	 * @param xdt
	 * @param sdt
	 * @return
	 * @throws SQLException
	 */
	private boolean compareschema(final XmlDatabaseTable xdt, final SqlDatabaseTable sdt) throws SQLException {
		for (final XmlDatabaseColumn xdc : xdt.getColumnVector()) {
			log(MessageFormat.format("Spalte {1} von Tabelle: {0}", xdt.getName(), xdc.getColumnName()));
			final String sql = xdc.getSqlUpdateCode(sdt.getColumn(xdc.getColumnName()), sdt, this.connection, false);
			if (sql != null) {
				try {
					log(MessageFormat.format("Einspielen der neuen Spalte mit: {0} ", sql));
					this.connection.createStatement().execute(sql);
					log(MessageFormat.format("Einspielen der neuen Spalte: {0} in die Tabelle {1} war erfolgreich ", xdc.getColumnName(), xdt.getName()));
				} catch (final SQLException e) {
					log(MessageFormat.format("Error SQLException: {0}", e.getMessage()), true);
				}
			}
		}
		return true;
	}

	/**
	 * Überprüft ob die Tabellen (xml, sql) die gleich Anzahl von Spalten haben, sollte eine Fehlen wird diese aus dem xml-Table Objekt entnommen und in die
	 * Tabelle auf der DB gespielt.
	 *
	 * @param xdt
	 * @param sdt
	 * @return
	 * @throws SQLException
	 */
	private boolean compareschema(final XmlDatabaseTable xdt, final SqlDatabaseTable sdt, final boolean LogDB) throws SQLException {
		if (LogDB == false) {
			return compareschema(xdt, sdt);
		} else {
			for (final XmlDatabaseColumn xdc : xdt.getColumnVector()) {
				log(MessageFormat.format("Spalte {1} von Tabelle: {0}", xdt.getName(), xdc.getColumnName()));
				final String sql = xdc.getSqlUpdateCode(sdt.getColumn(xdc.getColumnName()), sdt, this.connection, LogDB);
				if (sql != null) {
					try {
						log(MessageFormat.format("Einspielen der neuen Spalte mit: {0} ", sql));
						this.connection.createStatement().execute(sql);
						log(MessageFormat.format("Einspielen der neuen Spalte: {0} in die Tabelle {1} war erfolgreich ", xdc.getColumnName(), xdt.getName()));
					} catch (final SQLException e) {
						log(MessageFormat.format("Error SQLException: {0}", e.getMessage()), true);
					}
				}
			}
		}
		return true;
	}

	/**
	 * Diese Methode liest die gesamten Tables aus. Tables werden in der Setup.xml Datei mit aufgelistet. Sie stehen unter dem Namen: <schema> und werden unter
	 * in einer HashTable aufgelistst.
	 *
	 * @return
	 * @throws XmlException
	 * @throws IOException
	 * @throws BaseSetupException
	 * @throws ModuleNotFoundException
	 * @throws SQLException
	 */
	public boolean readSchema() throws XmlException, IOException, BaseSetupException, ModuleNotFoundException, SQLException {
		final SetupDocument doc = getSetupDocument();
		if (doc.getSetup().getSchema() != null) {
			final Tableschema[] tables = doc.getSetup().getSchema().getTableschemaArray();
			for (int i = 0; i < tables.length; i++) {
				if (!hashtables.containsKey(tables[i])) {
					tablevector.add(new TableVector(tables[i].getName(), tables[i].getType()));
					hashtables.put(tables[i].getName().toLowerCase(), tables[i].getName());
					log(MessageFormat.format("Table: {0}", tables[i].getName()));
				}
			}
			return true;
		}
		return false;
	}

	private boolean hasMdi(final SetupDocument doc) {
		return (doc.getSetup().getMdiCode() != null);
	}

	/**
	 * liest das übergebene SetupDocument aus und füllt das globale Objekt mainmdi mit den Daten aus der Setup.xml welches über das SetupDocument erreicht wird.
	 *
	 * @param doc
	 * @return
	 * @throws BaseSetupException
	 * @throws MenuCompareMDIException
	 * @throws EntryCompareMDIException
	 */
	private boolean readMdi(final SetupDocument doc) throws BaseSetupException, MenuCompareMDIException, EntryCompareMDIException {
		ch.minova.install.setup.mdi.Menu menu;
		ch.minova.install.setup.mdi.Menu menu1 = null;
		ch.minova.install.setup.mdi.Entry entry = null;
		ch.minova.install.setup.mdi.Action action = null;
		String text = "";
		String override = "";
		double position = 0;

		final boolean lastmenu = getVersionInfo().getModulName().equalsIgnoreCase(LastMdiModu);
		// hier muss darauf geachtet werden, dass überprüft wird, ob die
		// Setup.xml Datei überhaupt mdi-code besitzt
		if (doc.getSetup().getMdiCode() != null) {
			for (int i = 0; i < doc.getSetup().getMdiCode().getActionArray().length; i++) {
				log(MessageFormat.format("action anlegen: <action: {0}>", doc.getSetup().getMdiCode().getActionArray(i).getAction().toString()));
				action = mainmdi.getAction(doc.getSetup().getMdiCode().getActionArray(i).getAction(), true);
				if (action != null) {
					mainmdi.readAction(doc.getSetup().getMdiCode().getActionArray(i));
				}
			}
			try {
				menu = BaseSetup.mainmdi.getMenu("main");
			} catch (final Exception e) {
				menu = BaseSetup.mainmdi.getMenu("main", true);
			}
			// Änderung auf Menuarray
			if (doc.getSetup().getMdiCode().getMenuArray().length > 0) {
				for (int i = 0; i < doc.getSetup().getMdiCode().getMenuArray().length; i++) {
					if (doc.getSetup().getMdiCode().getMenuArray(i).getText() != null) {
						text = doc.getSetup().getMdiCode().getMenuArray(i).getText();
					} else {
						text = "@" + doc.getSetup().getMdiCode().getMenuArray(i).getId();
					}
					// Position aus dem menu lesen!
					try {
						if (doc.getSetup().getMdiCode().getMenuArray(i).getPosition() != 0) {
							position = doc.getSetup().getMdiCode().getMenuArray(i).getPosition();
						} else {
							position = 0;
						}
						override = doc.getSetup().getMdiCode().getMenuArray(i).getOverride();

					} catch (final Exception E) {
						throw new MenuCompareMDIException(E.getMessage());
					}
					log(MessageFormat.format("menu anlegen: <menu: id= {0} text= {1} position= {2} overreide = {3}>",
							doc.getSetup().getMdiCode().getMenuArray(i).getId(), text, position, override));
					// an dieser Stelle wird entweder ein vorhandenes Menu
					// ausgelesen oder ein noch nicht vorhandenen erstellt.
					menu1 = menu.getMenu(doc.getSetup().getMdiCode().getMenuArray(i).getId(), true);
					if (parameter.getProperty("override").equalsIgnoreCase("true") && override.equalsIgnoreCase("true")) {
						menu1.setPosition(position);
						menu1.setText(text);
						menu1.setOverride(override);
					} else {
						// Wenn die Menuposition 0 ist wird sie immer ersetzt.
						if (doc.getSetup().getMdiCode().getMenuArray(i).getPosition() == 0.0 || menu1.getPosition() == 0) {
							menu1.setPosition(position);
						} else if (menu1.getPosition() != doc.getSetup().getMdiCode().getMenuArray(i).getPosition()
								&& doc.getSetup().getMdiCode().getMenuArray(i).getPosition() != 0.0 && menu1.getPosition() != 0) {
							log(MessageFormat.format("aktuelles Modul: {0} message: {1}", this.versionInfo.getModulName(), MessageFormat.format(
									"Menu: {0}, Position: {1} != {2} (zu übernehmende Position aus Setup.xml)", menu1.getId(), menu1.getPosition(), position)),
									true);

							throw new MenuCompareMDIException(MessageFormat.format("aktuelles Modul: {0} message: {1}", this.versionInfo.getModulName(),
									MessageFormat.format("Menu: {0}, Position: {1} != {2} (zu übernehmende Position aus Setup.xml)", menu1.getId(),
											menu1.getPosition(), position)));
						} else {
							menu1.setPosition(position);
						}
						if (menu1.getText() != null && !menu1.getText().equalsIgnoreCase("") && lastmenu == false) {
							if (!menu1.getText().equalsIgnoreCase(text)) {
								log(MessageFormat.format("aktuelles Modul: {0} message:{1}", this.versionInfo.getModulName(),
										MessageFormat.format("Menu: {0}, text: {1} != {2}", menu1.getId(), menu1.getText(), text)), true);
								throw new MenuCompareMDIException(MessageFormat.format("aktuelles Modul: {0} message:{1}", this.versionInfo.getModulName(),
										MessageFormat.format("Menu: {0}, text: {1} != {2}", menu1.getId(), menu1.getText(), text)));
							}
						} else {
							menu1.setText(text);
						}
						if (menu1.getOverride() != null && !menu1.getOverride().equalsIgnoreCase("") && lastmenu == false) {
							if (!menu1.getOverride().equalsIgnoreCase(override)) {
								log(MessageFormat.format("aktuelles Modul: {0} message:{1}", this.versionInfo.getModulName(),
										MessageFormat.format("Menu: {0}, override: {1} != {2}", menu1.getId(), menu1.getOverride(), override)), true);
								throw new MenuCompareMDIException(MessageFormat.format("aktuelles Modul: {0} message:{1}", this.versionInfo.getModulName(),
										MessageFormat.format("Menu: {0}, override: {1} != {2}", menu1.getId(), menu1.getOverride(), override)));
							}
						} else {
							menu1.setOverride(override);
						}
					}
					if (menu1 != null) {
						try {
							if (getVersionInfo().getModulName().equalsIgnoreCase(LastMdiModu)) {
								menu.readMenu(doc.getSetup().getMdiCode().getMenuArray(i), true);
							} else {
								menu.readMenu(doc.getSetup().getMdiCode().getMenuArray(i), false);
							}
						} catch (final MenuCompareMDIException me) {
							throw new MenuCompareMDIException(
									MessageFormat.format("aktuelles Modul: {0} message:{1}", this.versionInfo.getModulName(), me.getMessage()));
						} catch (final EntryCompareMDIException ee) {
							throw new EntryCompareMDIException(
									MessageFormat.format("aktuelles Modul: {0} message:{1}", this.versionInfo.getModulName(), ee.getMessage()));
						} catch (final Exception e) {
							throw new RuntimeException(e);
						}
					}
				}
			}
			if (doc.getSetup().getMdiCode().getToolbar() != null) {
				override = "";
				position = 0;
				for (int i = 0; i < doc.getSetup().getMdiCode().getToolbar().getEntryArray().length; i++) {
					override = doc.getSetup().getMdiCode().getToolbar().getEntryArray(i).getOverride();
					position = doc.getSetup().getMdiCode().getToolbar().getEntryArray(i).getPosition();
					log(MessageFormat.format("entry anlegen: <entry: id= {0} position = {1} override={2}>",
							doc.getSetup().getMdiCode().getToolbar().getEntryArray(i).getId(),
							doc.getSetup().getMdiCode().getToolbar().getEntryArray(i).getPosition(),
							doc.getSetup().getMdiCode().getToolbar().getEntryArray(i).getOverride()));
					entry = new ch.minova.install.setup.mdi.Entry(doc.getSetup().getMdiCode().getToolbar().getEntryArray(i).getId(),
							doc.getSetup().getMdiCode().getToolbar().getEntryArray(i).getType());
					entry.setOverride(override);
					entry.setPosition(position);

					// if (mainmdi.getToolbar().existsEntry(entry.getId().toLowerCase())) {
					// Werte zuweisen für die Positionen und das Override
					mainmdi.getToolbar().addEntry(entry);
					log(MessageFormat.format("Eintrag in Toolbar: {0}", entry.getId()));
					// }
					if (parameter.getProperty("override").equalsIgnoreCase("true") && override.equalsIgnoreCase("true")) {
						mainmdi.getToolbar().getEntry(entry.getId().toLowerCase()).setOverride(override);
						mainmdi.getToolbar().getEntry(entry.getId().toLowerCase()).setPosition(position);
					}
				}
			}
		}
		return true;
	}

	public void handleSqlOfOrderedModules(final Connection connection2) throws BaseSetupException, XmlException, IOException, SQLException, SQLExeption {
		final BaseSetup[] a = orderedDependingModules.toArray(new BaseSetup[0]);
		final Connection con = connection2;

		for (final BaseSetup bs : a) {
			bs.handleSqlScripts(con);
		}
	}

	public boolean readXbsFile(final String xbsfile) throws XmlException, IOException, BaseSetupException {
		final String basedir = parameter.getProperty("basedir");
		final String xbspath = basedir + "/deploy";
		return readXbsFile(xbspath, xbsfile);
	}

	/**
	 * stellt sicher, dass ein Knoten mit dem angegebenen Name unter dem parent-Knoten existiert<br>
	 * (wenn nicht vorhanden, wird er angelegt)
	 *
	 * @param nodeName     Name des benötigten Knotens
	 * @param parent       parent-Knoten
	 * @param setNull      wenn es einen Knoten ohne Name gibt, benenne den um
	 * @param fixLowerCase korrigiere die Schreibweise auf lower case
	 * @return den gewünschten Knoten
	 */
	private Node ensureNode(final String nodeName, final Node parent, final boolean setNull, final boolean fixLowerCase) {
		Node neededNode = null;
		final Node[] nodes = parent.getNodeArray();
		for (int i = 0; i < nodes.length; i++) {
			if (setNull && nodes[i].getName() == null) {
				// wenn es einen Knoten ohne Name gibt, setze den nodeName
				nodes[i].setName(nodeName);
			} else if (nodes[i].getName().equals(nodeName)) {
				neededNode = nodes[i];
				break;
			} else if (fixLowerCase && nodes[i].getName().equalsIgnoreCase(nodeName)) {
				// wenn ein Knoten wie die nodeName heißt, mache lc draus
				nodes[i].setName(nodeName);
				neededNode = nodes[i];
				break;
			}
		}

		if (neededNode == null) {
			// wenn es den Knoten noch nicht gibt, füg ihn hinzu
			neededNode = parent.addNewNode();
			neededNode.setName(nodeName);
		}

		return neededNode;
	}

	/**
	 * noch mal das selbe wie {@link #ensureNode(String, Node, boolean)}, aber für Root-Node
	 *
	 * @param nodeName Name des benötigten Knotens
	 * @param parent   parent-Knoten
	 * @param setNull  wenn es einen Knoten ohne Name gibt, benenne den um
	 * @return den gewünschten Knoten
	 */
	private Node ensureNode(final String nodeName, final Root parent, final boolean setNull) {
		Node neededNode = null;
		final Node[] nodes = parent.getNodeArray();
		for (int i = 0; i < nodes.length; i++) {
			if (setNull && nodes[i].getName() == null) {
				// wenn es einen Knoten ohne Name gibt, setze den nodeName
				nodes[i].setName(nodeName);
			} else if (nodes[i].getName().equals(nodeName)) {
				neededNode = nodes[i];
				break;
			}
		}

		if (neededNode == null) {
			// wenn es den Knoten noch nicht gibt, füg ihn hinzu
			neededNode = parent.addNewNode();
			neededNode.setName(nodeName);
		}

		return neededNode;
	}

	/**
	 * stellt sicher, dass in der Map des parent-Knotens ein Entry mit dem angegebenen Wert existiert<br>
	 * (wenn nicht vorhanden, wird er mit dem angegebenen key angelegt)
	 *
	 * @param key
	 * @param value
	 * @param parent
	 * @return den gewünschten Entry
	 */
	private Map.Entry ensureEntry(final String key, final String value, final Map map) {
		final Map.Entry[] entries = map.getEntryArray();
		Map.Entry neededEntry = null;

		for (final Map.Entry e : entries) {
			if (e.getValue().equalsIgnoreCase(value)) {
				neededEntry = e;
			}
		}

		if (neededEntry == null) {
			neededEntry = map.addNewEntry();
			neededEntry.setKey(key);
			neededEntry.setValue(value);
		}

		return neededEntry;
	}

	/**
	 * stellt sicher, dass der Knoten einen <map>-Eintrag hat
	 *
	 * @param node
	 * @return die vorhandene oder erzeugte Map
	 */
	private Map ensureMap(final Node node) {
		Map map = node.getMap();
		if (map == null) {
			map = node.addNewMap();
		}

		return map;
	}

	/**
	 * noch mal das selbe wie {@link #ensureMap(Node)}, aber für Root-Node
	 *
	 * @param node
	 * @return die vorhandene oder erzeugte Map
	 */
	private Map ensureMap(final Root root) {
		Map map = root.getMap();
		if (map == null) {
			map = root.addNewMap();
		}

		return map;
	}

	/**
	 * macht Anpassungen an der Xbs-Datei wie z.B. die Erzeugung der Standardknoten<br>
	 * (siehe {@link #readXbsFile(String, String)})
	 *
	 * @param xbsdocument
	 * @throws BaseSetupException
	 */
	public void conformXbsFile(final PreferencesDocument xbsdocument) throws BaseSetupException {
		// leider ziemlich unpraktisch: Root und Node sind fast das selbe, sind
		// aber nicht kompatibel
		// und Node hat kein getParent()!!!
		Root root = xbsdocument.getPreferences().getRoot();
		if (root == null) {
			root = xbsdocument.getPreferences().addNewRoot();
		}

		// map muss fjedn vorhanden sein
		final Map rootMap = ensureMap(root);

		// ... sollte aber keine entries haben... die gehören zwei Ebenen tiefer
		if (this.getVersionInfo().getModulName().startsWith("de.")) {
			// das wird nur geprüft, wenn ich ein Kundenprojekt installiere...
			// ansonsten mache ich wegen einer Kleinigkeit die ganze xbs neu
			for (int i = 0; i < rootMap.getEntryArray().length; i++) {
				if (rootMap.getEntryArray(i).getKey().startsWith("import.")) {
					log("XBS-Datei muss neu erstellt werden", true);
					throw new BaseSetupException("import Entry soll entfernt werden");
					// rootMap.removeEntry(i);
					// i--; // die anderen Einträge rücken nach!
				}
			}
		}

		// minova-Knoten muss vorhanden sein
		final Node minovaNode = ensureNode("minova", root, true);
		ensureMap(minovaNode);

		// Knoten mit appName (Kleinbuchstaben) muss vorhanden sein
		final Node appNode = ensureNode(parameter.getProperty("app").toLowerCase(), minovaNode, true, true);
		final Map appMap = ensureMap(appNode);

		ensureEntry("import.1", "license.xbs", appMap);
		ensureEntry("import.2", "connection.xbs", appMap);

		// Ersetzung(en)
		for (final Map.Entry e : appMap.getEntryArray()) {
			e.setValue(e.getValue().toString().replace("${application}", getAppNameShort()));
		}
	}

	/**
	 * Diese Methode liest die übergebene xbs-Datei aus und speichert sie mit in dem globalen Object rootNodeRoot aus BaseSetup. Des weiteren wird, falls nicht
	 * bereits vorhanden, der "minova" node mit mit Kind "name der Software" (disposition) angelegt. Zusätzlich wird für den Fall, dass die xbs-Datei nicht
	 * besteht, diese angelegt mit den StandardKnoten: <code>
	 * <root type="system">
	 * <map/>
	 * <node name="minova">
	 * <map/>
	 * <node name="disposition">
	 * <map>
	 * <entry key="import.1" value="license.xbs"/>
	 * <entry key="import.2" value="connection.xbs"/>
	 * </map>
	 * </node>
	 * </node>
	 * </root>
	 * <br>
	 * </code> WIS 08.05.2014: die beiden Standard-Importe liegen jetzt zwei Ebenen weiter unten (#16618)
	 *
	 * @param xbspath der Pfad zur auszulesenden Datei wird hier angegeben.
	 * @param xbsfile die auszulesende Datei
	 * @return
	 * @throws XmlException
	 * @throws IOException
	 * @throws BaseSetupException
	 */
	public boolean readXbsFile(final String xbspath, final String xbsfile) throws XmlException, IOException, BaseSetupException {
		rootNodeRoot = new ch.minova.install.setup.xbs.Root("system");
		PreferencesDocument xbsdocument;
		try {
			final File file = new File(xbspath + xbsfile);
			xbsdocument = getPreferencesDocument(file);
			log(MessageFormat.format("Einlesen der XBS-Datei: {0}", file.getAbsoluteFile()), true);

			conformXbsFile(xbsdocument);
		} catch (final BaseSetupException e) {
			// wenn die xbs-Datei nicht vorhanden ist wird diese erstellt.
			log(MessageFormat.format("Einlesen der XBS-Datei schlug fehl: {0}", e.getMessage()));
			final File file = new File(xbspath + xbsfile);
			final File dir = new File(xbspath);
			dir.mkdirs();
			if (!dir.exists() && !dir.getPath().equals("")) {
				throw new BaseSetupException(MessageFormat.format(FILEFAILTOCREATE, xbspath + xbsfile));
			} else {
				file.createNewFile();
				log(MessageFormat.format("Die xbs-Datei wird neu erstellt: {0}", file.getAbsolutePath()), true);
				final PreferencesDocument prefDoc = PreferencesDocument.Factory.newInstance();
				prefDoc.addNewPreferences();
				conformXbsFile(prefDoc);

				savepref(prefDoc, file);
				xbsdocument = getPreferencesDocument(file);
			}
		}

		addEntriesToRoot(rootNodeRoot, xbsdocument.getPreferences().getRoot().getMap());
		addNodeToRoot(rootNodeRoot, xbsdocument.getPreferences().getRoot().getNodeArray());
		return true;
	}

	public boolean readMdiFile(final String mdipath, final String mdifile) throws XmlException, IOException, BaseSetupException {
		final File file = new File(mdipath + mdifile);
		log(MessageFormat.format("Pfad zur Datei: {0}", file.getAbsolutePath()));
		MainDocument maindocument;
		try {
			maindocument = getMainDocument(file);
		} catch (final Exception e1) {
			final MainDocument maindoc = MainDocument.Factory.newInstance();
			maindoc.addNewMain();
			maindoc.getMain().setIcon(getAppNameShort() + ".ico");
			maindoc.getMain().setTitel(parameter.getProperty("app"));
			maindoc.getMain().addNewToolbar();
			saveMainDoc(maindoc, file);
			maindocument = getMainDocument(file);
		}
		log("Auslesen der MDI Datei und Eintragen der voreingetragenen Dateien aus der MDI");

		final Main maindoc = maindocument.getMain();
		try {
			for (int i = 0; i < maindoc.getActionArray().length; i++) {
				mainmdi.getAction(maindoc.getActionArray(i).getAction(), true);
				mainmdi.readAction(maindoc.getActionArray(i));
			}
			if (maindoc.getMenu() != null) {
				mainmdi.getMenu(maindoc.getMenu().getId(), true);
				if (parameter.getProperty("override").equalsIgnoreCase("true")) {
					mainmdi.readMenu(maindoc.getMenu(), true);
				}
				mainmdi.readMenu(maindoc.getMenu(), false);
			}
			if (maindoc.getToolbar() != null) {
				if (maindoc.getToolbar().getEntryArray().length > 0) {
					mainmdi.getToolbar(maindoc.getToolbar().getFlat(), true);
					for (int i = 0; i < maindoc.getToolbar().getEntryArray().length; i++) {
						final ch.minova.install.setup.mdi.Entry entry = new ch.minova.install.setup.mdi.Entry(maindoc.getToolbar().getEntryArray(i).getId(),
								maindoc.getToolbar().getEntryArray(i).getType());
						entry.setOverride(maindoc.getToolbar().getEntryArray(i).getOverride());
						entry.setPosition(maindoc.getToolbar().getEntryArray(i).getPosition());
						if (!mainmdi.getToolbar().existsEntry(entry.getId().toLowerCase())) {
							mainmdi.getToolbar().addEntry(entry);
							log(MessageFormat.format("Eintrag in Toolbar: {0}", entry.getId()));
						}
					}
				}
			}
		} catch (final EntryCompareMDIException e) {
			log(MessageFormat.format("EntryPositionException: {0}", maindocument.getClass().getName()));
		} catch (final MenuCompareMDIException e) {
			log(MessageFormat.format("MenuPositionException: {0}", maindocument.getClass().getName()));
		} catch (final Exception e) {
			log(MessageFormat.format("Error reading MDI-File check the path of this file: {0}", file.getAbsolutePath()));
			throw new BaseSetupException(e.getMessage());
		}
		return true;
	}

	private static String OS = System.getProperty("os.name").toLowerCase();
	private static String OSarch = System.getProperty("os.arch").toLowerCase();

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isX86Arch() {
		return (OSarch.indexOf("86") >= 0);
	}

	/**
	 * @param path
	 * @param exe
	 */
	public void replaceChar(final String path, final String exe) {
		try {
			String value = FileUtils.readFileToString(new File(path));
			while (value.contains("@@executable@@")) {
				value = value.replace("@@executable@@", exe);
			}
			FileUtils.writeStringToFile(new File(path), value, "UTF-8", false);
		} catch (final IOException e) {
			log(MessageFormat.format("Error IOException: {0}", e.getMessage()));
			// e.printStackTrace();
		}
	}

	/**
	 * @param path        -Pfad
	 * @param file
	 * @param servicename
	 * @param dataending
	 */
	public void renameService(final String path, final String oldname, final String newname) {
		final File srcFile = new File(path + oldname);
		final File destFile = new File(path + newname);
		if (!destFile.exists()) {
			try {
				FileUtils.copyFile(srcFile, destFile);
			} catch (final IOException e) {
				log(MessageFormat.format("Error IOException: {0}", e.getMessage()));
				// e.printStackTrace();
			}
			deleteOldFiles(srcFile);
		}
	}

	/**
	 * Löscht die störende Datei wenn vorhanden.
	 *
	 * @param file
	 */
	private void deleteOldFiles(final File file) {
		if (file.exists()) {
			FileUtils.deleteQuietly(file);
		}
	}

	/**
	 * Baut den Service auf. Veranlasst das Kopieren der verschiedenen Dateien für die verschiedenen Systemarchitektueren und Betriebssysteme.
	 *
	 * @param windows
	 * @param arch64
	 * @param path
	 * @param servicename
	 */
	public void configService(final boolean windows, final boolean arch86, final String path, final String servicename) {
		if (windows) {
			log(MessageFormat.format("Betriebssystem: Windows", ""));
			if (!arch86) {
				log(MessageFormat.format("Betriebssystemarchitektur: 64Bit", ""), true);
				final File dir = new File(path + "/bin");
				File x64 = new File(path + "/bin64/libwrapper.so");
				deleteOldFiles(x64);
				x64 = new File(path + "/bin64/wrapper.sh");
				deleteOldFiles(x64);
				log(MessageFormat.format("Löschen der datei: {0}", x64.getAbsolutePath()));
				x64 = new File(path + "/bin64/wrapper");
				deleteOldFiles(x64);
				log(MessageFormat.format("Löschen der datei: {0}", x64.getAbsolutePath()));
				x64 = new File(path + "/bin64/wrapper.x64");
				deleteOldFiles(x64);
				try {
					FileUtils.deleteDirectory(dir);
				} catch (final IOException e) {
					log(MessageFormat.format("Error IOException: {0}", e.getMessage()));
				}
				final String pathnew = path + "/bin64/";
				renameService(pathnew, "wrapper.x64.exe", servicename + ".exe");
				renameService(pathnew, "wrapper.bat", servicename + ".bat");
				final File file = new File(pathnew);
				// Umbenennen des Ordners bin64 in bin
				file.renameTo(dir);
				// Ersezten von @@executeable@@
				replaceChar(path + "/bin/" + servicename + ".bat", servicename + ".exe");
				x64 = new File(path + "/bin64/wrapper.exe");
				deleteOldFiles(x64);
				x64 = new File(path + "/bin64/wrapper.bat");
				deleteOldFiles(x64);
			} else {
				log(MessageFormat.format("Betriebssystemarchitektur: 32Bit", ""), true);
				final File dir = new File(path + "/bin64");
				File x64 = new File(path + "/bin/libwrapper.so");
				deleteOldFiles(x64);
				log(MessageFormat.format("Löschen der datei: {0}", x64.getAbsolutePath()));
				x64 = new File(path + "/bin/wrapper.sh");
				deleteOldFiles(x64);
				log(MessageFormat.format("Löschen der datei: {0}", x64.getAbsolutePath()));
				x64 = new File(path + "/bin/wrapper");
				deleteOldFiles(x64);
				log(MessageFormat.format("Löschen der datei: {0}", x64.getAbsolutePath()));
				try {
					FileUtils.deleteDirectory(dir);
				} catch (final IOException e) {
					log(MessageFormat.format("Error IOException: {0}", e.getMessage()));
					// e.printStackTrace();
				}
				final String pathnew = path + "/bin/";
				renameService(pathnew, "wrapper.exe", servicename + ".exe");
				renameService(pathnew, "wrapper.bat", servicename + ".bat");
				replaceChar(path + "/bin/" + servicename + ".bat", servicename + ".exe");
				x64 = new File(path + "/bin/wrapper.exe");
				deleteOldFiles(x64);
				x64 = new File(path + "/bin/wrapper.bat");
				deleteOldFiles(x64);
			}
		} else {
			if (!arch86) {
				final String pathnew = path + "/bin64/";
				final File dir = new File(path + "/bin");
				File x64 = new File(pathnew + "wrapper.x64.exe");
				deleteOldFiles(x64);
				log(MessageFormat.format("Löschen der datei: {0}", x64.getAbsolutePath()));
				x64 = new File(pathnew + "wrapper.dll");
				deleteOldFiles(x64);
				log(MessageFormat.format("Löschen der datei: {0}", x64.getAbsolutePath()));
				x64 = new File(pathnew + "ntlmauth.dll");
				deleteOldFiles(x64);
				log(MessageFormat.format("Löschen der datei: {0}", x64.getAbsolutePath()));
				x64 = new File(pathnew + "wrapper.bat");
				deleteOldFiles(x64);
				log(MessageFormat.format("Löschen der datei: {0}", x64.getAbsolutePath()));
				try {
					FileUtils.deleteDirectory(dir);
				} catch (final IOException e) {
					log(MessageFormat.format("Error IOException: {0}", e.getMessage()));
				}
				renameService(pathnew, "wrapper", servicename);
				renameService(pathnew, "wrapper.sh", servicename + ".sh");
				final File bin = new File(pathnew);
				bin.renameTo(dir);
				replaceChar(path + "/bin/" + servicename + ".sh", servicename);
				x64 = new File(path + "/bin/wrapper.sh");
				deleteOldFiles(x64);
				x64 = new File(path + "/bin/wrapper");
				deleteOldFiles(x64);
			} else {
				final String pathnew = path + "/bin/";
				final File dir = new File(path + "/bin64");
				File x64 = new File(pathnew + "wrapper.exe");
				deleteOldFiles(x64);
				log(MessageFormat.format("Löschen der datei: {0}", x64.getAbsolutePath()));
				x64 = new File(pathnew + "wrapper.dll");
				deleteOldFiles(x64);
				log(MessageFormat.format("Löschen der datei: {0}", x64.getAbsolutePath()));
				x64 = new File(pathnew + "ntlmauth.dll");
				deleteOldFiles(x64);
				log(MessageFormat.format("Löschen der datei: {0}", x64.getAbsolutePath()));
				x64 = new File(pathnew + "wrapper.bat");
				deleteOldFiles(x64);
				log(MessageFormat.format("Löschen der datei: {0}", x64.getAbsolutePath()));
				try {
					FileUtils.deleteDirectory(dir);
				} catch (final IOException e) {
					log(MessageFormat.format("Error IOException: {0}", e.getMessage()));
				}
				renameService(pathnew, "wrapper", servicename);
				renameService(pathnew, "wrapper.sh", servicename + ".sh");
				replaceChar(path + "/bin/" + servicename + ".sh", servicename);
				x64 = new File(path + "/bin/wrapper.sh");
				deleteOldFiles(x64);
				x64 = new File(path + "/bin/wrapper");
				deleteOldFiles(x64);
			}
		}
	}

	public static String getAppName() {
		IApplicationType iat = null;
		final String nameapp = parameter.getProperty("app");
		if (nameapp != null) {
			iat = ApplicationType.getApplication(nameapp);

			if (iat == null) {
				log(MessageFormat.format("Die Applikation {0} wurde nicht gefunden", nameapp), true);
				throw new RuntimeException(MessageFormat.format("Die Applikation {0} wurde nicht gefunden", nameapp));
			}
		} else {
			iat = ApplicationType.DISPO;
		}
		return iat.getName();
	}

	public static String getAppNameShort() {
		IApplicationType iat = null;
		final String nameapp = parameter.getProperty("app");
		if (nameapp != null) {
			iat = ApplicationType.getApplication(nameapp);

			if (iat == null) {
				log(MessageFormat.format("Die Applikation {0} wurde nicht gefunden", nameapp), true);
				throw new RuntimeException(MessageFormat.format("Die Applikation {0} wurde nicht gefunden", nameapp));
			}
		} else {
			iat = ApplicationType.DISPO;
		}
		return iat.getShortName();
	}

	public String getAppNameLong() {
		IApplicationType iat = null;
		final String nameapp = parameter.getProperty("app");
		if (nameapp != null) {
			iat = ApplicationType.getApplication(nameapp);

			if (iat == null) {
				log(MessageFormat.format("Die Applikation {0} wurde nicht gefunden", nameapp), true);
				throw new RuntimeException(MessageFormat.format("Die Applikation {0} wurde nicht gefunden", nameapp));
			}
		} else {
			iat = ApplicationType.DISPO;
		}
		return iat.getLongName();
	}

	/**
	 * Ausfuehrung der ausgeloesten Methoden, welche durch den uebergebenen Array ausgewaehlt wurden
	 *
	 * @param args
	 */
	public void run(final String[] args) {
		// Um die args für alle rekursiv aufrufenden Module zur verfügung zu stellen, werden sie in eine in setupArgs abgespeichert
		// System.out.println(Arrays.toString(args));
		deleteParms();
		if (parameter == null) {
			analyseArgs(args);
		}
		run();
		if (modulesnotfoundvector.size() > 0) {
			System.err.println("Folgende Module konnten nicht gefunden werden:");
			for (final String string : modulesnotfoundvector) {
				System.err.println(string);
			}
			throw new RuntimeErrorException(new Error("Module nicht gefunden!"));
		}

		if (parameter.containsKey("app")) {
			IApplicationType iat = null;
			final String nameapp = parameter.getProperty("app");
			if (nameapp != null) {
				iat = ApplicationType.getApplication(nameapp);
				if (iat == null) {
					log(MessageFormat.format("Die Applikation {0} wurde nicht gefunden", nameapp), true);
					throw new RuntimeException(MessageFormat.format("Die Applikation {0} wurde nicht gefunden", nameapp));
				}
			} else {
				// FIXME
				iat = ApplicationType.DISPO;
			}
			parameter.setProperty("app", iat.getName());
		}
		if (parameter.containsKey("meta")) {
			return;
		}
		if (parameter.containsKey("override")) {
			System.out.println(MessageFormat.format("-override = {0}", parameter.getProperty("override")));
		}
		if (parameter.containsKey("nodb")) {
			System.out.println(MessageFormat.format("-nodb = {0}", parameter.getProperty("nodb")));
		}
		if (parameter.containsKey("service")) {
			System.out.println(MessageFormat.format("-service = {0}", parameter.getProperty("service")));
		}
		if (parameter.containsKey("c")) {
			System.out.println(MessageFormat.format("-customer = {0}", parameter.getProperty("c")));
		}
		if (parameter.containsKey("arch")) {
			System.out.println(MessageFormat.format("-arch = {0}", parameter.getProperty("arch")));
		}
		if (parameter.containsKey("truckdb") || parameter.containsKey("tdb")) {
			System.out.println(MessageFormat.format("- TruckDB = {0}", parameter.getProperty("tdb")));
		}
		if (parameter.containsKey("basedir")) {
			System.out.println(MessageFormat.format("-basedir = {0}", parameter.getProperty("basedir")));
		}
		if (parameter.containsKey("file")) {
			System.out.println(MessageFormat.format("-file = {0}", parameter.getProperty("file")));
		}
		if (parameter.containsKey("app")) {
			System.out.println(MessageFormat.format("-app = {0}", parameter.getProperty("app")));
		}
		if (parameter.containsKey("outdir")) {
			System.out.println(MessageFormat.format("-outdir = {0}", parameter.getProperty("outdir")));
		}
		if (parameter.containsKey("info")) {
			System.out.println("Eingangspaket: ch.minova.install.jar: \n"
					+ "Dieses Paket haelt die erfoderlichen Methoden bereit um ueber den gesamten Abhaengigkeitsbaum zu suchen,\n"
					+ "diesen vollstaendig zu installieren und weitere Information in Bezug auf die Versionen der einzelnen Pakete azeigen zu lassen.");
		}
		if (parameter.containsKey("cs") || parameter.containsKey("checksql")) {
			try {
				readSQLOfOrderedModules();
			} catch (final Exception e) {
				System.err.println(e.getMessage());
				throw new RuntimeErrorException(new Error(e));
			}
		}
		if (parameter.containsKey("checkconnection") || parameter.containsKey("cc")) {
			try {
				readoutconnection();
			} catch (final Exception e) {
				System.err.println(e.getMessage());
				throw new RuntimeErrorException(new Error(e));
			}
		}
		// nach dem ausführen der CheckModules wird die readmdi ausgeführt
		if (parameter.containsKey("readmdi")) {
			try {
				readMdiOfOrderedModules();
			} catch (final Exception e) {
				System.err.println(e.getMessage());
				throw new RuntimeErrorException(new Error(e));
			}
		}
		if (parameter.containsKey("execjava") || parameter.containsKey("ej")) {
			try {
				execjava(this.executejava);
			} catch (final Exception e) {
				System.err.println(e.getMessage());
				throw new RuntimeErrorException(new Error(e));
			}
		}

		if (parameter.containsKey("writeloggingdatabase") || parameter.containsKey("writelogdb")) {
			try {
				// an dieser stelle sollte abgefragt werden welche Datenbank für
				// das Schema aktualisiert werden soll!
				if (parameter.containsKey("nodb")) {
					log(MessageFormat.format("UpdateSchema - The connection to the Log-DB was denied beacuse of the argument -nodb(noDataBank)", ""), true);
				} else {

					this.connection = checkConnection();
					if (!connectioncheck()) {
						throw new Exception(new Error(
								"Verbindung zur Datenbank ist nicht korrekt. Bitte Pflegen Sie die zu verwendende DB Connection in die Connection.xbs ein!!!"));
					}
					final ResultSet rs = this.connection.createStatement()
							.executeQuery("select COUNT(*) as Anzahl from INFORMATION_SCHEMA.TABLES where TABLE_NAME = 'tVersion10'");
					rs.next();
					if (rs.getInt("Anzahl") == 0) {
						// Erstellen der Tabellen auf der Datenbank
						readTabelsOfOrderedModulesCreateLogDB();
						log(MessageFormat.format("Schema angelegt auf Datenbank: ", this.connection.getCatalog()));
					} else {
						// Updaten der Tabellen auf der Datenbank
						readTabelsOfOrderedModulesLogDB();
						log(MessageFormat.format("Schema aktualisiert auf Datenbank: ", this.connection.getCatalog()));
					}
					// Ausführen des Java-Codes
					this.executejava = ExecuteJavaType.UPDATESCHEMA;
					execjava(ExecuteJavaType.UPDATESCHEMA);
				}
			} catch (final Exception e) {
				throw new RuntimeErrorException(new Error(e));
			}
		}

		if (parameter.containsKey("updateschema") || parameter.containsKey("us")) {
			try {
				// an dieser stelle sollte abgefragt werden welche Datenbank für
				// das Schema aktualisiert werden soll!
				if (parameter.containsKey("nodb")) {
					log(MessageFormat.format("UpdateSchema - The connection to the DB was denied beacuse of the argument -nodb(noDataBank)", ""), true);
				} else {

					this.connection = checkConnection();
					if (!connectioncheck()) {
						throw new Exception(new Error(
								"Verbindung zur Datenbank ist nicht korrekt. Bitte Pflegen Sie die zu verwendende DB Connection in die Connection.xbs ein!!!"));
					}
					final ResultSet rs = this.connection.createStatement()
							.executeQuery("select COUNT(*) as Anzahl from INFORMATION_SCHEMA.TABLES where TABLE_NAME = 'tVersion10'");
					rs.next();
					if (rs.getInt("Anzahl") == 0) {
						// Erstellen der Tabellen auf der Datenbank
						readTabelsOfOrderedModulesCreate();
						log(MessageFormat.format("Schema angelegt auf Datenbank: ", this.connection.getCatalog()));
					} else {
						// Updaten der Tabellen auf der Datenbank
						readTabelsOfOrderedModules();
						log(MessageFormat.format("Schema aktualisiert auf Datenbank: ", this.connection.getCatalog()));
					}
					// Ausführen des Java-Codes
					this.executejava = ExecuteJavaType.UPDATESCHEMA;
					execjava(ExecuteJavaType.UPDATESCHEMA);
				}
			} catch (final Exception e) {
				throw new RuntimeErrorException(new Error(e));
			}
		}

		if (parameter.containsKey("readxbsfile")) {
			try {
				final String xbspath = parameter.getProperty("basedir");
				final String xbsfile = parameter.getProperty("file");
				readXbsFile(xbspath, xbsfile);
			} catch (final Exception e) {
				System.err.println(e.getMessage());
				throw new RuntimeErrorException(new Error(e));
			}
		}
		if (parameter.containsKey("readmdifile")) {
			try {
				final String xbspath = parameter.getProperty("basedir");
				final String xbsfile = parameter.getProperty("file");
				readMdiFile(xbspath, xbsfile);
			} catch (final Exception e) {
				System.err.println(e.getMessage());
				throw new RuntimeErrorException(new Error(e));
			}
		}
		if (parameter.containsKey("readxbs")) {
			try {
				readXbsOfOrderedModules();
			} catch (final Exception e) {
				System.err.println(e.getMessage());
				throw new RuntimeErrorException(new Error(e));
			}
		}

		if (parameter.contains("updatedatabase") || parameter.containsKey("ud")) {
			log("Modules to install:");
			final BaseSetup[] a = orderedDependingModules.toArray(new BaseSetup[0]);

			for (final BaseSetup s : a) {
				log(MessageFormat.format(" Module: {0}, version {1}", s.getVersionInfo().getModulName(), s.getVersionInfo().toString()));
			}
			try {
				if (parameter.containsKey("nodb")) {
					log(MessageFormat.format("UpdateDataBase - The connection to the DB was denied beacuse of the argument -nodb(noDataBank)", ""), true);
				} else {
					this.connection = checkConnection(null);
					if (!connectioncheck()) {
						throw new RuntimeErrorException(new Error(
								"Verbindung zur Datenbank ist nicht korrekt. Bitte Pflegen Sie die zu verwendende DB Connection in die Connection.xbs ein!!!"));
					}
					// Übergabe der Datenbankverbindung damit es schneller geht.
					handleSqlOfOrderedModules(this.connection);
					log(MessageFormat.format("Prozeduren ausgeführt auf der Datenbank: ", this.connection.getCatalog()));
					// Ausführen des Java-Codes
					this.executejava = ExecuteJavaType.UPDATEDATABASE;
					execjava(ExecuteJavaType.UPDATEDATABASE);
				}
			} catch (final Exception e) {
				log(e.getMessage(), true);
				throw new RuntimeErrorException(new Error(e.getMessage()));
			}
		}
		if (parameter.containsKey("copyfile") || parameter.containsKey("cf")) {
			try {
				readCopyFileOfOrderedModules(null);
				// Ausführen des Java-Codes
				this.executejava = ExecuteJavaType.COPYFILES;
				execjava(ExecuteJavaType.COPYFILES);
			} catch (final Exception e) {
				System.err.println(e.getMessage());
				throw new RuntimeErrorException(new Error(e));
			}
		}

		if (parameter.containsKey("installservicewithoutxbs") || parameter.containsKey("iswx") || parameter.containsKey("installservice")
				|| parameter.containsKey("is") || parameter.containsKey("installservicelite") || parameter.containsKey("isl")) {
			try {
				readServicesOfOrderedModules();
				// Ausführen des Java-Codes
				this.executejava = ExecuteJavaType.INSTALLSERVICE;
				execjava(ExecuteJavaType.INSTALLSERVICE);
			} catch (final Exception e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				throw new RuntimeErrorException(new Error(e));
			}
		}

		if (parameter.containsKey("writexbs")) {
			try {
				String outputdir = null;
				String xbspath = null;
				String xbsfile = null;
				if (!parameter.getProperty("outdir").equals("../")) {
					outputdir = parameter.getProperty("outdir") + "/";
				} else {
					outputdir = "../Program Files/" + getAppNameShort() + "/";
				}
				if (!parameter.getProperty("basedir").equals("../")) {
					xbspath = parameter.getProperty("basedir") + "/";
				} else {
					xbspath = "../Program Files/" + getAppNameShort() + "/";
				}
				if (!parameter.getProperty("file").equals("")) {
					xbsfile = parameter.getProperty("file");
				} else {
					// Der Name der Anwendung in Normalform : Dispo = Short
					// Disposition = Normal
					xbsfile = parameter.getProperty("app");
					parameter.setProperty("file", xbsfile);
				}
				if (!xbsfile.endsWith(".xbs")) {
					xbsfile = xbsfile + ".xbs";
				}
				writexbs(xbsfile, outputdir, xbspath);
				// Ausführen des Java-Codes
				this.executejava = ExecuteJavaType.WRITEXBS;
				execjava(ExecuteJavaType.WRITEXBS);
			} catch (final Exception e) {
				System.err.println(e.getMessage());
				throw new RuntimeErrorException(new Error(e));
			}
		}
		if (parameter.containsKey("writemdi")) {
			try {
				String outputdir = null;
				String mdipath = null;
				String mdifile = null;
				if (!parameter.getProperty("outdir").equals("../")) {
					outputdir = parameter.getProperty("outdir") + "/";
				} else {
					outputdir = "../Program Files/" + getAppNameShort() + "/";
				}
				if (!parameter.getProperty("basedir").equals("../")) {
					mdipath = parameter.getProperty("basedir") + "/";
				} else {
					mdipath = "../Program Files/" + getAppNameShort() + "/";
				}
				// Der Name der Anwendung in Normalform : Dispo = Short
				// Disposition = Normal
				mdifile = parameter.getProperty("app");
				mdifile = mdifile + "_MDI";
				parameter.setProperty("file", mdifile);
				if (!mdifile.endsWith(".xml")) {
					mdifile = mdifile + ".xml";
				}
				final File dir = new File(mdipath);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				File file = new File(mdipath + mdifile);
				if (!file.exists()) {
					file.createNewFile();
				}
				readMdiFile(mdipath, mdifile);
				log(MessageFormat.format("{0} wurde eingelesen", mdifile));
				readMdiOfOrderedModules();
				log(MessageFormat.format("Setup.xml Dateien wurden eingelesen", ""));
				MainDocument maindoc = getMainDocument(file);
				file = new File(outputdir + mdifile);
				mainmdi.getLcid();
				writeMdiToDocument(maindoc, file);
				maindoc = getMainDocument(file);
				// Ausführen des Java-Codes
				this.executejava = ExecuteJavaType.WRITEMDI;
				execjava(ExecuteJavaType.WRITEMDI);
			} catch (final MenuCompareMDIException me) {
				System.err.println(me.getMessage());
				throw new RuntimeErrorException(null, me.getMessage());
			} catch (final EntryCompareMDIException en) {
				System.err.println(en.getMessage());
				throw new RuntimeErrorException(null, en.getMessage());
			} catch (final Exception e) {
				System.err.println(e.getMessage());
				throw new RuntimeErrorException(null, e.getMessage());
			}
		}

		// Es werden die Sprachen aus dem ch/minova/reports entnommen und überschrieben.
		if (parameter.containsKey("convertlanguage") || parameter.containsKey("cl")) {
			final String[] language = new String[1];
			String xbspath = null;
			String xbsfile = null;
			if (!parameter.getProperty("basedir").equals("../")) {
				xbspath = parameter.getProperty("basedir") + "/";
			} else {
				xbspath = "../Program Files/" + getAppNameShort() + "/";
			}
			xbsfile = getAppName() + ".xbs";
			try {
				readXbsFile(xbspath, xbsfile);
			} catch (final XmlException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (final IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (final BaseSetupException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				language[0] = "-locale=en";

				if (parameter.get("language") != null) {
					try {
						language[0] = "-locale=" + parameter.get("language").toString();
					} catch (final Exception es) {
						System.out.println(MessageFormat.format(
								"Die eingetragene RepüortSprache konnte nicht in einen Strink konvertiert werden. FallBack-Reportsprache:{0}", language[0]));
					}
				} else {
					try {
						if (rootNodeRoot.getNode("minova", false).getNode(getAppName().toLowerCase(), false).getEntry("locale") != null && !rootNodeRoot
								.getNode("minova", false).getNode(getAppName().toLowerCase(), false).getEntry("locale").getValue().equalsIgnoreCase("")) {
							language[0] = rootNodeRoot.getNode("minova", false).getNode(getAppName().toLowerCase(), false).getEntry("locale").getValue();
						}
					} catch (final Exception ex) {
						System.out.println(MessageFormat.format(
								"Das Auslesen der XBS Datei ist fehlgeschlagen. Der locale-Eintrag konnte nicht im Knoten: {0} gefunden werden!",
								getAppName().toLowerCase()));
					}
				}
				System.out.println(MessageFormat.format("Reportsprache = {0}", language[0]));
				ConvertLanguage.main(language);
			} catch (final Exception e) {
				System.err.println(e.getMessage());
				try {
					language[0] = "-locale=en";
					ConvertLanguage.main(language);
				} catch (final Exception ex) {
					System.out.println(MessageFormat.format("Es wurde keine Sprache ausgewährt. Reportsprache = {0}", language[0]));
				}
			}
		}
	}

	private void readXbsFileService(final String xbspath, final String xbsfile) throws IOException {
		final File file = new File(xbspath + xbsfile);
		rootNodeRoot = new ch.minova.install.setup.xbs.Root("system");
		try {
			final File dir = new File(xbspath);
			dir.mkdirs();
			if (!dir.exists() && !dir.getPath().equals("")) {
				throw new BaseSetupException(MessageFormat.format(FILEFAILTOCREATE, xbspath + xbsfile));
			} else {
				file.createNewFile();
				log(MessageFormat.format("Die xbs-Datei wird neu erstellt: {0}", file.getAbsolutePath()), true);
				final PreferencesDocument prefDoc = PreferencesDocument.Factory.newInstance();
				final Preferences pref = prefDoc.addNewPreferences();
				final Root root = pref.addNewRoot();
				final Map rootMap = root.addNewMap();
				ensureEntry("import.1", "../../" + getAppNameShort() + "/" + getAppName() + ".xbs", rootMap);

				savepref(prefDoc, file);
			}
		} catch (final BaseSetupException e) {
			log(MessageFormat.format("Einlesen der XBS-Datei schlug fehl: {0}", e.getMessage()));
			e.printStackTrace();
			throw new RuntimeException(new Error());
		}
	}

	/**
	 * @param xbsfile   - Name des zu Schreibenden Files.
	 * @param outputdir - Verzeichnis, in das geschreiben werden muss
	 * @param xbspath   - Pfad zur xbs-Datei
	 * @throws XmlException
	 * @throws IOException
	 * @throws BaseSetupException
	 * @throws NodeException
	 */
	private void writexbs(final String xbsfile, final String outputdir, final String xbspath)
			throws XmlException, IOException, BaseSetupException, NodeException {
		// auslesen der eingelesenen BaseSetup Objekte
		readXbsFile(xbspath, xbsfile);
		log(MessageFormat.format("{0} wurde eingelesen", xbsfile));
		readXbsOfOrderedModules();
		log(MessageFormat.format("Setup.xml Dateien wurden eingelesen", ""));
		File file = new File(xbspath + xbsfile);
		PreferencesDocument pref = getPreferencesDocument(file);
		// schreiben in das übergebene xbs-File
		file = new File(outputdir + xbsfile);
		log(MessageFormat.format("Schreiben in {0}", file.getAbsolutePath()));
		writeXbsToDocument(pref, file);
		pref = getPreferencesDocument(file);
	}

	private void readServicesOfOrderedModules() throws XmlException, Exception {
		SetupDocument setup = null;
		BaseSetup reflectionclass = null;
		// Auslesen des required-service module
		final BaseSetup[] a = orderedDependingModules.toArray(new BaseSetup[0]);

		for (final BaseSetup bs : a) {
			final ArrayList<String> services = new ArrayList<String>();
			try {
				log(MessageFormat.format("Modul {0} wird bearbeitet", bs.getVersionInfo().getModulName()));
				setup = bs.getSetupDocument();
				if (setup.getSetup().getRequiredService() != null) {
					// Ausführen auf jedes der Services
					final Service[] service = setup.getSetup().getRequiredService().getServiceArray();
					for (int i = 0; i < service.length; i++) {
						// Objekt erstellen
						reflectionclass = getSetupClass(service[i]);
						// Abhängigkeiten des Modules abrufen
						reflectionclass.checkModules();
						// Aufruf an dieser Stelle wird rekursiv durchgeführt um
						bs.check_service(reflectionclass, setup, services);
					}
				}
			} catch (final Exception e) {
				log(MessageFormat.format("Error Exception: {0} setupklasse: {1}", e.getMessage(), setup.getClass().getName()), true);
				e.printStackTrace();
				throw new BaseSetupException(e.getMessage() + setup.getClass().getName());
			}
		}
	}

	/**
	 * Auslesen des RequiredService[] aus dem SetupDokument des übergebendenen Klassennamens
	 *
	 * @param reflectionclassname Name der Reflectionklasse
	 * @return Service[]
	 */
	private Service[] getServiceClassSetup(final String reflectionclassname) {
		BaseSetup bsservice;
		Service[] service = null;
		try {
			bsservice = getSetupClass(reflectionclassname);
			final SetupDocument bsservicesetup = bsservice.getSetupDocument();
			service = bsservicesetup.getSetup().getRequiredService().getServiceArray();
		} catch (final ModuleNotFoundException e) {
			log(MessageFormat.format("Die aufzurufende Klasse: {0} konnte nicht gefunden werden", reflectionclassname), true);
			e.printStackTrace();
		} catch (final XmlException e) {
			log(MessageFormat.format("Beim Auslesen der Setup.xml des Modul: {0} ist ein fehler aufgetreten", reflectionclassname), true);
			e.printStackTrace();
		} catch (final IOException e) {
			log(MessageFormat.format("IOException ist ein Fehler aufgetreten", reflectionclassname), true);
			e.printStackTrace();
		} catch (final BaseSetupException e) {
			log(MessageFormat.format("BaseSetupException ist ein Fehler aufgetreten", reflectionclassname), true);
			e.printStackTrace();
		}
		return service;
	}

	private WrapperConf.Entry[] getWrapperConfArray(final String reflectionclassname) {
		final Service[] service = getServiceClassSetup(reflectionclassname);
		WrapperConf.Entry[] wrapperconfentry = null;
		for (final int j = 0; j < service.length; ) {
			if (service[j].getWrapperConf() != null) {
				if (service[j].getWrapperConf().getEntryArray() != null) {
					wrapperconfentry = service[j].getWrapperConf().getEntryArray();
				}
			}
			return wrapperconfentry;
		}

		return null;
	}

	private Log4JConf.Entry[] getLog4JConfArray(final String reflectionclassname) {
		final Service[] service = getServiceClassSetup(reflectionclassname);
		Log4JConf.Entry[] log4Jconfentry = null;
		for (final int j = 0; j < service.length; ) {
			if (service[j].getLog4JConf() != null) {
				if (service[j].getLog4JConf().getEntryArray() != null) {
					log4Jconfentry = service[j].getLog4JConf().getEntryArray();
				}
			}
			return log4Jconfentry;
		}

		return null;
	}

	/**
	 * Diese Methode liest das übergebene Setup Objekt aus und erstellt entsprechende verzeichnissstrukturen für den Dienst. Der Name des Dienstes wird aus der
	 * "wrapper.app.parameter1" ausgelesen.
	 *
	 * @param bs
	 * @param setup
	 */
	@SuppressWarnings("unchecked")
	private void check_service(final BaseSetup bs, final SetupDocument setup, final ArrayList<String> services) {
		final String path = "../Program Files/";
		String dienstname = "";
		String Servicename = "";
		final Service[] service = setup.getSetup().getRequiredService().getServiceArray();
		try {
			for (int j = 0; j < service.length; j++) {
				Log4JConf.Entry[] log4Jconfentry = null;
				WrapperConf.Entry[] wrapperconfentry = null;
				Servicename = service[j].getServiceName();
				if (service[j].getWrapperConf() != null) {
					if (service[j].getWrapperConf().getEntryArray() != null) {
						wrapperconfentry = service[j].getWrapperConf().getEntryArray();
					}
				}
				if (service[j].getLog4JConf() != null) {
					if (service[j].getLog4JConf().getEntryArray() != null) {
						log4Jconfentry = service[j].getLog4JConf().getEntryArray();
					}
				}
				boolean wrapperappname = true;
				if (wrapperconfentry != null) {
					for (int i = 0; i < wrapperconfentry.length; i++) {
						// Dieser Parmerter wird für den Dienstnamen verwendet
						if (wrapperconfentry[i].getKey().equalsIgnoreCase("wrapper.app.parameter.1")) {
							wrapperappname = false;
							dienstname = wrapperconfentry[i].getValue();
							if (services.contains(dienstname)) {
								continue;
							}
							log(MessageFormat.format("Dienstname: {0}", dienstname), true);
							try {
								setParameter("service", dienstname, true);
								final BaseSetup basesetup = getSetupClass(service[j].getName());
								final Hashtable<String, VersionInfo> hashModulesdummy = (Hashtable<String, VersionInfo>) hashModules.clone();
								hashModules.clear();
								basesetup.checkServices(dienstname);
								hashModules.clear();
								hashModules = (Hashtable<String, VersionInfo>) hashModulesdummy.clone();
								basesetup.readCopyFileOfOrderedModules(orderedlocalModules.toArray(new BaseSetup[0]));
							} catch (final Exception e) {
								log(MessageFormat.format("Error Exception: {0}", e.getMessage()));
								e.printStackTrace();
							}
							if (!parameter.containsKey("arch")) {
								configService(isWindows(), isX86Arch(), path + "/" + dienstname, dienstname);
							} else {
								if (parameter.getProperty("arch").equalsIgnoreCase("64")) {
									configService(isWindows(), false, path + "/" + dienstname, dienstname);
								} else {
									configService(isWindows(), true, path + "/" + dienstname, dienstname);
								}
							}

							// Wenn das Modul schon das Service-Modul ist dann
							// muss hier nicht mehr ausgelesen werden
							if (!getVersionInfo().getModulName().equalsIgnoreCase(service[j].getName())) {
								writewrapperconf(dienstname, getWrapperConfArray(service[j].getName()), path);
								writelog4jconf(dienstname, getLog4JConfArray(service[j].getName()), path);
							}
							if (wrapperconfentry != null) {
								writewrapperconf(dienstname, wrapperconfentry, path);
							}
							if (log4Jconfentry != null) {
								writelog4jconf(dienstname, log4Jconfentry, path);
							}
							final String pathxbs = path + dienstname + "/" + "conf/";
							readXbsFileService(pathxbs, Servicename + ".xbs");
							services.add(dienstname);
							break;
						}
					}
				}
				if (wrapperappname) {
					log(MessageFormat.format("ACHRUNG!!!     Der angebene Dienst: {0} trägt keinen - wrapper.app.parameter.1 -", service[j].getName()), true);

				}
			}
		} catch (final Exception e) {
			log(MessageFormat.format("Error Exception: {0}", e.getMessage()), true);
			e.printStackTrace();
		}
	}

	/**
	 * Erstellen der log4J.properties Datei bzw. überschreiben der alten, vorhandenen Datei in dem Dienst der mittels des parameters: dienstname übergeben wird
	 *
	 * @param dienstname
	 * @param log4jconfentry
	 */
	private void writelog4jconf(final String dienstname, final Log4JConf.Entry[] log4jconfentry, final String path) {
		final ArrayList<String> lines = new ArrayList<String>();
		final HashMap<String, String> hashlines = new HashMap<String, String>();
		final String log4jheader = "#MINOVA properties \n" + "#";
		final File log4jproperties = new File(path + "/" + dienstname + "/conf/log4j.properties");
		// Auslesem der wrapperconf-key und values
		lines.add(log4jheader);
		if (log4jproperties.exists()) {
			try {
				// wir packen uns die sachen in eine Hashmap um die daten zu
				// kopieren.
				for (final String line : FileUtils.readLines(log4jproperties, "UTF-8")) {
					if (line.contains("=") && !line.contains("#encoding=UTF-8")) {
						final String splitline[] = line.split("=", 2);
						hashlines.put(splitline[0], splitline[1]);
					}
				}
			} catch (final Exception e1) {
				e1.printStackTrace();
			}
			// Vergleiche die ausgelesenen Einträge mit denen aus der Hashmap
			if (log4jconfentry != null) {
				for (final Log4JConf.Entry entry : log4jconfentry) {
					if (hashlines.containsKey(entry.getKey())) {
						hashlines.put(entry.getKey(), entry.getValue());
					}
				}
			}
			// schreiben der HashMap in den vorgesehnden Array
			final java.util.Iterator<java.util.Map.Entry<String, String>> it = hashlines.entrySet().iterator();
			java.util.Map.Entry<String, String> entry = null;
			while (it.hasNext()) {
				entry = it.next();
				lines.add(entry.getKey() + "=" + entry.getValue());
			}
		} else {
			if (log4jconfentry != null) {
				for (final Log4JConf.Entry entry : log4jconfentry) {
					lines.add(entry.getKey() + "=" + entry.getValue());
				}
			}
		}
		try {
			FileUtils.writeLines(log4jproperties, lines, false);
		} catch (final Exception e) {
			log(MessageFormat.format("Error Exception: {0}", e.getMessage()));
			e.printStackTrace();
			throw new RuntimeErrorException(new Error(e));
		}
	}

	/**
	 * Erstellen der Wrapper.conf Datei bzw. überschreiben der alten vorhandenen Datei in dem Dienst, der mittels des Parameters dienstname übergeben wird
	 *
	 * @param dienstname
	 * @param wrapperconfentry
	 * @param path
	 */
	private void writewrapperconf(final String dienstname, final WrapperConf.Entry[] wrapperconfentry, final String path) {
		final File wrapperconf = new File(path + "/" + dienstname + "/conf/wrapper.conf");
		final HashMap<String, String> hashlines = new HashMap<String, String>();
		final ArrayList<String> lines = new ArrayList<String>();
		final String wrapperheader = "#encoding=UTF-8\n" + "#include ../conf/wrapper-license.conf \n" + "#MINOVA properties \n" + "#";
		// Überschreiben der Werte aus der Hashmap
		lines.add(wrapperheader);
		if (wrapperconf.exists()) {
			try {
				// Auslesen der alten wrapper.conf Datei um Änderungen zu übernehmen
				for (final String line : FileUtils.readLines(wrapperconf, "UTF-8")) {
					if (line.contains("=") && !line.contains("#encoding=UTF-8")) {
						final String splitline[] = line.split("=", 2);
						hashlines.put(splitline[0], splitline[1]);
					}
				}
			} catch (final Exception e1) {
				e1.printStackTrace();
			}
			// Vergleiche die ausgelesenen Einträge mit denen aus der Hashmap
			if (wrapperconfentry != null) {
				for (final WrapperConf.Entry entry : wrapperconfentry) {
					if (hashlines.containsKey(entry.getKey())) {
						hashlines.put(entry.getKey(), entry.getValue());
					}
				}
			}
			// schreiben der HashMap in den vorgesehnden Array
			final Iterator<java.util.Map.Entry<String, String>> it = hashlines.entrySet().iterator();
			java.util.Map.Entry<String, String> entry = null;
			while (it.hasNext()) {
				entry = it.next();
				lines.add(entry.getKey() + "=" + entry.getValue());
			}
		} else {
			if (wrapperconfentry != null) {
				for (final WrapperConf.Entry entry : wrapperconfentry) {
					lines.add(entry.getKey() + "=" + entry.getValue());
				}
			}
		}
		try {
			FileUtils.writeLines(wrapperconf, "UTF-8", lines, false);
		} catch (final Exception e) {
			log(MessageFormat.format("Error Exception: {0}", e.getMessage()));
			e.printStackTrace();
			throw new RuntimeErrorException(new Error(e));
		}
	}

	public void run() {
		if (parameter.containsKey("h") || parameter.size() == 0) {
			printHelp();
		} else if (parameter.containsKey("meta")) {
			System.out.println(setMetaInfClasspath());
		} else if (parameter.containsKey("vi") || parameter.containsKey("versioninfo")) {
			log(MessageFormat.format("Module: {0},  version: {1}", getVersionInfo(this).getModulName(), getVersionInfo(this).toString()), true);
		} else if (parameter.containsKey("-vibn")) {
			log(MessageFormat.format("{0}", getVersionInfo(this).getBuildNumber()), true);
		} else {
			try {
				checkModules();
			} catch (final IncompatibleVersionException in) {
				System.err.println(in.getMessage());
				throw new RuntimeErrorException(new Error(in));
			} catch (final CircleFoundException ce) {
				System.err.println(ce.getMessage());
				throw new RuntimeErrorException(new Error(ce));
			} catch (final ModuleNotFoundException mo) {
				System.err.println(mo.getMessage());
				throw new RuntimeErrorException(new Error(mo));
			} catch (final BaseSetupException be) {
				System.err.println(be.getMessage());
				throw new RuntimeErrorException(new Error(be));
			} catch (final Exception e) {
				System.err.println(e.getMessage());
				throw new RuntimeErrorException(new Error(e));
			}
		}
	}

	private Connection connection = null;

	public Connection checkConnectionSSPIJNIClient() {
		try {
			if (this.connection == null || this.connection.isClosed()) {
				this.connection = null;
				SSPIJNIClient.getInstance();
			} else {
				return this.connection;
			}
		} catch (final SQLException e1) {
			log(MessageFormat.format("Connection.isClosed threw Exception: {0}", e1.getMessage()), true);
			this.connection = null;
		} catch (final Exception e) {
			log(MessageFormat.format("SSPIJNIClient threw Exception: {0}", e.getMessage()), true);
			try {
				copydll(isWindows(), isX86Arch());
			} catch (final BaseSetupException e1) {
				e1.printStackTrace();
			}
		}
		return this.connection;
	}

	/**
	 * In dieser Methode wird entschieden welche Connection.xbs für den Verbindungsaufbau verwendet wird. Wird als Übergabeparameter "null" übergeben, dann wird
	 * die Standard Connection.xbs ausgewählt, andernfalls wird der String der übergeben wird Überprüft und die entsprechende Datenabnkverbindung wird aufgebaut
	 * / überprüft.
	 *
	 * @param pathToFile
	 * @return
	 */
	public Connection checkConnection(final String pathToFile) {
		this.connection = checkConnectionSSPIJNIClient();
		log("checkConnectionSSPIJNIClient");
		if (this.connection != null) {
			return this.connection;
		}
		if (pathToFile == null) {
			this.connection = checkConnection();
		} else {
			try {
				readEveryXbsFile(pathToFile, "connection.xbs");
				connect();
			} catch (final BaseSetupException e) {
				System.err.println(e.getMessage());
				throw new RuntimeErrorException(new Error(e));
			}
		}
		return this.connection;
	}

	/**
	 * Diese Funktion gibt den ConnectionString aus. Connection2 in der Regel für die Anwendung. Sollte der Parameter tdb angegeben sein, wird die
	 * truckconnection gesucht
	 */
	public void readoutconnection() {
		checkConnection();
		String connectionString = null;
		final String appname = parameter.getProperty("app").toLowerCase();
		ch.minova.install.setup.xbs.Node n = rootNodeRoot.getNode("minova", false);
		n = n.getNode(appname, false);
		final ch.minova.install.setup.xbs.Entry connection2Entry = n.getEntry("connection2");
		final ch.minova.install.setup.xbs.Entry connectiontruckdbEntry = n.getEntry("truckconnection");
		final ch.minova.install.setup.xbs.Entry connectionlogdbEntry = n.getEntry("logconnection");
		final ch.minova.install.setup.xbs.Entry driverClass = n.getEntry("driver");
		log(MessageFormat.format("Driver: ''{0}''", driverClass.getValue()));
		// Entscheidung welche connection für das -ud -fs verwendet werden soll
		if (parameter.containsKey("tdb")) {
			if (n.getEntry("truckconnection") != null) {
				connectionString = connectiontruckdbEntry.getValue();
			} else {
				log("Es fehlt der Eintrag 'truckconnection' in der connection.xbs", true);
				connectionString = connection2Entry.getValue();
			}
		} else if (parameter.containsKey("logdb")) {
			if (n.getEntry("logconnection") != null) {
				connectionString = connectionlogdbEntry.getValue();
			} else {
				log("Es fehlt der Eintrag 'logconnection' in der connection.xbs", true);
				connectionString = connection2Entry.getValue();
			}
		} else {
			connectionString = connection2Entry.getValue();
		}
		log(MessageFormat.format("\n CONNECTION STRING: {0} \n", connectionString), true);
	}

	/**
	 * Diese Methode liest die eingelesene xbs Datei aus Dabei werden die Einträge connection2 und den Driver aus.
	 */
	public void connect() {
		final String appname = parameter.getProperty("app").toLowerCase();
		String connectionString = null;
		ch.minova.install.setup.xbs.Node n = rootNodeRoot.getNode("minova", false);
		n = n.getNode(appname, false);
		final ch.minova.install.setup.xbs.Entry connection2Entry = n.getEntry("connection2");
		final ch.minova.install.setup.xbs.Entry connectiontruckdbEntry = n.getEntry("truckconnection");
		final ch.minova.install.setup.xbs.Entry connectionlogdbEntry = n.getEntry("logconnection");
		final ch.minova.install.setup.xbs.Entry driverClass = n.getEntry("driver");
		log(MessageFormat.format("Driver: ''{0}''", driverClass.getValue()));
		// Entscheidung welche connection für das -ud -fs verwendet werden soll
		if (parameter.containsKey("tdb")) {
			if (n.getEntry("truckconnection") != null) {
				connectionString = connectiontruckdbEntry.getValue();
			} else {
				throw new RuntimeException("Abbruch des Vorgangs, der geforderte Eintrag 'truckconnection' wurde nicht in der connection.xbs gefunden");
			}
		} else if (parameter.containsKey("logdb")) {
			if (n.getEntry("logconnection") != null) {
				connectionString = connectionlogdbEntry.getValue();
			} else {
				throw new RuntimeException("Abbruch des Vorgangs, der geforderte Eintrag 'logconnection' wurde nicht in der connection.xbs gefunden");
			}
		} else {
			connectionString = connection2Entry.getValue();
		}
		if (connectionString.startsWith("jdbc:postgresql:")) {
			sqldialect = "plpgsql";
		}
		try {
			final Object d = Class.forName(driverClass.getValue()).newInstance();
			log(MessageFormat.format("Driver: {0}", d.toString()));
			this.connection = DriverManager.getConnection(connectionString);
			this.connection.createStatement().execute("select 1");
			log("Connection OK!");
			connectiondb = connectionString;
		} catch (final Exception e) {
			log(MessageFormat.format("Connection: {0} not OK! Error: {1}", connectionString, e.getMessage()), true);
			log(MessageFormat.format("Error Exception: {0}", e.getMessage()));
			// e.printStackTrace();
		}
	}

	/**
	 * Mit dieser Methode überprüfen wir mal die Verbindung zur eingegebenen Datenbank. Wir verbinden uns mit dem Treiber und dem Connection-String
	 * (connection2). Benutzer und Kennwort werden als Parameter übergeben.
	 */
	public Connection checkConnection() {
		try {
			final String basedir = parameter.getProperty("basedir");
			final String appname = getAppNameShort();
			// String appname = filename.substring(0, filename.indexOf("."));
			final String username = parameter.getProperty("u");
			// String password = parameter.getProperty("p");
			log(MessageFormat.format("User: {0}", username));
			final File fi1 = new File(basedir);
			String h = fi1.getAbsolutePath();
			h = h.replace("/..", "");
			h = h.replace("\\..", "");
			final File fi = new File(h);
			final String verz = "../" + getFolderName(fi.getAbsolutePath()) + "/";
			try {
				readEveryXbsFile("../Program Files/" + appname + "/", "connection.xbs");
			} catch (final Exception e1) {
				log(MessageFormat.format("Error: {0}. Es wird versucht auf eine mögliche connection.xbs aus dem deploy-Order zuzugreifen", e1.getMessage()),
						true);
				try {
					readEveryXbsFile(verz, "connection.xbs");
				} catch (final Exception e) {
					log(MessageFormat.format("Error: {0}, es wird versucht die aktuelle connection.xbs in deploy zu kopieren", e1.getMessage()), true);
					copyxbstodeploy();
					try {
						readEveryXbsFile(verz, "connection.xbs");
					} catch (final Exception e2) {
						System.err.println(e2.getMessage());
						throw new RuntimeErrorException(new Error(e2));
					}
				}
			}
			connect();
		} catch (final Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return this.connection;
	}

	/**
	 * Aus einem Übergebenen Dateipfad wird das Elternverzeichnis zurückgegeben
	 *
	 * @param path
	 * @return
	 */
	private String getFolderName(final String path) {
		String folderpath = path;
		folderpath = folderpath.substring(folderpath.indexOf("/") + 1, folderpath.length());
		folderpath = folderpath.substring(folderpath.indexOf("\\") + 1, folderpath.length());
		if (folderpath.contains("/") || folderpath.contains("\\")) {
			folderpath = getFolderName(folderpath);
		}
		return folderpath;
	}

	/**
	 * Diese Methode kopiert für das Betriebssystem die entsprechend benätigte "ntlmauth.dll". Diese *.dll wird für den Aufbau der Datenbankverbindung benötigt.
	 * Je nach Architektur wird die 64 Bit oder 32 Bit Variante kopiert.
	 *
	 * @param windows
	 * @param x86Arch
	 * @throws BaseSetupException
	 */
	private void copydll(final boolean windows, final boolean x86Arch) throws BaseSetupException {
		// String basedir = parameter.getProperty("basedir");
		final BaseSetup[] a = orderedDependingModules.toArray(new BaseSetup[0]);

		for (final BaseSetup bs : a) {
			final ch.minova.install.setup.copyfile.CopyFile cf = new ch.minova.install.setup.copyfile.CopyFile();
			try {
				if (bs.getVersionInfo().getModulName().equals("ch.minova.install")) {

					final String basedir = parameter.getProperty("basedir");
					final File fi1 = new File(basedir);
					String h = fi1.getAbsolutePath();
					h = h.replace("/..", "");
					h = h.replace("\\..", "");
					log(MessageFormat.format("Aktuelles Verzeichnis BASEDIR: {0}", h), true);
					final File fi = new File(h);
					final String verz = "../" + getFolderName(fi.getAbsolutePath());
					log(MessageFormat.format("Aktuelles Verzeichnis order: {0}", verz), true);
					if (x86Arch) {
						final FileCopy n = cf.getFileCopy("ntlmauth.dll", verz, "/bin/", true);
						log(MessageFormat.format("copy file {0} {1} -> {2}", n.getFromdir(), n.getFilename(), n.getTodir()), true);
						bs.copyFiles(bs, cf);
					} else {
						final FileCopy n = cf.getFileCopy("ntlmauth.dll", verz, "/bin64/", true);
						log(MessageFormat.format("copy file {0} {1} -> {2}", n.getFromdir(), n.getFilename(), n.getTodir()), true);
						bs.copyFiles(bs, cf);
					}
				}
			} catch (final BaseSetupException e) {
				throw new BaseSetupException(e.getMessage());
			}
		}
	}

	/**
	 * Kopiert einen InputStream, sodass der originale InputStream wieder geschlossen werden kann.
	 *
	 * @param is
	 * @return
	 * @author wild
	 */
	private InputStream copyStream(final InputStream is) {
		if (is == null) {
			return null;
		}

		final StringBuffer sb = new StringBuffer();
		try {
			while (is.available() > 0) {
				final byte[] input = new byte[is.available()];
				is.read(input);
				sb.append(new String(input));
			}
			log(sb.toString(), false);
		} catch (final IOException e) {
			// e.printStackTrace();
		}

		return new ByteArrayInputStream(sb.toString().getBytes());
	}

	/**
	 * Diese Methode kopiert die connection.xbs Datei in das deploy-Verzeichnis um die Verbindung zu der Datenebank zu gewährleisten.
	 *
	 * @throws XmlException
	 * @throws IOException
	 * @throws BaseSetupException
	 */
	public void copyxbstodeploy() throws XmlException, IOException, BaseSetupException {
		SetupDocument setup = null;
		final String basedir = parameter.getProperty("basedir");
		final File fi1 = new File(basedir);
		String h = fi1.getAbsolutePath();
		h = h.replace("/..", "");
		h = h.replace("\\..", "");
		final File fi = new File(h);
		final String verz = "../" + getFolderName(fi.getAbsolutePath());
		final BaseSetup[] a = orderedDependingModules.toArray(new BaseSetup[0]);

		for (final BaseSetup bs : a) {
			try {
				log(MessageFormat.format("Datei die ausgelesen wird: {0}", bs.getVersionInfo().getModulName() + "  -  setup.xml"));
				if (bs.getVersionInfo().getModulName().startsWith("de.minova.")) {
					setup = bs.getSetupDocument();

					if (setup.getSetup().getCopyFile() != null) {
						final ch.minova.install.setup.copyfile.CopyFile cf = new ch.minova.install.setup.copyfile.CopyFile();
						log(MessageFormat.format("Auslesen der Dateien", ""));

						try {
							for (final Filecopy fc : setup.getSetup().getCopyFile().getFilecopyArray()) {
								if (fc.getFilename().equals("connection.xbs")) {

									final FileCopy n = cf.getFileCopy(fc.getFilename(), verz, fc.getFromdir(), true);
									log(MessageFormat.format("copy file {0} {1} -> {2}", n.getFromdir(), n.getFilename(), n.getTodir()));
								}
							}
						} catch (final Exception e) {
							log(MessageFormat.format("Exception: {0}", e.getMessage()), true);
							throw new BaseSetupException(e.getMessage() + setup.getClass().getName());
						}
						bs.copyFiles(bs, cf);
					} else {
						log(MessageFormat.format("Keine Dateien zum Kopieren angegeben", ""), true);
					}
				}
			} catch (final BaseSetupException e) {
				throw new BaseSetupException(e.getMessage() + setup.getClass().getName());
			}
		}
	}

	/**
	 * übernimmt das schreiben der Menus in das übergeben MainDocument Objekt
	 *
	 * @param mainDoc
	 * @throws NodeException
	 * @throws BaseSetupException
	 * @throws MenuCompareMDIException
	 */
	private void writeMdiMenuToDocument(final MainDocument mainDoc) throws NodeException, BaseSetupException, MenuCompareMDIException {
		// An dieser Stelle sollte sortiert werden.
		sortmenues(mainmdi);
		//
		if (mainDoc.getMain().getMenu() != null) {
			final int menucount = mainDoc.getMain().getMenu().getMenuArray().length;
			for (int j = 0; j < menucount; j++) {
				mainDoc.getMain().getMenu().removeMenu(0);
			}
		}
		for (int i = 0; i < mainmdi.getMenues().size(); i++) {
			log(MessageFormat.format("mainmdi  <menu id={0}>", mainmdi.getMenues().get(i).getId()));

			if (mainDoc.getMain().getMenu() != null) {

				try {
					mainmdi.writeMenu(mainDoc.getMain().getMenu(), mainmdi.getMenues().get(i));
				} catch (BaseSetupException e) {
					throw new RuntimeException(e);
				}
			} else {
				final ch.minova.core.install.MenuDocument.Menu menu = mainDoc.getMain().addNewMenu();
				menu.setId("main");
				try {
					mainmdi.writeMenu(mainDoc.getMain().getMenu(), mainmdi.getMenues().get(i));
				} catch (BaseSetupException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * Methode zur Sortierung der Menues<br>
	 * Behandlung von Menus die keine Positionsangabe haben, werden wie die größte Zahl behandelt. (Alles andere kommt vorher)
	 *
	 * @param mainmdi2 Menus die zuvor eingelesen wurden. Enthalten uch Einträge
	 * @throws MenuCompareMDIException
	 */
	private void sortmenues(final ch.minova.install.setup.mdi.Main mainmdi2) throws MenuCompareMDIException {
		int menulistsize = 0;
		boolean addMenu = false;
		final Vector<Menu> sortmenues = new Vector<Menu>();
		final ArrayList<Menu> menualist = new ArrayList<Menu>();
		for (int i = 0; i < mainmdi2.getMenues().size(); i++) {
			addMenu = false;
			if (i == 0) {
				// liste mit dem ersten element füllen.
				menualist.add(mainmdi2.getMenues().get(i));
			}
			if (i > 0) {
				if (mainmdi2.getMenues().get(i).getPosition() == 0) {
					menualist.add(0, mainmdi2.getMenues().get(i));
				} else {
					menulistsize = menualist.size();

					for (int k = 0; k < menulistsize; k++) {
						if (menualist.get(k).getPosition() > mainmdi2.getMenues().get(i).getPosition()) {
							// Schreibe das Menu an die Position von
							menualist.add(k, mainmdi2.getMenues().get(i));
							addMenu = true;
							break;
						} else if (menualist.get(k).getPosition() == mainmdi2.getMenues().get(i).getPosition()) {
							throw new MenuCompareMDIException(MessageFormat.format("menu: {0} und menu: {1} haben die gleiche Position!",
									menualist.get(k).getId(), mainmdi2.getMenues().get(i).getId()));
						}

					}
					if (!addMenu) {
						menualist.add(mainmdi2.getMenues().get(i));
					}
				}
			}
		}
		// Vektor mit den sortierten Menues füllen
		for (int z = 0; z < menualist.size(); z++) {
			sortmenues.add(menualist.get(z));
		}
		for (int i = 0; i < mainmdi2.getMenues().size(); i++) {
			mainmdi2.getMenues().get(i).setMenues(sortmenu(mainmdi2.getMenues().get(i)));
		}
		mainmdi2.setMenues(sortmenues);
	}

	private Vector<Menu> sortmenu(final Menu menu) {
		int menulistsize = 0;
		final Vector<Menu> sortmenues = new Vector<Menu>();
		final ArrayList<Menu> menualist = new ArrayList<Menu>();
		for (int i = 0; i < menu.getMenues().size(); i++) {
			if (i == 0) {
				// liste mit dem ersten element füllen.
				menualist.add(menu.getMenues().get(i));
			}
			if (i > 0) {
				if (menu.getMenues().get(i).getPosition() == 0) {
					menualist.add(0, menu.getMenues().get(i));
				} else {
					menulistsize = menualist.size();
					for (int k = 0; k < menulistsize; k++) {
						if (menualist.get(k).getPosition() > menu.getMenues().get(i).getPosition()) {
							// Schreibe das Menu an die Position von
							menualist.add(k, menu.getMenues().get(i));
						}
					}
				}
			}
		}
		// Vektor mit den sortierten Menues füllen
		for (int z = 0; z < menualist.size(); z++) {
			sortmenues.add(menualist.get(z));
		}
		// rekursion einbauen damit dieses Funktion
		for (int l = 0; l < menu.getMenues().size(); l++) {
			menu.getMenues().get(l).setMenues(sortmenu(menu.getMenues().get(l)));
		}
		return sortmenues;
	}

	/**
	 * @throws EntryCompareMDIException
	 * @throws MenuCompareMDIException
	 * @throws BaseSetupException       schreibt die Knoten und einträge aus dem Main und speichert diese dann in der angegebnenen Datei (file) @param mainDoc @param file @throws
	 *                                  NodeException @throws
	 */
	public void writeMdiToDocument(final MainDocument mainDoc, final File file)
			throws NodeException, BaseSetupException, MenuCompareMDIException, EntryCompareMDIException {
		mainmdi.writeAction(mainDoc);
		writeMdiMenuToDocument(mainDoc);
		writeToolbarToDocument(mainDoc);
		try {
			if (!file.getParentFile().exists()) {
				log(MessageFormat.format("Ordner erstellen: {0}", file.getParentFile().getAbsolutePath()));
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				log(MessageFormat.format("Datei erstellen: {0}", file.getAbsolutePath()));
				file.createNewFile();
			}

			saveMainDoc(mainDoc, file);
		} catch (final IOException e) {
			log(MessageFormat.format("Ordner erstellen schlug fehl: {0}", file.getAbsolutePath()));
			throw new BaseSetupException(MessageFormat.format(FAILSAVEDATA, file.getPath()));
		}
	}

	private void writeToolbarToDocument(final MainDocument mainDoc) throws EntryCompareMDIException {
		if (mainmdi.getToolbar() != null) {
			final int toolbararray = mainDoc.getMain().getToolbar().getEntryArray().length;
			if (mainDoc.getMain().getToolbar() != null) {
				for (int i = 0; i < toolbararray; i++) {
					mainDoc.getMain().getToolbar().removeEntry(0);
				}
			}
			sorttoolbar();
			for (int i = 0; i < mainmdi.getToolbar().getEntries().size(); i++) {
				log(MessageFormat.format("Eintrag in Toolbar: {0}", mainmdi.getToolbar().getEntries().get(i).getId()));
				mainmdi.writeNewEntryToToolbar(mainDoc.getMain().getToolbar(), mainmdi.getToolbar().getEntries().get(i));
			}
		}
	}

	private void sorttoolbar() throws EntryCompareMDIException {
		final Vector<Entry> sortentries = new Vector<Entry>();
		final ArrayList<Entry> entryalist = new ArrayList<Entry>();
		boolean addentry = false;
		int menulistsize = 0;
		for (int i = 0; i < mainmdi.getToolbar().getEntries().size(); i++) {
			addentry = false;
			if (i == 0) {
				// liste mit dem ersten element füllen.
				entryalist.add(mainmdi.getToolbar().getEntries().get(i));
			}
			if (i > 0) {
				if (mainmdi.getToolbar().getEntries().get(i).getPosition() == 0) {
					entryalist.add(0, mainmdi.getToolbar().getEntries().get(i));
				} else {
					menulistsize = entryalist.size();

					for (int k = 0; k < menulistsize; k++) {
						if (entryalist.get(k).getPosition() > mainmdi.getToolbar().getEntries().get(i).getPosition()) {
							// Schreibe das Menu an die Position von
							entryalist.add(k, mainmdi.getToolbar().getEntries().get(i));
							addentry = true;
							break;
						} else if (entryalist.get(k).getPosition() == mainmdi.getToolbar().getEntries().get(i).getPosition()) {
							if (entryalist.get(k).getPosition() != 0) {
								throw new EntryCompareMDIException(
										MessageFormat.format("Toolbar-Eintrag: {0} und Toolbar-Eintrag: {1} haben die gleiche Position: {2}!",
												entryalist.get(k).getId(), mainmdi.getToolbar().getEntries().get(i).getId(), entryalist.get(k).getPosition()));
							} else {
								log(MessageFormat.format("Toolbar-Eintrag: {0} wurde an die Position 0 geschreiben.",
										mainmdi.getToolbar().getEntries().get(i).getId().toString()), true);
								entryalist.add(0, mainmdi.getToolbar().getEntries().get(i));
								addentry = true;
							}
						}
					}
					if (!addentry) {
						entryalist.add(mainmdi.getToolbar().getEntries().get(i));
					}
				}
			}
		}
		// Vektor mit den sortierten Menues füllen
		for (int z = 0; z < entryalist.size(); z++) {
			sortentries.add(entryalist.get(z));
		}

		mainmdi.getToolbar().setEntries(sortentries);
	}

	/**
	 * schreibt die Knoten aus dem rootNode in das Document
	 *
	 * @throws IOException
	 * @throws NodeException
	 */
	public void writeXbsToDocument(final PreferencesDocument pref, final File file) throws IOException, NodeException {
		rootNodeRoot.writeEntryToRoot(pref.getPreferences(), rootNodeRoot);
		rootNodeRoot.writeNodeToRoot(pref.getPreferences(), rootNodeRoot);
		if (!file.getParentFile().exists()) {
			log(MessageFormat.format("Ordner erstellen: {0}", file.getParentFile().getAbsolutePath()));
			file.getParentFile().mkdirs();
		}
		if (!file.exists()) {
			log(MessageFormat.format("Datei erstellen: {0}", file.getAbsolutePath()));
			file.createNewFile();
		}
		// Hier wird beim Speichern eine XmlOption mit übergeben die das *.xbs
		// file formatiert
		savepref(pref, file);
	}

	/**
	 * Diese Methode ist nur von UnitTests zu benutzen!!
	 */
	public static void removeArgs() {
		deleteParms();
		BaseSetup.rootNodeRoot = new ch.minova.install.setup.xbs.Root("system");
		orderedDependingModules.clear();
		hashModules.clear();
		hashModulesActive.clear();
		hashModulesMdi.clear();
		hashModulesXbs.clear();
	}

	public static void deleteParms() {
		parameter = null;
	}

	/**
	 * Diese Methode ist nur von UnitTests zu benutzen!!
	 */
	public static Vector<BaseSetup> getOrderedModule() {
		return orderedDependingModules;
	}

	public void setSetupDocument(SetupDocument setupDocument) {
		this.setupDocument = setupDocument;
	}
}