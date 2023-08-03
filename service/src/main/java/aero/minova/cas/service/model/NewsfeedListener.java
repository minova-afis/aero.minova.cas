package aero.minova.cas.service.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "xtcasNewsfeedListener")
public class NewsfeedListener extends DataEntity {

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "CASServiceKey", nullable = false)
	public CASServices casService;

	@NotNull
	@Size(max = 50)
	@Column(name = "Topic", length = 50)
	public String topic;
}