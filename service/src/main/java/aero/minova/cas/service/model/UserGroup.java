package aero.minova.cas.service.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Data
@Table(name = "xtcasUserGroup")
public class UserGroup extends DataEntity {

	@NotNull
	@Size(max = 50)
	@Column(name = "UserCode")
	public String usercode;

	@NotNull
	@Size(max = 250)
	@Column(name = "SecurityToken")
	public String securitytoken;

}