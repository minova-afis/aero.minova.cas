package aero.minova.cas.service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "xtcasServiceMessageReceiverLoginType")
public class ServiceMessageReceiverLoginType extends DataEntity {}
