package ch.minova.install.setup.mdi;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import ch.minova.install.setup.BaseSetup;
import ch.minova.install.setup.BaseSetupException;
import ch.minova.install.setup.EntryCompareMDIException;
import ch.minova.install.setup.MenuCompareMDIException;

public class Menu {
	private final HashMap<String, Entry> entryMap = new HashMap<String, Entry>();
	private final Vector<Entry> entries = new Vector<Entry>();
	HashMap<String, Menu> menuMap = new HashMap<String, Menu>();
	Vector<Menu> menues = new Vector<Menu>();

	public void setMenues(final Vector<Menu> menues) {
		this.menues = menues;
	}

	private String id;
	private String text;
	private double position;

	public double getPosition() {
		return this.position;
	}

	public String getOverride() {
		return this.override;
	}

	private String override;

	public Menu(final String id) {
		this.id = id;
	}

	public Menu(final String id, final String text) {
		this.id = id;
		this.text = text;
	}

	public String getId() {
		return this.id;
	}

	public String getText() {
		return this.text;
	}

	/**
	 * Positionen um das Menue zu sortieren.
	 */
	public void setText(final String text) {
		this.text = text;
	}

	public void setPosition(final double position) {
		this.position = position;
	}

	public void setOverride(final String override) {
		this.override = override;

	}

	public void addEntry(final Entry entry) throws EntryCompareMDIException {
		if (!entry.getType().endsWith("-")) {
			if (this.entryMap.containsKey(entry.getId().toLowerCase())) {
				final Entry e = this.entryMap.get(entry.getId().toLowerCase());
				e.setType(entry.getType());
				if (e.getPosition() == 0 || entry.getPosition() == 0.0) {
					e.setPosition(entry.getPosition());
				} else if (e.getPosition() != entry.getPosition() && e.getPosition() != 0 && entry.getPosition() != 0.0) {
					throw new EntryCompareMDIException(
							MessageFormat.format("Entry: {0}, Position:{1} != {2}", e.getId(), e.getPosition(), entry.getPosition()));
				}
			} else {
				for (int i = 0; i <= this.entries.size(); i++) {
					if (i == this.entries.size()) {
						// am ende einfügen
						this.entries.add(entry);
						this.entryMap.put(entry.getId().toLowerCase(), entry);
						break;
					}

					if (entry.getPosition() < this.entries.get(i).getPosition()) {
						// an einer speziellen Position einfügen
						this.entries.add(i, entry);
						this.entryMap.put(entry.getId().toLowerCase(), entry);
						break;
					}
				}
			}
			// delete entry with!
		} else {
			if (this.entryMap.containsKey(entry.getId().toLowerCase())) {
				deleteEntry(entry);
			}
		}
	}

	/**
	 * fügt in den Einträgen einen Separator hinzu! Ein Seperator wird nur dann hinzugefügt, wenn unmittelbar vor diesem keiner besteht. Es darf ein seperator
	 * vor oder hinter einem Eintrag gesetzt werden.
	 */
	public void addSeperator() {
		final String separator = "separator";
		// erster Eintrag in der Liste der Einträge
		for (int i = 0; i < this.entries.size(); i++) {
			if (i == 0) {
				if (this.entries.get(i).getSeperatorAfter()) {
					this.entries.add(i + 1, new Entry(separator));
					i++;
				}
			}
			// zwischen 1-size
			else if (i < this.entries.size() - 1) {
				if (this.entries.get(i).getSeperatorAfter() && !this.entries.get(i).getSeperatorBefore()) {
					this.entries.add(i + 1, new Entry(separator));
					i++;
				} else if (this.entries.get(i).getSeperatorBefore() && !this.entries.get(i).getSeperatorAfter()) {
					if (this.entries.get(i - 1).getType().equals(separator)) {
						// Ist bereits ein Seperator unmittelbar vor dem Eintrg
						continue;
					}
					this.entries.add(i++, new Entry(separator));
				} else if (this.entries.get(i).getSeperatorBefore() && this.entries.get(i).getSeperatorAfter()) {
					// seperator After
					this.entries.add(i + 1, new Entry(separator));
					if (this.entries.get(i - 1).getType().equals(separator)) {
						// Ist bereits ein Seperator unmittelbar vor dem Eintrg
						i++;
						continue;
					}
					this.entries.add(i++, new Entry(separator));
				}
			} else {
				// letzter Eintrag in der Liste aus Einträgen
				if (this.entries.get(i).getSeperatorBefore()) {
					if (!this.entries.get(i - 1).getType().equals(separator)) {
						this.entries.add(i++, new Entry(separator));
					}
				}
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

	public void deleteEntry(final Entry entry) {
		if (this.entryMap.containsKey(entry.getId().toLowerCase())) {
			final Entry e = this.entryMap.remove(entry.getId().toLowerCase());
			this.entries.remove(e);
		}
	}

	/*
	 * public void deleteEntry(String entrykey) { if (entryMap.containsKey(entrykey.toLowerCase())) { Entry e = entryMap.remove(entrykey.toLowerCase());
	 * entries.remove(e); } }
	 */

	public int getEntryCount() {
		return this.entries.size();
	}

	public boolean existsEntry(final String id) throws EntryCompareMDIException {
		if (this.entryMap.containsKey(id.toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<Entry> getEntries() {
		final ArrayList<Entry> entryarraylist = new ArrayList<Entry>();
		for (final Entry n : this.entries) {
			entryarraylist.add(n);
		}
		return entryarraylist;
	}

	public void addMenu(final Menu menu) throws MenuCompareMDIException {
		// ----------------------------------------CHANGE------------------------------------
		if (!menu.getId().endsWith("-")) {
			if (this.menuMap.containsKey(menu.getId().toLowerCase())) {
				final Menu m = this.menuMap.get(menu.getId().toLowerCase());
				m.setText(menu.getText());

				if (m.getPosition() != menu.getPosition()) {
					throw new MenuCompareMDIException(MessageFormat.format("Menu: {0}, Position:{1} != {2}", m.getId(), m.getPosition(), menu.getPosition()));
				}
			} else {
				this.menues.add(menu);
				this.menuMap.put(menu.getId().toLowerCase(), menu);
			}
		} else {
			// delete menu if - is the ending of id-string
			if (this.menuMap.containsKey(menu.getId().toLowerCase().replace("-", ""))) {
				final Menu m = this.menuMap.get(menu.getId().toLowerCase().replace("-", ""));
				deleteMenu(m);
			}
		}
	}

	public Menu getMenu(final String id) {
		if (this.menuMap.containsKey(id.toLowerCase())) {
			return this.menuMap.get(id.toLowerCase());
		} else {
			return null;
		}
	}

	public String getMenuText(final String id) {
		if (this.menuMap.containsKey(id.toLowerCase())) {
			final Menu e = this.menuMap.get(id.toLowerCase());
			return e.getText();
		} else {
			return null;
		}
	}

	public void deleteMenu(final Menu menu) {
		if (this.menuMap.containsKey(menu.getId().toLowerCase())) {
			final Menu m = this.menuMap.remove(menu.getId().toLowerCase());
			this.menues.remove(m);
		}
	}

	public int getMenuCount() {
		return this.menues.size();
	}

	public boolean existsMenu(final String id) throws MenuCompareMDIException {
		if (this.menuMap.containsKey(id.toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}

	public void merge(final ch.minova.core.install.MenuDocument.Menu menu, final boolean override)
			throws BaseSetupException, MenuCompareMDIException, EntryCompareMDIException {
		// wenn der Befehl zum überschreiben gegeben wurde und der Eintrag auch überschreiben werden darf (Override = true)
		if (override && menu.getOverride().equalsIgnoreCase("true")) {
			this.id = menu.getId();
			this.text = menu.getText();
			this.position = menu.getPosition();
			this.override = menu.getOverride();
		}
		try {
			final ch.minova.core.install.MenuDocument.Menu.Entry[] entry = menu.getEntryArray();
			log(MessageFormat.format(" <menu: id= {0} text= {1} position= {2} override= {3}>", menu.getId(), menu.getText(), menu.getPosition(),
					menu.getOverride()));
			for (int i = 0; i < entry.length; i++) {
				final ch.minova.core.install.MenuDocument.Menu.Entry e = menu.getEntryArray(i);
				// wenn der Befehl zum überschreiben gegeben wurde und der Eintrag auch überschreiben werden darf (Override = true)
				if (existsEntry(menu.getEntryArray(i).getId()) && override && e.getOverride().equalsIgnoreCase("true")) {
					this.deleteEntry(menu.getEntryArray(i).getId());
				} else {
					final Entry newEntry = new Entry(e.getId(), e.getType());
					newEntry.setPosition(e.getPosition());
					newEntry.setOverride(e.getOverride());
					newEntry.setSeperatorAfter(e.getSeparatorAfter());
					newEntry.setSeperatorBefore(e.getSeparatorBefore());
					newEntry.setType(e.getType());
					addEntry(newEntry);
					log(MessageFormat.format(" <entry: id= {0} type= {1} position= {2} override= {3}>", newEntry.getId(), newEntry.getType(),
							newEntry.getPosition(), newEntry.getOverride()));
				}
			}
			// }
			for (int i = 0; i < menu.getMenuArray().length; i++) {
				log(MessageFormat.format("merge noch ein menu <menu: id= {0} text= {1}>", menu.getMenuArray(i).getId(), menu.getMenuArray(i).getText()));
				readMenu(menu.getMenuArray(i), override);
			}
		} catch (final EntryCompareMDIException en) {
			throw en;
		} catch (final Exception e) {
			throw new BaseSetupException(MessageFormat.format(BaseSetup.FAILTOWRITENODETODOCUMENT, menu.toString()));
		}
	}

	private void log(final String message) {
		System.out.println(message);
	}

	public void readMenu(final ch.minova.core.install.MenuDocument.Menu menu, final boolean override)
			throws BaseSetupException, MenuCompareMDIException, EntryCompareMDIException {
		final Menu m = getMenu(menu, true);
		if (m != null) {
			m.merge(menu, override);
		}
	}

	public Menu getMenu(final ch.minova.core.install.MenuDocument.Menu menu, final boolean create) {
		// ----------------------------------------CHANGE------------------------------------
		if (!menu.getId().endsWith("-")) {
			if (this.menuMap.containsKey(menu.getId().toLowerCase())) {
				return this.menuMap.get(menu.getId().toLowerCase());
			} else {
				if (create) {
					final Menu n = new Menu(menu.getId());
					n.setOverride(menu.getOverride());
					n.setPosition(menu.getPosition());
					n.setText(menu.getText());
					this.menuMap.put(menu.getId().toLowerCase(), n);
					this.menues.add(n);
					return n;
				}
				return null;
			}
		} else {
			// delete menu if - is the ending of id-string
			if (this.menuMap.containsKey(menu.getId().toLowerCase().replace("-", ""))) {
				final Menu m = this.menuMap.get(menu.getId().toLowerCase().replace("-", ""));
				deleteMenu(m);
				return null;
			}
			return null;
		}
	}

	public Menu getMenu(final String id2, final boolean create) {
		// ----------------------------------------CHANGE------------------------------------
		if (!id2.endsWith("-")) {
			if (this.menuMap.containsKey(id2.toLowerCase())) {
				return this.menuMap.get(id2.toLowerCase());
			} else {
				if (create) {
					final Menu n = new Menu(id2);
					this.menuMap.put(id2.toLowerCase(), n);
					this.menues.add(n);
					return n;
				}
				return null;
			}
		} else {
			// delete menu if - is the ending of id-string
			if (this.menuMap.containsKey(id2.toLowerCase().replace("-", ""))) {
				final Menu m = this.menuMap.get(id2.toLowerCase().replace("-", ""));
				deleteMenu(m);
				return null;
			}
			return null;
		}
	}

	public void deleteEntry(final String id) {
		if (this.entryMap.containsKey(id.toLowerCase())) {
			final Entry n = this.entryMap.remove(id.toLowerCase());
			for (int i = 0; i < this.entries.size(); i++) {
				if (n.getId().equals(this.entries.get(i).getId())) {
					this.entries.remove(i);
				}
			}
		}
	}

	public ArrayList<Menu> getMenues() {
		final ArrayList<Menu> menuArraylist = new ArrayList<Menu>();
		for (final Menu m : this.menues) {
			menuArraylist.add(m);
		}
		return menuArraylist;
	}
}