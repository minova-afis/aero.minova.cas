package aero.minova.cas.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import aero.minova.cas.CustomLogger;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SystemDatabase {
	private final EntityManager entityManager;
	private final CustomLogger customLogger;

	public Connection getConnection() {
		try {
			Map<String, Object> properties = entityManager.getEntityManagerFactory().getProperties();
			HikariDataSource dataSource = (HikariDataSource) properties.get("javax.persistence.nonJtaDataSource");
			Connection conn = dataSource.getConnection();

			conn.setAutoCommit(false);
			return dataSource.getConnection();
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