package ch.minova.install.setup.mdi;

public class Entry {
	private String id;

	private String type;
	private boolean seperatorafter;
	private boolean seperatorbefore;
	private String key;
	private double position;
	private String value;

	private String override;

	public Entry(final String id, final String type) {
		this.id = id;
		setType(type);
	}

	public Entry(final String type) {
		this.type = type;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getKey() {
		return this.key;
	}

	public double getPosition() {
		return this.position;
	}

	public String getOverride() {
		return this.override;
	}

	public String getValue() {
		return this.value;
	}

	public String getId() {
		return this.id;
	}

	public boolean getSeperatorBefore() {
		return this.seperatorbefore;
	}

	public boolean getSeperatorAfter() {
		return this.seperatorafter;
	}

	public String getType() {
		return this.type;
	}

	public void setPosition(final double position) {
		this.position = position;
	}

	public void setOverride(final String override) {
		this.override = override;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	public void setSeperatorBefore(final boolean seperatorbefore) {
		this.seperatorbefore = seperatorbefore;
	}

	public void setSeperatorAfter(final boolean seperatorafter) {
		this.seperatorafter = seperatorafter;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public void setType(final String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "(<" + this.id + ">,<" + this.type + ">)";
	}

	public void merge(final ch.minova.core.install.MenuDocument.Menu.Entry entry) {
		this.id = entry.getId();
		this.position = entry.getPosition();
		if (entry.getSeparatorAfter()) {
			this.seperatorafter = entry.getSeparatorAfter();
		}
		if (entry.getSeparatorBefore()) {
			this.seperatorbefore = entry.getSeparatorBefore();
		}
		this.type = entry.getType();
	}
}