package aero.minova.tracintegration;

import java.util.Date;

/**
 * @author wild
 * @since 12.4.0
 */
public class Milestone {
	private String description;
	private String name;
	private Date due;
	private Integer completed;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String milestoneName) {
		this.name = milestoneName;
	}

	public String getName() {
		return name;
	}

	/**
	 * @param due
	 *            Das Fälligkeitsdatum des Milestones
	 */
	public void setDue(Date due) {
		this.due = due;
	}

	public Date getDue() {
		return due;
	}

	public void setCompleted(Integer completed) {
		this.completed = completed;
	}

	public Integer getCompleted() {
		return completed;
	}
}