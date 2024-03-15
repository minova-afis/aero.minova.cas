package aero.minova.cas.service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "xtcasNewsfeedListener")
public class NewsfeedListener extends DataEntity {

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "CASServiceKey", nullable = false)
	private CASServices casService;

	@NotNull
	@Size(max = 50)
	@Column(name = "Topic", length = 50)
	private String topic;
}