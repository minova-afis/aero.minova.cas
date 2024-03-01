package aero.minova.cas.service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "xtcasUserPrivilege")
public class UserPrivilege extends ExtendedDataEntity {

	@Size(max = 100)
	@Column(name = "TransactionChecker", length = 100)
	private String transactionchecker;

}