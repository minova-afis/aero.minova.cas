package aero.minova.cas.service.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "xtcasUserGroup")
public class UserGroup extends ExtendedDataEntity {

	@NotNull
	@Size(max = 50)
	@Column(name = "UserCode", length = 50)
	public String userCode;

	@NotNull
	@Size(max = 250)
	@Column(name = "SecurityToken", length = 250)
	public String securityToken;

}