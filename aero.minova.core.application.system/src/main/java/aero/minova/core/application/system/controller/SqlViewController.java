package aero.minova.core.application.system.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Locale;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.Table;
import lombok.val;

@RestController
public class SqlViewController {

	public static final String SQL_IS_NULL = "is null";
	public static final String SQL_IS_NOT_NULL = "is not null";
	public static final String[] SQL_OPERATORS = { "<>", "<=", ">=", "<", ">", "=", "between(", "in(", "not like", "like", SQL_IS_NULL, SQL_IS_NOT_NULL };

	Connection sqlConnection;

	private Connection msSqlConnection() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			return DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=AFIS_HAM", "sa", "Minova+0");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GetMapping(value = "data/index", produces = "application/json")
	public Table getIndexView() {
		try {
		if (sqlConnection == null) {
			sqlConnection = msSqlConnection();
		}
		Table movementTable = new Table();
		sqlConnection.createStatement().execute(sql)
		return movementTable;
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * TODO name, viewFields entfernen. Diese methode stammt ursprünglich aus "ch.minova.ncore.data.sql.SQLTools#prepareViewString". Bereitet einen View-String
	 * vor und berücksichtigt eine evtl. angegebene Maximalanzahl Ergebnisse
	 * 
	 * @param name
	 *            View/Tabellenname
	 * @param viewFields
	 *            Felder, die in der View erwartet werden
	 * @param params
	 *            Suchzeilen (z.B. Suchparameter), wobei auch ein Spezialfeld mit dem Namen 'AND' genutzt werden kann, um die Kriterien zu verknüpfen
	 * @param autoLike
	 *            wenn true, dann werden alle String-Parameter, die noch kein % haben, mit einem '%' am Ende versehen
	 * @param maxRows
	 *            maximale Anzahl Ergebnisse (Zeilen), die die Abfrage liefern soll, 0 für unbegrenzt
	 * @return Präparierter View-String, der ausgeführt werden kann
	 * @author wild
	 * @since 10.28.0
	 * @throws IllegalArgumentException
	 */
	String prepareViewString(Table params, boolean autoLike, int maxRows) throws IllegalArgumentException {

		final StringBuffer sb = new StringBuffer("select");
		if (maxRows > 0) {
			sb.append(" top ").append(maxRows);
		}
		sb.append(" * from ").append(params.getName());

		if (params.getColumns().size() > 0 && params.getRows().size() > 0) {
			final String where = prepareWhereClause(params, autoLike);
			sb.append(where);
		}

		return sb.toString();
	}

	/**
	 * @param viewFields
	 *            Felder, die in der View erwartet werden
	 * @param params
	 *            Suchzeilen (z.B. Suchparameter), wobei auch ein Spezialfeld mit dem Namen 'AND' genutzt werden kann, um die Kriterien zu verknüpfen
	 * @param autoLike
	 *            wenn true, dann werden alle String-Parameter, die noch kein % haben, mit einem '%' am Ende versehen
	 * @return die Where-Klausel für die angegebenen Parameter
	 * @author wild
	 * @since 10.28.0
	 */
	private String prepareWhereClause(Table params, boolean autoLike) {
		final StringBuffer where = new StringBuffer();
		for (int rowI = 0; rowI < params.getRows().size(); rowI++) {
			final Row r = params.getRows().get(rowI);
			// TODO Nicht annehmen, dass die spezielle &-Spalte die letzte Spalte ist.
			final boolean and = r.getValues().get(r.getValues().size() - 1).getBooleanValue();

			// Eine where Zeile aufbauen
			final StringBuffer clause = new StringBuffer();
			COLS: for (final Column def : params.getColumns()) {
				if (Column.AND_FIELD_NAME.equalsIgnoreCase(def.getName())) {
					continue COLS;
				}

				final Object valObj = r.getValues().get(params.getColumns().indexOf(def)).getValue();
				String strValue = valObj.toString().trim();
				if (strValue != null && strValue.length() != 0) {
					if (clause.length() > 0) {
						clause.append(" and ");
					}
					if (def.getType().equals(DataType.DATE)) {
						// #1543: nur Zeit vergleichen!
						clause.append("convert(nvarchar(8), ").append(def.getName()).append(", 8)");
					} else {
						clause.append(def.getName());
					}

					// #13193
					if (strValue.equalsIgnoreCase("null") || strValue.equalsIgnoreCase("not null")) {
						strValue = "is " + strValue;
					}
					if (!hasOperator(strValue)) {
						if (autoLike && valObj instanceof String && def.getType() == DataType.STRING && !strValue.contains("%")) {
							strValue += "%";
						}

						if (def.getType() == DataType.STRING && (strValue.contains("%") || strValue.contains("_"))) {
							clause.append(" like");
						} else {
							clause.append(" =");
						}
					}

					strValue = encloseInCommasIfRequired(def, strValue);
					clause.append(' ').append(strValue);
				}
			}

			// Wenn es etwas gab, dann fügen wir diese Zeile der kompletten WHERE-clause hinzu
			if (clause.length() > 0) {
				if (where.length() == 0) {
					where.append("\r\nwhere ");
				} else {
					where.append(and ? "\r\n  and " : "\r\n   or ");
				}
				where.append('(').append(clause.toString()).append(')');
			}
		}

		return where.toString();
	}

	/**
	 * Abhängig von dem Feld-Typ, wird der Wert von Kommas umgeben oder nicht
	 */
	protected static String encloseInCommasIfRequired(Column vd, String value) {
		if (vd == null || value == null) {
			return value;
		}

		if (SQL_IS_NULL.equalsIgnoreCase(value.trim()) || SQL_IS_NOT_NULL.equalsIgnoreCase(value.trim())) {
			return value;
		} else if (vd.getType() == DataType.STRING || vd.getType().equals(DataType.INSTANT) || vd.getType().equals(DataType.ZONED)) {
			final int i = getOperatorEndIndex(value);
			if (i > 0) {
				return value.substring(0, i) + " '" + value.substring(i).trim() + "'";
			} else {
				return "'" + value.substring(i).trim() + "'";
			}
		}
		return value;
	}

	/**
	 * Prüft, ob der String einen SQL Operator am Anfang hat
	 * 
	 * @param value
	 * @return
	 */
	protected static boolean hasOperator(String value) {
		return getOperatorEndIndex(value) != 0;
	}

	/**
	 * Wenn es einen Operator gibt, dann liefert die Funktion den Index bis zu dem sich der Operator erstreckt
	 * 
	 * @param value
	 * @return 0, wenn es keinen Operator gibt
	 */
	protected static int getOperatorEndIndex(String value) {
		if (value == null || value.length() == 0) {
			return 0;
		}
		// Wir simulieren einen ltrim, um die Anfangsposition des Operators festzustellen
		String tmp = (value + "_").trim();
		tmp = tmp.toLowerCase(Locale.ENGLISH).substring(0, tmp.length() - 1);
		final int shift = value.length() - tmp.length();
		for (final String sqlOperator : SQL_OPERATORS) {
			if (tmp.startsWith(sqlOperator)) {
				return shift + sqlOperator.length();
			}
		}
		return 0;
	}

}