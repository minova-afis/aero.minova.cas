package aero.minova.cas.service.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "xtcasUserPrivilege")
public class UserPrivilege extends DataEntity {

	@Size(max = 100)
	@Column(name = "TransactionChecker")
	public String transactionchecker;

}