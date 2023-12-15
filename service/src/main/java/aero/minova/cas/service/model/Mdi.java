package aero.minova.cas.service.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "xtcasMdi")
public class Mdi extends DataEntity {

	@Size(max = 100)
	@Column(name = "Icon", length = 100)
	public String icon;

	@Size(max = 100)
	@Column(name = "Label", length = 100)
	public String label;

	@Size(max = 100)
	@Column(name = "Menu", length = 100)
	public String menu;

	@Column(name = "Position")
	public double position;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "MdiTypeKey", nullable = false)
	public MdiType mdiType;

	@NotNull
	@Size(max = 500)
	@Column(name = "ModulName", length = 500)
	public String modulName;

	@Size(max = 50)
	@Column(name = "SecurityToken", length = 50)
	public String securityToken;

}