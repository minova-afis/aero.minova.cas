package aero.minova.cas.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

	@org.springframework.beans.factory.annotation.Value("${aero.minova.message.age:7}")
	int allowedMessageAge;

	@org.springframework.beans.factory.annotation.Value("${aero.minova.number.of.attempts:10}")
	int allowedNumberOfAttempts;

	@Override
	public void accept(Object t, Object u) {
		// TODO Generiere Nachricht und speichere sie in der xtcasServiceMessage

	}

	@Scheduled(cron = "${aero.minova.check.message.intervall:0 * * * * *}")
	private void sendQueueMessage() {

		// Holt sich alle Nachrichten, die noch nicht versandt wurden.
		Table messagesToBeSend = getNextMessage();

		if (messagesToBeSend != null && messagesToBeSend.getRows().size() > 0) {
			List<Integer> blockedServices = new ArrayList<>();

			for (Row pendingMessage : messagesToBeSend.getRows()) {

				// Falls die Nachricht älter ist als das allowedMessageAge und falls die NumberOfAttempts höher ist als die allowedNumberOfAttempts, muss die
				// Nachricht gelöscht werden.
				if (pendingMessage.getValues().get(8).getInstantValue().isBefore(Instant.now().minus(allowedMessageAge, ChronoUnit.DAYS))
						&& pendingMessage.getValues().get(7).getIntegerValue() > allowedNumberOfAttempts) {
					deleteMessage(pendingMessage);
					continue;
				}

				// Versuche die Nachricht an den Dienst zu verschicken.
				boolean sendSuccessfull = sendMessage(pendingMessage);
				if (sendSuccessfull) {
					safeAsSent(pendingMessage);
				} else {
					/*
					 * Falls eine Nachricht nicht an einen Dienst geschickt werden konnte, wird dessen Key auf die blockedServices-Liste gesetzt, da alle
					 * weiteren Nachrichten von der nicht versandten anhängen könnten. Deshalb werden in diesem Intervall keine Nachrichten mehr an diesen
					 * Dienst geschickt.
					 */
					blockedServices.add(pendingMessage.getValues().get(0).getIntegerValue());
					increaseAttempts(pendingMessage);
				}
			}
		}
	}

	private void deleteMessage(Row pendingMessage) {
		// TODO Auto-generated method stub

	}

	/**
	 * Erhöht die NumberOfAttempts der übergebenen Nachricht.
	 * 
	 * @param pendingMessages
	 *            Eine Row mit den Übergabeparamtern:CASServiceKey, CASServiceName, ServiceURL, Port, MessageKey, Message, isSent, NumberOfAttempts,
	 *            MessageCreationDate
	 */
	private void increaseAttempts(Row pendingMessages) {
		int attempts = pendingMessages.getValues().get(7).getIntegerValue() + 1;

		Table setSent = new Table();
		setSent.setName("xpcasUpdateServiceMessage");
		setSent.addColumn(new Column("KeyLong", DataType.INTEGER));
		setSent.addColumn(new Column("isSent", DataType.BOOLEAN));
		setSent.addColumn(new Column("NumberOfAttempts", DataType.INTEGER));

		Row setSentRow = new Row();
		setSentRow.addValue(pendingMessages.getValues().get(4));
		setSentRow.addValue(pendingMessages.getValues().get(6));
		setSentRow.addValue(new Value(attempts, null));

		setSent.addRow(setSentRow);
		try {
			spc.unsecurelyProcessProcedure(setSent);
		} catch (Exception e) {
			logger.logError("The message with key " + pendingMessages.getValues().get(4).getStringValue()
					+ " could not be send! Number of attempts is increased to " + attempts, e);
			throw new RuntimeException(e);
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
		messagesRequest.addColumn(new Column("MessageCreationDate", DataType.INSTANT));

		Row messagesRow = new Row();
		messagesRow.addValue(null);
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
			logger.logError("The QueueService could not access the view xvcasCASServiceMessage", e);
			throw new RuntimeException(e);
		}
		return unsendMessages;
	}

	private boolean sendMessage(Row nextMessage) {

		// TODO Nachrichten versenden
		return false;
	}

	private void safeAsSent(Row nextMessage) {
		Table setSent = new Table();
		setSent.setName("xpcasUpdateServiceMessage");
		setSent.addColumn(new Column("KeyLong", DataType.INTEGER));
		setSent.addColumn(new Column("isSent", DataType.BOOLEAN));
		setSent.addColumn(new Column("NumberOfAttempts", DataType.INTEGER));

		Row setSentRow = new Row();
		setSentRow.addValue(nextMessage.getValues().get(4));
		setSentRow.addValue(new Value(true, null));
		setSentRow.addValue(nextMessage.getValues().get(7));

		setSent.addRow(setSentRow);
		try {
			spc.unsecurelyProcessProcedure(setSent);
		} catch (Exception e) {
			logger.logError("The message with key " + nextMessage.getValues().get(4).getStringValue() + " could not access view xvcasCASServiceMessage", e);
			throw new RuntimeException(e);
		}
	}

}