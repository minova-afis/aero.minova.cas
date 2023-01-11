package aero.minova.cas.service.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "xtcasServiceMessage")
public class ServiceMessage {

	@NotNull
	@Column(name = "KeyLong")
	public int keylong;

	@NotNull
	@ManyToOne(optional = false)
	public CASServices casservice;

	@NotNull
	@Size(max = 1024)
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
	public LocalDateTime messagecreationdate;

	@NotNull
	@Size(max = 50)
	@Column(name = "LastUser")
	public String lastuser = "CAS_JPA";

	@NotNull
	@Column(name = "LastDate")
	public LocalDateTime lastdate = LocalDateTime.now();

	@NotNull
	@Column(name = "LastAction")
	public int lastaction = 1;

}