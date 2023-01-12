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
@Table(name = "xtcasError")
public class Error {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "KeyLong")
	public int keylong;

	@NotNull
	@Size(max = 50)
	@Column(name = "Username")
	public String username;

	@NotNull
	@Size(max = 250)
	@Column(name = "ErrorMessage")
	public String errormessage;

	@NotNull
	@Column(name = "Date")
	public Timestamp lastdate = Timestamp.from(Instant.now());

}