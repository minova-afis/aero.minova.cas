package aero.minova.cas.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.controller.SqlProcedureController;

@SuppressWarnings("rawtypes")
@Service
public class QueueService implements BiConsumer {

	@Autowired
	CustomLogger logger;

	@Autowired
	SecurityService securityService;

	@Autowired
	SqlProcedureController spc;

	@Override
	public void accept(Object t, Object u) {
		// TODO Generiere Nachricht und speichere sie in der xtcasServiceMessage

	}

	@Scheduled(cron = "${aero.minova.check.message.intervall:0 * * * * *}")
	private void sendQueueMessage() {
		Table messagesToBeSend = getNextMessage();
		if (messagesToBeSend != null && messagesToBeSend.getRows().size() > 0) {
			List<Integer> blockedServices = new ArrayList<>();

			for (Row pendingMessages : messagesToBeSend.getRows()) {

				boolean sendSuccessfull = sendMessage(messagesToBeSend);
				if (sendSuccessfull) {
					safeAsSent(pendingMessages);
				} else {
					/*
					 * Falls eine Nachricht nicht an einen Dienst geschickt werden konnte, wird dessen Key auf die blockedServices-Liste gesetzt, da alle
					 * weiteren Nachrichten von der nicht versandten anhängen könnten. Deshalb werden in diesem Intervall keine Nachrichten mehr an diesen
					 * Dienst geschickt.
					 */
					blockedServices.add(pendingMessages.getValues().get(0).getIntegerValue());
				}
			}
		}
	}

	private Table getNextMessage() {
		Table unsendMessages;

		Table messagesRequest = new Table();
		messagesRequest.setName("xvcasCASServiceMessage");
		messagesRequest.addColumn(new Column("CASServiceKey", DataType.INTEGER));
		messagesRequest.addColumn(new Column("CASServiceName", DataType.STRING));
		messagesRequest.addColumn(new Column("ServiceURL", DataType.STRING));
		messagesRequest.addColumn(new Column("Port", DataType.INTEGER));
		messagesRequest.addColumn(new Column("MessageKey", DataType.INTEGER));
		messagesRequest.addColumn(new Column("Message", DataType.STRING));
		messagesRequest.addColumn(new Column("isSent", DataType.BOOLEAN));
		messagesRequest.addColumn(new Column("NumberOfAttempts", DataType.INTEGER));

		Row messagesRow = new Row();
		messagesRow.addValue(null);
		messagesRow.addValue(null);
		messagesRow.addValue(null);
		messagesRow.addValue(null);
		messagesRow.addValue(null);
		messagesRow.addValue(null);
		messagesRow.addValue(null);
		messagesRow.addValue(null);

		messagesRequest.addRow(messagesRow);
		try {
			unsendMessages = securityService.unsecurelyGetIndexView(messagesRequest);
		} catch (Exception e) {
			logger.logError("The QueueService could not access view xvcasCASServiceMessage", e);
			throw new RuntimeException(e);
		}
		return unsendMessages;
	}

	private boolean sendMessage(Table nextMessage) {
		// TODO Falls NumberOfAttempts zu hoch -> Nachricht 'löschen'
		// TODO Zu alte Nachrichten löschen (7 Tage?)
		// TODO Nachrichten versenden
		return false;
	}

	private void safeAsSent(Row nextMessage) {
		Table setSent = new Table();
		setSent.setName("xpcasUpdateServiceMessage");
		setSent.addColumn(new Column("CASServiceKey", DataType.INTEGER));
		setSent.addColumn(new Column("CASServiceName", DataType.STRING));
		setSent.addColumn(new Column("ServiceURL", DataType.STRING));
		setSent.addColumn(new Column("Port", DataType.INTEGER));
		setSent.addColumn(new Column("MessageKey", DataType.INTEGER));
		setSent.addColumn(new Column("Message", DataType.STRING));
		setSent.addColumn(new Column("isSent", DataType.BOOLEAN));
		setSent.addColumn(new Column("NumberOfAttempts", DataType.INTEGER));

		Row setSentRow = new Row();
		setSentRow.addValue(null);
		setSentRow.addValue(null);
		setSentRow.addValue(null);
		setSentRow.addValue(null);
		setSentRow.addValue(nextMessage.getValues().get(4));
		setSentRow.addValue(null);
		setSentRow.addValue(new Value(true, null));
		setSentRow.addValue(null);

		setSent.addRow(setSentRow);
		try {
			spc.unsecurelyProcessProcedure(setSent);
		} catch (Exception e) {
			logger.logError("The message with key " + nextMessage.getValues().get(4).getStringValue() + " could not access view xvcasCASServiceMessage", e);
			throw new RuntimeException(e);
		}
	}

}