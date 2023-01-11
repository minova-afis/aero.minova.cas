package aero.minova.cas.service.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "xtcasServiceProperties")
public class ServiceProperties {

	@NotNull
	@Column(name = "KeyLong")
	public int keylong;

	@NotNull
	@Size(max = 128)
	@Column(name = "Service")
	public String service;

	@NotNull
	@Size(max = 256)
	@Column(name = "Property")
	public String property;

	@NotNull
	@Size(max = 1024)
	@Column(name = "Val")
	public String val;

	@Size(max = 50)
	@Column(name = "LastUser")
	public String lastuser = "CAS_JPA";

	@Column(name = "LastDate")
	public LocalDateTime lastdate = LocalDateTime.now();

	@Column(name = "LastAction")
	public int lastaction = 1;

}