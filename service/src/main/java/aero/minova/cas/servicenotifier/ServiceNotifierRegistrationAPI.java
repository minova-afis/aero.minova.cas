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

	// TODO: In Table
	/**
	 * Sucht anhand des Spaltennamens an welcher Position sich diese befindet und gibt die Stelle zurück. Falls der Name nicht vorkommt, wird eine
	 * IllegalArgumentException geworfen.
	 * 
	 * @param t
	 *            Tabelle, in welcher gesucht werden soll.
	 * @param columnName
	 *            Spaltenname, nach welchem gesucht werden soll.
	 * @return Die Position als int.
	 */
	public int findColumnPosition(Table t, String columnName) {
		for (int i = 0; i < t.getColumns().size(); i++) {
			if (t.getColumns().get(i).getName().toLowerCase().equals(columnName.toLowerCase())) {
				return i;
			}
		}
		throw new IllegalArgumentException(
				"Column name " + columnName + " could not be found for procedure " + t.getName() + "! No message can be created with this input table!");
	}

}
