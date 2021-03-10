package aero.minova.core.application.system.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.Table;
import aero.minova.core.application.system.domain.Value;
import aero.minova.core.application.system.sql.SystemDatabase;
import lombok.val;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	SystemDatabase systemDatabase;
	Logger logger = LoggerFactory.getLogger(SqlViewController.class);

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

	private Table prepareExceptionReturnTable(Exception ex) {
		Table outputTable = new Table();
		outputTable.setName("Error");
		outputTable.addColumn(new Column("International Message", DataType.STRING));
		String errorMessage = ex.getMessage();

		if (errorMessage == null) {
			errorMessage = "msg.NoErrorMessageAvailable";
		}
		// falls die Message in der Form 'msg.Beispiel %ParameterDerInDieMessageNachDemÜbersetzenEingefügtWird' ist
		List<String> errorMessageParts = Stream.of(errorMessage.split("%"))//
				.map(String::trim)//
				.collect(Collectors.toList());

		// Alle Spalten müssen erstellt werden BEVOR sie befüllt werden
		// Hinzufügen der Spalten für die InputParameter der Internationalierung
		for (int i = 1; i < errorMessageParts.size(); i++) {
			outputTable.addColumn(new Column("MessageInputParam" + i, DataType.STRING));
		}

		Row internatMsg = new Row();

		// Hinzufügen der International Message und der InputParameter der Internationalierung
		for (int i = 0; i < errorMessageParts.size(); i++) {
			Value param = new Value(errorMessageParts.get(i), null);
			internatMsg.addValue(param);
		}

		outputTable.addRow(internatMsg);

		Exception sqlE = new Exception("Couldn't execute query: " + errorMessage, ex);
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

	public void saveErrorInDatabase(Exception e) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
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
			logger.info("Execute : " + errorStatement + " with values: " + username + ", " + e.getMessage() + ", " + timeOfError);
			callableErrorStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e1) {
			logger.error("Error could not be saved in database");
		} finally {
			systemDatabase.freeUpConnection(connection);
		}
	}
}
