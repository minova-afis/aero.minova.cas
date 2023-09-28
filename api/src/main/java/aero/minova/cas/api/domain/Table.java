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

	public Value getValue(int columnIndex, int rowIndex) {
		return getRows().get(rowIndex).getValues().get(columnIndex);
	}

	public Value getValue(String columnName, int rowIndex) {
		for (int i = 0; i < columns.size(); i++) {
			if (columns.get(i).getName().equalsIgnoreCase(columnName)) {
				return rows.get(rowIndex).getValues().get(i);
			}
		}

		return null;
	}

	public void setValue(Value v, String columName, int rowIndex) {
		int colIndxByName = findColumnPosition(columName);
		rows.get(rowIndex).getValues().set(colIndxByName, v);
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
	 * Sucht anhand des Spaltennamens an welcher Position sich diese befindet und gibt die Stelle zurück. Falls der Name nicht vorkommt, wird -1 zurückgegeben
	 * 
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
		return -1;
	}

	/**
	 * Gibt die Spalte mit dem gegebenen Namen zurück (Case egal). Wird keine passende Spalte gefunden wird null zurückgegeben
	 * 
	 * @param columnName
	 *            Spaltenname, nach welchem gesucht werden soll.
	 * @return
	 */
	public Column getColumn(String columnName) {
		for (Column c : columns) {
			if (c.getName().equalsIgnoreCase(columnName)) {
				return c;
			}
		}
		return null;
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

	@Override
	public String toString() {
		StringBuilder columnsString = new StringBuilder("\n");
		for (Column c : columns) {
			columnsString.append(c + " ");
		}
		StringBuilder rowString = new StringBuilder("\n");
		for (Row r : rows) {
			rowString.append(r + "\n");
		}
		return "Table " + name + columnsString.toString() + rowString.toString();
	}
}