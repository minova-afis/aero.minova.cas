package aero.minova.cas.service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "xtcasLuUserPrivilegeUserGroup")
public class LuUserPrivilegeUserGroup extends DataEntity {

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "UserPrivilegeKey", nullable = false)
	private UserPrivilege userPrivilege;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "UserGroupKey", nullable = false)
	private UserGroup userGroup;

	@NotNull
	@Column(name = "RowLevelSecurity")
	private boolean rowLevelSecurity = false;

}