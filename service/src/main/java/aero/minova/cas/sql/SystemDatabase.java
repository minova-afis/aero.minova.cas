package aero.minova.cas.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import com.zaxxer.hikari.pool.ProxyConnection;
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

	/**
	 * @return Die zurückgegebene Verbindung muss wieder explizit geschlossen werden.
	 * Garbage Collection schließt über den Finalizer die Verbindung nicht.
	 *
	 * Am besten wird dies über ein try-with getätigt und bei einer {@link SQLException},
	 * sollte diese als {@link RuntimeException} weiter ausgegeben werden und nicht nur gelogged werden,
	 * da dies impliziert, dass auch die SQL-Befehle nicht richtig ausgeführt wurden.
	 *
	 * {@link Statement} welche mit der Verbindung erstellt wurden,
	 * sollten auch explizit geschlossen werden,
	 * da die {@link Connection} von einem Connection-Pool kommt,
	 * welche einen {@link Connection}-Wrapper ausgibt,
	 * der bei {@link Connection#close()} nicht von diesem erstelle Objekte schließt.
	 * Das Nicht-Schließen der {@link Statement} und {@link java.sql.ResultSet} verursacht keine Probleme,
	 * da HikariCP beim {@link ProxyConnection#close()} auch alle {@link Statement} der Verbindung schließt.
	 */
	public Connection getConnection() {
		try {
			Connection connection = dataSource().getConnection();
			connection.setAutoCommit(false);
			return connection;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @deprecated Stattdessen sollte try-with verwendet werden.
	 * Zwar muss man so jeweils die Exception als RuntimeException neu schmeißen,
	 * allerdings sollte nicht angenommen werden,
	 * dass SQL erfolgreich ausgeführt werden konnte, wenn dessen Verbindung nicht geschlossen werden konnte.
	 * Zudem braucht es keine eigene Close-Methode, da {@link Connection#close()} unabhängig von der Implementierung
	 * vertrauenswürdig ist. Sollte dies nicht der Fall sein, kann man in {@link #getConnection()}
	 * einen eigenen {@link Connection}-Wrapper schreiben.
	 * @param connection
	 */
	@Deprecated
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