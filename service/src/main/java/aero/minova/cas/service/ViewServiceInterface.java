package aero.minova.cas.service;

import java.util.List;

import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;

public interface ViewServiceInterface {

	/**
	 * Wie {@link #getIndexView(Table)}, nur ohne die erste Sicherheits-Abfrage, um die maximale Länge zu erhalten Ist nur für die Sicherheitsabfragen gedacht,
	 * um nicht zu viele unnötige SQL-Abfrgane zu machen.
	 *
	 * @param inputTable
	 *            Die Parameter, der SQL-Anfrage die ohne Sicherheitsprüfung durchgeführt werden soll.
	 * @return Das Ergebnis der Abfrage.
	 */
	public Table unsecurelyGetIndexView(Table inputTable);

	/**
	 * @param params
	 *            Suchzeilen (z.B. Suchparameter), wobei auch ein Spezialfeld mit dem Namen 'AND' genutzt werden kann, um die Kriterien zu verknüpfen
	 * @param autoLike
	 *            wenn true, dann werden alle String-Parameter, die noch kein % haben, mit einem '%' am Ende versehen
	 * @return die Where-Klausel für die angegebenen Parameter
	 * @author wild
	 */
	public String prepareWhereClause(Table params, boolean autoLike);

	public String prepareViewString(Table params, boolean autoLike, int maxRows, List<Row> authorities);

	/**
	 * Diese Methode stammt ursprünglich aus "ch.minova.ncore.data.sql.SQLTools#prepareViewString". Bereitet einen View-String vor und berücksichtigt eine evtl.
	 * angegebene Maximalanzahl Ergebnisse
	 *
	 * @param params
	 *            Suchzeilen (z.B. Suchparameter), wobei auch ein Spezialfeld mit dem Namen 'AND' genutzt werden kann, um die Kriterien zu verknüpfen
	 * @param autoLike
	 *            wenn true, dann werden alle String-Parameter, die noch kein % haben, mit einem '%' am Ende versehen
	 * @param maxRows
	 *            maximale Anzahl Ergebnisse (Zeilen), die die Abfrage liefern soll, 0 für unbegrenzt
	 * @param count
	 *            Gibt an ob nur die Anzahl der Ergebniss (Zeilen), gezählt werden sollen.
	 * @param authorities
	 *            Eine Liste an autorisierten UserGruppen. Wird für die RowLevelSecurity benötigt.
	 * @return Präparierter View-String, der ausgeführt werden kann
	 * @throws IllegalArgumentException
	 * @author wild
	 */
	public String prepareViewString(Table params, boolean autoLike, int maxRows, boolean count, List<Row> authorities);
}