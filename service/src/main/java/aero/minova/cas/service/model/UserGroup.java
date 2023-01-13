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

	@NotNull
	@Size(max = 50)
	@Column(name = "UserCode")
	public String usercode;

	@NotNull
	@Size(max = 250)
	@Column(name = "SecurityToken")
	public String securitytoken;

}