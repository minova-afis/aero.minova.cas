package ch.minova.install.setup.mdi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class Toolbar {
	private final HashMap<String, Entry> entryMap = new HashMap<String, Entry>();
	private Vector<Entry> entries = new Vector<Entry>();
	private boolean flat;

	public Toolbar() {}

	public Toolbar(final boolean flat) {
		this.flat = flat;
	}

	public boolean getFlat() {
		return this.flat;
	}

	public void setFlat(final boolean flat) {
		this.flat = flat;
	}

	public ArrayList<Entry> getEntries() {
		final ArrayList<Entry> entryArraylist = new ArrayList<Entry>();
		for (final Entry m : this.entries) {
			entryArraylist.add(m);
		}
		return entryArraylist;
	}

	public void setEntries(final Vector<Entry> entries) {
		this.entries = entries;
	}

	public void addEntry(final Entry entry) {
		// ----------------------------------------CHANGE------------------------------------
		if (!entry.getType().endsWith("-")) {
			if (this.entryMap.containsKey(entry.getId().toLowerCase())) {
				final Entry e = this.entryMap.get(entry.getId().toLowerCase());
				e.setType(entry.getType());
			} else {
				for (int i = 0; i <= this.entries.size(); i++) {
					if (i == this.entries.size()) {
						// am ende einfügen
						entry.setId(entry.getId().toLowerCase());
						this.entries.add(entry);
						this.entryMap.put(entry.getId().toLowerCase(), entry);
						break;
					}

					if (entry.getPosition() < this.entries.get(i).getPosition()) {
						// an einer speziellen Position einfügen
						entry.setId(entry.getId().toLowerCase());
						this.entries.add(i, entry);
						this.entryMap.put(entry.getId().toLowerCase(), entry);
						break;
					}
				}
			}
		} else {
			if (this.entryMap.containsKey(entry.getId().toLowerCase())) {
				System.out.println("löschen eines Eintrages! Toolbarentry: " + entry.getId().toString());
				final Entry e = this.entryMap.get(entry.getId().toLowerCase().replace("-", ""));
				deleteEntry(e);
			}
		}
	}

	public Entry getEntry(final String id) {
		if (this.entryMap.containsKey(id.toLowerCase())) {
			return this.entryMap.get(id.toLowerCase());
		} else {
			return null;
		}
	}

	public String getEntryValue(final String id) {
		if (this.entryMap.containsKey(id.toLowerCase())) {
			final Entry e = this.entryMap.get(id.toLowerCase());
			return e.getType();
		} else {
			return null;
		}
	}

	public void deleteEntry(final String entrykey) {
		if (this.entryMap.containsKey(entrykey.toLowerCase())) {
			final Entry e = this.entryMap.remove(entrykey.toLowerCase());
			this.entries.remove(e);
		}
	}

	public void deleteEntry(final Entry entry) {
		if (this.entryMap.containsKey(entry.getId().toLowerCase())) {
			final Entry e = this.entryMap.remove(entry.getId().toLowerCase());
			this.entries.remove(e);
		}
	}

	public int getEntryCount() {
		return this.entries.size();
	}

	public boolean existsEntry(final String id) {
		if (this.entryMap.containsKey(id.toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}
}