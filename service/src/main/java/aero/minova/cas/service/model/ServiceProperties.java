package aero.minova.cas.service.model;

import java.sql.Timestamp;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Data
@Table(name = "xtcasServiceProperties")
public class ServiceProperties {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	public Timestamp lastdate = Timestamp.from(Instant.now());

	@Column(name = "LastAction")
	public int lastaction = 1;

}