package aero.minova.cas.service.model;

import java.time.LocalDateTime;

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
	private Integer keyLong;

	@NotNull
	@Size(max = 200)
	@Column(length = 200)
	public String keyText;

	private Integer lastAction = 1;

	@Size(max = 50)
	@Column(length = 50)
	String lastUser = BaseService.getCurrentUser();

	@Column(columnDefinition = "TIMESTAMP")
	public LocalDateTime lastDate = LocalDateTime.now();
}