package aero.minova.cas.service.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "xtcasUser")
public class User {

	@NotNull
	@Column(name = "KeyLong")
	public int keylong;

	@NotNull
	@Size(max = 10)
	@Column(name = "KeyText")
	public String keytext;

	@NotNull
	@Size(max = 50)
	@Column(name = "UserSecurityToken")
	public String usersecuritytoken;

	@NotNull
	@Size(max = 250)
	@Column(name = "Memberships")
	public String memberships;

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