package aero.minova.cas.service.model;

import java.sql.Timestamp;
import java.time.Instant;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "xtcasColumnSecurity")
public class ColumnSecurity {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "KeyLong")
	public int keylong;

	@NotNull
	@Size(max = 50)
	@Column(name = "TableName", length = 50)
	public String tablename;

	@NotNull
	@Size(max = 50)
	@Column(name = "ColumnName", length = 50)
	public String columnname;

	@NotNull
	@Size(max = 50)
	@Column(name = "SecurityToken", length = 50)
	public String securitytoken;

	@NotNull
	@Size(max = 50)
	@Column(name = "LastUser", length = 50)
	public String lastuser = "CAS_JPA";

	@NotNull
	@Column(name = "LastDate")
	public Timestamp lastdate = Timestamp.from(Instant.now());

	@NotNull
	@Column(name = "LastAction")
	public int lastaction = 1;

}