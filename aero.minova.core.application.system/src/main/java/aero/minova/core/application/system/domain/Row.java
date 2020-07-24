package aero.minova.core.application.system.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Row {
	private List<Value> values = new ArrayList<>();

	public void addValue(Value v) {
		getValues().add(v);
	}

	public List<Value> getValues() {
		return values;
	}

	public void setValues(List<Value> values) {
		this.values = values;
	}
}