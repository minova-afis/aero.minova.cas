package aero.minova.cas.service.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
}