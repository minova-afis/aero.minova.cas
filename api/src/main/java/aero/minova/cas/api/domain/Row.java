package aero.minova.cas.api.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Row implements Serializable {
	private static final long serialVersionUID = 202603161620L;
	private List<Value> values;

	// how: default constructor
	public Row() {
		this.values = new ArrayList<>();
	}
	
	// how: constructor with the ability to set arrayList capacity
	public Row(int initialValuesCapacity) {
		this.values = new ArrayList<>(initialValuesCapacity);
	}

	public void addValue(Value v) {
		getValues().add(v);
	}

	public List<Value> getValues() {
		return values;
	}

	public void setValues(List<Value> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "Row [values=" + values + "]";
	}

}