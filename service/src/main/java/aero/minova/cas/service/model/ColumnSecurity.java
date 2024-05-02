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
@Table(name = "xtcasColumnSecurity")
public class ColumnSecurity extends DataEntity {

	@NotNull
	@Size(max = 50)
	@Column(name = "TableName", length = 50)
	private String tableName;

	@NotNull
	@Size(max = 50)
	@Column(name = "ColumnName", length = 50)
	private String columnName;

	@NotNull
	@Size(max = 50)
	@Column(name = "SecurityToken", length = 50)
	private String securityToken;
}