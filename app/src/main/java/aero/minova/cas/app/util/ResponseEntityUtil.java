package aero.minova.cas.app.util;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import aero.minova.cas.api.domain.SqlProcedureResult;
import aero.minova.cas.api.domain.Table;

public class ResponseEntityUtil {
	private ResponseEntityUtil() {
	}

	/**
	 * Erstellt eine ResponseEntity aus einer Tabelle
	 * 
	 * @param table
	 * @param setAsResultSet true für z.B. List/Resolve von Lookups oder Lesen von
	 *                       Grids, false für fast alles andere
	 * @return
	 */
	public static ResponseEntity<SqlProcedureResult> createResponseEntity(Table table, boolean setAsResultSet) {
		SqlProcedureResult res = new SqlProcedureResult();
		if (setAsResultSet) {
			res.setResultSet(table);
		} else {
			res.setOutputParameters(table);
		}
		res.setReturnCode(0);
		return ResponseEntity//
				.ok()//
				.contentType(MediaType.APPLICATION_JSON)//
				.body(res);
	}

	/**
	 * Erstellt eine ResponseEntity für Fehler aus einer Tabelle
	 * 
	 * @param table
	 * @param setAsResultSet true für z.B. List/Resolve von Lookups oder Lesen von
	 *                       Grids, false für fast alles andere
	 * @return
	 */
	public static ResponseEntity<SqlProcedureResult> createResponseEntityError(Table table, boolean setAsResultSet) {
		SqlProcedureResult res = new SqlProcedureResult();
		if (setAsResultSet) {
			res.setResultSet(table);
		} else {
			res.setOutputParameters(table);
		}
		res.setReturnCode(-1);
		return ResponseEntity//
				.badRequest()//
				.contentType(MediaType.APPLICATION_JSON)//
				.body(res);
	}

}
