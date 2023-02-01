package aero.minova.cas.service.model;

import java.sql.Timestamp;
import java.time.Instant;

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
	private Long keyLong;

	@Column(name = "KeyText")
	private String keyText;

	@Column(name = "LastAction")
	private Integer lastAction = 1;

	@Size(max = 50)
	@Column(name = "LastUser")
	String lastUser = "CAS_JPA";

	@Column(name = "LastDate")
	public Timestamp lastdate = Timestamp.from(Instant.now());

}