package aero.minova.cas.service.model;

import javax.validation.constraints.NotNull;
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
@Table(name = "xtcasProcedureNewsfeed")
public class ProcedureNewsfeed extends DataEntity {

	@NotNull
	@Size(max = 50)
	@Column(name = "Topic", length = 50)
	public String topic;
}