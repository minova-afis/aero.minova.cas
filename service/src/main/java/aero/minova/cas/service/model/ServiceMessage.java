package aero.minova.cas.service.model;

import java.sql.Timestamp;
import java.time.Instant;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "xtcasServiceMessage")
public class ServiceMessage {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "KeyLong")
	public int keylong;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "CASServiceKey", nullable = false)
	public CASServices casservice;

	@NotNull
	@Lob
	@Column(name = "Message")
	public String message;

	@NotNull
	@Column(name = "IsSent")
	public boolean issent = false;

	@NotNull
	@Column(name = "NumberOfAttempts")
	public int numberofattempts = 0;

	@NotNull
	@Column(name = "MessageCreationDate")
	public Timestamp messagecreationdate;

	@NotNull
	@Column(name = "Failed")
	public boolean failed = false;

	@NotNull
	@Size(max = 50)
	@Column(name = "LastUser")
	public String lastuser = "CAS_JPA";

	@NotNull
	@Column(name = "LastDate")
	public Timestamp lastdate = Timestamp.from(Instant.now());

	@NotNull
	@Column(name = "LastAction")
	public int lastaction = 1;

}