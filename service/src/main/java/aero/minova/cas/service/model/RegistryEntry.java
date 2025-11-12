package aero.minova.cas.service.model;

import java.time.LocalDateTime;

import aero.minova.cas.service.BaseService;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode
@ToString(callSuper = true)
@Table(name = "tRegistry")
public class RegistryEntry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer keyLong;
	
    @Size(max = 1024)
    @Column(name = "KeyText", length = 1024, nullable = false)
    private String keyText;

    @Size(max = 1024)
    @Column(name = "DefaultValue")
    private String defaultValue;

    @Size(max = 1024)
    @Column(name = "\"Value\"")
    private String value;

    @Column(name = "Active", nullable = false)
    private Boolean active;

    @Size(max = 150)
    @Column(name = "LastUser", length = 150)
    private String lastUser = BaseService.getCurrentUser();

    @Column(name = "LastDate")
    private LocalDateTime lastDate;
}
