package aero.minova.cas.service.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "xtcasLuUserPrivilegeUserGroup")
public class LuUserPrivilegeUserGroup {

	@NotNull
	@Column(name = "KeyLong")
	public int keylong;

	@NotNull
	@ManyToOne(optional = false)
	public UserPrivilege userprivilege;

	@NotNull
	@ManyToOne(optional = false)
	public UserGroup usergroup;

	@NotNull
	@Column(name = "RowLevelSecurity")
	public boolean rowlevelsecurity = false;

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