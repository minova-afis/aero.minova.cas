package aero.minova.core.application.system.sql;

import java.util.HashSet;
import java.util.Set;

/**
 * Wurde aus ch.minova.ncore.data.source.IDataSource kopiert.
 * 
 * Beschreibt eine Strategie bei der Skriptausführung
 * 
 * @author dombrovski
 */
public enum ExecuteStrategy {
	// ############################
	// ####### ALLGEMEINES ########
	// ############################
	/**
	 * Die Ausführung findet nur statt wenn die Eingangstabelle mindestens eine Zeile hat. Wenn die Eingabe leer ist, dann werden trotzdem alle Events (BEFORE,
	 * AFTER_EXECUTE usw) verteilt
	 */
	EXECUTE_ONLY_WITH_NON_NULL_INPUT,
	/** Vor und nach der Ausführung wird eine Transaktion gestartet bzw. gestoppt */
	USE_TRANSACTION,
	/** Vor und nach der Ausführung wird NOCOUNT an- bzw. ausgeschaltet */
	USE_NOCOUNT,
	/** True wenn die Datenquelle im eigenen Thread Daten laden soll */
	EXECUTE_IN_OWN_THREAD,
	/** Wenn die Datenquelle mehr Parameter liefert, als definiert wurden, dann wird die interne Tabelle automatisch um weiter Felder/Spalten erweitert */
	AUTO_TABLE_EXTENSION,
	/**
	 * Im Falle des Neufüllens der result-Table werden Marker (selected, deleted) und ggf. andere Anzeigeeinstellungen gespeichert und nach dem Neufüllens
	 * wieder gesetzt.
	 */
	RESTORE_MARKER,
	/** Wenn true, dann wird (wenn möglich) eine Ähnlichkeitssuche verwendet (hängt von der Implementierung ab) */
	AUTO_LIKE,
	/** zeigt eine Hinweismeldung an, falls beim Laden keine Daten gefunden wurden */
	INFO_IF_NO_RESULT,
	/** Versucht die Datenquelle erneut auszuführen, falls ein Fehler aufgetreten ist */
	DEADLOCK_TRY_AGAIN,
	/** Wenn möglich, wird im Falle eines DeadLocks eine entsprechende Meldung angezeigt */
	DEADLOCK_POPUP,
	/** falls beim Speichern einer Zeile ein Fehler auftritt, werden die anderen Zeilen noch verarbeitet und ein Pool aller Fehler geworfen */
	POOL_ERRORS,

	// ############################
	// ###### SET STRATEGIE #######
	// ############################
	/** Wenn gesetzt, dann werden die Felder nach dem Setzen validiert */
	VALIDATE_FIELDS,
	/** Vor der Ausführung erhält die Prozedur Primärschlüssel */
	SET_PRIMARY_FIELDS,
	/** Vor der Ausführung erhält die Prozedur alle Schlüssel (Default) */
	SET_NON_PRIMARY_FIELDS,
	/** Vor der Ausführung erhält die Prozedur die mit prefDataSourceSet markierten Felder */
	SET_PREF_FIELDS, // #24845
	/** Die Prozedur erhält die Werte anhand der Feldnamen */
	SET_VIA_FIELD_NAME,
	/** die Prozedur erhält die Werte anhand des Feldindexes */
	SET_VIA_FIELD_INDEX,

	// ###############################
	// ####### OUTPUT STRATEGIE ######
	// ###############################
	/** ALT - KOMPATIBILITÄT */
	@Deprecated
	OUTPUT_PRIMARY_FIEDS,
	/** Primärschlüssel als output definieren */
	OUTPUT_PRIMARY_FIELDS,
	/** Nicht-primärschlüssel als output definieren */
	OUTPUT_NON_PRIMARY_FIELDS,
	/** mit prefDataSourceOut markierte Felder als output definieren */
	OUTPUT_PREF_FIELDS, // #24845
	/** Der Output wird mittels SELECT erzeugt (SQL-Only) */
	OUTPUT_VIA_SELECT,
	/** Fragt den Benutzer (wenn möglich) wenn zu viele Datensätze geladen wurden */
	ASK_IF_TOO_MANY_RESULTS,

	// ###############################
	// ###### RETURN STRATEGIE #######
	// ###############################
	/** Der return-Wert der Daten-Komponente (z.B. Prozedur, View). Wird nicht von allen Datenquellen unterstützt */
	RETURN_CODE_IGNORE,
	/** Der return Wert <> 0 bedeutet Fehler. Wird nicht von allen Datenquellen unterstützt */
	RETURN_CODE_IS_ERROR_IF_NOT_0,
	/** Der return Wert <> 1 bedeutet Fehler. Wird nicht von allen Datenquellen unterstützt */
	RETURN_CODE_IS_ERROR_IF_NOT_1,
	/** Der return Wert -1 bedeutet Abbruch. Wird nicht von allen Datenquellen unterstützt */
	RETURN_CODE_IS_CANCEL_IF_MINUS_1;

	/**
	 * Liefert Strategien, die konträr zu der angegebenen sind. So ist z.B. SET_ALL_FIELDS konträr zu SET_NO_FIELDS
	 */
	public static final Set<ExecuteStrategy> getContrairedStrategies(ExecuteStrategy strategy) {
		final Set<ExecuteStrategy> toRet = new HashSet<ExecuteStrategy>();
		if (strategy != null) {
			switch (strategy) {
			case RETURN_CODE_IS_ERROR_IF_NOT_0:
				toRet.add(RETURN_CODE_IS_ERROR_IF_NOT_1);
				toRet.add(RETURN_CODE_IGNORE);
				break;
			case RETURN_CODE_IS_ERROR_IF_NOT_1:
				toRet.add(RETURN_CODE_IS_ERROR_IF_NOT_0);
				toRet.add(RETURN_CODE_IGNORE);
				break;
			case RETURN_CODE_IS_CANCEL_IF_MINUS_1:
				toRet.add(RETURN_CODE_IGNORE);
				break;
			case RETURN_CODE_IGNORE:
				toRet.add(RETURN_CODE_IS_ERROR_IF_NOT_1);
				toRet.add(RETURN_CODE_IS_ERROR_IF_NOT_0);
				toRet.add(RETURN_CODE_IS_CANCEL_IF_MINUS_1);
				break;
			case SET_VIA_FIELD_INDEX:
				toRet.add(SET_VIA_FIELD_NAME);
				break;
			case SET_VIA_FIELD_NAME:
				toRet.add(SET_VIA_FIELD_INDEX);
				break;
			case DEADLOCK_POPUP:
				toRet.add(DEADLOCK_TRY_AGAIN);
				break;
			case DEADLOCK_TRY_AGAIN:
				toRet.add(DEADLOCK_POPUP);
				break;
			case SET_PRIMARY_FIELDS:
			case SET_NON_PRIMARY_FIELDS:
				toRet.add(SET_PREF_FIELDS);
				break;
			case SET_PREF_FIELDS:
				toRet.add(SET_PRIMARY_FIELDS);
				toRet.add(SET_NON_PRIMARY_FIELDS);
				break;
			case OUTPUT_PRIMARY_FIEDS:
			case OUTPUT_PRIMARY_FIELDS:
			case OUTPUT_NON_PRIMARY_FIELDS:
				toRet.add(OUTPUT_PREF_FIELDS);
				break;
			case OUTPUT_PREF_FIELDS:
				toRet.add(OUTPUT_PRIMARY_FIELDS);
				toRet.add(OUTPUT_NON_PRIMARY_FIELDS);
				break;
			default:
				break;
			}
		}
		return toRet;
	}

	/**
	 * Liefert true, wenn die Strategie einen Rückgabewert von der Prozedur (ala 'return 1') erfordert.
	 */
	public static boolean returnRequired(Set<ExecuteStrategy> strategy) {
		if (strategy == null) {
			strategy = STANDARD;
		}
		return strategy.contains(RETURN_CODE_IS_CANCEL_IF_MINUS_1) || strategy.contains(RETURN_CODE_IS_ERROR_IF_NOT_0)
				|| strategy.contains(RETURN_CODE_IS_ERROR_IF_NOT_1);
	}

	/** Standardstrategie: alle Parameter setzen (primary und non-primary) + via index setzen */
	public static final Set<ExecuteStrategy> STANDARD = new HashSet<ExecuteStrategy>();

	static {
		// Standard: Prozedur mit allen Parametern füllen und über den Index ausführen
		STANDARD.add(SET_PRIMARY_FIELDS);
		STANDARD.add(SET_NON_PRIMARY_FIELDS);
		STANDARD.add(SET_VIA_FIELD_INDEX);
		// ebenfalls Standard: nocount, das brauchen wir nur in den wenigsten Fällen... dann muss explizit
		// removeExecuteStrategy(ExecuteStrategy.USE_NOCOUNT);
		// aufgerufen werden
		// hab aber rausgefunden, dass standard nicht immer verwendet wird!
		// also muss USE_NOCOUNT derzeit manuell hinzugefügt werden wo man es braucht!
		STANDARD.add(USE_NOCOUNT);
	}
}