package aero.minova.cas.service.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class ExtendedDataEntity extends DataEntity {

<<<<<<< jal-extended_data_entity
	@Column(length = 50)
=======
	@Column(length = 200)
>>>>>>> 8e9d292 ExtendedDataEntity Klasse für Entities, die auch eine Beschreibung haben
	private String description;

}
