package aero.minova.cas.app.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;

public class TableDeserializer implements JsonDeserializer<Table> {

	@Override
	public Table deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
			throws JsonParseException {

		if (jsonElement == null || type == null) {
			return null;
		}

		Table table = new Table();

		JsonArray list;
		if (jsonElement.isJsonArray()) {
			list = jsonElement.getAsJsonArray();
		} else {
			list = new JsonArray();
			list.add(jsonElement.getAsJsonObject());
		}

		List<String> columnNames = createColumns(table, list);

		// Zeilen hinzufügen
		addRows(table, list, columnNames);

		return table;
	}

	private List<String> createColumns(Table table, JsonArray list) {
		List<String> columnNames = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			for (Entry<String, JsonElement> e : list.get(i).getAsJsonObject().entrySet()) {
				Set<String> names = getNameAndValueForJsonElement(e.getKey(), e.getValue()).keySet();
				for (String s : names) {
					if (!columnNames.contains(s)) {
						columnNames.add(s);
						table.addColumn(new Column(s, DataType.STRING));
					}
				}
			}
		}
		return columnNames;
	}

	private void addRows(Table table, JsonArray list, List<String> columnNames) {
		for (int i = 0; i < list.size(); i++) {
			JsonObject element = list.get(i).getAsJsonObject();

			Row r = new Row();
			for (int j = 0; j < columnNames.size(); j++) {
				r.addValue(null);
			}

			for (Entry<String, JsonElement> e : element.entrySet()) {
				Map<String, String> values = getNameAndValueForJsonElement(e.getKey(), e.getValue());
				for (Entry<String, String> entry : values.entrySet()) {
					r.getValues().set(columnNames.indexOf(entry.getKey()), new Value(entry.getValue(), null));
				}
			}
			table.addRow(r);
		}
	}

	private Map<String, String> getNameAndValueForJsonElement(String prevName, JsonElement e) {
		Map<String, String> names = new HashMap<>();
		if (e instanceof JsonObject obj) {
			for (Entry<String, JsonElement> entry : obj.entrySet()) {
				names.putAll(getNameAndValueForJsonElement(prevName + "." + entry.getKey(), entry.getValue()));
			}
		} else if (e instanceof JsonArray jsonArray) {
			for (int i = 0; i < jsonArray.size(); i++) {
				names.putAll(getNameAndValueForJsonElement(prevName + "[" + i + "]", jsonArray.get(i)));
				// Auch jeweils negativen Index hinzufügen
				names.putAll(
						getNameAndValueForJsonElement(prevName + "[" + (i - jsonArray.size()) + "]", jsonArray.get(i)));
			}
		} else {
			names.put(prevName, e.getAsString());
		}

		return names;
	}

}
