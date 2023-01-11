package aero.minova.cas.service.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "xtcasUsers")
public class Users {

	@NotNull
	@Column(name = "KeyLong")
	public int keylong;

	@NotNull
	@Size(max = 50)
	@Column(name = "Username")
	public String username;

	@NotNull
	@Size(max = 100)
	@Column(name = "Password")
	public String password;

	@Size(max = 50)
	@Column(name = "LastUser")
	public String lastuser = "CAS_JPA";

	@Column(name = "LastDate")
	public LocalDateTime lastdate = LocalDateTime.now();

	@Column(name = "LastAction")
	public int lastaction = 1;

}