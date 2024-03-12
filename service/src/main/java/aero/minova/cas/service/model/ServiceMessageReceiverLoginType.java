package aero.minova.cas.service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "xtcasServiceMessageReceiverLoginType")
public class ServiceMessageReceiverLoginType extends DataEntity {

}
