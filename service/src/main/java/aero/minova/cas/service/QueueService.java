package aero.minova.cas.service;

import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.controller.SqlProcedureController;
import aero.minova.cas.servicenotifier.ServiceNotifierService;

@SuppressWarnings("rawtypes")
@Service
public class QueueService implements BiConsumer {

	@Autowired
	CustomLogger logger;

	@Autowired
	SecurityService securityService;

	@Autowired
	ServiceNotifierService serviceNotifierService;

	@Autowired
	SqlProcedureController spc;

	@org.springframework.beans.factory.annotation.Value("${aero.minova.message.age:7}")
	int allowedMessageAge;

	@org.springframework.beans.factory.annotation.Value("${aero.minova.number.of.attempts:10}")
	int allowedNumberOfAttempts;

	RestTemplate restTemplate;

	// Map<ProzedurName, Map< ServiceName, BiFunction>>
	Map<String, Map<String, BiFunction>> serviceMessageCreators = new HashMap<>();

	/**
	 * Registriert eine BiFunction auf einen Prozedurnamen.
	 * 
	 * @param procedureName
	 *            Der Name der Prozedur, nach welcher die BiFunction ausgeführt werden soll.
	 * @param function
	 *            Die BiFunction, die Ausgeführt werden soll. Muss vom Typ BiFunction<Table, HttpResponse<?>, String> sein.
	 */
	public void registerServiceMessageCreator(String procedureName, String serviceName, BiFunction<Table, HttpResponse<?>, String> function) {
		if (!serviceMessageCreators.containsKey(procedureName)) {
			Map<String, BiFunction> functions = new HashMap<>();
			functions.put(serviceName, function);
			serviceMessageCreators.put(procedureName, functions);
		}
	}

	/**
	 * Löscht alle BiFunctionen, die für einen Service registriert waren.
	 * 
	 * @param procedureName
	 * @param serviceName
	 */
	public void unregisterServiceMessageCreator(String procedureName, String serviceName) {
		if (serviceMessageCreators.containsKey(procedureName) && serviceMessageCreators.get(procedureName).containsKey(serviceName)) {
			serviceMessageCreators.get(procedureName).remove(serviceName);
		}
	}

	/**
	 * Versucht in einem bestimmten regelmäßigen Abstand unversendete Nachrichten an Dienste zu verschicken.
	 */
	@Scheduled(cron = "${aero.minova.check.message.intervall:0 * * * * *}")
	private void sendQueueMessage() {
		// Es wird hier ein neuer SecurityContext benötigt, da sonst die Methode 'getUserContext im SqlProcedureController abbrechen würde.
		SecurityContextHolder.getContext().setAuthentication(new Authentication() {

			@Override
			public String getName() {
				return "CAS QueueService";
			}

			@Override
			public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

			}

			@Override
			public boolean isAuthenticated() {
				return true;
			}

			@Override
			public Object getPrincipal() {
				return null;
			}

			@Override
			public Object getDetails() {
				return null;
			}

			@Override
			public Object getCredentials() {
				return null;
			}

			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return null;
			}
		});

		// Holt sich alle Nachrichten, die noch nicht versandt wurden.
		Table messagesToBeSend = getNextMessage();

		if (messagesToBeSend != null && messagesToBeSend.getRows().size() > 0) {

			for (Row pendingMessage : messagesToBeSend.getRows()) {

				// Falls die Nachricht älter ist als das allowedMessageAge und falls die NumberOfAttempts höher ist als die allowedNumberOfAttempts, muss die
				// Nachricht gelöscht werden.
				if (pendingMessage.getValues().get(8).getInstantValue().isBefore(Instant.now().minus(allowedMessageAge, ChronoUnit.DAYS))
						|| pendingMessage.getValues().get(7).getIntegerValue() >= allowedNumberOfAttempts) {
					deleteMessage(pendingMessage);
					continue;
				}

				// Versuche die Nachricht an den Dienst zu verschicken.
				boolean sendSuccessfull = sendMessage(pendingMessage);
				if (sendSuccessfull) {
					safeAsSent(pendingMessage);
				} else {
					logger.logNewsfeed(pendingMessage.getValues().get(1).getStringValue() + " is not reachable!");
				}
				increaseAttempts(pendingMessage);
			}
		}
	}

	/**
	 * Speichert eine Nachricht in der Datenbank, welche beim nächsten Intervall verschickt wird.
	 */
	@Override
	public void accept(Object t, Object u) {

		if (t instanceof Table && u instanceof ResponseEntity) {
			Table table = (Table) t;

			Map<String, BiFunction> applyableFunctions = serviceMessageCreators.get(table.getName());

			if (applyableFunctions != null) {

				// Wenn eine Prozedur ausgeführt wurde, müssen Nachrichten für alle betroffenen Dienste generiert werden.
				for (Map.Entry<String, BiFunction> entry : applyableFunctions.entrySet()) {
					@SuppressWarnings("unchecked")
					String message = (String) entry.getValue().apply(t, u);
					saveMessage(message, table.getName(), entry.getKey());
				}
			}
		}
	}

	/**
	 * Speichert eine Nachricht für einen Service.
	 * 
	 * @param message
	 *            Die Nachricht, die gespeichert werden soll.
	 * @param procedureName
	 *            Die Prozedur, wegen welcher die Nachricht erstellt wurde.
	 * @param serviceName
	 *            Der Name des Services, an welchen die Nachricht geschickt werden soll.
	 */
	private void saveMessage(String message, String procedureName, String serviceName) {
		Table servicesToBeNotified = serviceNotifierService.findViewEntry(new Value(serviceName, null), new Value(procedureName, null), null, null, null);

		for (Row services : servicesToBeNotified.getRows()) {
			Table setSent = new Table();
			setSent.setName("xpcasInsertServiceMessage");
			setSent.addColumn(new Column("KeyLong", DataType.INTEGER));
			setSent.addColumn(new Column("CASServiceKey", DataType.INTEGER));
			setSent.addColumn(new Column("Message", DataType.STRING));

			Row setSentRow = new Row();
			setSentRow.addValue(null);
			setSentRow.addValue(services.getValues().get(0));
			setSentRow.addValue(new Value(message, null));

			setSent.addRow(setSentRow);
			try {
				logger.logNewsfeed("Saving message '" + message + "' for service: " + serviceName);
				spc.unsecurelyProcessProcedure(setSent);
				logger.logNewsfeed("Message saved!");
			} catch (Exception e) {
				logger.logError("Error while trying to save message for service: " + serviceName, e);
				throw new RuntimeException(e);
			}
		}

	}

	/**
	 * Löscht eine Nachricht aus der Datenbank, damit nicht mehr versucht wird sie zu versenden.
	 * 
	 * @param pendingMessage
	 *            Eine Row, bei welcher der KeyLong der Nachricht an 5.Stelle steht.
	 */
	private void deleteMessage(Row pendingMessage) {
		Table messageToDelete = new Table();
		messageToDelete.setName("xpcasDeleteServiceMessage");
		messageToDelete.addColumn(new Column("KeyLong", DataType.INTEGER));

		Row setSentRow = new Row();
		setSentRow.addValue(pendingMessage.getValues().get(4));

		messageToDelete.addRow(setSentRow);
		try {
			logger.logNewsfeed("Deleting message with key " + pendingMessage.getValues().get(4).getIntegerValue());
			spc.unsecurelyProcessProcedure(messageToDelete);
		} catch (Exception e) {
			logger.logError("The message with key " + pendingMessage.getValues().get(4).getIntegerValue() + " could not be deleted!", e);
			throw new RuntimeException(e);
		}
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
			logger.logError("Number of attempts could not be increased for message with key: " + pendingMessages.getValues().get(4).getIntegerValue(), e);
			throw new RuntimeException(e);
		}
		logger.logNewsfeed("The message with key " + pendingMessages.getValues().get(4).getIntegerValue()
				+ " could not be send! Number of attempts is increased to " + attempts);
	}

	/**
	 * Liest alle nicht versendeten Nachrichten aus der Datenbank.
	 * 
	 * @return Eine Table, bei welcher jede Row eine nicht versandte Nachricht ist.
	 */
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

	/**
	 * Versendet eine Nachricht.
	 * 
	 * @param nextMessage
	 *            Eine Row, in der folgende Übergabeparamtern enthalten sind:CASServiceKey, CASServiceName, ServiceURL, Port, MessageKey, Message, isSent,
	 *            NumberOfAttempts, MessageCreationDate
	 * @return true, falls der Versandt erfolgreich war. Andernfalls false.
	 */
	private boolean sendMessage(Row nextMessage) {
		RestTemplate restTemplate = new RestTemplate();
		String url = nextMessage.getValues().get(2).getStringValue() + ":" + nextMessage.getValues().get(3).getIntegerValue();

		HttpEntity<?> request = new HttpEntity<Object>(nextMessage.getValues().get(5));
		logger.logNewsfeed("Trying to send message with key " + nextMessage.getValues().get(4).getIntegerValue() + " to " + url);
		try {
			restTemplate.exchange(url + "/cas-event-listener", HttpMethod.POST, request, Void.class);
			logger.logNewsfeed("Sending message: " + nextMessage.getValues().get(5));
		} catch (Exception e) {
			logger.logError("Could not send message to " + url, e);
			return false;
		}
		return true;
	}

	/**
	 * Speichert eine Nachricht als 'isSent' in der Datenbank.
	 * 
	 * @param nextMessage
	 *            Eine Row, bei welcher der KeyLong der Nachricht an 5. Stelle und die NumberOfAttempts an 8.Stelle stehen.
	 */
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