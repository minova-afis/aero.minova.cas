package aero.minova.cas.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.controller.SqlProcedureController;
import aero.minova.cas.service.model.CASServices;
import aero.minova.cas.service.model.NewsfeedListener;
import aero.minova.cas.service.model.ServiceMessage;
import aero.minova.cas.service.repository.CASServicesRepository;
import aero.minova.cas.service.repository.ServiceMessageRepository;
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

	// Hierbei handelt es sich um Tage
	@org.springframework.beans.factory.annotation.Value("${aero.minova.message.age:7}")
	int allowedMessageAge;

	@org.springframework.beans.factory.annotation.Value("${aero.minova.number.of.attempts:10}")
	int allowedNumberOfAttempts;

	@Autowired
	RestTemplate restTemplate;

	// Map<ProzedurName, Map< ServiceName, BiFunction>>
	Map<String, Map<String, BiFunction<Table, ResponseEntity<Object>, String>>> serviceMessageCreators = new HashMap<>();

	@Autowired
	ServiceMessageRepository serviceMessageRepo;

	@Autowired
	CASServicesRepository casServiceRepo;

	Map<CASServices, OAuth2Token> oauth2TokenList = new HashMap<>();

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

		List<ServiceMessage> messagesToBeSend;

		try {
			// Holt sich alle Nachrichten, die noch nicht versandt wurden oder noch nicht gefailed sind.
			messagesToBeSend = serviceMessageRepo.findAllByIsSentFalseAndFailedFalseAndLastActionGreaterThan(0);
		} catch (Exception e) {
			logger.logError("Could not read next message. The QueueService could not read tServiceMessage.", e);
			throw new RuntimeException(e);
		}

		if (messagesToBeSend != null && !messagesToBeSend.isEmpty()) {

			for (ServiceMessage pendingMessage : messagesToBeSend) {

				// Wenn die allowedNumberOfAttempts zu hoch wird, setzen wir Failed auf true und die Nachricht soll nicht weiter versucht werden zu verschicken.
				if (pendingMessage.getNumberOfAttempts() >= allowedNumberOfAttempts) {
					pendingMessage.setFailed(true);
					serviceMessageRepo.saveAndFlush(pendingMessage);
					continue;
				}

				// Falls die Nachricht älter ist als das allowedMessageAge muss die Nachricht gelöscht werden.
				if (pendingMessage.getMessageCreationDate().toInstant(ZoneOffset.UTC).isBefore(Instant.now().minus(allowedMessageAge, ChronoUnit.DAYS))) {
					deleteMessage(pendingMessage);
					continue;
				}

				// Versuche die Nachricht an den Dienst zu verschicken.
				boolean sendSuccessfull = sendMessage(pendingMessage);
				if (sendSuccessfull) {
					safeAsSent(true, pendingMessage);
				} else {
					safeAsSent(false, pendingMessage);
					logger.logQueueService(pendingMessage.getCasService().getKeyText() + " is not reachable!");
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

					String message = entry.getValue().apply(t, u);
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
		List<NewsfeedListener> servicesToBeNotified = serviceNotifierService.findViewEntry(null, topic);

		for (NewsfeedListener services : servicesToBeNotified) {
			try {
				CASServices service = casServiceRepo.findByKeyLong(services.getCasService().getKeyLong()).get();
				ServiceMessage serviceMessage = new ServiceMessage();

				serviceMessage.setCasService(service);
				serviceMessage.setMessage(message);

				serviceMessage.setMessageCreationDate(LocalDateTime.now());

				serviceMessageRepo.saveAndFlush(serviceMessage);

				logger.logQueueService("Saving message for " + topic + " for service " + services.getCasService().getKeyText() + "  because of " + procedureName
						+ ": '" + message + "'");
				logger.logQueueService("Message saved!");
			} catch (Exception e) {
				logger.logError("Error while trying to save message " + message, e);
				throw new RuntimeException(e);
			}
		}

	}

	/**
	 * Löscht eine Nachricht aus der Datenbank, damit nicht mehr versucht wird sie zu versenden. NEUERUNG: Wir löschen Nachrichten nicht mehr, sondern setzen
	 * sie nur noch auf 'Failed'.
	 * 
	 * @param pendingMessage
	 *            Eine ServiceMessage, die es zu 'löschen' gilt.
	 */
	private void deleteMessage(ServiceMessage pendingMessage) {
		try {
			logger.logQueueService("Deleting message with key " + pendingMessage.getKeyLong());
			serviceMessageRepo.delete(pendingMessage);
		} catch (Exception e) {
			logger.logError("The message with key " + pendingMessage.getKeyLong() + " could not be deleted!", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Versendet eine Nachricht.
	 * 
	 * @param pendingMessage
	 *            Eine Row, in der folgende Übergabeparamtern enthalten sind:CASServiceKey, CASServiceName, ServiceURL, Port, MessageKey, Message, isSent,
	 *            NumberOfAttempts, MessageCreationDate
	 * @return true, falls der Versandt erfolgreich war. Andernfalls false.
	 */
	private boolean sendMessage(ServiceMessage pendingMessage) {
		// URL + : + Port
		String url = pendingMessage.getCasService().getPort() != 0
				? pendingMessage.getCasService().getServiceUrl() + ":" + pendingMessage.getCasService().getPort()
				: pendingMessage.getCasService().getServiceUrl();
		String message = pendingMessage.getMessage();

		int serviceMessageReceiverLoginTypeKey = pendingMessage.getCasService().getReceiverLoginType() != null
				? pendingMessage.getCasService().getReceiverLoginType().getKeyLong()
				: 0;

		HttpEntity<?> request;
		try {

			// Falls BasicAuth:
			if (serviceMessageReceiverLoginTypeKey == 2) {

				// Username + : + Password
				String credentials = pendingMessage.getCasService().getUsername() + ":" + pendingMessage.getCasService().getPassword();

				HttpHeaders header = new HttpHeaders();
				byte[] encodedAuth = Base64.encodeBase64(credentials.getBytes(StandardCharsets.UTF_8), false);
				header.add("Authorization", "Basic " + encodedAuth);

				request = new HttpEntity<>(message, header);

				// Falls OAuth2:
			} else if (serviceMessageReceiverLoginTypeKey == 3) {

				OAuth2Token oauth2Token;
				CASServices service = pendingMessage.getCasService();

				if (!oauth2TokenList.containsKey(service)) {
					oauth2Token = getOAuth2Token(service);
					oauth2TokenList.put(service, oauth2Token);

				} else if (oauth2TokenList.get(service).getExpiryDate().isBefore(Instant.now())) {
					oauth2Token = getOAuth2Token(service);
					oauth2TokenList.replace(service, oauth2Token);
				}
				String token = oauth2TokenList.get(service).getToken();

				// Access Token in eigentlichen Aufruf setzen.
				HttpHeaders headers1 = new HttpHeaders();
				headers1.add("Authorization", "Bearer " + token);
				headers1.setContentType(MediaType.APPLICATION_JSON);
				request = new HttpEntity<>(message, headers1);

			} else {
				request = new HttpEntity<>(message);
			}
			logger.logQueueService("Trying to send message with key " + pendingMessage.getKeyLong() + " to " + url);
			restTemplate.exchange(url, HttpMethod.POST, request, Void.class);
			logger.logQueueService("Sending message: " + message);
		} catch (Exception e) {
			logger.logError("Could not send message to " + url, e);
			return false;
		}
		return true;
	}

	/**
	 * Anfrage an einen im übergebenen Service definierten Server schicken, um einen neuen OAuth2 Token zu erhalten.
	 * 
	 * @param service,
	 *            ein CASServices Objekt.
	 * @return ein gültiges OAuth2Token Objekt, ansonsten wird eine IllegalArgumentException geworfen.
	 */
	private OAuth2Token getOAuth2Token(CASServices service) {
		try {
			// Zuerst einen Aufruf an die TokenUrl/ an den Token Server machen, um sich einen Token zu holen.
			String credentials = service.getClientId() + ":" + service.getClientSecret();
			String encodedAuth = new String(Base64.encodeBase64(credentials.getBytes(StandardCharsets.UTF_8), false));
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.add("Authorization", "Basic " + encodedAuth);
			headers.add("Content-Type", "application/x-www-form-urlencoded");

			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("grant_type", "password");
			map.add("username", service.getUsername());
			map.add("password", service.getPassword());
			map.add("scope", "test");

			HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(map, headers);
			String accessTokenUrl = service.getTokenURL();

			ResponseEntity<String> response = restTemplate.exchange(accessTokenUrl, HttpMethod.POST, tokenRequest, String.class);

			// Access Token aus der JSON response lesen.
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(response.getBody());
			String token = node.path("access_token").asText();

			String expiryDate = response.getHeaders().getValuesAsList("Expires").get(1);

			return new OAuth2Token(token, expiryDate);
		} catch (Exception e) {
			throw new IllegalArgumentException("QueueService was not able to read the access token from tokenurl " + service.getTokenURL());
		}
	}

	/**
	 * Speichert den Status einer Nachricht als 'isSent' in der Datenbank. Erhöht gleichzeitig auch die NumberOfAttempts.
	 * 
	 * @param isSent
	 *            True, falls die Nachricht erfolgreich versandt wurde. False, wenn nicht.
	 * @param nextMessage
	 *            Eine ServiceMessage.
	 */
	private void safeAsSent(boolean isSent, ServiceMessage nextMessage) {
		try {
			if (isSent) {
				serviceMessageRepo.delete(nextMessage);
			} else {
				nextMessage.setSent(isSent);
				nextMessage.setNumberOfAttempts(nextMessage.getNumberOfAttempts() + 1);

				serviceMessageRepo.saveAndFlush(nextMessage);
			}
		} catch (Exception e) {
			logger.logError("Could not update message with key " + nextMessage.getKeyLong() + ".", e);
			throw new RuntimeException(e);
		}
	}
}