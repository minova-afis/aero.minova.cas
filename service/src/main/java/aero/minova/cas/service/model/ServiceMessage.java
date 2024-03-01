package aero.minova.cas.service.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "xtcasServiceMessage")
public class ServiceMessage extends DataEntity {

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "CASServiceKey", nullable = false)
	private CASServices casService;

	@NotNull
	@Lob
	@Column(name = "Message")
	private String message;

	@NotNull
	@Column(name = "IsSent")
	private boolean isSent = false;

	@NotNull
	@Column(name = "NumberOfAttempts")
	private int numberOfAttempts = 0;

	@NotNull
	@Column(name = "MessageCreationDate", columnDefinition = "TIMESTAMP")
	private LocalDateTime messageCreationDate;

	@NotNull
	@Column(name = "Failed")
	private boolean failed = false;
}