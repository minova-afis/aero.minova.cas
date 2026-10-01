package aero.minova.cas.app.extension;

import jakarta.annotation.PostConstruct;

import aero.minova.cas.service.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserExtension extends BaseExtension<User> {

    @PostConstruct
    void setPrefix() {
        viewPrefix = "xvcas";
        procedurePrefix = "xpcas";
        tablePrefix = "xtcas";
        super.basicSetup();
    }
}
