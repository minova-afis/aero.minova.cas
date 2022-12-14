package aero.minova.cas.api.restapi;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import aero.minova.cas.api.domain.PingResponse;
import aero.minova.cas.api.domain.SqlProcedureResult;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.XSqlProcedureResult;
import aero.minova.cas.api.domain.XTable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Component
// Wird von Spring gebraucht, da sonst eine NoSuchBeanDefinitionException geworfen wird.
@NoArgsConstructor
public class ClientRestAPI {

	private String username;
	private String password;
	private String url;

	private RestTemplate restTemplate;

	@Autowired
	@Getter
	private Gson gson;

	public ClientRestAPI(String username, String password, String url, Gson gson) {
		this.username = username;
		this.password = password;
		this.url = url;
		this.gson = gson;
		restTemplate = new RestTemplate();
		ArrayList<HttpMessageConverter<?>> converters = new ArrayList<>();
		converters.add(new GsonHttpMessageConverter(gson));
		restTemplate.setMessageConverters(converters);
	}

	public ClientRestAPI(String username, String password, String url) {
		this.username = username;
		this.password = password;
		this.url = url;
		restTemplate = new RestTemplate();
		ArrayList<HttpMessageConverter<?>> converters = new ArrayList<>();
		converters.add(new GsonHttpMessageConverter(gson));
		restTemplate.setMessageConverters(converters);
	}

	private HttpHeaders createHeaders(String username, String password) {
		HttpHeaders headers = new HttpHeaders();
		String auth = username + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8), false);
		String authHeader = "Basic " + new String(encodedAuth);
		headers.set("Authorization", authHeader);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.ALL));
		return headers;
	}

	/**
	 * Sendet einen Ping Request. Dies funktioniert nur, wenn die Anmelde-Daten (Benutzername und Passwort) richtig sind. Es kann somit dazu genutzt werden, um
	 * die Anmeldung zu prüfen.
	 *
	 * @return Die PingResponse als ResponseEntity.
	 */
	public ResponseEntity<PingResponse> ping() {
		HttpEntity<?> request = new HttpEntity<>(createHeaders(username, password));
		return restTemplate.exchange(url + "/ping", HttpMethod.GET, request, PingResponse.class);
	}

	// View Controller

	/**
	 * Sendet einen Request für eine View. ToDo - Frage : seh ich es richtig, dass über Columns in inputTable Filter-Bedingungen und über inputTable.metaData
	 * die Größe des ResultSets eingestellt werden kann? Oder kommt <i>immer alles</i> zurück?
	 *
	 * @param inputTable
	 *            Die Table, für welche eine View zurückgegeben werden soll.
	 * @return Eine Table mit dem gesamten Inhalt der View.
	 */
	public ResponseEntity<Table> sendViewRequest(Table inputTable) {
		HttpEntity<Table> request = new HttpEntity<>(inputTable, createHeaders(username, password));
		return restTemplate.exchange(url + "/data/index", HttpMethod.POST, request, Table.class);
	}

	// SqlProcedureController

	/**
	 * Sendet einen Request, um eine Prozedur auszuführen.
	 *
	 * @param inputTable
	 *            Die Table mit den Parametern der Prozedur.
	 * @return Die OutputParameter und das SqlProcedureResult der Prozedur als Table.
	 */
	public ResponseEntity<SqlProcedureResult> sendProcedureRequest(Table inputTable) {
		HttpEntity<Table> request = new HttpEntity<>(inputTable, createHeaders(username, password));
		return restTemplate.exchange(url + "/data/procedure", HttpMethod.POST, request, SqlProcedureResult.class);
	}

	/**
	 * Sendet einen Request, um eine Prozedur auszuführen.
	 *
	 * @param inputTable
	 *            Die Table mit den Parametern der Prozedur.
	 * @return Die OutputParameter und das SqlProcedureResult der Prozedur als Table.
	 */
	public <T> ResponseEntity<T> sendGenericProcedureRequest(Table inputTable, Class<T> type) {
		HttpEntity<Table> request = new HttpEntity<>(inputTable, createHeaders(username, password));
		return restTemplate.exchange(url + "/data/procedure", HttpMethod.POST, request, type);
	}

	// XSqlProcedureController

	/**
	 * Sendet einen Request, um mehrere zusammenhängende Prozeduren auszuführen.
	 *
	 * @param inputTable
	 *            Eine Liste von Tables, bzw. eine XTable mit den Parametern der Prozeduren und IDs.
	 * @return Die OutputParameter und das SqlProcedureResult der Prozeduren als Liste von Tables mit IDs.
	 */
	public ResponseEntity<List<XSqlProcedureResult>> sendXProcedureRequest(List<XTable> inputTable) {
		HttpEntity<List<XTable>> request = new HttpEntity<>(inputTable, createHeaders(username, password));
		return restTemplate.exchange(url + "/data/x-procedure", HttpMethod.POST, request, new ParameterizedTypeReference<List<XSqlProcedureResult>>() {});
	}

	// FilesController

	/**
	 * Sendet den Namen einer Datei, bzw. den Pfad einer Datei, welche sich im Root-Verzeichnis des Servers befinden muss. Falls diese Datei vorhanden ist, wird
	 * sie an den Sender zurückgegeben.
	 *
	 * @param path
	 *            Der Pfad oder nur der Name der Datei als String.
	 * @return Die Datei als byte[].
	 */
	public ResponseEntity<byte[]> sendGetFileRequest(String path) {
		HttpEntity<String> request = new HttpEntity<>(path, createHeaders(username, password));
		return restTemplate.exchange(url + "/files/read", HttpMethod.POST, request, byte[].class);
	}

	/**
	 * Sendet einen File-Namen oder Pfad zur Datei an den Server. Gibt den Hash der gesendeten Datei zurück.
	 *
	 * @param path
	 *            Der Pfad oder nur der Name der zu hashenden Datei als String.
	 * @return Den Hash der Datei als byte[].
	 */
	public ResponseEntity<byte[]> sendGetHashRequest(String path) {
		HttpEntity<String> request = new HttpEntity<>(path, createHeaders(username, password));
		return restTemplate.exchange(url + "/files/hash", HttpMethod.POST, request, byte[].class);
	}

	/**
	 * Sendet einen Ordner-Namen oder Pfad zum Ordner an den Server. Gibt den Ordner als Zip zurück.
	 *
	 * @param path
	 *            Der Pfad zum Ordner oder der Name des Ordners als String.
	 * @return Das Zip des Ordners als byte[].
	 */
	public ResponseEntity<byte[]> sendGetZipRequest(String path) {
		HttpEntity<String> request = new HttpEntity<>(path, createHeaders(username, password));
		return restTemplate.exchange(url + "/files/zip", HttpMethod.POST, request, byte[].class);
	}

	/**
	 * Lädt eine Datei vom Client hoch zum Server. Wird genutzt, um Logs hochzuladen.
	 *
	 * @param log
	 *            Die Log-Datei als byte[].
	 * @return HtpStatus.OK bei Erfolg.
	 */
	public HttpStatus sendUploadLogRequest(byte[] log) {
		HttpEntity<byte[]> request = new HttpEntity<>(log, createHeaders(username, password));
		ResponseEntity<Void> response = restTemplate.exchange(url + "/upload/logs", HttpMethod.POST, request, Void.class);

		return HttpStatus.valueOf(response.getStatusCode().value());
	}

	public void setGson(Gson gson) {
		this.gson = gson;
		ArrayList<HttpMessageConverter<?>> converters = new ArrayList<>();
		converters.add(new GsonHttpMessageConverter(gson));
		restTemplate.setMessageConverters(converters);
	}
}
