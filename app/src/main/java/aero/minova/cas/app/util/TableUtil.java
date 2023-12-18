package aero.minova.cas.app.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import com.google.gson.JsonPrimitive;

import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;

public class TableUtil {
	static TemporalDeserializer ds = new TemporalDeserializer();

	private TableUtil() {
	}

	/**
	 * Erzeuge eine Table mit den Metadaten der <code>tableWithDataTypes</code> und
	 * dem Inhalt aus der <code>jsonTable</code>. <br>
	 * Hinweis: wenn es in <code>jsonTable</code> <code>null</code>-Werte gibt,
	 * werden diese in die entsprechenden Zellen im Ergebnis kopiert! <br>
	 * Hinweis: setTotalResults() wirkt auch auf die herein gegebene Table.
	 *
	 * @param jsonTable
	 * @param tableWithDataTypes stellt den Namen und die Column-Datentypen bereit.
	 * @return eine neue Table mit gleicher Struktur wie tableWithDataTypes und dem
	 *         Inhalt aus jsonTable.
	 */
	public static Table addDataTypeToTable(Table jsonTable, Table tableWithDataTypes) {

		Table resultTable = new Table();
		resultTable.setName(tableWithDataTypes.getName());
		if (tableWithDataTypes.getMetaData() != null) {
			resultTable.setMetaData(tableWithDataTypes.getMetaData());
			resultTable.getMetaData().setTotalResults(jsonTable.getRows().size()); // damit wird auch tableWithDataTypes
																					// verändert!
		}

		for (Column c : tableWithDataTypes.getColumns()) {
			resultTable.addColumn(new Column(c.getName(), c.getType(), c.getOutputType()));
		}

		for (Row r : jsonTable.getRows()) {

			Row newRow = new Row();

			for (int i = 0; i < resultTable.getColumns().size(); i++) {

				int indexInJson;
				try {
					indexInJson = jsonTable.findColumnPosition(resultTable.getColumns().get(i).getName());
				} catch (IllegalArgumentException e) {
					indexInJson = -1;
				}

				if (indexInJson < 0) {
					newRow.addValue(null);
				} else {
					Value stringValue = r.getValues().get(indexInJson);
					DataType typeToConvertTo = resultTable.getColumns().get(i).getType();
					Value convertedValue = convertValue(stringValue, typeToConvertTo);
					newRow.addValue(convertedValue);
				}
			}

			resultTable.addRow(newRow);
		}

		return resultTable;
	}

	/**
	 * Vom übergebenen Value wird der StringValue geholt und in den gewünschten Typ
	 * gewandelt.
	 *
	 * @param stringValue     wird in gewünschten Typ gewandelt, kann
	 *                        <code>null</code> ergeben.
	 * @param typeToConvertTo gewünschter Ziel-Typ
	 * @return Value mit gewünschten Ziel-Typ, oder <code>null</code>.
	 */
	private static Value convertValue(Value stringValue, DataType typeToConvertTo) {

		if (stringValue == null) {
			return null;
		}

		String stringRep = stringValue.getStringValue();
		Value v;
		switch (typeToConvertTo) {
		case BIGDECIMAL:
			v = new Value(BigDecimal.valueOf(Double.parseDouble(stringRep)), null);
			break;
		case BOOLEAN:
			v = new Value(Boolean.parseBoolean(stringRep), null);
			break;
		case DOUBLE:
			v = new Value(Double.parseDouble(stringRep), null);
			break;
		case INSTANT:

			try {
				v = new Value(Timestamp.valueOf(stringRep).toInstant(), null);
			} catch (IllegalArgumentException e) {

				Temporal deserialize = ds.deserialize(new JsonPrimitive(stringRep), LocalDateTime.class, null);

				LocalDateTime from;
				if (deserialize instanceof LocalDate ld) {
					from = ld.atStartOfDay();
				} else if (deserialize instanceof LocalTime lt) {
					from = lt.atDate(LocalDate.parse("1900-01-01"));
				} else {
					from = LocalDateTime.from(deserialize);
				}

				ZonedDateTime zonedDateTime = ZonedDateTime.of(from, ZoneId.of("UTC"));

				v = new Value(Instant.from(zonedDateTime), null);
			}

			break;
		case INTEGER:
			if (stringRep.contains(".")) {
				v = new Value(((int) Double.parseDouble(stringRep)), null);
			} else {
				v = new Value(Integer.parseInt(stringRep), null);
			}
			break;
		case LONG:
			v = new Value(Long.parseLong(stringRep), null);
			break;
		case STRING:
			v = stringValue;
			break;
		case ZONED:
			v = new Value(ZonedDateTime.parse(stringRep), null);
			break;
		default:
			v = stringValue;
			break;
		}

		return v;
	}

	public static Table createTableWithLastActionFilter(Table inputTable) {
		Table tableWithLastActionFilter = new Table();
		tableWithLastActionFilter.setMetaData(inputTable.getMetaData());
		tableWithLastActionFilter.setName(inputTable.getName());
		for (Column c : inputTable.getColumns()) {
			tableWithLastActionFilter.addColumn(c);
		}
		tableWithLastActionFilter.addColumn(new Column("LastAction", DataType.INTEGER));

		int rowCounter = 0;
		for (Row r : inputTable.getRows()) {

			boolean allEmpty = true;
			for (int i = 1; i < r.getValues().size(); i++) { // Ersten Wert (&-Spalte) überspringen
				if (r.getValues().get(i) != null) {
					allEmpty = false;
					break;
				}
			}

			if (rowCounter == 0 || !allEmpty) {
				r.getValues().add(new Value(1, ">="));
			} else {
				r.getValues().add(null);
			}

			tableWithLastActionFilter.addRow(r);
			rowCounter++;
		}
		return tableWithLastActionFilter;
	}
}
