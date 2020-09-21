package aero.minova.trac.integration;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wild
 * @since 12.4.0
 */
public class TracTicketIntegration {
	public static final int TICKET_NULL = 0;
	public static final int TICKET_OK = 1;
	public static final int TICKET_INCOMPLETE = 2;

	private Ticket ticket;

	private final HashMap<String, String> values = new HashMap<>(5);
	private final Pattern sisPattern = Pattern.compile(
			"(^|\\s+)== Abrechnung SIS ==\\s*\\|\\|Feld\\|\\|Wert\\|\\|((\\s*\\|\\|(Kunde|Projekt|Kontrakt|Aufwand|Beschreibung)\\|\\|[^\\|]*\\|\\|)*)",
			Pattern.MULTILINE);
	private final Pattern keyTextPattern = Pattern.compile("\\s*\\|\\|(Kunde|Projekt|Kontrakt|Aufwand|Beschreibung)\\|\\|([^\\|]*)\\|\\|");

	private boolean hasLang;

	public TracTicketIntegration() {
		// Werte initialisieren
		values.put("ServiceKey", null);
		values.put("ServiceObjectKey", null);
		values.put("ServiceContractKey", null);
		values.put("OrderReceiverKey", null);
		values.put("Description", null);
	}

	/**
	 * Diese Methode nimmt das erste Schlüsselwort des Tickets und sucht eine entsprechende WIKI-Seite. Wenn eine gefunden wird, wird auch auf dieser
	 * nachgeschaut, ob wir Daten für die Abrechnung finden.
	 */
	private void analyseKeyWords() {
		final Wiki wiki = ticket.getWiki();
		try {
			final String content = wiki.getContent();
			final Matcher sisMatcher = sisPattern.matcher(content);
			if (sisMatcher.find()) {
				findSisData(sisMatcher);
			}
		} catch (final NullPointerException npe) {}
	}

	/**
	 * Diese Methode soll die Abrechnungsdaten aus dem Milestone laden
	 */
	private void analyseMilestone() {
		final Milestone milestone = ticket.getMilestone();
		try {
			final String description = milestone.getDescription();
			final Matcher sisMatcher = sisPattern.matcher(description);
			// Abrechnungsdaten aus dem Meilenstein-Text
			if (sisMatcher.find()) {
				findSisData(sisMatcher);
			}
		} catch (final NullPointerException npe) {
			// dann eben nicht
		}
	}

	/**
	 * Diese Methode soll die Abrechnungsdaten aus dem Ticket laden
	 */
	private void analyseTicket() {
		final String description = ticket.getDescription();
		final Matcher sisMatcher = sisPattern.matcher(description);
		// 1. Versuch: Abrechnungsdaten aus dem Ticket
		if (sisMatcher.find()) {
			findSisData(sisMatcher);
		}
		// 2. Versuch: Abrechnungsdaten aus dem Meilenstein
		if (anyNull()) {
			analyseMilestone();
		}

		// 3. Versuch: Abrechnungsdaten aus den Keywords
		if (anyNull()) {
			analyseKeyWords();
		}
	}

	/**
	 * prüft, ob ein wichtiges Feld noch null ist
	 * 
	 * @return true, wenn ein wichtiges Feld noch null ist, sonst false
	 */
	private boolean anyNull() {
		return values.get("OrderReceiverKey") == null || values.get("ServiceContractKey") == null || values.get("ServiceObjectKey") == null
				|| values.get("ServiceKey") == null;
	}

	/**
	 * sucht die Abrechnungsdaten
	 * 
	 * @param sisMatcher
	 */
	private void findSisData(final Matcher sisMatcher) {
		final String sisData = sisMatcher.group();
		final Matcher keyTextMatcher = keyTextPattern.matcher(sisData);
		int pos = 0;
		while (keyTextMatcher.find(pos)) {
			final String type = keyTextMatcher.group(1);
			final String text = keyTextMatcher.group(2);
			if (values.get("OrderReceiverKey") == null && type.equals("Kunde")) {
				values.put("OrderReceiverKey", text);
			} else if (values.get("ServiceContractKey") == null && type.equals("Kontrakt")) {
				values.put("ServiceContractKey", text);
			} else if (values.get("ServiceObjectKey") == null && type.equals("Projekt")) {
				values.put("ServiceObjectKey", text);
			} else if (values.get("ServiceKey") == null && type.equals("Aufwand")) {
				values.put("ServiceKey", text);
			} else if (values.get("Description") == null && type.equals("Beschreibung")) {
				values.put("Description", text);
				hasLang = true;
			}

			pos = keyTextMatcher.end();
		}
	}

	/**
	 * liefert die Ticket-Beschreibung
	 * 
	 * @return die Ticket-Beschreibung
	 */
	public String getDescription() {
		final String description = values.get("Description");
		// Die Beschreibung wurde wohl nicht überschrieben, wir nehmen die Summary
		if (description == null) {
			return "#" + ticket.getId() + ": " + ticket.getSummary();
		} else {
			return "#" + ticket.getId() + ": " + description;
		}
	}

	/**
	 * liefert den OrderReceiver (Kunde)
	 * 
	 * @return KeyText vom OrderReceiver
	 */
	public String getOrderReceiver() {
		return values.get("OrderReceiverKey");
	}

	/**
	 * liefert den Service (Aufwand)
	 * 
	 * @return KeyText vom Service
	 */
	public String getService() {
		return values.get("ServiceKey");
	}

	/**
	 * liefert den ServiceContract (Kontrakt)
	 * 
	 * @return KeyText vom ServiceContract
	 */
	public String getServiceContract() {
		return values.get("ServiceContractKey");
	}

	/**
	 * liefert das ServiceObjekt (Projekt)
	 * 
	 * @return KeyText vom ServiceObjekt
	 */
	public String getServiceObject() {
		return values.get("ServiceObjectKey");
	}

	/**
	 * ermittelt den Status<br>
	 * 0: kein Ticket zugewiesen<br>
	 * 1: Ticket i.O.<br>
	 * 2: Ticket unvollständig
	 * 
	 * @return Status des zugewiesenen Tickets
	 */
	public int getStatus() {
		if (ticket == null) {
			return TICKET_NULL;
		} else if (anyNull()) {
			return TICKET_INCOMPLETE;
		} else {
			return TICKET_OK;
		}
	}

	/**
	 * holt den gewünschten Wert
	 * 
	 * @param key
	 * @return
	 */
	public String getValue(final String key) {
		return key.equals("Description") ? getDescription() : values.get(key);
	}

	/**
	 * Diese Methode soll die Summary des Tickets im Trac auf den übergebenen Text setzen
	 */
	public boolean setDetail(String summary, int ticketNumber, String text) {
		Server server = Server.getInstance();
		Ticket changeTicket = server.getTicket(ticketNumber);

		if (changeTicket == null) {
			// server.deleteTicket(ticketNumber);
			return false;

		} else {
			changeTicket.setSummary(summary);
			changeTicket.update(text);
			return true;
		}
	}

	/**
	 * Diese Methode soll kontrollieren, ob das Ticket einen übersetzten Text besitzt
	 */
	public boolean hasEnglish(int ticketNumber) {
		boolean hasLanguage = hasLang;

		Server server = Server.getInstance();
		Ticket langTicket = server.getTicket(ticketNumber);

		if (langTicket == null) {
			hasLanguage = false;
		}

		return hasLanguage;
	}

	/**
	 * Diese Methode soll kontrollieren, ob das Ticket den übersetzten Text benutzt
	 */
	public boolean usesEnglish(int ticketNumber, String ticketSummary) {
		boolean kommtVor = false;

		Server server = Server.getInstance();
		Ticket langTicket = server.getTicket(ticketNumber);

		if (langTicket.getDescription().equals(ticketSummary)) {
			kommtVor = true;
		}

		return kommtVor;
	}

	/**
	 * setzt das Ticket
	 * 
	 * @param ticket
	 */
	public void setTicket(final Ticket ticket) {
		if (ticket != null) {
			this.ticket = ticket;
			values.put("OrderReceiverKey", null);
			values.put("ServiceContractKey", null);
			values.put("ServiceObjectKey", null);
			values.put("ServiceKey", null);
			values.put("Description", null);
			hasLang = false;
			analyseTicket();
		}
	}

	/**
	 * Setzt die Ticketnummer. Wenn der Trac-Server das Ticket kennt, wird das Ticket gesetzt.
	 * 
	 * @see #setTicket(Ticket)
	 * @param tracNumber
	 */
	public void setTicketNumber(final int tracNumber) {
		final Server server = Server.getInstance();
		setTicket(server.getTicket(tracNumber));
	}
}