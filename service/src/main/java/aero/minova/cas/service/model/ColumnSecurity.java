package aero.minova.cas.service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "xtcasColumnSecurity")
@AllArgsConstructor
@NoArgsConstructor
public class ColumnSecurity extends DataEntity {

	public ColumnSecurity(int keyLong, String keyText, String tableName, String columnName, String securityToken) {
		setKeyLong(keyLong);
		setKeyText(keyText);
		this.tableName = tableName;
		this.columnName = columnName;
		this.securityToken = securityToken;
	}

	@NotNull
	@Size(max = 50)
	@Column(name = "TableName", length = 50)
	private String tableName;

	@NotNull
	@Size(max = 50)
	@Column(name = "ColumnName", length = 50)
	private String columnName;

	@Size(max = 50)
	@Column(name = "SecurityToken", length = 50)
	private String securityToken;
}