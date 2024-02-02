package aero.minova.cas.service.model;

import java.sql.Timestamp;
import java.time.Instant;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import aero.minova.cas.service.BaseService;
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
	private Integer keyLong;

	@NotNull
	@Size(max = 200)
	@Column(name = "KeyText", length = 200)
	public String keyText;

	@Column(name = "LastAction")
	private Integer lastAction = 1;

	@Size(max = 50)
	@Column(name = "LastUser", length = 50)
	String lastUser = BaseService.getCurrentUser();

	@Column(name = "LastDate")
	public Timestamp lastDate = Timestamp.from(Instant.now());
}