package aero.minova.cas.app.extension;

import jakarta.annotation.PostConstruct;

import aero.minova.cas.service.model.ColumnSecurity;
import org.springframework.stereotype.Component;

@Component
public class ColumnSecurityExtension extends BaseExtension<ColumnSecurity> {

    @PostConstruct
    void setPrefix() {
        viewPrefix = "xvcas";
        procedurePrefix = "xpcas";
        tablePrefix = "xtcas";
        super.basicSetup();
    }
}
