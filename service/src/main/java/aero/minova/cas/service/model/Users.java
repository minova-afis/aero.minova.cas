package aero.minova.cas.service.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "xtcasUsers")
public class Users extends DataEntity {

	@Size(max = 50)
	@Column(name = "Description", length = 50)
	public String description;
	
	@NotNull
	@Size(max = 50)
	@Column(name = "Username", length = 50)
	public String username;

	@NotNull
	@Size(max = 100)
	@Column(name = "Password", length = 100)
	public String password;

}
