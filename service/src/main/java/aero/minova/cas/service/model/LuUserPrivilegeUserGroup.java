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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "xtcasLuUserPrivilegeUserGroup")
public class LuUserPrivilegeUserGroup {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "KeyLong")
	public int keylong;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "UserPrivilegeKey", nullable = false)
	public UserPrivilege userprivilege;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "UserGroupKey", nullable = false)
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
	public Timestamp lastdate = Timestamp.from(Instant.now());

	@NotNull
	@Column(name = "LastAction")
	public int lastaction = 1;

}