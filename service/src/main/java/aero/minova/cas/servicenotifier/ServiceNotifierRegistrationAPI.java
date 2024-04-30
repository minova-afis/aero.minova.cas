package aero.minova.cas.servicenotifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;

@Service
public class ServiceNotifierRegistrationAPI {

	@Autowired
	CustomLogger logger;

	@Autowired
	ServiceNotifierService notifierService;

	public void registerService(String keyText, String serviceURL, int port, int serviceMessageReceiverLoginType, String username, String password,
			String clientID, String clientSecret, String tokenURL) {
		try {
			Table casServiceTable = new Table();
			casServiceTable.setName("xpcasRegisterCASService");

			casServiceTable.addColumn(new Column("KeyText", DataType.STRING));
			casServiceTable.addColumn(new Column("ServiceURL", DataType.STRING));
			casServiceTable.addColumn(new Column("Port", DataType.INTEGER));
			casServiceTable.addColumn(new Column("ServiceMessageReceiverLoginType", DataType.STRING));
			casServiceTable.addColumn(new Column("Username", DataType.STRING));
			casServiceTable.addColumn(new Column("Password", DataType.STRING));
			casServiceTable.addColumn(new Column("ClientID", DataType.STRING));
			casServiceTable.addColumn(new Column("ClientSecret", DataType.STRING));
			casServiceTable.addColumn(new Column("TokenURL", DataType.STRING));

			Row registerServiceRow = new Row();
			registerServiceRow.addValue(new Value(keyText, null));
			registerServiceRow.addValue(new Value(serviceURL, null));
			registerServiceRow.addValue(new Value(port, null));
			registerServiceRow.addValue(new Value(serviceMessageReceiverLoginType, null));
			registerServiceRow.addValue(new Value(username, null));
			registerServiceRow.addValue(new Value(password, null));
			registerServiceRow.addValue(new Value(clientID, null));
			registerServiceRow.addValue(new Value(clientSecret, null));
			registerServiceRow.addValue(new Value(tokenURL, null));

			casServiceTable.addRow(registerServiceRow);

			notifierService.registerService(casServiceTable);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Regstriert ein Topic auf diesen Dienst.
	 * 
	 * @param topic
	 *            Topic, für welches dieser Dienst eine Nachricht bekommen soll.
	 */
	public void registerNewsfeedListener(String topic, String serviceName) {
		try {
			Table newsfeedListenerTable = new Table();
			newsfeedListenerTable.setName("xpcasRegisterNewsfeedListener");

			newsfeedListenerTable.addColumn(new Column("CASServiceName", DataType.STRING));
			newsfeedListenerTable.addColumn(new Column("Topic", DataType.STRING));

			Row registerNewsfeedRow = new Row();
			registerNewsfeedRow.addValue(new Value(serviceName, null));
			registerNewsfeedRow.addValue(new Value(topic, null));

			newsfeedListenerTable.addRow(registerNewsfeedRow);

			notifierService.registerNewsfeedListener(newsfeedListenerTable);

		} catch (Exception e) {
			logger.logError("The Newsfeedlistener could not be registered for topic " + topic + " and service " + serviceName, e);
		}
	}

	/**
	 * Regstiert, welche Ausführung von Prozeduren welche Topics triggern.
	 * 
	 * @param procedureName
	 *            Prozedurname, welche ausgeführt wird.
	 * @param topic
	 *            Topic, das angetriggert wird.
	 */
	public void registerProcedureNewsfeed(String procedureName, String topic) {
		try {
			Table newsfeedTable = new Table();
			newsfeedTable.setName("xpcasRegisterProcedureNewsfeed");

			newsfeedTable.addColumn(new Column("KeyText", DataType.STRING));
			newsfeedTable.addColumn(new Column("Topic", DataType.STRING));

			Row registerNewsfeedRow = new Row();
			registerNewsfeedRow.addValue(new Value(procedureName, null));
			registerNewsfeedRow.addValue(new Value(topic, null));

			newsfeedTable.addRow(registerNewsfeedRow);

			notifierService.registerProcedureNewsfeed(newsfeedTable);

		} catch (Exception e) {
			logger.logError("The Newsfeedlistener could not be registered for topic " + topic + " and procedure " + procedureName, e);
		}
	}
}
