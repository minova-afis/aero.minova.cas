package aero.minova.cas.service.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "xtcasUserGroup")
public class UserGroup extends DataEntity {

	@Size(max = 50)
	@Column(name = "Description", length = 50)
	public String description;

	@NotNull
	@Size(max = 50)
	@Column(name = "UserCode", length = 50)
	public String userCode;

	@NotNull
	@Size(max = 250)
	@Column(name = "SecurityToken", length = 250)
	public String securityToken;

}