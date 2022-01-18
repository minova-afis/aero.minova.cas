package aero.minova.core.application.system;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.NoSuchFileException;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.ErrorMessage;
import aero.minova.core.application.system.domain.ProcedureException;
import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.SqlProcedureResult;
import aero.minova.core.application.system.domain.Table;
import aero.minova.core.application.system.domain.TableException;
import aero.minova.core.application.system.domain.Value;
import aero.minova.core.application.system.domain.XProcedureException;
import aero.minova.core.application.system.domain.XSqlProcedureResult;
import aero.minova.core.application.system.domain.XTable;
import aero.minova.core.application.system.sql.SystemDatabase;
import lombok.val;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	SystemDatabase systemDatabase;
	CustomLogger customLogger = new CustomLogger();

	@ExceptionHandler(XProcedureException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public List<XSqlProcedureResult> xProcedureException(XProcedureException ex, WebRequest request) {
		return prepareExceptionReturnXSqlProcedureResult(ex);
	}

	@ExceptionHandler(ProcedureException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public SqlProcedureResult procedureException(ProcedureException ex, WebRequest request) {
		return prepareExceptionReturnSqlProcedureResult(ex);
	}

	@ExceptionHandler(TableException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public Table tableException(TableException ex, WebRequest request) {
		return prepareExceptionReturnTable(ex);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public Table illegalArgumentException(IllegalArgumentException ex, WebRequest request) {
		return prepareExceptionReturnTable(ex);
	}

	@ExceptionHandler(UnsupportedOperationException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public Table unsupportedOperationException(UnsupportedOperationException ex, WebRequest request) {
		return prepareExceptionReturnTable(ex);
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public Table runtimeException(RuntimeException ex, WebRequest request) {
		return prepareExceptionReturnTable(ex);
	}

	@ExceptionHandler(SQLException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public Table sQLException(SQLException ex, WebRequest request) {
		return prepareExceptionReturnTable(ex);
	}

	@ExceptionHandler(NoSuchFileException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public Table noSuchFileException(NoSuchFileException ex, WebRequest request) {
		return prepareExceptionReturnTable(ex);
	}

	@ExceptionHandler(IllegalAccessException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public Table illegalAccessException(IllegalAccessException ex, WebRequest request) {
		return prepareExceptionReturnTable(ex);
	}

	/**
	 * Gibt eine Table mit dem Namen "Error" zurück. Diese beinhaltet in der ersten Row eine detailierte Fehlermeldung. In allen darauffolgenden Rows werden
	 * weitere Parameter, welche später auf der Clientseite in die Fehlermeldung eingefügt werden, aufgelistet. Diese werden mit in der Message der Exception
	 * übergeben und werden mit '%' gekennzeichnet. Die Exception wird außerdem zusammen mit dem Username und dem Datum in derDatenbank geloggt.
	 * 
	 * @param ex
	 *            Die Exception.
	 * @return Eine Table, welche die Exception in Tabellenform beinhaltet.
	 */
	protected Table prepareExceptionReturnTable(Exception ex) {
		Table outputTable = new Table();
		outputTable.setName("Error");
		outputTable.addColumn(new Column("International Message", DataType.STRING));
		String errorMessage = null;
		try {
			errorMessage = ex.getCause().getMessage();
		} catch (Exception e) {
			errorMessage = ex.getMessage();
		}

		if (errorMessage == null) {
			errorMessage = "msg.NoErrorMessageAvailable";
		}

		// Alles vor 'msg.' wegschmeißen.
		errorMessage = errorMessage.substring(errorMessage.indexOf("msg."));

		/*
		 * Es gibt zwei Fehlermeldungsformate: 1. 'ADO | 25 | msg.sql.51103 @p tUnit.Description.16 @s kg | Beipieltext' 2. 'msg.PrivilegeError %tBeispiel'
		 */
		if (errorMessage.contains("|")) {
			// Verarbeiten der Fehlermeldungen in Form: 1. 'ADO | 25 | msg.sql.51103 @p tUnit.Description.16 @s kg | Beipieltext'
			outputTable = handleSqlErrorMessage(outputTable, errorMessage);
		} else {
			// Verarbeiten der Fehlermeldungen in Form: 2. 'msg.PrivilegeError %tBeispiel'
			outputTable = handleGenericErrorMessage(outputTable, errorMessage);
		}

		Exception sqlE = new Exception(errorMessage, ex);
		ErrorMessage error = new ErrorMessage();
		error.setErrorMessage(sqlE);
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String messages = sw.toString();
		List<String> trace = Stream.of(messages.split("\n\tat|\n"))//
				.map(String::trim)//
				.collect(Collectors.toList());
		error.setTrace(trace);
		outputTable.setReturnErrorMessage(error);
		saveErrorInDatabase(ex);
		return outputTable;
	}

	/**
	 * Bringt das Format 'msg.PrivilegeError %tBeispiel' in Form einer Table noch ohne Stacktrace und returnErrorMessage.
	 * 
	 * @param outputTable
	 *            Die bisher gebaute Table, welche dann auch uzrückgegeben wird.
	 * @param errorMessage
	 *            Die Fehlermeldung, welche auseinander gebaut werden muss.
	 * @return Die outputTable befüllt mit dem Inhalt der errorMessage.
	 */
	private Table handleGenericErrorMessage(Table outputTable, String errorMessage) {
		Row parameterValues = new Row();

		// Falls in der Message noch Parameter mit '%' vorkommen, z.B.: 'msg.Beispiel %ParameterDerInDieMessageNachDemÜbersetzenEingefügtWird', werden sie hier
		// raus gefiltert und kommen in ihre eigene Zeile.
		List<String> errorMessageParts = Stream.of(errorMessage.split("%"))//
				.map(String::trim)//
				.collect(Collectors.toList());

		// Alle Spalten müssen erstellt werden BEVOR sie befüllt werden
		// Hinzufügen der Spalten für die InputParameter der Internationalierung
		for (int i = 1; i < errorMessageParts.size(); i++) {
			outputTable.addColumn(new Column("MessageInputParam" + i, DataType.STRING));
		}

		// Hinzufügen der International Message und der InputParameter der Internationalierung
		for (int i = 0; i < errorMessageParts.size(); i++) {
			Value param = new Value(errorMessageParts.get(i), null);
			parameterValues.addValue(param);
		}

		outputTable.addRow(parameterValues);
		return outputTable;
	}

	/**
	 * Bringt das Format 'ADO | 25 | msg.sql.51103 @p tUnit.Description.16 @s kg | Beipieltext' in Form einer Table noch ohne Stacktrace und returnErrorMessage.
	 * 
	 * @param outputTable
	 *            Die bisher gebaute Table, welche dann auch uzrückgegeben wird.
	 * @param errorMessage
	 *            Die Fehlermeldung, welche auseinander gebaut werden muss.
	 * @return Die outputTable befüllt mit dem Inhalt der errorMessage.
	 */
	private Table handleSqlErrorMessage(Table outputTable, String errorMessage) {

		List<String> sqlErrorMessage = Stream.of(errorMessage.split("\\|"))//
				.map(String::trim)//
				.collect(Collectors.toList());

		// Ab hier benutzen wir den ersten Teil der sqlErrorMessage.

		// Splitte den String überall da, wo ein @ vorkommt.
		String[] types = sqlErrorMessage.get(0).split("@");

		Row internatMsg = new Row();

		// Falls es sich um die erste Art der Fehlermeldung handelt, wird hier 'msg.sql.BeispielFehlermeldung' eingefügt.
		internatMsg.addValue(new Value(types[0], null));

		// Im ersten Array ist der Part mit msg.Fehlermeldung. Den wollen wir hier nicht.
		// Im letzten Array von types ist nur noch die Standard-Errormessage. Die brauchen wir hier auch nicht.
		for (int i = 1; i < types.length; i++) {
			types[i] = types[i].trim();
			int blank = types[i].indexOf(' ');
			outputTable.addColumn(new Column(types[i].substring(0, blank), DataType.STRING));
			internatMsg.addValue(new Value((types[i].subSequence(blank + 1, types[i].length())).toString(), null));
		}

		// Die Column MUSS 'DEFAULT' heißen.
		outputTable.addColumn(new Column("DEFAULT", DataType.STRING));

		// Die Standard-Fehlermeldung hinzufügen
		internatMsg.addValue(new Value(sqlErrorMessage.get(1), null));

		outputTable.addRow(internatMsg);
		return outputTable;
	}

	/**
	 * Gibt die Exception in Form eines SqlProcedureResults zurück. Die Table, welches die detailierte Exception beinhaltet, ist hierbei das ResultSet des
	 * SqlProcedureResults. Alle möglichen ReturnCodes werdenauf -1 gesetzt. Die Exception wird außerdem zusammen mit dem Username und dem Datum in derDatenbank
	 * geloggt.
	 * 
	 * @param ex
	 *            Die Exception.
	 * @return Ein SqlProcedureResult, welches die Exception in Tabellenform als ResultSet beinhaltet.
	 */
	protected SqlProcedureResult prepareExceptionReturnSqlProcedureResult(Exception ex) {
		SqlProcedureResult result = new SqlProcedureResult();
		Table resultSetTable = prepareExceptionReturnTable(ex);

		result.setResultSet(resultSetTable);
		List<Integer> errorReturnCode = new ArrayList<>();
		errorReturnCode.add(-1);
		result.setReturnCodes(errorReturnCode);
		result.setReturnCode(-1);
		return result;
	}

	/**
	 * Verarbeitet die xProcedureException. Die Liste der XSqlProcedureResult, welche zurück gegeben wird, beinhaltet erst die erfolgreich verarbeiteten
	 * XSqlProcedureResults, dann ein XSqlProcedureResult, welches die typische Fehlermeldung beinhaltet. Dann wird die Liste der XSqlProcedureResults so lange
	 * mit leeren XSqlProcedureResults gefüllt, bis sie dieselbe Länge hat, wie die anfagns gesendete XTable. Hierbei werden alle IDs der XTables in derselben
	 * Reihenfolge übernommen. Die Exception wird außerdem zusammen mit dem Username und dem Datum in derDatenbank geloggt.
	 * 
	 * @param ex
	 *            Eine XProcedureException. Diese beinhaltet neben der Exception, noch eine Liste an XTable, welche bei der Anfrage der Transaktion übergeben
	 *            wurde, und die bisherigen Results als Liste.
	 * @return Eine Liste von XSqlProcedureResults.
	 */
	protected List<XSqlProcedureResult> prepareExceptionReturnXSqlProcedureResult(XProcedureException ex) {
		List<XTable> xtables = ex.getXTables();
		List<XSqlProcedureResult> results = ex.getResults();
		List<XSqlProcedureResult> xsqlProcedureResults = new ArrayList<>();

		// Ergebnisse von erfolgreich durchgeführten Prozeduren übernehmen.
		xsqlProcedureResults.addAll(results);

		// Error Eintrag an der Stelle, an der der Fehler autritt, machen.
		SqlProcedureResult errorResult = prepareExceptionReturnSqlProcedureResult(ex);
		String errorID = xtables.get(results.size()).getId();
		XSqlProcedureResult error = new XSqlProcedureResult(errorID, errorResult);
		xsqlProcedureResults.add(error);

		// Damit das XSqlProcedureResult auf dieselbe Länge kommt, wie die XTable-Liste, restliche Einträge mit null füllen.
		while (xsqlProcedureResults.size() < xtables.size()) {
			SqlProcedureResult innerNullResultSet = new SqlProcedureResult();
			innerNullResultSet.setReturnCode(-1);
			XSqlProcedureResult nullResult = new XSqlProcedureResult(xtables.get(xsqlProcedureResults.size()).getId(), innerNullResultSet);
			xsqlProcedureResults.add(nullResult);
		}
		return xsqlProcedureResults;
	}

	/**
	 * Erzeugt einen Eintrag in der Tabelle xtcasError. Hierbei wird der Nutzer, die Fehlermeldung und das momentane Datum in der Datenbank eingetragen.
	 * 
	 * @param e
	 *            Die Exception, welche geworfen wurde.
	 */
	public void saveErrorInDatabase(Exception e) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		// Abfrage mur für jUnit-Tests, da dabei Authentication = null
		if (auth != null) {
			username = auth.getName();
		} else {
			username = "unknown";
		}
		String errorStatement = "INSERT INTO xtcasError (Username, ErrorMessage, Date) VALUES (?,?,?)";

		final val connection = systemDatabase.getConnection();
		try {
			Timestamp timeOfError = Timestamp.from(Instant.now());
			CallableStatement callableErrorStatement = connection.prepareCall(errorStatement);
			callableErrorStatement.setString(1, username);
			callableErrorStatement.setString(2, e.getMessage());
			callableErrorStatement.setTimestamp(3, timeOfError);
			{
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				logger.info(
						"CAS : Execute : " + errorStatement + " with values: " + username + ", " + e.getMessage() + ", " + timeOfError + "/n" + sw.toString());
			}
			callableErrorStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e1) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e1.printStackTrace(pw);
			customLogger.errorLogger.error("CAS : Error could not be saved in database." + "/n" + sw.toString());
		} finally {
			systemDatabase.freeUpConnection(connection);
		}
	}
}
