package aero.minova.core.application.system.sql;

import java.sql.Connection;
import java.sql.DriverManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Es ist zur Zeit immer eine SQL-Datenbank. Es ist zur Zeit nicht zwingenderweise dieselbe Datenbank, wie die default-Datenbank dieser Spring-Boot-Anwendung.
 * 
 * @author avots
 */
@Component
public class SystemDatabase {
	@Value("${aero.minova.database.url:jdbc:sqlserver://localhost;databaseName=AFIS_HAM}")
	String connectionString;

	@Value("${aero.minova.database.user.name:sa}")
	String userName;
	@Value("${aero.minova.database.user.password:Minova+0}")
	String userPassword;

	private Connection connection;

	public Connection connection() {
		try {
			if (connection == null) {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				connection = DriverManager.getConnection(connectionString, userName, userPassword);
				connection.setAutoCommit(false);
			}
			return connection;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}