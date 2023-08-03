package aero.minova.cas.service.model;

import java.sql.Timestamp;
import java.time.Instant;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class DataEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "KeyLong")
	private int keyLong;

	@NotNull
	@Size(max = 50)
	@Column(name = "KeyText", length = 50)
	public String keyText;

	@Column(name = "LastAction")
	private Integer lastAction = 1;

	@Size(max = 50)
	@Column(name = "LastUser", length = 50)
	String lastUser = "CAS_JPA";

	@Column(name = "LastDate")
	public Timestamp lastDate = Timestamp.from(Instant.now());
}