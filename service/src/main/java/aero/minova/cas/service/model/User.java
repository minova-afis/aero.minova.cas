package aero.minova.cas.service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "xtcasUser")
public class User extends DataEntity {

	@NotNull
	@Size(max = 50)
	@Column(name = "UserSecurityToken", length = 50)
	private String userSecurityToken;

	@Size(max = 250)
	@Column(name = "Memberships", length = 250)
	private String memberships;
}