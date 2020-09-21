package aero.minova.trac.integration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.lustin.trac.xmlprc.TrackerDynamicProxy;

/**
 * @author saak
 * @since 12.4.0
 */
public class Server {
	private static Server server = null;
	private TrackerDynamicProxy trackerDynamicProxy = null;

	public static Server getInstance() {
		// FIXME
		return getInstance("bugzilla", "123wilfried");
	}

	public static Server getInstance(String username, String password) {
		// falls (noch) keine Instanz vorhanden ist, erstelle eine
		// FIXME falls sich username / password ändern, bleibt die Instanz gleich
		if (server == null) {
			try {
				Server newServer = new Server();
				XmlRpcClientConfigImpl conf = new XmlRpcClientConfigImpl();
				conf.setBasicUserName(username);
				conf.setBasicPassword(password);
				conf.setServerURL(new URL("http://trac.minova.com/trac/minova/login/xmlrpc"));

				XmlRpcClient client = new XmlRpcClient();
				client.setConfig(conf);

				newServer.trackerDynamicProxy = new TrackerDynamicProxy(client);
				server = newServer;
			} catch (MalformedURLException e) {
				e.printStackTrace();
				server = null;
			}
		}

		return server;
	}

	/**
	 * Diese Methode holt ein Ticket von Trac Server
	 * 
	 * @param id
	 *            Ticketnummer
	 * @return null, wenn das Ticket nicht geladen werden konnte; sonst das gefudene Ticket
	 */
	public Ticket getTicket(int id) {
		return getTicket(id, false);
	}

	public Ticket getTicket(int id, boolean debug) {
		org.lustin.trac.xmlprc.Ticket ticket = (org.lustin.trac.xmlprc.Ticket) trackerDynamicProxy.newInstance(org.lustin.trac.xmlprc.Ticket.class);

		Ticket newTicket = new Ticket();
		try {
			Vector<?> details = ticket.get(id);
			// @SuppressWarnings("unused")
			// Object x = ticket.query("max=100,modified=2013-02-22..2013-02-23,status=closed");
			for (Iterator<?> i = details.iterator(); i.hasNext();) {
				Object value = i.next();
				if (value instanceof HashMap) {
					@SuppressWarnings("unchecked")
					HashMap<String, ?> attributes = (HashMap<String, ?>) value;
					newTicket.setId(id);
					newTicket.setSummary((String) attributes.get("summary"));
					newTicket.setDescription((String) attributes.get("description"));
					newTicket.setKeywords((String) attributes.get("keywords"));
					newTicket.setMilestoneName((String) attributes.get("milestone"));

					if (debug) {
						for (Iterator<String> attributeName = attributes.keySet().iterator(); attributeName.hasNext();) {
							Object object = attributeName.next();
							System.out.println(object + ": " + attributes.get(object));
						}
					}
				}
			}
		} catch (Exception e) {
			newTicket = null;
		}
		return newTicket;
	}

	public Milestone getMilestone(String milestoneName) {
		org.lustin.trac.xmlprc.Ticket.Milestone milestone = (org.lustin.trac.xmlprc.Ticket.Milestone) trackerDynamicProxy
				.newInstance(org.lustin.trac.xmlprc.Ticket.Milestone.class);

		Milestone newMilestone = new Milestone();
		try {
			Hashtable<String, ?> attributes = milestone.get(milestoneName);
			newMilestone.setName(milestoneName);
			newMilestone.setDescription((String) attributes.get("description"));
			if (attributes.get("due") instanceof Date) {
				newMilestone.setDue((Date) attributes.get("due"));
			}
			newMilestone.setCompleted((Integer) attributes.get("completed"));
		} catch (Exception e) {
			newMilestone = null;
		}
		return newMilestone;
	}

	/**
	 * Liest den Inhalt der angegebenen Wiki-Seite
	 * 
	 * @param wikiAddress
	 *            die (interne) Adresse der Wiki-Seite, z.B. "Module/ch.minova.sap.sales"
	 * @return {@link Wiki}
	 */
	public Wiki getWiki(String wikiAddress) {
		org.lustin.trac.xmlprc.Wiki wiki = (org.lustin.trac.xmlprc.Wiki) trackerDynamicProxy.newInstance(org.lustin.trac.xmlprc.Wiki.class);

		Wiki newWiki = new Wiki(wikiAddress);
		try {
			String content = wiki.getPage(wikiAddress);
			if (content == null) {
				newWiki = null;
			} else {
				newWiki.setContent(content);
			}
		} catch (Exception e) {
			newWiki = null;
		}
		return newWiki;
	}

	/**
	 * Schreibt den Inhalt auf die angegebene Wiki-Seite.
	 * 
	 * @param internalWiki
	 *            Objekt der internen {@link Wiki}-Klasse
	 * @author wild
	 * @since 11.0.0
	 */
	public void setWiki(Wiki internalWiki) {
		org.lustin.trac.xmlprc.Wiki wiki = (org.lustin.trac.xmlprc.Wiki) trackerDynamicProxy.newInstance(org.lustin.trac.xmlprc.Wiki.class);

		// null ist nicht zulässig...
		Hashtable<String, ?> pageInfo = new Hashtable<>();
		try {
			// wir geben einfach die bisherigen Infos wieder mit rein, das WIKI macht es dann richtig (neue Versionsnr. usw.)
			pageInfo = wiki.getPageInfo(internalWiki.getAddress());
		} catch (Exception ex) {
			// wenn ein Fehler auftritt, ist die Seite wohl noch nicht vorhanden
		}
		wiki.putPage(internalWiki.getAddress(), internalWiki.getContent(), pageInfo);
	}

	// theoretisches Update eines Tickets
	public void updateTicket(Ticket newTicket, String text) {
		org.lustin.trac.xmlprc.Ticket ticket = (org.lustin.trac.xmlprc.Ticket) trackerDynamicProxy.newInstance(org.lustin.trac.xmlprc.Ticket.class);

		try {
			// Vector<?> details = ticket.get(newTicket.getId());
			// Vector <HashMap> hM = ticket.getTicketFields();

			// Zeige Inhalt - zur Kontrolle
			// System.out.println("------------");
			// System.out.println("Inhalt von getTicketFields()");
			// System.out.println(hM.toString());
			// System.out.println("------------");
			// System.out.println("Inhalt von get(i)");
			// System.out.println(details.toString());
			// System.out.println("------------");

			Hashtable<String, Object> ht = new Hashtable<>();

			ht.put("summary", newTicket.getSummary().toString());
			ticket.update(newTicket.getId(), text, ht, false);
		} catch (Exception e) {
			e.printStackTrace();
			server = null;
		}
	}

	// //theoretisches Löschen eines Tickets mit übergebener Nummer
	// public void deleteTicket(int id){
	//
	// org.lustin.trac.xmlprc.Ticket ticket = (org.lustin.trac.xmlprc.Ticket) trackerDynamicProxy.newInstance(org.lustin.trac.xmlprc.Ticket.class);
	// ticket.delete(id);
	//
	// }
}