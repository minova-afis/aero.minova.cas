package aero.minova.tracintegration;

// import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wild
 * @since 12.4.0
 */
public class Ticket {
	private int id; // #:
	private String description; // description:
	private Milestone milestone = null; // milestone:
	private String milestoneName;
	private String keywords; // keywords:
	private Object summary; // summary:
	private Wiki wiki = null;
	private String wikiAddress;
	private Pattern keywordPattern = Pattern.compile("([a-zA-Z\\-_0-9]*)");

	// private Boolean billable; // billable:
	// private String reporter; // reporter:
	// private String ticketType; // type
	// private String version; // version:
	// private Timestamp creationTime; // time:
	// private Timestamp changeTime; // changetime:
	// private String blockedBy; // blockedby:
	// private String blocking; // blocking:
	// private String component; // component:
	// private Double totalHours; // totalhours:
	// private String status; // status:
	// private Double hours; // hours:
	// private String resolution; // resolution:
	// private String priority; // priority:
	// private Double offeredHours; // offeredhours:
	// private String owner; // owner:
	// private Timestamp dueDate; // duedate;
	// private Double estimatedHours; // estimatedhours
	// private String cc; // cc:

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Milestone getMilestone() {
		if (milestone == null && milestoneName != null && !milestoneName.isEmpty()) {
			milestone = Server.getInstance().getMilestone(milestoneName);
		}
		return milestone;
	}

	public void setMilestone(Milestone milestone) {
		this.milestone = milestone;
	}

	public String getMilestoneName() {
		return milestoneName;
	}

	public void setMilestoneName(String milestoneName) {
		this.milestoneName = milestoneName;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;

		// Adresse für die Wikiseite des Projektes zusammensetzen
		wiki = null; // damit ist auch die wiki-Seite ggf. neu zu laden
		Matcher m = keywordPattern.matcher(keywords);
		if (m.find() && m.group(1).length() > 0) {
			if (m.group(1).contains("_")) {
				wikiAddress = "ISO/project/mpks/steckbriefe/" + m.group(1);
			} else {
				wikiAddress = "kontrakte/" + m.group(1);
			}
		} else {
			wikiAddress = null;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public void setSummary(Object summary) {
		this.summary = summary;
	}

	public Object getSummary() {
		return summary;
	}

	public Wiki getWiki() {
		if (wikiAddress == null) {
			return null;
		} else if (wiki == null) {
			wiki = Server.getInstance().getWiki(wikiAddress);
		}
		return wiki;
	}

	public void update(String text) {
		Server.getInstance().updateTicket(this, text);
	}

	@Override
	public String toString() {
		return "#" + id + ": " + summary;
	}
}