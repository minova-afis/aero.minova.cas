package aero.minova.cas.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariDataSource;

import aero.minova.cas.CustomLogger;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SystemDatabase {

	@Value("${spring.datasource.url:jdbc:sqlserver://localhost;encrypt=false;databaseName=AFIS_HAM}")
	String connectionString;

	@Value("${spring.datasource.username:sa}")
	String userName;
	@Value("${spring.datasource.password:password}")
	String userPassword;
	private final EntityManager entityManager;
	private final CustomLogger customLogger;

	private static final String MSSQLDIALECT = "SQLServer";

	private HikariDataSource dataSource() {
		Map<String, Object> properties = entityManager.getEntityManagerFactory().getProperties();
		return (HikariDataSource) properties.get("javax.persistence.nonJtaDataSource");
	}

	public Connection getConnection() {
		try {
			Connection connection = dataSource().getConnection();
			connection.setAutoCommit(false);
			return connection;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void closeConnection(Connection connection) {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			customLogger.logError("Connection '" + connection + "' could not be closed: ", e);
		}
	}

	public DataSource getDataSource() throws SQLException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		dataSource.setUrl(connectionString);
		dataSource.setUsername(userName);
		dataSource.setPassword(userPassword);
		return dataSource;
	}

	public void softEvictConnections() {
		dataSource().getHikariPoolMXBean().softEvictConnections();
	}

	/**
	 *
	 * @return Gibt wahr zurück, wenn der JDBC-Dialekt-ID den String "SQLServer" beinhalten und somit der SQL-Code MSSQL-Server kompatibel ist.
	 * Zu beachten ist, dass die Methode bei H2-Datenbanken falsch zurückgibt.
	 */
	public boolean isSQLDatabase() {
		final Session session = (Session) entityManager.getDelegate();
		final SessionFactoryImpl sessionFactory = (SessionFactoryImpl) session.getSessionFactory();
		final String dialect = sessionFactory.getJdbcServices().getDialect().toString();
		return dialect.contains(MSSQLDIALECT);
	}
}