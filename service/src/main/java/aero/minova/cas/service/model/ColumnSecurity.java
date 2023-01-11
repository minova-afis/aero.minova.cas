package aero.minova.cas.service.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "xtcasColumnSecurity")
public class ColumnSecurity {

	@NotNull
	@Column(name = "KeyLong")
	public int keylong;

	@NotNull
	@Size(max = 50)
	@Column(name = "TableName")
	public String tablename;

	@NotNull
	@Size(max = 50)
	@Column(name = "ColumnName")
	public String columnname;

	@NotNull
	@Size(max = 50)
	@Column(name = "SecurityToken")
	public String securitytoken;

	@NotNull
	@Size(max = 50)
	@Column(name = "LastUser")
	public String lastuser = "CAS_JPA";

	@NotNull
	@Column(name = "LastDate")
	public LocalDateTime lastdate = LocalDateTime.now();

	@NotNull
	@Column(name = "LastAction")
	public int lastaction = 1;

}