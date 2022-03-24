package aero.minova.cas.client.restapi;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import aero.minova.cas.client.domain.PingResponse;
import aero.minova.cas.client.domain.SqlProcedureResult;
import aero.minova.cas.client.domain.Table;
import aero.minova.cas.client.domain.XSqlProcedureResult;
import aero.minova.cas.client.domain.XTable;
import lombok.NoArgsConstructor;

@Component
// Wird von Spring gebraucht, da sonst eine NoSuchBeanDefinitionException geworfen wird.
@NoArgsConstructor
public class ClientRestAPI {

	String username;
	String password;
	String url;

	RestTemplate restTemplate;

	public ClientRestAPI(String username, String password, String url) {
		this.username = username;
		this.password = password;
		this.url = url;

		restTemplate = new RestTemplate();
	}

	private HttpHeaders createHeaders(String username, String password) {
		HttpHeaders headers = new HttpHeaders();
		String auth = username + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		headers.set("Authorization", authHeader);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.ALL));
		return headers;
	}

	/**
	 * Sendet einen Ping Request.
	 * 
	 * @return Die PingResponse als int.
	 */
	public int ping() {
		HttpEntity<?> request = new HttpEntity<Object>(createHeaders(username, password));
		ResponseEntity<PingResponse> response = restTemplate.exchange(url + "/ping", HttpMethod.GET, request, PingResponse.class);
		return response.getBody().getReturnCode();
	}

	// View Controller
	/**
	 * Sendet einen Request für eine View.
	 * 
	 * @param inputTable
	 *            Die Table, für welche eine View zurückgegeben werden soll.
	 * @return Eine Table mit dem gesamten Inhalt der View.
	 */
	public Table sendViewRequest(Table inputTable) {
		HttpEntity<Table> request = new HttpEntity<Table>(inputTable, createHeaders(username, password));
		ResponseEntity<Table> response = restTemplate.exchange(url + "/data/index", HttpMethod.POST, request, Table.class);
		return response.getBody();
	}

	// SqlProcedureController
	/**
	 * Sendet einen Request, um eine Prozedur auszuführen.
	 * 
	 * @param inputTable
	 *            Die Table mit den Parametern der Prozedur.
	 * @return Die OutpuParameter und das SqlProcedureResult der Prozedur als Table.
	 */
	public SqlProcedureResult sendProcedureRequest(Table inputTable) {
		HttpEntity<Table> request = new HttpEntity<>(inputTable);
		ResponseEntity<SqlProcedureResult> response = restTemplate.exchange(url + "/data/procedure", HttpMethod.POST, request, SqlProcedureResult.class);
		return response.getBody();
	}

	// XSqlProcedureController
	/**
	 * Sendet einen Request, um mehrere zusammenhängende Prozeduren auszuführen.
	 * 
	 * @param inputTable
	 *            Eine Liste von Tables, bzw. eine XTable mit den Parametern der Prozeduren und IDs.
	 * @return Die OutpuParameter und das SqlProcedureResult der Prozeduren als Liste von Tables mit IDs.
	 */
	public XSqlProcedureResult sendXProcedureRequest(XTable inputTable) {
		HttpEntity<XTable> request = new HttpEntity<>(inputTable);
		ResponseEntity<XSqlProcedureResult> response = restTemplate.exchange(url + "/data/x-procedure", HttpMethod.POST, request, XSqlProcedureResult.class);
		return response.getBody();
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
	public byte[] sendGetFileRequest(String path) {
		HttpEntity<String> request = new HttpEntity<>(path);
		ResponseEntity<byte[]> response = restTemplate.exchange(url + "/files/read", HttpMethod.POST, request, byte[].class);
		return response.getBody();
	}

	/**
	 * Sendet einen File-Namen oder Pfad zur Datei an den Server. Gibt den Hash der gesendeten Datei zurück.
	 * 
	 * @param path
	 *            Der Pfad oder nur er Name der zu hashenden Datei als String.
	 * @return Den Hash der Datei als byte[].
	 */
	public byte[] sendGetHashRequest(String path) {
		HttpEntity<String> request = new HttpEntity<>(path);
		ResponseEntity<byte[]> response = restTemplate.exchange(url + "/files/hash", HttpMethod.POST, request, byte[].class);
		return response.getBody();
	}

	/**
	 * Sendet einen Ordner-Namen oder Pfad zum Ordner an den Server. Gibt den Ordner als Zip zurück.
	 * 
	 * @param path
	 *            Der Pfad zum Ordner oder der Name des Ordners als String.
	 * @return Das Zip des Ordners als byte[].
	 */
	public byte[] sendGetZipRequest(String path) {
		HttpEntity<String> request = new HttpEntity<>(path);
		ResponseEntity<byte[]> response = restTemplate.exchange(url + "/files/zip", HttpMethod.POST, request, byte[].class);
		return response.getBody();
	}

	/**
	 * Lädt eine Datei vom Client hoch zum Server. Wird genutzt, um Logs hochzuladen.
	 * 
	 * @param log
	 *            Die Log-Datei als byte[].
	 * @return HtpStatus.OK bei Erfolg.
	 */
	public HttpStatus sendUploadLogRequest(byte[] log) {
		HttpEntity<byte[]> request = new HttpEntity<>(log);
		ResponseEntity<Void> response = restTemplate.exchange(url + "/upload/logs", HttpMethod.POST, request, Void.class);
		return response.getStatusCode();
	}
}
