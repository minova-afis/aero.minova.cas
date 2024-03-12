package aero.minova.cas.service.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
	public String service;

	@NotNull
	@Size(max = 256)
	@Column(name = "Property", length = 256)
	public String property;

	@NotNull
	@Size(max = 1024)
	@Column(name = "Val", length = 1024)
	public String val;
}