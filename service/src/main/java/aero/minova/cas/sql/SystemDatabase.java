package aero.minova.cas.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

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

	public Connection getConnection() {
		try {
			Map<String, Object> properties = entityManager.getEntityManagerFactory().getProperties();
			HikariDataSource dataSource = (HikariDataSource) properties.get("javax.persistence.nonJtaDataSource");

			Connection connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			return connection;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void closeConnection(Connection connection) {
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			customLogger.logError("Connection '" + connection + "' could not be closed: ", e);
		}
	}
}