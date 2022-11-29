package aero.minova.cas.api.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Table implements Serializable {
	private static final long serialVersionUID = 202106161633L;
	private String name;
	private TableMetaData metaData;
	private List<Column> columns = new ArrayList<>();
	private List<Row> rows = new ArrayList<>();
	private ErrorMessage returnErrorMessage;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Value getValue(String columnName, Row row) {
		for (int i = 0; i < columns.size(); i++) {
			if (columns.get(i).getName().equalsIgnoreCase(columnName)) {
				return row.getValues().get(i);
			}
		}

		return null;
	}

	public void fillMetaData(Table inputTable, int limit, int totalResults, int page) {
		if (inputTable.getMetaData() == null) {
			metaData = new TableMetaData();
		} else {
			this.metaData = inputTable.getMetaData();
		}

		if (limit <= 0) {
			limit = totalResults;
		}
		if (totalResults > 0 && limit > 0) {
			int totalPages = (int) Math.ceil(totalResults / (double) limit);
			metaData.setResultsLeft(Math.max(totalResults - (page * limit), 0));
			metaData.setTotalPages(totalPages);
		}
		metaData.setLimited(limit);
		metaData.setPage(page);
		metaData.setTotalResults(totalResults);
	}

	/**
	 * Sucht anhand des Spaltennamens an welcher Position sich diese befindet und gibt die Stelle zurück. Falls der Name nicht vorkommt, wird eine
	 * IllegalArgumentException geworfen.
	 * 
	 * @param t
	 *            Tabelle, in welcher gesucht werden soll.
	 * @param columnName
	 *            Spaltenname, nach welchem gesucht werden soll.
	 * @return Die Position als int.
	 */
	public int findColumnPosition(String columnName) {
		for (int i = 0; i < columns.size(); i++) {
			if (columns.get(i).getName().equalsIgnoreCase(columnName)) {
				return i;
			}
		}
		throw new IllegalArgumentException("Column name " + columnName + " could not be found for table " + name + "!");
	}

	public void addColumn(Column c) {
		if (!getRows().isEmpty()) {
			throw new IllegalArgumentException();
		}
		getColumns().add(c);
	}

	public void addColumns(List<Column> c) {
		c.forEach(this::addColumn);
	}

	public void addRow(Row r) {
		if (getColumns().size() != r.getValues().size()) {
			throw new IllegalArgumentException("msg.TableError");
		}
		getRows().add(r);
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public List<Row> getRows() {
		return rows;
	}

	public void setRows(List<Row> rows) {
		this.rows = rows;
	}

	public TableMetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(TableMetaData metaData) {
		this.metaData = metaData;
	}

	public ErrorMessage getReturnErrorMessage() {
		return returnErrorMessage;
	}

	public void setReturnErrorMessage(ErrorMessage errorMessage) {
		this.returnErrorMessage = errorMessage;
	}
}