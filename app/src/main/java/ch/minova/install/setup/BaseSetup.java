package ch.minova.install.setup;

import static aero.minova.cas.resources.ResourceFileSystemProvider.FILE_SYSTEM_PROVIDER;
import static aero.minova.cas.setup.xml.setup.ScriptType.TYPE_FUNCTION;
import static aero.minova.cas.setup.xml.setup.ScriptType.TYPE_PROCEDURE;
import static aero.minova.cas.setup.xml.setup.ScriptType.TYPE_SCRIPT;
import static aero.minova.cas.setup.xml.setup.ScriptType.TYPE_TABLE;
import static aero.minova.cas.setup.xml.setup.ScriptType.TYPE_VIEW;
import static java.nio.file.Files.readAllBytes;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.setup.xml.setup.ScriptType;
import aero.minova.cas.setup.xml.setup.SetupType;
import aero.minova.cas.setup.xml.setup.TableschemaType;
import ch.minova.install.setup.schema.SqlDatabase;
import ch.minova.install.setup.schema.SqlDatabaseTable;
import ch.minova.install.setup.schema.XmlDatabaseColumn;
import ch.minova.install.setup.schema.XmlDatabaseTable;
import ch.minova.install.sql.TVersion;

/**
 * Basisklasse für die Installer der Module
 *
 * @author erlanger
 */
public class BaseSetup {

	public static final String MISSINGFILE = "Die Datei {0} wurde nicht gefunden.";

	public static Vector<TableVector> tablevector = new Vector<TableVector>();
	public static Hashtable<String, String> hashtables = new Hashtable<String, String>();
	public static Hashtable<String, VersionInfo> hashModules = new Hashtable<String, VersionInfo>();
	private static Vector<BaseSetup> orderedDependingModules = new Vector<BaseSetup>();

	private static String sqldialect = "sql";
	private Connection connection = null;
	private HashMap<String, String> mymap;
	private SetupType setupDocument;
	protected VersionInfo versionInfo;

	public static Properties parameter = null;

	public static final String FAILTOWRITENODETODOCUMENT = "Error: Der Knoten {0} konnte nicht in die Datei eingetragen werden!";

	CustomLogger logger;

	public BaseSetup(CustomLogger logger) {
		this.logger = logger;
	}

	public boolean readoutSchemaCreate(final Connection con) throws IOException, BaseSetupException {
		return readoutSchemaCreate(con, Optional.empty(), Optional.empty());
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
				logger.logError(e);
			} catch (final SQLException e) {
				log(MessageFormat.format("Error SQLException: {0}", e.getMessage()));
				logger.logError(e);
			}
		}
	}

	public boolean readoutSchemaCreate(final Connection con, Optional<Path> tableLibrary, Optional<Path> sqlLibrary) throws IOException, BaseSetupException {
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
		} catch (final Exception e) {
			log(MessageFormat.format("Exception create tables \n {0}", e.getMessage()), true);
			throw new RuntimeException(e);
		}
		sqldatabase = new SqlDatabase();
		// checkConnection(null);
		sqlTable = null;
		sqlCode = null;
		xmlTable = null;
		try {
			sqldatabase.readDataBase(this.connection);
		} catch (final SQLException e1) {
			log(MessageFormat.format("Error SQLException: {0}", e1.getMessage()));
			logger.logError(e1);
		} catch (final InstantiationException e1) {
			log(MessageFormat.format("Error InstantiationException: {0}", e1.getMessage()));
			logger.logError(e1);
		} catch (final IllegalAccessException e1) {
			log(MessageFormat.format("Error IllegalAccessException: {0}", e1.getMessage()));
			logger.logError(e1);
		} catch (final ClassNotFoundException e1) {
			log(MessageFormat.format("Error ClassNotFoundException: {0}", e1.getMessage()));
			logger.logError(e1);
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
					throw new RuntimeException(e);
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
					throw new RuntimeException(e);
				}
			}
		}
		return false;
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

	private String readSqlScript(String name, Path sqlLibrary) {
		try {
			return new String(readAllBytes(sqlLibrary.resolve(name + ".sql")), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String readSqlFromJarFileToString(final String moduleName, final String folderPath, final String fileName, Optional<Path> sqlLibrary)
			throws IOException {
		if (sqlLibrary.isPresent()) {
			return new String(readAllBytes(sqlLibrary.get().resolve(fileName)), StandardCharsets.UTF_8);
		}
		final String completeName = moduleName.replaceAll("\\.", "/") + "/" + folderPath + "/" + fileName;
		try {
			final InputStream inputStream = readFromJarFileToInputStream(moduleName, folderPath, fileName, sqlLibrary);
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
			logger.logError(npe);
		} catch (final IOException e) {
			log(MessageFormat.format("Error IOException: {0}", e.getMessage()));
			logger.logError(e);
		}
		return null;
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
		return readSqlFromJarFileToString(moduleName, folderPath, fileName, Optional.empty());
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
		return readFromJarFileToInputStream(moduleName, folderPath, fileName, Optional.empty());
	}

	private InputStream readFromJarFileToInputStream(final String moduleName, final String folderPath, final String fileName, Optional<Path> sqlLibrary) {
		if (sqlLibrary.isPresent()) {
			try {
				return new FileInputStream(sqlLibrary.get().resolve(moduleName).toString());
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
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
			logger.logError(e);
		} finally {
			if (jFile != null) {
				try {
					// schließt auch alle selbst geöffneten InputStreams
					jFile.close();
				} catch (final IOException e) {
					logger.logError(e);
				}
			}
		}

		if (sqlIn == null) {
			// Dann versuchen wir es über das Dateisystem (eher für Debugging)
			try {
				sqlIn = getClass().getResourceAsStream("../" + folderPath + "/" + fileName);
			} catch (final NullPointerException npe) {
				sqlIn = null;
				logger.logError(npe);
			}
		}

		if (sqlIn == null) {
			log(MessageFormat.format("Die Datei: {0} ist nicht in {1} zu finden!", completeName, moduleName + ".jar"), true);
		}
		return sqlIn;
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
	protected void log(final String message, final boolean forceOutput) {
		if (forceOutput || (parameter.containsKey("verbose") || parameter.containsKey("v"))) {
			logger.logSetup(message);
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
			logger.logError(e);
			throw new SQLException(e);
		}
	}

	private InputStream readTableXml(String tableName, Optional<Path> tableLibrary) {
		if (tableLibrary.isEmpty()) {
			return readFromJarFileToInputStream(getVersionInfo().getModulName(), "tables", tableName + ".table.xml");
		} else {
			try {
				if (true) {
					final Optional<Path> tableXml = FILE_SYSTEM_PROVIDER.walk(tableLibrary.get()).stream().map(path -> {
						// TODO Einheitliche XML-Namen verwenden.
						if (Files.isRegularFile(path) && path.getFileName().toString().equals(tableName + ".table.xml")
								|| path.getFileName().toString().equals(tableName + ".xml")) {
							return Optional.of(path);
						}
						return Optional.<Path> empty();
					})//
							.filter(path -> !path.isEmpty()).map(path -> path.get()).findFirst();
					if (tableXml.isEmpty()) {
						throw new RuntimeException("Table xml " + tableName + "not found.");
					}
					return Files.newInputStream(tableXml.get());
				} else {
					final Optional<Path> tableXml = Files.walk(tableLibrary.get()).map(path -> {
						// TODO Einheitliche XML-Namen verwenden.
						if (Files.isRegularFile(path) && path.getFileName().toString().equals(tableName + ".table.xml")
								|| path.getFileName().toString().equals(tableName + ".xml")) {
							return Optional.of(path);
						}
						return Optional.<Path> empty();
					})//
							.filter(path -> !path.isEmpty()).map(path -> path.get()).findFirst();
					if (tableXml.isEmpty()) {
						throw new RuntimeException("Table xml " + tableName + "not found.");
					}
					return Files.newInputStream(tableXml.get());
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
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
			logger.logError(e);
		}
		return "null";
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

	private String generateUpdateTableConstraintsPK_UK(final SqlDatabaseTable sqlTable, final XmlDatabaseTable xmlTable) throws SQLException {
		return xmlTable.compareConstrainsPK_UK(sqlTable, this.connection, this.mymap);
	}

	private String generateUpdateTableConstraintsFK(final SqlDatabaseTable sqlTable, final XmlDatabaseTable xmlTable) throws SQLException {
		return xmlTable.compareConstrainsFK(sqlTable, this.connection, this.mymap);
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
			logger.logError(e);
		}

		return new ByteArrayInputStream(sb.toString().getBytes());
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

	public boolean readoutSchema(final Connection con) throws IOException, BaseSetupException, SQLException {
		return readoutSchema(con, Optional.empty(), Optional.empty());
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
			throws IOException, BaseSetupException, SQLException {
		checktVersion10(con, sqlLibrary);
		SqlDatabase sqldatabase = new SqlDatabase();
		XmlDatabaseTable xmlTable = null;
		SqlDatabaseTable sqlTable = null;
		String sqlCode = null;
		SetupType doc = getSetupDocument();
		if (doc.getSchema() == null) {
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
					if (doc.getSchema().get(i).getName().equalsIgnoreCase(tablevector.get(i).getName())) {
						// soll das script vorher ausgeführt werden
						if (doc.getSchema().get(i).getExecute().toString().equalsIgnoreCase("before")) {
							execSqlScripts(tablevector.get(i).getName(), sqlLibrary);
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
				if (doc.getSchema().get(i).getName().equalsIgnoreCase(tablevector.get(i).getName())) {
					// soll das script vorher ausgeführt werden
					if (doc.getSchema().get(i).getExecute().toString().equalsIgnoreCase("after")) {
						try {
							execSqlScripts(tablevector.get(i).getName(), sqlLibrary);
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
	 * auslesen der Setup.xml Datei des Modul. Dabei wird eine SetupDocumet erstellt, welche auch zurückgegebn wird
	 *
	 * @return
	 * @throws IOException
	 * @throws BaseSetupException
	 */
	private SetupType getSetupDocument() throws IOException, BaseSetupException {
		if (setupDocument != null) {
			return setupDocument;
		}
		throw new IllegalStateException("Getting `Setup.xml` via class resource was removed with version 12.");
	}

	private void execSqlScripts(final String tablename, Optional<Path> sqlLibrary) throws IOException, SQLException {
		final String sqlScript = readSqlFromJarFileToString(getVersionInfo().getModulName(), sqldialect, tablename + ".sql", sqlLibrary);
		log(MessageFormat.format("{0} Ausführen des Scripts: \n {1}", tablename, sqlScript));
		executeSqlScript(sqlScript);
	}

	/**
	 * Ausführen und Einlesen der Sql-Scripts, dabei werden zwischen 4 unterschiedlichen Typen unterschieden. View, Script, Procedure und Function
	 *
	 * @param con
	 * @throws IOException
	 * @throws BaseSetupException
	 * @throws SQLException
	 * @throws SQLExeption
	 */
	public void handleSqlScripts(final Connection con) throws IOException, BaseSetupException, SQLException, SQLExeption {
		handleSqlScripts(con, Optional.empty());
	}

	public void handleSqlScripts(final Connection con, final Optional<Path> sqlLibrary) throws IOException, BaseSetupException, SQLException, SQLExeption {
		SetupType doc;
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
		if (doc.getSqlCode() != null) {
			final List<ScriptType> scripts = doc.getSqlCode();
			for (int i = 0; i < scripts.size(); i++) {
				final ScriptType scp = scripts.get(i);
				final String name = scp.getName();
				final String type = scp.getType();
				log(MessageFormat.format("Script: {0}, Type= {1}", name, type.toString()));
				// connection = checkConnection(null);
				// falls es Versions gibt wird überprüft!
				final String sqlScript = readSqlFromJarFileToString(getVersionInfo().getModulName(), sqldialect, name + ".sql", sqlLibrary);
				log(sqlScript, false);
				if (type.equals(TYPE_SCRIPT)) {
					executeSqlScript(sqlScript);
				} else if (type.equals(TYPE_TABLE)) {
					readSQLOfOrderedModules();
				} else if (type.equals(TYPE_PROCEDURE) || type.equals(TYPE_VIEW) || type.equals(TYPE_FUNCTION)) {
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

	public void readSQLOfOrderedModules() throws IOException, BaseSetupException, SQLException {
		SetupType setup = null;

		final BaseSetup[] a = orderedDependingModules.toArray(new BaseSetup[0]);

		for (final BaseSetup bs : a) {
			try {
				setup = bs.getSetupDocument();
				log(MessageFormat.format("Aus der Setup.xml Datei wird der sql-Code ausgelesen Modul:{0}_{1}", bs.getVersionInfo().getModulName(),
						bs.getVersionInfo().toString()));
			} catch (final BaseSetupException e) {
				logger.logError(e);
				throw new BaseSetupException(e.getMessage() + setup.getClass().getName());
			}
		}
	}

	/**
	 * @param sqlScript
	 * @param name
	 * @param versionInfo2
	 *            (wird derzeit nicht verwendet)
	 * @param type
	 * @param con
	 * @throws SQLException
	 */
	private void runScript(final String sqlScript, final String name, final VersionInfo versionInfo2, final String type, final Connection con)
			throws SQLException {
		if (type.equals(TYPE_FUNCTION)) {
			executeSqlFunction(sqlScript, name, true, con);
		} else if (type.equals(TYPE_PROCEDURE)) {
			executeSqlProcedure(sqlScript, name, true, con);
		} else if (type.equals(TYPE_VIEW)) {
			executeSqlView(sqlScript, name, true, con);
		}
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
					log(MessageFormat.format("Could not check tVersion10 via {0}: {1}", sqlScript, e.getMessage()), true);
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
			throw new SQLException(e);
		}
	}

	/**
	 * Diese Methode liest die eingelesene xbs Datei aus Dabei werden die Einträge connection2 und den Driver aus.
	 */
	public void connect() {
		throw new RuntimeException("BaseSEtup#connect not implemented yet.");
	}

	/**
	 * Auslesen des Modulenamen inlusive der zugehoerigen Versionsnummer
	 *
	 * @param clazz
	 *            BaseSetup
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

	/**
	 * In dieser Methode wird entschieden welche Connection.xbs für den Verbindungsaufbau verwendet wird. Wird als Übergabeparameter "null" übergeben, dann wird
	 * die Standard Connection.xbs ausgewählt, andernfalls wird der String der übergeben wird Überprüft und die entsprechende Datenabnkverbindung wird aufgebaut
	 * / überprüft.
	 *
	 * @param pathToFile
	 * @return
	 */
	public Connection checkConnection(final String pathToFile) {
//		this.connection = checkConnectionSSPIJNIClient();
		log("checkConnectionSSPIJNIClient");
		if (this.connection != null) {
			return this.connection;
		}
		if (pathToFile == null) {
			this.connection = checkConnection();
		} else {
			/*
			 * try { readEveryXbsFile(pathToFile, "connection.xbs"); connect(); } catch (final BaseSetupException e) { System.err.println(e.getMessage()); throw
			 * new RuntimeErrorException(new Error(e)); }
			 */
		}
		return this.connection;
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
	 * Mit dieser Methode überprüfen wir mal die Verbindung zur eingegebenen Datenbank. Wir verbinden uns mit dem Treiber und dem Connection-String
	 * (connection2). Benutzer und Kennwort werden als Parameter übergeben.
	 */
	public Connection checkConnection() {
		throw new RuntimeException("BaseSetup#checkConnection not implemented yet.");
	}

	/**
	 * Diese Methode liest die gesamten Tables aus. Tables werden in der Setup.xml Datei mit aufgelistet. Sie stehen unter dem Namen: <schema> und werden unter
	 * in einer HashTable aufgelistst.
	 *
	 * @return
	 * @throws IOException
	 * @throws BaseSetupException
	 * @throws ModuleNotFoundException
	 * @throws SQLException
	 */
	public boolean readSchema() throws IOException, BaseSetupException, ModuleNotFoundException, SQLException {
		final SetupType doc = getSetupDocument();
		if (doc.getSchema() != null) {
			final List<TableschemaType> tables = doc.getSchema();
			for (int i = 0; i < tables.size(); i++) {
				if (!hashtables.containsKey(tables.get(i))) {
					tablevector.add(new TableVector(tables.get(i).getName(), tables.get(i).getType()));
					hashtables.put(tables.get(i).getName().toLowerCase(), tables.get(i).getName());
					log(MessageFormat.format("Table: {0}", tables.get(i).getName()));
				}
			}
			return true;
		}
		return false;
	}

	public void setSetupDocument(SetupType setupDocument) {
		this.setupDocument = setupDocument;
	}

}