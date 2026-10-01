package aero.minova.cas.app.extension;

import jakarta.annotation.PostConstruct;

import aero.minova.cas.service.model.ServiceProperties;
import org.springframework.stereotype.Component;

@Component
public class ServicePropertiesExtension extends BaseExtension<ServiceProperties> {

    @PostConstruct
    void setPrefix() {
        viewPrefix = "xvcas";
        procedurePrefix = "xpcas";
        tablePrefix = "xtcas";
        super.basicSetup();
    }
}
