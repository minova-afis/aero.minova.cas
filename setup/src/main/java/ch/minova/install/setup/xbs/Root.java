package ch.minova.install.setup.xbs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import ch.minova.core.install.Map;
import ch.minova.core.install.PreferencesDocument.Preferences;
import ch.minova.install.setup.BaseSetup;

public class Root {
	private final HashMap<String, Entry> entryMap = new HashMap<String, Entry>();
	private final Vector<Entry> entries = new Vector<Entry>();// verwendungszweck
	// dieses Vectors ?
	private final HashMap<String, Node> nodeMap = new HashMap<String, Node>();// nodemap
	// ->
	private final Vector<Node> nodes = new Vector<Node>();
	private String type;

	public Root(final String type) {
		setType(type);
	}

	public ArrayList<Node> getNodes() {
		final ArrayList<Node> nodearraylist = new ArrayList<Node>();
		for (final Node n : this.nodes) {
			nodearraylist.add(n);
		}
		return nodearraylist;
	}

	public ArrayList<Entry> getEntries() {
		final ArrayList<Entry> entryarraylist = new ArrayList<Entry>();
		for (final Entry n : this.entries) {
			entryarraylist.add(n);
		}
		return entryarraylist;
	}

	public void addEntry(final Entry entry) {
		if (this.entryMap.containsKey(entry.getKey().toLowerCase())) {
			final Entry e = this.entryMap.get(entry.getKey().toLowerCase());
			String value = e.getValue();
			if (e.getValue().contains("${application}")) {
				value = value.replace("${application}", ch.minova.install.setup.BaseSetup.getAppNameShort());
				e.setValue(value);
			} else if (e.getValue().contains("${application-long}")) {
				value = value.replace("${application}", ch.minova.install.setup.BaseSetup.getAppName());
				e.setValue(value);
			} else {
				e.setValue(entry.getValue());
			}
		} else {
			this.entries.add(entry);
			this.entryMap.put(entry.getKey().toLowerCase(), entry);
		}
	}

	public Entry getEntry(final String key) {
		if (this.entryMap.containsKey(key.toLowerCase())) {
			return this.entryMap.get(key.toLowerCase());
		} else {
			return null;
		}
	}

	public String getEntryValue(final String key) {
		if (this.entryMap.containsKey(key.toLowerCase())) {
			final Entry e = this.entryMap.get(key.toLowerCase());
			return e.getValue();
		} else {
			return null;
		}
	}

	public void deleteEntry(final Entry entry) {
		if (this.entryMap.containsKey(entry.getKey().toLowerCase())) {
			final Entry e = this.entryMap.remove(entry.getKey().toLowerCase());
			this.entries.remove(e);
		}
	}

	public int getEntryCount() {
		return this.entries.size();
	}

	/**
	 * @param node
	 */
	public void addNode(final Node node) {
		if (this.nodeMap.containsKey(node.getName().toLowerCase())) {
			final Node n = this.nodeMap.get(node.getName().toLowerCase());
			n.merge(n);
		} else {
			this.nodeMap.put(node.getName().toLowerCase(), node);
			this.nodes.add(node);
		}
	}

	public void readNode(final ch.minova.core.install.NodeDocument.Node node) {
		final Node n = getNode(node.getName(), true);
		n.merge(node);
	}

	public Node getNode(final String name, final boolean create) {
		if (this.nodeMap.containsKey(name.toLowerCase())) {
			return this.nodeMap.get(name.toLowerCase());
		} else {
			if (create) {
				final Node n = new Node(name);
				this.nodeMap.put(name.toLowerCase(), n);
				this.nodes.add(n);
				return n;
			}
			return null;
		}
	}

	public void deleteNode(final String name) {
		if (this.nodeMap.containsKey(name.toLowerCase())) {
			final Node n = this.nodeMap.remove(name.toLowerCase());
			this.nodes.remove(n);
		}
	}

	public int getNodeCount() {
		return this.nodes.size();
	}

	/**
	 * löscht bereits vorhandenen Knoten aus dem Document und schreibt dann den aktuellen Knoten oder, und legt neue Knoten in dem Document an.
	 * 
	 * @param nodeDoc
	 * @param nodeToWrite
	 * @return
	 * @throws NodeException
	 */
	public boolean writeNode(final ch.minova.core.install.NodeDocument.Node nodeDoc, final Node nodeToWrite) throws NodeException {
		boolean getanswer = false;
		boolean deleteNode = false;
		if (nodeToWrite != null) {
			for (int k = 0; k < nodeToWrite.getNodeCount(); k++) {
				final ch.minova.core.install.NodeDocument.Node[] nodeDocs = nodeDoc.getNodeArray();
				for (int i = 0; i < nodeDocs.length; i++) {
					if (nodeDocs[i].getName().equals(nodeToWrite.getNodes().get(k).getName())) {
						deleteNode = true;
						nodeDoc.removeNode(i);
						nodeDoc.insertNewNode(i);
						nodeDoc.getNodeArray(i).setName(nodeToWrite.getNodes().get(k).getName());
						// alle Entries in den Knoten übertragen
						writeNewEntryToNode(nodeDoc.getNodeArray(i), nodeToWrite.getNodes().get(k));
						// alle Nachfolgenden Knoten Schreiben
						getanswer = writeNode(nodeDoc.getNodeArray(i), nodeToWrite.getNode(nodeToWrite.getNodes().get(k).getName(), false));
						if (!getanswer) {
							throw new NodeException(MessageFormat.format(BaseSetup.FAILTOWRITENODETODOCUMENT, nodeDoc.getNodeArray(k).getName()));
						}
					}
				}
				if (!deleteNode) {
					nodeDoc.addNewNode();
					nodeDoc.getNodeArray(nodeDoc.getNodeArray().length - 1).setName(nodeToWrite.getNodes().get(k).getName());
					// alle Entries in den Knoten üebertragen
					writeNewEntryToNode(nodeDoc.getNodeArray(k), nodeToWrite.getNodes().get(k));
					// letzter Knoten der Hinzugefügt worden ist
					getanswer = writeNode(nodeDoc.getNodeArray(k), nodeToWrite.getNode(nodeToWrite.getNodes().get(k).getName(), false));
					if (!getanswer) {
						throw new NodeException(MessageFormat.format(BaseSetup.FAILTOWRITENODETODOCUMENT, nodeDoc.getNodeArray(k).getName()));
					}
				}
				deleteNode = false;
			}
		}
		return true;
	}

	/**
	 * fügt alle Einträge zu den Knoten hinzu
	 * 
	 * @param nodeDoc
	 * @param nodeToWrite
	 */
	private void writeNewEntryToNode(final ch.minova.core.install.NodeDocument.Node nodeDoc, final Node nodeToWrite) {
		nodeDoc.addNewMap();
		final Map map = nodeDoc.getMap();
		for (int i = 0; i < nodeToWrite.getEntryCount(); i++) {
			map.addNewEntry();
			map.getEntryArray(i).setKey(nodeToWrite.getEntries().get(i).getKey());
			map.getEntryArray(i).setValue(nodeToWrite.getEntries().get(i).getValue());
		}
	}

	/**
	 * Einträge werden hinzugefügt, bzw erneuert
	 * 
	 * @param nodeDoc
	 * @param nodeToWrite
	 */
	public void writeEntryToNode(final ch.minova.core.install.NodeDocument.Node nodeDoc, final Node nodeToWrite) {
		final Map map = nodeDoc.getMap();
		// Alle Einträge in dem Knoten des rootNodes überprüfen
		for (int i = 0; i < nodeToWrite.getEntryCount(); i++) {
			final Map.Entry[] entries = map.getEntryArray();
			boolean entryexists = false;
			// Überprüfung ob der Eintrag bereits vorhanden ist.
			for (int j = 0; j < entries.length; j++) {
				if (entries[j].getKey().equals(nodeToWrite.getEntries().get(i).getKey())) {
					entries[j].setValue(nodeToWrite.getEntries().get(i).getValue());
					entryexists = true;
				}
			}
			// Falls es den Eintrag noch nicht gibt, wird dieser angelegt
			if (!entryexists) {
				map.addNewEntry();
				map.getEntryArray(map.getEntryArray().length - 1).setKey(nodeToWrite.getEntries().get(i).getKey());
				map.getEntryArray(map.getEntryArray().length - 1).setValue(nodeToWrite.getEntries().get(i).getValue());
			}
		}
	}

	/**
	 * schreibt die einträge in den root bereich einer xbs-Datei
	 * 
	 * @param preferences
	 * @param rootNodetest
	 */
	public void writeEntryToRoot(final Preferences preferences, final ch.minova.install.setup.xbs.Root rootNodetest) {
		final Map map = preferences.getRoot().getMap();
		// Alle Einträge in dem Knoten des rootNodes überprüfen
		for (int i = 0; i < rootNodetest.getEntryCount(); i++) {
			final Map.Entry[] entries = map.getEntryArray();
			boolean entryexists = false;
			// Überprüfung ob der Eintrag bereits vorhanden ist.
			for (int j = 0; j < entries.length; j++) {
				if (entries[j].getKey().equals(rootNodetest.getEntries().get(i).getKey())) {
					entries[j].setValue(rootNodetest.getEntries().get(i).getValue());
					entryexists = true;
				}
			}
			// Falls es den Eintrag noch nicht gibt, wird dieser angelegt
			if (!entryexists) {
				map.addNewEntry();
				map.getEntryArray(map.getEntryArray().length - 1).setKey(rootNodetest.getEntries().get(i).getKey());
				map.getEntryArray(map.getEntryArray().length - 1).setValue(rootNodetest.getEntries().get(i).getValue());
			}
		}
	}

	/**
	 * schreibt in den Root die ersten Nodes und danach werden writeNodes aufgerufen weil der Root der einzige Knoten ist, der zus�tzlich auch Entries hat. und
	 * Nodes , jedoch sit dieser Knoten der oberste Knoten einer xbs-Datei !
	 * 
	 * @param preferences
	 * @param rootNodetest
	 * @return
	 * @throws NodeException
	 */
	public boolean writeNodeToRoot(final Preferences preferences, final ch.minova.install.setup.xbs.Root rootNodetest) throws NodeException {
		boolean getanswer = false;
		boolean deleteNode = false;
		final Preferences.Root rootDoc = preferences.getRoot();
		for (int k = 0; k < rootNodetest.getNodeCount(); k++) {
			final ch.minova.core.install.NodeDocument.Node[] nodeDocs = rootDoc.getNodeArray();
			for (int i = 0; i < nodeDocs.length; i++) {
				if (nodeDocs[i].getName().equals(rootNodetest.getNodes().get(k).getName())) {
					deleteNode = true;
					rootDoc.removeNode(i);
					rootDoc.insertNewNode(i);
					rootDoc.getNodeArray(i).setName(rootNodetest.getNodes().get(k).getName());
					// alle Entries in den Knoten �bertragen
					writeNewEntryToNode(rootDoc.getNodeArray(i), rootNodetest.getNodes().get(k));
					// alle Nachfolgenden Knoten Schreiben
					getanswer = writeNode(rootDoc.getNodeArray(i), rootNodetest.getNode(rootNodetest.getNodes().get(k).getName(), false));
					if (!getanswer) {
						throw new NodeException(MessageFormat.format(BaseSetup.FAILTOWRITENODETODOCUMENT, rootDoc.getNodeArray(k).getName()));
					}
				}
			}
			if (!deleteNode) {
				rootDoc.addNewNode();
				rootDoc.getNodeArray(rootDoc.getNodeArray().length - 1).setName(rootNodetest.getNodes().get(k).getName());
				// alle Entries in den Knoten �ebertragen
				writeNewEntryToNode(rootDoc.getNodeArray(k), rootNodetest.getNodes().get(k));
				// letzter Knoten der Hinzugef�gt worden ist
				getanswer = writeNode(rootDoc.getNodeArray(k), rootNodetest.getNode(rootNodetest.getNodes().get(k).getName(), false));
				if (!getanswer) {
					throw new NodeException(MessageFormat.format(BaseSetup.FAILTOWRITENODETODOCUMENT, rootDoc.getNodeArray(k).getName()));
				}
			}
			deleteNode = false;
		}
		return true;
	}

	/**
	 * Anhand des Namens des Knotens kann ermittelt werden ob der Knoten bereits vorhanden ist
	 * 
	 * @param Name
	 * @return
	 */
	public boolean existsNode(final String Name) {
		if (this.nodeMap.containsKey(Name.toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}

	public String getType() {
		return this.type;
	}

	public void setType(final String type) {
		this.type = type;
	}
}