package aero.minova.cas.override;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.OutputType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.SqlProcedureResult;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.controller.SqlProcedureController;

public class OverrideService {

	@Autowired
	SqlProcedureController spc;

	@Autowired
	CustomLogger logger;

	@PostConstruct
	private void setup() {
		spc.registerExtension("registerService", inputTable -> {
			try {
				int keyLong = registerService(inputTable);
				return new ResponseEntity(keyLong, HttpStatus.ACCEPTED);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		spc.registerExtension("unregisterService", inputTable -> {
			try {
				int keyLong = registerService(inputTable);
				return new ResponseEntity(keyLong, HttpStatus.ACCEPTED);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		spc.registerExtension("registerProcedureNewsfeed", inputTable -> {
			try {
				return new ResponseEntity(HttpStatus.ACCEPTED);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

	/**
	 * Registriert einen Dienst beim CAS und schreibt die dazugehörigen Daten in die xtcasCASServices Tabelle. Die übergebene Table muss dafür in der ersten
	 * Spalte den Namen des Dienstes beinhalten, in der Zweiten die ServiceURL und in der Dritten den Port.
	 * 
	 * @param inputTable
	 *            Die Table, welche in der ersten Spalte den Dienstnamen, in der Zweiten die ServiceURL und in der dritten Column den Port benötigt.
	 * @return Den KeyLong des Dienstes als int.
	 */
	public int registerService(Table inputTable) {
		SqlProcedureResult registerResult = new SqlProcedureResult();

		Table registerSerivceTable = new Table();
		registerSerivceTable.setName("xpcasInsertCASService");
		registerSerivceTable.addColumn(new Column("KeyLong", DataType.INTEGER, OutputType.OUTPUT));
		registerSerivceTable.addColumn(new Column("KeyText", DataType.STRING));
		registerSerivceTable.addColumn(new Column("ServiceURL", DataType.STRING));
		registerSerivceTable.addColumn(new Column("Port", DataType.INTEGER));

		Row registerRow = new Row();
		registerRow.addValue(null);
		registerRow.addValue(inputTable.getRows().get(0).getValues().get(0));
		registerRow.addValue(inputTable.getRows().get(0).getValues().get(1));
		registerRow.addValue(inputTable.getRows().get(0).getValues().get(2));

		registerSerivceTable.addRow(registerRow);
		try {
			registerResult = spc.unsecurelyProcessProcedure(registerSerivceTable);
		} catch (Exception e) {
			logger.logError("The service " + inputTable.getRows().get(0).getValues().get(0).getStringValue() + " could not be registered", e);
			throw new RuntimeException(e);
		}

		return registerResult.getOutputParameters().getRows().get(0).getValues().get(0).getIntegerValue();
	}

	/**
	 * Löscht den Eintrag des Dienstes aus der Datenbank und meldet ihn vom CAS ab.
	 * 
	 * @param inputTable
	 *            eine Table, welche in der ersten Spalte den Dienstnamen, in der Zweiten die ServiceURL und in der dritten Column den Port benötigt.
	 */
	public void unregisterService(Table inputTable) {
		Table unregisterSerivceTable = new Table();
		unregisterSerivceTable.setName("xpcasDeleteCASService");
		unregisterSerivceTable.addColumn(new Column("KeyLong", DataType.INTEGER, OutputType.OUTPUT));
		unregisterSerivceTable.addColumn(new Column("KeyText", DataType.STRING));
		unregisterSerivceTable.addColumn(new Column("ServiceURL", DataType.STRING));
		unregisterSerivceTable.addColumn(new Column("Port", DataType.INTEGER));

		Row unregisterRow = new Row();
		unregisterRow.addValue(null);
		unregisterRow.addValue(inputTable.getRows().get(0).getValues().get(0));
		unregisterRow.addValue(inputTable.getRows().get(0).getValues().get(1));
		unregisterRow.addValue(inputTable.getRows().get(0).getValues().get(2));

		unregisterSerivceTable.addRow(unregisterRow);
		try {
			spc.unsecurelyProcessProcedure(unregisterSerivceTable);
		} catch (Exception e) {
			logger.logError("The service " + inputTable.getRows().get(0).getValues().get(0).getStringValue() + " could not be unregistered!", e);
			throw new RuntimeException(e);
		}
	}

}
