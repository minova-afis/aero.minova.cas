package aero.minova.core.application.system.setup;

import aero.minova.core.application.system.CustomLogger;
import aero.minova.core.application.system.service.FilesService;
import aero.minova.core.application.system.sql.SystemDatabase;
import ch.minova.core.install.SetupDocument;
import ch.minova.install.setup.BaseSetup;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Optional;

/**
 * Diese Klasse installiert SQL-Code, Procedure und Schemas aus den "Setup.xml"s mithilfe des Install-Tools.
 * Dabei wurde gesorgt, dass der Code des Install-Tools möglichst wenige geändert wurde.
 */
@Service
public class InstallToolIntegration {

	@Autowired SystemDatabase systemDatabase;
	@Autowired CustomLogger logger;
	@Autowired FilesService files;

	/**
	 * Installiert eine gegebene "Setup.xml" mit dem Install-Tool.
	 * Es wird der Code möglichst so ausgeführt,
	 * als würde man das Tool mit update schema (us),
	 * update database (ud) und module only (mo).
	 * Es wird also nur die SQL-Datenbank der "Setup.xml" installiert und die Abhängkeiten ignoriert.
	 *
	 * @param setupXml Die "Setup.xml" welche installiert wird.
	 */
	public void installSetup(Path setupXml) {
		try {
			final InputStream is = new BufferedInputStream(new FileInputStream(setupXml.toFile()));
			final Connection connection = systemDatabase.getConnection();
			try {
				connection.setAutoCommit(true);
				BaseSetup.parameter = System.getProperties();
				final SetupDocument setupDocument = (SetupDocument) SetupDocument.Factory.parse(is, null);
				BaseSetup.hashModules.clear();
				BaseSetup.hashtables.clear();
				BaseSetup.tablevector.clear();
				final BaseSetup setup = new BaseSetup();
				setup.setSetupDocument(setupDocument);
				setup.readSchema();
				final ResultSet rs = connection.createStatement()
						.executeQuery("select COUNT(*) as Anzahl from INFORMATION_SCHEMA.TABLES where TABLE_NAME = 'tVersion10'");
				rs.next();
				final Optional<Path> tableLibrary = Optional.of(files.getSystemFolder().resolve("tables"));
				final Optional<Path> sqlLibrary = Optional.of(files.getSystemFolder().resolve("sql"));
				setup.readoutSchemaCreate(connection, tableLibrary, sqlLibrary);
				if (rs.getInt("Anzahl") == 0) {
					setup.readoutSchemaCreate(connection, tableLibrary, sqlLibrary);
					logger.logSql("Schema angelegt auf Datenbank: " + setupDocument.getSetup().getName());
				} else {
					setup.readoutSchema(connection, tableLibrary, sqlLibrary);
					logger.logSql("Schema aktualisiert auf Datenbank: " + setupDocument.getSetup().getName());
				}
				connection.commit();
			} finally {
				systemDatabase.freeUpConnection(connection);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
