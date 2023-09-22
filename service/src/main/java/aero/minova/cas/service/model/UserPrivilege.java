package aero.minova.cas.service.model;

import javax.validation.constraints.Size;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "xtcasUserPrivilege")
public class UserPrivilege extends DataEntity {

	@Size(max = 50)
	@Column(name = "Description", length = 50)
	public String description;

	@Size(max = 100)
	@Column(name = "TransactionChecker", length = 100)
	public String transactionchecker;

}