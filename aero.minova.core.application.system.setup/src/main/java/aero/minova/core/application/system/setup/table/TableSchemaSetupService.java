package aero.minova.core.application.system.setup.table;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;

/**
 * Das ist dr Table-Schema-Installer,
 * welcher auf dem internen Install-Tool der Minova basiert.
 */
@Service
public class TableSchemaSetupService {
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
	private boolean readoutSchema(final Connection con) throws org.apache.xmlbeans.XmlException, IOException, BaseSetupException, SQLException {
		checktVersion10(con);
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

					final InputStream is = readFromJarFileToInputStream(getVersionInfo().getModulName(), "tables", tablevector.get(i).getName() + ".table.xml");
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
					final InputStream is = readFromJarFileToInputStream(getVersionInfo().getModulName(), "tables", tablevector.get(i).getName() + ".table.xml");
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
					final InputStream is = readFromJarFileToInputStream(getVersionInfo().getModulName(), "tables", tablevector.get(i).getName() + ".table.xml");
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
					final InputStream is = readFromJarFileToInputStream(getVersionInfo().getModulName(), "tables", tablevector.get(i).getName() + ".table.xml");
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

	private void checktVersion10(Connection con) {
		throw new RuntimeException("TODO");
	}
}
