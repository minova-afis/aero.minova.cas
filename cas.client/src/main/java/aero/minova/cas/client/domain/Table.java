package aero.minova.cas.client.domain;

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

	public void fillMetaData(Table inputTable, int limit, int totalResults, int page) {
		TableMetaData metaData = inputTable.getMetaData();
		if (inputTable.getMetaData() == null) {
			metaData = new TableMetaData();
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

		this.metaData = metaData;
	}

	public void addColumn(Column c) {
		if (getRows().size() != 0) {
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

	public void setMetaData(TableMetaData meta_data) {
		this.metaData = meta_data;
	}

	public ErrorMessage getReturnErrorMessage() {
		return returnErrorMessage;
	}

	public void setReturnErrorMessage(ErrorMessage errorMessage) {
		this.returnErrorMessage = errorMessage;
	}
}