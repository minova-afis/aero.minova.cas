package aero.minova.trac.integration.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.Table;
import aero.minova.core.application.system.domain.Value;
import aero.minova.trac.integration.TracTicketIntegration;

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
	// private final Server server = Server.getInstance();

	@GetMapping(value = "data/ticket", produces = "application/json")
	public Table getTicket(@RequestParam String ticketNo) {
		Table ticketTable = null;

		try {
			int ticketNumber = Integer.parseInt(ticketNo);
			ticketTable = fetchFromTrac(ticketNumber);
		} catch (NumberFormatException ex) {
			// TODO Fehler anzeigen
		}

		return ticketTable != null ? ticketTable : createEmptyTicketTable();
	}

	private Table fetchFromTrac(int ticketNumber) {
		Table ticketTable = createEmptyTicketTable();
		tracIntegration.setTicketNumber(ticketNumber);
		if (ticketNumber == -123) {
			ticketTable.addRow(ticketInformation(-123, "MIN", "WFC", "LOHN", "ZPROGRAM", "#37: Trac-Ticket Dummy Implementierung auf publictest bereitstellen."));
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
		return ticketInformation;
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
		// für die Keys bräuchten wir Datenbankzugriff

		return ticketTable;
	}
}