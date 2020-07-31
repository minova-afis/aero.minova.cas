package aero.minova.core.application.system.sql;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Es ist zur Zeit immer eine SQL-Datenbank. Es ist zur Zeit nicht zwingenderweiße die selbe Datenbank, wie die default-Datenbank dieser Spring-Boot-Anwendung.
 * 
 * @author avots
 */
public class SystemDatabase {

	private SystemDatabase() {
		throw new UnsupportedOperationException();
	}

	public static Connection connection() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			return DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=AFIS_HAM", "sa", "Minova+0");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
