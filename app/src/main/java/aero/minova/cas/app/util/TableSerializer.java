package aero.minova.cas.app.util;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;

public class TableSerializer implements JsonSerializer<Table> {

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneId.of("UTC"));

	public static final Pattern PATTERN_FOR_LIST = Pattern.compile(".*\\[(-?\\d*)]");

	@Override
	public JsonElement serialize(Table table, Type type, JsonSerializationContext context) {

		JsonArray resultList = new JsonArray();

		for (int i = 0; i < table.getRows().size(); i++) {

			JsonObject jsonObject = new JsonObject();

			for (int j = 0; j < table.getColumns().size(); j++) {

				String columnName = table.getColumns().get(j).getName();

				Value v = table.getRows().get(i).getValues().get(j);

				if (v == null || v.getValue() == null) {
					continue;
				}

				addProperty(jsonObject, table.getColumns().get(j).getType(), columnName, v);
			}

			resultList.add(jsonObject);
		}

		if (resultList.size() == 1) {
			return resultList.get(0);
		}
		return resultList;
	}

	private void addProperty(JsonElement jsonElement, DataType type, String columnName, Value v) {

		// Ersten Buchstaben in lower case
		columnName = columnName.substring(0, 1).toLowerCase() + columnName.substring(1);

		String[] split = columnName.split("\\.");

		if (split.length == 1) {
			addSimpleProperty(jsonElement, columnName, v);
		} else if (jsonElement instanceof JsonObject jsonObject) {
			Matcher m = PATTERN_FOR_LIST.matcher(split[0]);
			if (m.find()) {
				String nameWithout = split[0].substring(0, split[0].indexOf("["));
				int index = Integer.parseInt(m.group(1));

				JsonArray array = jsonObject.getAsJsonArray(nameWithout);
				if (array == null) {
					array = new JsonArray();
					jsonObject.add(nameWithout, array);
				}

				JsonElement childElement = new JsonObject();
				if (index < 0 && array.size() > array.size() + index && array.size() + index >= 0) {
					childElement = array.get(array.size() + index);
				} else if (index >= 0 && array.size() > index) {
					childElement = array.get(index);
				} else { // TODO: Was wenn Nicht in richtiger reihenfolge? Besonderes bei negativ?
					array.add(childElement);
				}

				addProperty(childElement, type, columnName.substring(columnName.indexOf(".") + 1, columnName.length()),
						v);

			} else {
				JsonObject childElement = jsonObject.getAsJsonObject(split[0]);
				if (childElement == null) {
					childElement = new JsonObject();
					jsonObject.add(split[0], childElement);
				}

				addProperty(childElement, type, columnName.substring(columnName.indexOf(".") + 1, columnName.length()),
						v);
			}
		}

	}

	private void addSimpleProperty(JsonElement jsonElement, String columnName, Value v) {
		if (jsonElement instanceof JsonObject jsonObject) {

			if (v.getValue() instanceof Instant instant) {
				jsonObject.addProperty(columnName, formatter.format(instant));
			} else {
				jsonObject.addProperty(columnName, v.getValue().toString());
			}

		} else if (jsonElement instanceof JsonArray jsonArray) {
			jsonArray.add(v.getValue().toString());
		}
	}

}
