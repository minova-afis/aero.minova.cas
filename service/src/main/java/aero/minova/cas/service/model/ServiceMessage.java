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
	public CASServices casService;

	@NotNull
	@Lob
	@Column(name = "Message")
	public String message;

	@NotNull
	@Column(name = "IsSent")
	public boolean isSent = false;

	@NotNull
	@Column(name = "NumberOfAttempts")
	public int numberOfAttempts = 0;

	@NotNull
	@Column(name = "MessageCreationDate", columnDefinition = "TIMESTAMP")
	public LocalDateTime messageCreationDate;

	@NotNull
	@Column(name = "Failed")
	public boolean failed = false;
}