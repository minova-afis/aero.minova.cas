package aero.minova.cas.service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "xtcasMdi")
public class Mdi extends DataEntity {

	@Size(max = 100)
	@Column(name = "Icon", length = 100)
	private String icon;

	@Size(max = 100)
	@Column(name = "Label", length = 100)
	private String label;

	@Size(max = 100)
	@Column(name = "Menu", length = 100)
	private String menu;

	@Column(name = "Position")
	private double position;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "MdiTypeKey", nullable = false)
	private MdiType mdiType;

	@NotNull
	@Size(max = 500)
	@Column(name = "ModulName", length = 500)
	private String modulName;

	@Size(max = 50)
	@Column(name = "SecurityToken", length = 50)
	private String securityToken;

}