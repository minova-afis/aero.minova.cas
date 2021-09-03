package ch.minova.install.setup.xbs;

public class Entry {
	private final String key;
	private String value;

	public Entry(final String key, final String value) {
		this.key = key;
		setValue(value);
	}

	public String getKey() {
		return this.key;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "(<" + this.key + ">,<" + this.value + ">)";
	}
}