package aero.minova.cas.service;

import java.util.List;

import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;

public interface ViewServiceInterface {

	// Wenn IFLESSTHANZEROTHANMAXROWS kleiner als 0 ist, werden bei View-Aufrufen immer alle Einträge zurückgegeben.
	static final int IF_LESS_THAN_ZERO_THEN_MAX_ROWS = -1;

	/**
	 * @param params
	 *            Suchzeilen (z.B. Suchparameter), wobei auch ein Spezialfeld mit dem Namen 'AND' genutzt werden kann, um die Kriterien zu verknüpfen
	 * @param autoLike
	 *            wenn true, dann werden alle String-Parameter, die noch kein % haben, mit einem '%' am Ende versehen
	 * @return die Where-Klausel für die angegebenen Parameter
	 * @author wild
	 */
	public String prepareWhereClause(Table params, boolean autoLike);

	default String prepareViewString(Table params, boolean autoLike, int maxRows, List<Row> authorities) {
		return prepareViewString(params, autoLike, maxRows, false, authorities);
	}

	/*
	 * Pagination nach der Seek-Methode; bessere Performance als Offset bei großen Datensätzen. Wird NICHT für den "normalen" Index-Aufruf verwendet, da immer
	 * davon ausgegangen wird, dass ein KeyLong in der View/Table vorhanden ist.
	 */
	public String pagingWithSeek(Table params, boolean autoLike, int maxRows, boolean count, int page, List<Row> authorities);

	/**
	 * @param params
	 *            Suchzeilen (z.B. Suchparameter), wobei auch ein Spezialfeld mit dem Namen 'AND' genutzt werden kann, um die Kriterien zu verknüpfen
	 * @param autoLike
	 *            wenn true, dann werden alle String-Parameter, die noch kein % haben, mit einem '%' am Ende versehen
	 * @param maxRows
	 *            maximale Anzahl Ergebnisse (Zeilen), die die Abfrage liefern soll, alles kleiner als 0 für unbegrenzt. Hier wird meistens einfach die
	 *            {@link #IF_LESS_THAN_ZERO_THEN_MAX_ROWS} Konstante verwendet.
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