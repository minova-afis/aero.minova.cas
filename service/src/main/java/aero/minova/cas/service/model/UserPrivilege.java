package aero.minova.cas.service.model;

import javax.validation.constraints.Size;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
	public String transactionchecker;

}