package aero.minova.core.application.system.setup.table;

import aero.minova.core.application.system.CustomLogger;
import aero.minova.core.application.system.setup.ModuleNotFoundException;
import aero.minova.core.application.system.sql.SystemDatabase;
import ch.minova.core.install.SetupDocument;
import ch.minova.core.install.TableschemaDocument;
import ch.minova.install.setup.BaseSetup;
import ch.minova.install.setup.schema.SqlDatabase;
import ch.minova.install.setup.schema.SqlDatabaseTable;
import ch.minova.install.setup.schema.XmlDatabaseTable;
import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.rowset.RowSetWarning;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
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

	@Autowired SystemDatabase systemDatabase;
	@Autowired CustomLogger logger;

	public void setupTableSchemas(Path setupXml) {
		try {
			final InputStream is = new BufferedInputStream(new FileInputStream(setupXml.toFile()));
			final Connection connection = systemDatabase.getConnection();
			try {
				final SetupDocument setupDocument = (SetupDocument) SetupDocument.Factory.parse(is, null);
				final BaseSetup setup = new BaseSetup();
				setupDocument.getSetup().getName();
			} finally {
				systemDatabase.freeUpConnection(connection);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
