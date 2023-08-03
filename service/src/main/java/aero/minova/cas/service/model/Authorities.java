package aero.minova.cas.service.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "xtcasAuthorities")
@AllArgsConstructor
@NoArgsConstructor
public class Authorities extends DataEntity {

	@NotNull
	@Size(max = 50)
	@Column(name = "Username", length = 50)
	public String username;

	@NotNull
	@Size(max = 50)
	@Column(name = "Authority", length = 50)
	public String authority;
}