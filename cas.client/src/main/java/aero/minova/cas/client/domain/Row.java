package aero.minova.cas.client.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Row implements Serializable {
	private static final long serialVersionUID = 202106161638L;
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