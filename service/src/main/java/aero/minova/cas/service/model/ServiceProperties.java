package aero.minova.cas.service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "xtcasServiceProperties")
public class ServiceProperties extends DataEntity {

	@NotNull
	@Size(max = 128)
	@Column(name = "Service", length = 128)
	private String service;

	@NotNull
	@Size(max = 256)
	@Column(name = "Property", length = 256)
	private String property;

	@NotNull
	@Size(max = 1024)
	@Column(name = "Val", length = 1024)
	private String val;
}