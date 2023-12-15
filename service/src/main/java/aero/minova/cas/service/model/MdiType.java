package aero.minova.cas.service.model;

import javax.validation.constraints.Size;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "xtcasMdiType")
public class MdiType extends DataEntity {

	@Size(max = 50)
	@Column(name = "Description", length = 50)
	public String description;

}