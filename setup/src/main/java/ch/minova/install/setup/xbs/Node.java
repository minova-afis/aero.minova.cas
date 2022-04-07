package ch.minova.install.setup.xbs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import ch.minova.core.install.Map;
import ch.minova.install.setup.BaseSetup;

public class Node {
	private final HashMap<String, Entry> entryMap = new HashMap<String, Entry>();
	private final Vector<Entry> entries = new Vector<Entry>();// verwendungszweck dieses Vectors ?
	private final HashMap<String, Node> nodeMap = new HashMap<String, Node>();// nodemap ->
	private final Vector<Node> nodes = new Vector<Node>();
	private final String name;

	public Node(final String name) {
		this.name = name;
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
		if (!entry.getKey().endsWith("-")) {
			if (this.entryMap.containsKey(entry.getKey().toLowerCase())) {
				final Entry e = this.entryMap.get(entry.getKey().toLowerCase());
				e.setValue(entry.getValue());
			} else {
				this.entries.add(entry);
				this.entryMap.put(entry.getKey().toLowerCase(), entry);
			}
		}
		// löscht den eintrag, falls dieser mit "-" endet!
		else {
			String keyname;
			keyname = entry.getKey().substring(0, entry.getKey().length() - 1);
			keyname.toLowerCase();
			if (this.entryMap.containsKey(keyname)) {
				this.deleteEntry(keyname);
			}
		}
		// löscht den eintrag, falls dieser mit "-" endet!
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

	public void deleteEntry(final String entryname) {
		if (this.entryMap.containsKey(entryname.toLowerCase())) {
			final Entry e = this.entryMap.remove(entryname.toLowerCase());
			this.entries.remove(e);
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

	public void addNode(final Node node) {
		if (!node.getName().endsWith("-")) {
			if (this.nodeMap.containsKey(node.name.toLowerCase())) {
				final Node n = this.nodeMap.get(node.name.toLowerCase());
				n.merge(n);
			} else {
				this.nodeMap.put(node.name.toLowerCase(), node);
				this.nodes.add(node);
			}
		} else {
			// löscht den eintrag, falls dieser mit "-" endet!
			String name;
			name = node.getName().substring(0, node.getName().length() - 1);
			name = name.toLowerCase();
			if (this.nodeMap.containsKey(name)) {
				deleteNode(name);
			}
		}
	}

	public void merge(final Node n) {
		for (int i = 0; i < n.getEntryCount(); i++) {
			addEntry(n.entries.get(i));
		}
		for (int i = 0; i < n.nodes.size(); i++) {
			addNode(n.nodes.get(i));
		}
	}

	public void readNode(final ch.minova.core.install.NodeDocument.Node node) {
		if (!node.getName().endsWith("-")) {
			final Node n = getNode(node.getName(), true);
			n.merge(node);
		} else {
			// löscht den eintrag, falls dieser mit "-" endet!
			String name;
			name = node.getName().substring(0, node.getName().length() - 1);
			name = name.toLowerCase();
			if (this.nodeMap.containsKey(name)) {
				deleteNode(name);
			}
		}
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
				// alle Entries in den Knoten übertragen
				writeNewEntryToNode(nodeDoc.getNodeArray(k), nodeToWrite.getNodes().get(k));
				// letzter Knoten der Hinzugefügt worden ist
				getanswer = writeNode(nodeDoc.getNodeArray(k), nodeToWrite.getNode(nodeToWrite.getNodes().get(k).getName(), false));
				if (!getanswer) {
					throw new NodeException(MessageFormat.format(BaseSetup.FAILTOWRITENODETODOCUMENT, nodeDoc.getNodeArray(k).getName()));
				}
			}
			deleteNode = false;
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
			// überprüfung ob der Eintrag bereits vorhanden ist.
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
	 * fügt Knoten zusammen, wenn dieser bereits besteht
	 * 
	 * @param node
	 */
	void merge(final ch.minova.core.install.NodeDocument.Node node) {
		try {
			final Map map = node.getMap();
			if (map != null) {
				if (!map.isNil()) {
					for (int i = 0; i < node.getMap().getEntryArray().length; i++) {
						final Map.Entry e = node.getMap().getEntryArray(i);
						addEntry(new Entry(e.getKey(), e.getValue()));
					}
				}
			}
			for (int i = 0; i < node.getNodeArray().length; i++) {
				readNode(node.getNodeArray(i));
			}
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean existsNode(final String Name) {
		if (this.nodeMap.containsKey(Name.toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}

	public String getName() {
		return this.name;
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

	public void readMap(final Map map1) {
		try {
			final Map map = map1;
			if (!map.isNil()) {
				for (int i = 0; i < map1.getEntryArray().length; i++) {
					final Map.Entry e = map1.getEntryArray(i);
					addEntry(new Entry(e.getKey(), e.getValue()));
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}