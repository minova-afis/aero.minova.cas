package ch.minova.install.setup.mdi;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

// import ch.minova.core.install.ToolbarDocument.Toolbar;
import ch.minova.install.setup.BaseSetup;
import ch.minova.install.setup.BaseSetupException;
import ch.minova.install.setup.EntryCompareMDIException;
import ch.minova.install.setup.MenuCompareMDIException;
import ch.minova.install.setup.xbs.NodeException;

public class Main {
	private final HashMap<String, Action> actionMap = new HashMap<String, Action>();
	private final Vector<Action> actions = new Vector<Action>();
	private Menu menu = new Menu("main");
	private Toolbar toolbar = new Toolbar();
	private String lcid;
	private String country;
	private String icon;
	private String language;
	private String title;
	private String variant;

	public String getLcid() {
		return this.lcid;
	}

	public String getCountry() {
		return this.country;
	}

	public String getIcon() {
		return this.icon;
	}

	public String getLanguage() {
		return this.language;
	}

	public String getTitle() {
		return this.title;
	}

	public String getVariant() {
		return this.variant;
	}

	public void setLcid(final String lcid) {
		this.lcid = lcid;
	}

	public void setCountry(final String country) {
		this.country = country;
	}

	public void setIcon(final String icon) {
		this.icon = icon;
	}

	public void setLanguage(final String language) {
		this.language = language;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setVariant(final String variant) {
		this.variant = variant;
	}

	public void addAction(final Action action) {
		// ----------------------------------------CHANGE------------------------------------
		if (!action.getAction().endsWith("-")) {
			if (this.actionMap.containsKey(action.getAction().toLowerCase())) {
				final Action a = this.actionMap.get(action.getAction().toLowerCase());
				a.setAction(action.getAction());
			} else {
				this.actions.add(action);
				this.actionMap.put(action.getAction().toLowerCase(), action);
			}
		} else {
			if (this.actionMap.containsKey(action.getAction().toLowerCase())) {
				final Action a = this.actionMap.get(action.getAction().toLowerCase().replace("-", ""));
				deleteAction(a);
			}
		}
	}

	public Action getAction(final String action) {
		if (this.actionMap.containsKey(action.toLowerCase())) {
			return this.actionMap.get(action.toLowerCase());
		} else {
			return null;
		}
	}

	public String Action(final String action) {
		if (this.actionMap.containsKey(action.toLowerCase())) {
			final Action a = this.actionMap.get(action.toLowerCase());
			return a.getAction();
		} else {
			return null;
		}
	}

	public void deleteAction(final Action action) {
		if (this.actionMap.containsKey(action.getAction().toLowerCase())) {
			final Action a = this.actionMap.remove(action.getAction().toLowerCase());
			this.actions.remove(a);
		}
	}

	public void deleteAction(final String action) {
		if (this.actionMap.containsKey(action.toLowerCase())) {
			final Action a = this.actionMap.remove(action.toLowerCase());
			this.actions.remove(a);
		}
	}

	public int getActionCount() {
		return this.actions.size();
	}

	public boolean existsAction(final String id) {
		if (this.actionMap.containsKey(id.toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}

	// An dieser Stelle muss sorttiert werden.
	public void addMenu(final Menu menu) throws MenuCompareMDIException {
		if (!menu.getId().endsWith("-")) {
			// gibt es ein menu mit der gleichen id in der HashMap?
			if (menu.menuMap.containsKey(menu.getId().toLowerCase())) {
				final Menu m = menu.menuMap.get(menu.getId().toLowerCase());
				m.setText(menu.getText());
				// Wenn es eine position gibt, wird diese
				if (m.getPosition() != menu.getPosition()) {
					throw new MenuCompareMDIException(MessageFormat.format("Menu: {0}, Position:{1} != {2}", m.getId(), m.getPosition(), menu.getPosition()));
				}
			} else {
				menu.menues.add(menu);
				menu.menuMap.put(menu.getId().toLowerCase(), menu);
			}
		} else {
			if (menu.menuMap.containsKey(menu.getId().toLowerCase().replace("-", ""))) {
				final Menu m = menu.menuMap.get(menu.getId().toLowerCase().replace("-", ""));
				deleteMenu(m);
			}
		}
	}

	/**
	 * @param id
	 *            (not used)
	 * @return
	 */
	public Menu getMenu(final String id) {
		return this.menu;
	}

	public String getMenuText(final String id) {
		if (this.menu.menuMap.containsKey(id.toLowerCase())) {
			final Menu e = this.menu.menuMap.get(id.toLowerCase());
			return e.getText();
		} else {
			return null;
		}
	}

	public void deleteMenu(final Menu menu) {
		if (menu.menuMap.containsKey(menu.getId().toLowerCase())) {
			final Menu m = menu.menuMap.remove(menu.getId().toLowerCase());
			menu.menues.remove(m);
		}
	}

	public int getMenuCount() {
		return this.menu.menues.size();

	}

	public boolean existsMenu(final String id) {
		if (this.menu.menuMap.containsKey(id.toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}

	public Toolbar getToolbar() {
		return this.toolbar;
	}

	public void setToolbar(final Toolbar toolbar) {
		this.toolbar = toolbar;
	}

	public void readAction(final ch.minova.core.install.ActionDocument.Action action) {
		final Action a = getAction(action.getAction(), true);
		if (a != null) {
			a.merge(action);
		}
	}

	public Action getAction(final String action, final boolean create) {
		if (!action.endsWith("-")) {
			if (this.actionMap.containsKey(action.toLowerCase())) {
				return this.actionMap.get(action.toLowerCase());
			} else {
				if (create) {
					final Action n = new Action(action);
					this.actionMap.put(action.toLowerCase(), n);
					this.actions.add(n);
					return n;
				}
				return null;
			}
		} else {
			if (this.actionMap.containsKey(action.toLowerCase().replace("-", ""))) {
				deleteAction(action.toLowerCase().replace("-", ""));
				return null;
			}
			return null;
		}
	}

	public Menu getMenu(final String name, final boolean create) {
		if (this.menu.getId().equals(name)) {
			return this.menu;
		} else {
			if (create) {
				this.menu = new Menu(name);
				return this.menu;
			}
			return null;
		}
	}

	public Toolbar getToolbar(final boolean flat, final boolean b) {
		if (getToolbar().getFlat()) {
			return getToolbar();
		} else {
			if (b) {
				this.toolbar = new Toolbar(flat);
				return this.toolbar;
			}
			return null;
		}
	}

	public void readMenu(final ch.minova.core.install.MenuDocument.Menu menu, final boolean override)
			throws BaseSetupException, MenuCompareMDIException, EntryCompareMDIException {
		final Menu m = getMenu(menu.getId(), true);
		if (m != null) {
			m.merge(menu, override);
		}
	}

	/**
	 * @param entryDoc
	 * @param entry
	 */
	public void setEntry(final ch.minova.core.install.MenuDocument.Menu.Entry entryDoc, final Entry entry) {
		if (entry.getType().equals("seperator")) {
			entryDoc.setType(entry.getType());
		} else {
			entryDoc.setId(entry.getId());
			entryDoc.setPosition((float) entry.getPosition());
			entryDoc.setOverride(entry.getOverride());
			entryDoc.setType(entry.getType());
		}
	}

	/**
	 * Setzt die Felder der Action auf den Wert den die eingelesene action hat
	 * 
	 * @param actiondoc
	 * @param action
	 */
	public void setAction(final ch.minova.core.install.ActionDocument.Action actiondoc, final Action action) {
		// geordnet nach dem Alphabet
		actiondoc.setAction(action.getAction());
		if (action.getDetailVisible() != null) {
			actiondoc.setDetailVisible(action.getDetailVisible());
		}
		if (action.getDialog() != null) {
			actiondoc.setDialog(action.getDialog());
		}
		if (action.getDocumentation() != null) {
			actiondoc.setDocumentation(action.getDocumentation());
		}
		if (action.getGeneric()) {
			actiondoc.setGeneric(action.getGeneric());
		}
		actiondoc.setIcon(action.getIcon());
		actiondoc.setId(action.getId());

		if (action.getParam() != null) {
			actiondoc.setParam(action.getParam());
		}
		if (action.getShortcut() != null) {
			actiondoc.setShortcut(action.getShortcut());
		}
		if (action.getSupressPrint() != null) {
			actiondoc.setSupressPrint(action.getSupressPrint());
		}
		actiondoc.setText(action.getText());

		if (action.getVisible() != null) {
			actiondoc.setVisible(action.getVisible());
		}
		log(MessageFormat.format(" <action: icon= {0} ID= {1} text={2}>", action.getIcon(), action.getId(), action.getText()));
	}

	/**
	 * schreibt die Actions aus dem Objekt: BaseSetup.mainmdi in die mdidoc ! Nach dieser Methode muss mainmdi gespiechert werden unter den Prefenrences...
	 * 
	 * @param mdidoc
	 * @throws NodeException
	 */
	public void writeAction(final ch.minova.core.install.MainDocument mainDoc) throws NodeException {
		boolean existAction = false;
		for (final Action action : this.actions) {
			existAction = false;
			for (int i = 0; i < mainDoc.getMain().getActionArray().length; i++) {
				// Wenn Action vorhanden, dann wird sie �berschrieben
				if (action.getAction().equals(mainDoc.getMain().getActionArray(i).getAction())) {
					mainDoc.getMain().removeAction(i);
					mainDoc.getMain().insertNewAction(i);
					setAction(mainDoc.getMain().getActionArray(i), action);
					existAction = true;
				}
			}
			// Wenn Action nicht vorhanden, dann wird sie neu geschrieben
			if (!existAction) {
				mainDoc.getMain().addNewAction();
				setAction(mainDoc.getMain().getActionArray(mainDoc.getMain().getActionArray().length - 1), action);
			}
		}
	}

	/**
	 * @return Array mit allen Einträgen des Menus
	 */
	public ArrayList<Menu> getMenues() {
		final ArrayList<Menu> menuArraylist = new ArrayList<Menu>();
		for (final Menu m : this.menu.menues) {
			menuArraylist.add(m);
		}
		return menuArraylist;
	}

	/**
	 * schreibt Menue Eintraege und das Menu in die Doc, nach diesem Aufruf muss das Objekt Document mdidoc gespeichert werden.
	 * 
	 * @param oldmenu
	 *            - Menu in das geschreiben werden muss
	 * @param newmenu
	 *            - Eintragen in das Menu
	 * @return
	 * @throws NodeException
	 * @throws BaseSetupException
	 */
	public boolean writeMenu(final ch.minova.core.install.MenuDocument.Menu oldmenu, final Menu newmenu) throws NodeException, BaseSetupException {
		boolean getanswer = false;
		final ch.minova.core.install.MenuDocument.Menu[] menus = oldmenu.getMenuArray();
		// boolean deleteNode = false;
		// for (int k = 0; k < menu.getMenuArray().length; k++) {
		// for (int i = 0; i < menus.length; i++) {
		// // Position anders als sortiert!
		// // Behandlung eines Menus wenn bereits vorhanden.
		// if (menus[i].getId().equals(newmenu.getId())) {
		// deleteNode = true;
		// oldmenu.removeMenu(i);
		// oldmenu.insertNewMenu(i);
		// oldmenu.getMenuArray(i).setId(newmenu.getId());
		// oldmenu.getMenuArray(i).setText(newmenu.getText());
		// oldmenu.getMenuArray(i).setPosition(newmenu.getPosition());
		// oldmenu.getMenuArray(i).setOverride(newmenu.getOverride());
		//
		// char c = newmenu.getId().charAt(0);
		// c = Character.toUpperCase(c);
		// oldmenu.getMenuArray(oldmenu.getMenuArray().length - 1).setText(oldmenu.getMenuArray(i).getText());
		// log(MessageFormat.format(" <menu: id= {0} text= {1} position= {2} override ={3}>", oldmenu.getMenuArray(i).getId(), oldmenu.getMenuArray(i)
		// .getText(), oldmenu.getMenuArray(i).getPosition(), oldmenu.getMenuArray(i).getOverride()));
		// // alle Entries in den Knoten übertragen
		// // add Seperator
		// newmenu.addSeperator();
		// writeNewEntryToMenu(oldmenu.getMenuArray(i), newmenu);
		// // alle Nachfolgenden Knoten Schreiben
		// if (newmenu.getMenuCount() > 0) {
		// for (int j = 0; j < newmenu.getMenuCount(); j++) {
		// getanswer = writeMenu(oldmenu.getMenuArray(i), newmenu.getMenues().get(j));
		// }
		// } else {
		// getanswer = true;
		//
		// }
		// if (!getanswer) {
		// throw new NodeException(MessageFormat.format(BaseSetup.FAILTOWRITENODETODOCUMENT, oldmenu.getMenuArray(i).getId()));
		// }
		// }
		// }

		oldmenu.addNewMenu();
		oldmenu.getMenuArray(oldmenu.getMenuArray().length - 1).setId(newmenu.getId());
		oldmenu.getMenuArray(oldmenu.getMenuArray().length - 1).setText(newmenu.getText());
		oldmenu.getMenuArray(oldmenu.getMenuArray().length - 1).setPosition((float) newmenu.getPosition());
		oldmenu.getMenuArray(oldmenu.getMenuArray().length - 1).setOverride(newmenu.getOverride());
		char c = newmenu.getId().charAt(0);
		c = Character.toUpperCase(c);
		oldmenu.getMenuArray(oldmenu.getMenuArray().length - 1).setText(newmenu.getText());
		newmenu.addSeperator();
		writeNewEntryToMenu(oldmenu.getMenuArray(oldmenu.getMenuArray().length - 1), newmenu);
		// Hier wird überprüft ob es nch ein Untermenu gibt, dieses wird
		// dann eingetragen!
		if (newmenu.getMenuCount() > 0) {
			for (int j = 0; j < newmenu.getMenuCount(); j++) {
				log(MessageFormat.format(" writeMenu( {0} , {1})", oldmenu.getMenuArray(oldmenu.getMenuArray().length - 1).getId(),
						newmenu.getMenues().get(j).getId()));
				getanswer = writeMenu(oldmenu.getMenuArray(oldmenu.getMenuArray().length - 1), newmenu.getMenues().get(j));
			}
		} else {
			getanswer = true;
		}
		// letzter Knoten der Hinzugefügt worden ist
		if (menus.length > 0) {
			try {
				if (newmenu.getMenuCount() >= 0) {
					if (!newmenu.getMenues().isEmpty()) {
						final ch.minova.core.install.MenuDocument.Menu m = oldmenu.getMenuArray(oldmenu.getMenuArray().length - 1);
						final Menu m2 = newmenu.getMenues().get(0);
						log(MessageFormat.format(" writeMenu( {0} , {1})", m.getId(), m2.getId()));
						getanswer = writeMenu(m, m2);
					}
				} else {
					getanswer = true;
				}
			} catch (final Exception e) {
				e.printStackTrace();
				throw new BaseSetupException(MessageFormat.format("Fehler beim Schreiben der Mdi-Datei", ""));
			}
		} else {
			getanswer = true;
		}
		if (!getanswer) {
			throw new NodeException(MessageFormat.format(BaseSetup.FAILTOWRITENODETODOCUMENT, oldmenu.getMenuArray(oldmenu.getMenuArray().length - 1).getId()));
		}
		return true;
	}

	/**
	 * standard Logging, in diesem Fall werden die Nachrichten nur ausgegeben wenn der Parameter -verbose hinter die Nachricht geschreiben wird
	 * 
	 * @param message
	 */
	protected void log(final String message) {
		System.out.println(message);
	}

	/**
	 * Einträge werden in das Menu gesetzt wenn diese Berteits bestehen werden sie zusammengefügt und durch die neueen Einträge ersetzt
	 * 
	 * @param menuDoc
	 * @param menu
	 */
	private void writeNewEntryToMenu(final ch.minova.core.install.MenuDocument.Menu menuDoc, final Menu menu) {
		// Alle Einträge in dem Knoten des rootNodes Überprüfen
		for (int i = 0; i < menu.getEntryCount(); i++) {
			final ch.minova.core.install.MenuDocument.Menu.Entry[] entries = menuDoc.getEntryArray();
			boolean entryexists = false;
			// Überprüfung ob der Eintrag bereits vorhanden ist.
			for (int j = 0; j < entries.length; j++) {
				if (entries[j].getType().equals("separator")) {
					continue;
				}
				if (entries[j].getId().equals(menu.getEntries().get(i).getId())) {
					menu.getEntries().get(i).merge(entries[j]);
					entryexists = true;
				}
			}
			// Falls es den Eintrag noch nicht gibt, wird dieser angelegt
			if (!entryexists) {
				//
				if (menu.getEntries().get(i).getType().equals("separator")) {
					menuDoc.addNewEntry();
					log(MessageFormat.format(" <entry: id= {0} type= {1}>", menu.getEntries().get(i).getId(), menu.getEntries().get(i).getType()));
					setEntry(menuDoc.getEntryArray(menuDoc.getEntryArray().length - 1), menu.getEntries().get(i));
				} else {
					if (!menu.getEntries().get(i).getId().endsWith("-")) {
						menuDoc.addNewEntry();
						log(MessageFormat.format(" <entry: id= {0} type= {1}>", menu.getEntries().get(i).getId(), menu.getEntries().get(i).getType()));
						setEntry(menuDoc.getEntryArray(menuDoc.getEntryArray().length - 1), menu.getEntries().get(i));
					} else {
						log(MessageFormat.format("Entry-id ends with delete suffix \"-\" <entry: id= {0} type= {1}>", menu.getEntries().get(i).getId(),
								menu.getEntries().get(i).getType()));
					}
				}
			}
		}
	}

	public void writeNewEntryToToolbar(final ch.minova.core.install.ToolbarDocument.Toolbar toolbar2, final Entry entry) {
		toolbar2.addNewEntry();
		toolbar2.getEntryArray(toolbar2.getEntryArray().length - 1).setId(entry.getId());
		toolbar2.getEntryArray(toolbar2.getEntryArray().length - 1).setType(entry.getType());
		toolbar2.getEntryArray(toolbar2.getEntryArray().length - 1).setPosition((float) entry.getPosition());
		toolbar2.getEntryArray(toolbar2.getEntryArray().length - 1).setOverride(entry.getOverride());
	}

	public void setMenues(final Vector<Menu> sortmenues) {
		final Menu m = getMenu("main");
		m.setMenues(sortmenues);
	}
}