package aero.minova.cas.app.extension;

import jakarta.annotation.PostConstruct;

import aero.minova.cas.service.model.UserGroup;
import org.springframework.stereotype.Component;

@Component
public class UserGroupExtension extends BaseExtension<UserGroup> {

    @PostConstruct
    void setPrefix() {
        viewPrefix = "xvcas";
        procedurePrefix = "xpcas";
        tablePrefix = "xtcas";
        super.basicSetup();
    }
}
