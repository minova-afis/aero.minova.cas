package aero.minova.cas.service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "xtcasMdiType")
public class MdiType extends DataEntity {

}