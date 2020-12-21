package aero.minova.trac.integration.controller;

import aero.minova.core.application.system.controller.SqlViewController;
import aero.minova.core.application.system.domain.*;
import aero.minova.trac.integration.TracTicketIntegration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

/**
 * @author wild
 * @since 12.4.0
 */
@RestController
public class TracController {
	// Felder aus dem Ticket
	public static final String TICKET_ID = "TicketKey";
	public static final String TICKET_ORDERRECEIVER = "OrderReceiver";
	public static final String TICKET_SERVICECONTRACT = "ServiceContract";
	public static final String TICKET_SERVICEOBJECT = "ServiceObject";
	public static final String TICKET_SERVICE = "Service";
	public static final String TICKET_DESCRIPTION = "Description";
	// Felder aus der Datenbank
	public static final String ORDERRECEIVER_KEY = "OrderReceiverKey";
	public static final String SERVICECONTRACT_KEY = "ServiceContractKey";
	public static final String SERVICEOBJECT_KEY = "ServiceObjectKey";
	public static final String SERVICE_KEY = "ServiceKey";

	private final TracTicketIntegration tracIntegration = new TracTicketIntegration();

	@Autowired
	SqlViewController svc;

	@GetMapping(value = "data/ticket", produces = "application/json")
	public Table getTicket(@RequestParam String ticketNo) {
		Table ticketTable = null;

		try {
			int ticketNumber = Integer.parseInt(ticketNo);
			ticketTable = fetchFromTrac(ticketNumber);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(MessageFormat.format("Ticketnummer {0} ist nicht numerisch", ticketNo), ex);
		}

		return ticketTable != null ? ticketTable : createEmptyTicketTable();
	}

	private Table fetchFromTrac(int ticketNumber) {
		Table ticketTable = createEmptyTicketTable();
		tracIntegration.setTicketNumber(ticketNumber);
		if (ticketNumber == -123) {
			ticketTable
					.addRow(ticketInformation(-123, "MIN", "WFC", "LOHN", "ZPROGRAM", "#37: Trac-Ticket Dummy Implementierung auf publictest bereitstellen."));
		} else {
			if (tracIntegration.getStatus() == TracTicketIntegration.TICKET_OK) {
				ticketTable.addRow(//
						ticketInformation(//
								ticketNumber//
								, tracIntegration.getOrderReceiver()//
								, tracIntegration.getServiceContract()//
								, tracIntegration.getServiceObject()//
								, tracIntegration.getService()//
								, tracIntegration.getDescription()));
			}
		}
		return ticketTable;
	}

	private Row ticketInformation(int ticketNumber, String orderReceiver, String serviceContract, String serviceObject, String service, String description) {
		final Row ticketInformation = new Row();
		ticketInformation.addValue(new Value(ticketNumber));
		ticketInformation.addValue(new Value(orderReceiver));
		ticketInformation.addValue(new Value(serviceContract));
		ticketInformation.addValue(new Value(serviceObject));
		ticketInformation.addValue(new Value(service));
		ticketInformation.addValue(new Value(description));

		ticketInformation.addValue(resolveLookup(orderReceiver, "tOrderReceiver"));
		ticketInformation.addValue(resolveLookup(serviceContract, "tServiceContract"));
		ticketInformation.addValue(resolveLookup(serviceObject, "tServiceObject"));
		ticketInformation.addValue(resolveLookup(service, "tService"));

		return ticketInformation;
	}

	/**
	 * Holt sich für den Keytext den KeyLong aus der Datenbank
	 * 
	 * @param keyText
	 * @param tableName
	 * @return
	 */
	private Value resolveLookup(String keyText, String tableName) {
		Table inputTable = new Table();
		inputTable.setName(tableName);
		inputTable.addColumn(new Column("KeyLong", DataType.INTEGER));
		inputTable.addColumn(new Column("KeyText", DataType.STRING));
//		inputTable.addColumn(new Column("Description", DataType.STRING)); evtl. erweitern um Description
		inputTable.addColumn(new Column("LastAction", DataType.INTEGER));
		Row inputRow = new Row();
		inputRow.addValue(null);
		inputRow.addValue(new Value(keyText));
//		inputRow.addValue(null); // Description
		inputRow.addValue(new Value(">0")); // LastAction: nur nicht gelöschte
		inputTable.addRow(inputRow);
		Table outputTable = svc.getIndexView(inputTable);
		Value outputValue = null;
		if (!outputTable.getRows().isEmpty()) {
			outputValue = outputTable.getRows().get(0).getValues().get(0);
		}
		return outputValue;
	}

	private Table createEmptyTicketTable() {
		Table ticketTable = new Table();
		ticketTable.setName("Ticket");

		ticketTable.addColumn(new Column(TICKET_ID, DataType.INTEGER));
		ticketTable.addColumn(new Column(TICKET_ORDERRECEIVER, DataType.STRING));
		ticketTable.addColumn(new Column(TICKET_SERVICECONTRACT, DataType.STRING));
		ticketTable.addColumn(new Column(TICKET_SERVICEOBJECT, DataType.STRING));
		ticketTable.addColumn(new Column(TICKET_SERVICE, DataType.STRING));
		ticketTable.addColumn(new Column(TICKET_DESCRIPTION, DataType.STRING));
		// für die Keys brauchen wir Datenbankzugriff
		ticketTable.addColumn(new Column(ORDERRECEIVER_KEY, DataType.INTEGER));
		ticketTable.addColumn(new Column(SERVICECONTRACT_KEY, DataType.INTEGER));
		ticketTable.addColumn(new Column(SERVICEOBJECT_KEY, DataType.INTEGER));
		ticketTable.addColumn(new Column(SERVICE_KEY, DataType.INTEGER));

		return ticketTable;
	}
}