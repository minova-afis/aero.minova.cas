package aero.minova.cas.service.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name = "xtcasError")
public class Error {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "KeyLong")
	private int keylong;

	@NotNull
	@Size(max = 50)
	@Column(name = "Username", length = 50)
	private String username;

	@NotNull
	@Size(max = 250)
	@Column(name = "ErrorMessage", length = 250)
	private String errormessage;

	@NotNull
	@Column(name = "Date", columnDefinition = "TIMESTAMP")
	private LocalDateTime lastdate = LocalDateTime.now();

}