package aero.minova.cas.service.model;

import java.sql.Timestamp;
import java.time.Instant;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
@Table(name = "xtcasUser")
public class User extends DataEntity {

	@NotNull
	@Size(max = 50)
	@Column(name = "UserSecurityToken", length = 50)
	public String usersecuritytoken;

	@NotNull
	@Size(max = 250)
	@Column(name = "Memberships", length = 250)
	public String memberships;

	@NotNull
	@Size(max = 50)
	@Column(name = "LastUser", length = 50)
	public String lastuser = "CAS_JPA";

	@NotNull
	@Column(name = "LastDate")
	@Temporal(TemporalType.TIMESTAMP)
	public Timestamp lastdate = Timestamp.from(Instant.now());

	@NotNull
	@Column(name = "LastAction")
	public int lastaction = 1;

}