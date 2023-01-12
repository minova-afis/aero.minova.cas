package aero.minova.cas.service.model;

import java.sql.Timestamp;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Data
@Table(name = "xtcasMdi")
public class Mdi {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "KeyLong")
	public int keylong;

	@Size(max = 100)
	@Column(name = "ID")
	public String id;

	@Size(max = 100)
	@Column(name = "Icon")
	public String icon;

	@Size(max = 100)
	@Column(name = "Label")
	public String label;

	@Size(max = 100)
	@Column(name = "Menu")
	public String menu;

	@Column(name = "Position")
	public double position;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "MdiTypeKey", nullable = false)
	public MdiType mditype;

	@NotNull
	@Size(max = 500)
	@Column(name = "ModulName")
	public String modulname;

	@Size(max = 50)
	@Column(name = "SecurityToken")
	public String securitytoken;

	@NotNull
	@Size(max = 50)
	@Column(name = "LastUser")
	public String lastuser = "CAS_JPA";

	@NotNull
	@Column(name = "LastDate")
	public Timestamp lastdate = Timestamp.from(Instant.now());

	@NotNull
	@Column(name = "LastAction")
	public int lastaction = 1;

}