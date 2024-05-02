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
@Table(name = "xtcasUsers")
public class Users extends ExtendedDataEntity {

	@NotNull
	@Size(max = 50)
	@Column(name = "Username", length = 50)
	private String username;

	@NotNull
	@Size(max = 100)
	@Column(name = "Password", length = 100)
	private String password;

}
