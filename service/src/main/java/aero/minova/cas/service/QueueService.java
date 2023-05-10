package aero.minova.cas.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.controller.SqlProcedureController;
import aero.minova.cas.servicenotifier.ServiceNotifierService;
import jakarta.annotation.PostConstruct;

@Service
public class QueueService implements BiConsumer<Table, ResponseEntity<Object>> {

	@Autowired
	private CustomLogger logger;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private ServiceNotifierService serviceNotifierService;

	@Autowired
	private SqlProcedureController spc;

	@Autowired
	private ProcedureService procedureService;

	// Hierbei handelt es sich um Tage
	@org.springframework.beans.factory.annotation.Value("${aero.minova.message.age:7}")
	int allowedMessageAge;

	@org.springframework.beans.factory.annotation.Value("${aero.minova.number.of.attempts:10}")
	int allowedNumberOfAttempts;

	RestTemplate restTemplate;

	// Map<ProzedurName, Map< ServiceName, BiFunction>>
	Map<String, Map<String, BiFunction<Table, ResponseEntity<Object>, String>>> serviceMessageCreators = new HashMap<>();

	@PostConstruct
	public void init() {
		spc.setQueueService(this);
	}

	/**
	 * Registriert eine BiFunction auf einen Prozedurnamen.
	 * 
	 * @param procedureName
	 *            Der Name der Prozedur, nach welcher die BiFunction ausgeführt werden soll.
	 * @param function
	 *            Die BiFunction, die Ausgeführt werden soll. Muss vom Typ BiFunction<Table, HttpResponse<?>, String> sein.
	 */
	public void registerServiceMessageCreator(String procedureName, String topic, BiFunction<Table, ResponseEntity<Object>, String> function) {
		if (serviceMessageCreators.containsKey(procedureName) && serviceMessageCreators.get(procedureName).containsKey(topic)) {
			throw new IllegalArgumentException("There is already a message creator registered for procedure and topic: " + procedureName + ", " + topic);
		} else if (!serviceMessageCreators.containsKey(procedureName)) {
			Map<String, BiFunction<Table, ResponseEntity<Object>, String>> functions = new HashMap<>();
			functions.put(topic, function);
			serviceMessageCreators.put(procedureName, functions);
		} else {
			serviceMessageCreators.get(procedureName).put(topic, function);
		}
	}

	/**
	 * Versucht in einem bestimmten regelmäßigen Abstand unversendete Nachrichten an Dienste zu verschicken.
	 */
	@Scheduled(cron = "${aero.minova.check.message.intervall:0 * * * * *}")
	private void sendQueueMessage() {
		try {
			if (!securityService.isTablePresent("xvcasCASServiceMessage")) {
				return;
			}
		} catch (Exception e) {
			logger.logError("Could not find view xvcasCASServiceMessage. No messages will be send to registered services!", e);
		}

		// Es wird hier ein neuer SecurityContext benötigt, da sonst die Methode 'getUserContext im SqlProcedureController abbrechen würde.
		SecurityContextHolder.getContext().setAuthentication(new Authentication() {

			@Override
			public String getName() {
				return "CAS QueueService";
			}

			@Override
			public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
				// Hier muss bisher nicht implementiert werden.
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
				return Collections.emptyList();
			}
		});

		// Holt sich alle Nachrichten, die noch nicht versandt wurden.
		Table messagesToBeSend = getNextMessage();

		if (messagesToBeSend != null && !messagesToBeSend.getRows().isEmpty()) {

			for (Row pendingMessage : messagesToBeSend.getRows()) {

				// Falls die Nachricht älter ist als das allowedMessageAge und falls die NumberOfAttempts höher ist als die allowedNumberOfAttempts, muss die
				// Nachricht gelöscht werden.
				if (pendingMessage.getValues().get(messagesToBeSend.findColumnPosition("MessageCreationDate")).getInstantValue()
						.isBefore(Instant.now().minus(allowedMessageAge, ChronoUnit.DAYS))
						|| pendingMessage.getValues().get(messagesToBeSend.findColumnPosition("NumberOfAttempts"))
								.getIntegerValue() >= allowedNumberOfAttempts) {
					deleteMessage(pendingMessage);
					continue;
				}

				// Versuche die Nachricht an den Dienst zu verschicken.
				boolean sendSuccessfull = sendMessage(pendingMessage);
				if (sendSuccessfull) {
					safeAsSent(true, pendingMessage);
				} else {
					safeAsSent(false, pendingMessage);
					logger.logQueueService(pendingMessage.getValues().get(1).getStringValue() + " is not reachable!");
				}
			}
		}
	}

	/**
	 * Speichert eine Nachricht in der Datenbank, welche beim nächsten Intervall verschickt wird.
	 */
	@Override
	public void accept(Table t, ResponseEntity<Object> u) {

		if (t instanceof Table && u instanceof ResponseEntity) {

			Map<String, BiFunction<Table, ResponseEntity<Object>, String>> topicSpecificMessages = serviceMessageCreators.get(t.getName());

			if (topicSpecificMessages != null) {

				// Wenn eine Prozedur ausgeführt wurde, müssen Nachrichten für alle betroffenen Dienste generiert werden.
				for (Map.Entry<String, BiFunction<Table, ResponseEntity<Object>, String>> entry : topicSpecificMessages.entrySet()) {

					String message = entry.getValue().apply(t, u).toString();
					saveMessage(message, t.getName(), entry.getKey());
				}
			}

		}
	}

	/**
	 * Speichert eine Nachricht für einen Topic.
	 * 
	 * @param message
	 *            Die Nachricht, die gespeichert werden soll.
	 * @param procedureName
	 *            Die Prozedur, wegen welcher die Nachricht erstellt wurde.
	 * @param topic
	 *            Das Topic, welches verändert wurde.
	 */
	private void saveMessage(String message, String procedureName, String topic) {
		Table servicesToBeNotified = serviceNotifierService.findViewEntry(null, null, new Value(topic, null), null, null);

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
				logger.logQueueService("Saving message for " + topic + " for service " + services.getValues().get(3).getStringValue() + "  because of "
						+ procedureName + ": '" + message + "'");
				procedureService.unsecurelyProcessProcedure(setSent);
				logger.logQueueService("Message saved!");
			} catch (Exception e) {
				logger.logError("Error while trying to save message " + message, e);
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
			logger.logQueueService("Deleting message with key " + pendingMessage.getValues().get(4).getIntegerValue());
			procedureService.unsecurelyProcessProcedure(messageToDelete);
		} catch (Exception e) {
			logger.logError("The message with key " + pendingMessage.getValues().get(4).getIntegerValue() + " could not be deleted!", e);
			throw new RuntimeException(e);
		}
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
		messagesRequest.addColumn(new Column("ServiceMessageReceiverLoginTypeKey", DataType.INTEGER));
		messagesRequest.addColumn(new Column("Username", DataType.STRING));
		messagesRequest.addColumn(new Column("Password", DataType.STRING));
		messagesRequest.addColumn(new Column("ClientID", DataType.STRING));
		messagesRequest.addColumn(new Column("ClientSecret", DataType.STRING));
		messagesRequest.addColumn(new Column("TokenURL", DataType.STRING));

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
			logger.logError("Could not read next message. The QueueService could not access the view " + messagesRequest.getName(), e);
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
		// URL + : + Port
		String url = nextMessage.getValues().get(2).getStringValue() + ":" + nextMessage.getValues().get(3).getIntegerValue();
		String message = nextMessage.getValues().get(5).getStringValue();

		int serviceMessageReceiverLoginTypeKey = nextMessage.getValues().get(9).getIntegerValue();

		HttpEntity<?> request;

		// Falls BasicAuth:
		if (serviceMessageReceiverLoginTypeKey == 2) {

			// Username + : + Password
			String credentials = nextMessage.getValues().get(10).getStringValue() + ":" + nextMessage.getValues().get(11).getIntegerValue();

			HttpHeaders header = new HttpHeaders();
			byte[] encodedAuth = Base64.encodeBase64(credentials.getBytes(StandardCharsets.UTF_8), false);
			header.add("Authorization", "Basic " + encodedAuth);

			request = new HttpEntity<>(message, header);

			// Falls OAuth2:
		} else if (serviceMessageReceiverLoginTypeKey == 3) {

			// Zuerst einen Aufruf an die TokenUrl/ an den Token Server machen, um sich einen Token zu holen.
			String credentials = nextMessage.getValues().get(10).getStringValue() + ":" + nextMessage.getValues().get(11).getIntegerValue();
			byte[] encodedAuth = Base64.encodeBase64(credentials.getBytes(StandardCharsets.UTF_8), false);
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.add("Authorization", "Basic " + encodedAuth);

			HttpEntity<String> tokenRequest = new HttpEntity<>(headers);
			String accessTokenUrl = nextMessage.getValues().get(14).getStringValue();
			ResponseEntity<String> response = restTemplate.exchange(accessTokenUrl, HttpMethod.POST, tokenRequest, String.class);

			// Access Token aus der JSON response lesen.
			ObjectMapper mapper = new ObjectMapper();
			String token;
			try {
				JsonNode node = mapper.readTree(response.getBody());
				token = node.path("access_token").asText();
			} catch (Exception e) {
				throw new IllegalArgumentException("QueueService was not able to read the access token from tokenurl " + accessTokenUrl);
			}

			// Access Token in eigentlichen Aufruf setzen.
			HttpHeaders headers1 = new HttpHeaders();
			headers1.add("Authorization", "Bearer " + token);
			request = new HttpEntity<>(message, headers1);

		} else {
			request = new HttpEntity<>(message);
		}
		logger.logQueueService("Trying to send message with key " + nextMessage.getValues().get(4).getIntegerValue() + " to " + url);
		try {
			logger.logQueueService("Sending message: " + message);
			restTemplate.exchange(url, HttpMethod.POST, request, Void.class);
		} catch (Exception e) {
			logger.logError("Could not send message to " + url, e);
			return false;
		}
		return true;
	}

	/**
	 * Speichert den Status einer Nachricht als 'isSent' in der Datenbank. Erhöht gleichzeitig auch die NumberOfAttempts.
	 * 
	 * @param isSent
	 *            True, falls die Nachricht erfolgreich versandt wurde. False, wenn nicht.
	 * @param nextMessage
	 *            Eine Row, bei welcher der KeyLong der Nachricht an 5. Stelle und die NumberOfAttempts an 8.Stelle stehen.
	 */
	private void safeAsSent(boolean isSent, Row nextMessage) {
		int attempts = nextMessage.getValues().get(7).getIntegerValue() + 1;

		Table setSent = new Table();
		setSent.setName("xpcasUpdateServiceMessage");
		setSent.addColumn(new Column("KeyLong", DataType.INTEGER));
		setSent.addColumn(new Column("isSent", DataType.BOOLEAN));
		setSent.addColumn(new Column("NumberOfAttempts", DataType.INTEGER));

		Row setSentRow = new Row();
		setSentRow.addValue(nextMessage.getValues().get(4));
		setSentRow.addValue(new Value(isSent, null));
		setSentRow.addValue(new Value(attempts, null));

		setSent.addRow(setSentRow);
		try {
			procedureService.unsecurelyProcessProcedure(setSent);
		} catch (Exception e) {
			logger.logError("Could not update message with key " + nextMessage.getValues().get(4).getStringValue() + ".", e);
			throw new RuntimeException(e);
		}
	}
}