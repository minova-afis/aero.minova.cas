package aero.minova.cas.service.model;

import java.time.LocalDateTime;

import aero.minova.cas.service.BaseService;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode
@ToString(callSuper = true)
@Table(name = "tFile")
public class DBFile {
	@Id
    @Size(max = 1024)
    @Column(name = "KeyText", length = 1024, nullable = false)
    private String keyText;

    @Lob
    @Column(name = "DefaultValue")
    private byte[] defaultValue;

    @Column(name = "DefaultValueCRC")
    private Integer defaultValueCRC;
    
    @Column(name = "DefaultValueMD5", length = 16)
    private byte[] defaultValueMD5;

    @Lob
    @Column(name = "\"Value\"")
    private byte[] value;

    @Column(name = "ValueCRC")
    private Integer valueCRC;
    
    @Column(name = "ValueMD5", length = 16)
    private byte[] valueMD5;

    @Column(name = "Active", nullable = false)
    private Boolean active;

    @Size(max = 150)
    @Column(name = "LastUser", length = 150)
    private String lastUser = BaseService.getCurrentUser();

    @Column(name = "LastDate")
    private LocalDateTime lastDate;
}
