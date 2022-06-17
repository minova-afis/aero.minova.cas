package aero.minova.cas.servicenotifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.controller.SqlProcedureController;

@Service
public class ServiceNotifierRegistrationAPI {

	@Autowired
	SqlProcedureController spc;

	@Autowired
	CustomLogger logger;

	/**
	 * Regstriert ein Topic auf diesen Dienst.
	 * 
	 * @param topic
	 *            Topic, für welches dieser Dienst eine Nachricht bekommen soll.
	 */
	public void registerNewsfeedListener(String topic, String serviceName) {
		Table newsfeedTable = new Table();
		newsfeedTable.setName("xpcasRegisterNewsfeedListener");

		newsfeedTable.addColumn(new Column("CASServiceName", DataType.STRING));
		newsfeedTable.addColumn(new Column("Topic", DataType.STRING));

		Row registerNewsfeedRow = new Row();
		registerNewsfeedRow.addValue(new Value(serviceName, null));
		registerNewsfeedRow.addValue(new Value(topic, null));

		newsfeedTable.addRow(registerNewsfeedRow);

		try {
			spc.executeProcedure(newsfeedTable);
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
		Table newsfeedTable = new Table();
		newsfeedTable.setName("xpcasRegisterProcedureNewsfeed");

		newsfeedTable.addColumn(new Column("KeyText", DataType.STRING));
		newsfeedTable.addColumn(new Column("Topic", DataType.STRING));

		Row registerNewsfeedRow = new Row();
		registerNewsfeedRow.addValue(new Value(procedureName, null));
		registerNewsfeedRow.addValue(new Value(topic, null));

		newsfeedTable.addRow(registerNewsfeedRow);

		try {
			spc.executeProcedure(newsfeedTable);
		} catch (Exception e) {
			logger.logError("The Newsfeedlistener could not be registered for topic " + topic + " and procedure " + procedureName, e);
		}
	}
}
