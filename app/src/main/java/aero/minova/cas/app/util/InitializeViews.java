package aero.minova.cas.app.util;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import aero.minova.cas.CustomLogger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class InitializeViews {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private DataSource dataSource;

	@Autowired
	CustomLogger customLogger;

	private static final String MSSQLDIALECT = "SQLServer".toLowerCase();
	private static final String POSTGRES = "postgres".toLowerCase();
	private static final String H2 = "h2".toLowerCase();

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@EventListener(ApplicationReadyEvent.class)
	public void loadData() {

		final Session session = (Session) entityManager.getDelegate();
		final SessionFactoryImpl sessionFactory = (SessionFactoryImpl) session.getSessionFactory();
		final String dialect = sessionFactory.getJdbcServices().getDialect().toString();

		// Aktuell nur views einspielen wenn Postgres oder H2 Datenbank genutzt wird
		if (dialect.toLowerCase().contains(MSSQLDIALECT)) {
			logger.warn("SQL Views aren't created automatically, execute setup!");
			return;
		} else if (!dialect.toLowerCase().contains(POSTGRES) && !dialect.toLowerCase().contains(H2)) {
			logger.warn("Unkown JDBC Dialect {}, can't create views", dialect);
			return;
		}

		List<String> viewnames = new ArrayList<>();
		viewnames.add("xvcasColumnSecurityIndex.sql");
		viewnames.add("xvcasMdiIndex.sql");
		viewnames.add("xvcasServicePropertiesIndex.sql");
		viewnames.add("xvcasUserGroupIndex.sql");
		viewnames.add("xvcasUserIndex.sql");
		viewnames.add("xvcasUserIndex2.sql");
		viewnames.add("xvcasUserPrivilegeIndex.sql");
		viewnames.add("xvcasUserSecurity.sql");
		viewnames.add("xvcasUsersIndex.sql");
		viewnames.add("xvcasUsersIndex2.sql");

		for (String s : viewnames) {
			ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(false, false, "UTF-8", new ClassPathResource("postgres/" + s));
			resourceDatabasePopulator.execute(dataSource);
		}
	}
}