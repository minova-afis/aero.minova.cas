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
import jakarta.persistence.Table;
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
	@Column(name = "Service", length = 128)
	public String service;

	@NotNull
	@Size(max = 256)
	@Column(name = "Property", length = 256)
	public String property;

	@NotNull
	@Size(max = 1024)
	@Column(name = "Val", length = 1024)
	public String val;

	@Size(max = 50)
	@Column(name = "LastUser", length = 50)
	public String lastuser = "CAS_JPA";

	@Column(name = "LastDate")
	public Timestamp lastdate = Timestamp.from(Instant.now());

	@Column(name = "LastAction")
	public int lastaction = 1;

}