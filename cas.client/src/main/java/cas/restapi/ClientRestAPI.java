package cas.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import cas.domain.SqlProcedureResult;
import cas.domain.Table;
import cas.domain.XSqlProcedureResult;
import cas.domain.XTable;

@Component
public class ClientRestAPI {

	String username;
	String password;
	String url;

	@Autowired
	RestTemplate restTemplate;

	@SuppressWarnings("deprecation")
	public ClientRestAPI(String username, String password, String url) {
		this.username = username;
		this.password = password;
		this.url = url;

		restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(username, password));
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
		HttpEntity<Table> request = new HttpEntity<>(inputTable);
		ResponseEntity<Table> response = restTemplate.postForEntity(url + "/data/index", request, Table.class);
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
		ResponseEntity<SqlProcedureResult> response = restTemplate.postForEntity(url + "/data/procedure", request, SqlProcedureResult.class);
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
		ResponseEntity<XSqlProcedureResult> response = restTemplate.postForEntity(url + "/data/x-procedure", request, XSqlProcedureResult.class);
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
		ResponseEntity<byte[]> response = restTemplate.postForEntity(url + "/files/read", request, byte[].class);
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
		ResponseEntity<byte[]> response = restTemplate.postForEntity(url + "/files/hash", request, byte[].class);
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
		ResponseEntity<byte[]> response = restTemplate.postForEntity(url + "/files/zip", request, byte[].class);
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
		ResponseEntity<Void> response = restTemplate.postForEntity(url + "/upload/logs", request, Void.class);
		return response.getStatusCode();
	}
}
