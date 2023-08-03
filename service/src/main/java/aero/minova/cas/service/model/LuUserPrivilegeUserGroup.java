package aero.minova.cas.service.model;

import javax.validation.constraints.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "xtcasLuUserPrivilegeUserGroup")
public class LuUserPrivilegeUserGroup extends DataEntity {

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "UserPrivilegeKey", nullable = false)
	public UserPrivilege userPrivilege;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "UserGroupKey", nullable = false)
	public UserGroup userGroup;

	@NotNull
	@Column(name = "RowLevelSecurity")
	public boolean rowLevelSecurity = false;

}