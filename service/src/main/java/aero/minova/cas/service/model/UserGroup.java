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
@Table(name = "xtcasUserGroup")
public class UserGroup extends ExtendedDataEntity {

	@Size(max = 50)
	@Column(name = "UserCode", length = 50)
	private String userCode;

	@NotNull
	@Size(max = 250)
	@Column(name = "SecurityToken", length = 250)
	private String securityToken;

}