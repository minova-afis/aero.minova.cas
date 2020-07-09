package ch.minova.service.core.application.system.domain;

import java.util.ArrayList;
import java.util.List;

public class Table {
	String name;
	List<Column_> columns = new ArrayList<>();
	List<Row> rows = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addColumn(Column_ c) {
		if (getRows().size() != 0) {
			throw new IllegalArgumentException();
		}
		getColumns().add(c);
	}
	
	public void addColumns(List<Column_ > c) {
		c.forEach(this::addColumn);
	}

	public void addRow(Row r) {
		if (getColumns().size() != r.getValues().size()) {
			throw new IllegalArgumentException();
		}
		getRows().add(r);
	}

	public List<Column_> getColumns() {
		return columns;
	}

	public void setColumns(List<Column_> columns) {
		this.columns = columns;
	}

	public List<Row> getRows() {
		return rows;
	}

	public void setRows(List<Row> rows) {
		this.rows = rows;
	}
}
