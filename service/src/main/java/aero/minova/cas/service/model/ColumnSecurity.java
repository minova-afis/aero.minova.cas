package aero.minova.cas.service.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "xtcasColumnSecurity")
public class ColumnSecurity extends DataEntity {

	@NotNull
	@Size(max = 50)
	@Column(name = "TableName", length = 50)
	public String tableName;

	@NotNull
	@Size(max = 50)
	@Column(name = "ColumnName", length = 50)
	public String columnName;

	@NotNull
	@Size(max = 50)
	@Column(name = "SecurityToken", length = 50)
	public String securityToken;
}