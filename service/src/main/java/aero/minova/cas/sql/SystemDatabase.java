package aero.minova.cas.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariDataSource;

import aero.minova.cas.CustomLogger;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SystemDatabase {

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