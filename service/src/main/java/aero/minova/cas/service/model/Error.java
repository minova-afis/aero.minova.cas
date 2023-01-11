package aero.minova.cas.service.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "xtcasError")
public class Error {

	@NotNull
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
	public LocalDateTime date = LocalDateTime.now();

}