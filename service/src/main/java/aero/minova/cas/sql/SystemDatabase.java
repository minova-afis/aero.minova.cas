package aero.minova.cas.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

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

	public DataSource getDataSource() throws SQLException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		dataSource.setUrl(connectionString);
		dataSource.setUsername(userName);
		dataSource.setPassword(userPassword);
		return dataSource;
	}
}