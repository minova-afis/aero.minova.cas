package aero.minova.cas.service;

import static org.junit.Assert.assertTrue;

import aero.minova.cas.CoreApplicationSystemApplication;
import aero.minova.cas.service.repository.ServiceMessageReceiverLoginTypeRepository;
import aero.minova.cas.servicenotifier.ServiceNotifierService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// benötigt, damit JUnit-Tests nicht abbrechen
@SpringBootTest(
        classes = CoreApplicationSystemApplication.class,
        properties = {"application.runner.enabled=false"})
class ServiceNotifierServiceTest {

    @Autowired
    ServiceNotifierService serviceNotifierService;

    @Autowired
    ServiceMessageReceiverLoginTypeRepository serviceMessageReceiverLoginTypeRepository;

    @Test
    void assureLoginTypesAreCreated() {
        assertTrue(!serviceMessageReceiverLoginTypeRepository
                .findByKeyTextAndLastActionGreaterThan("None", 0)
                .isEmpty());
        assertTrue(!serviceMessageReceiverLoginTypeRepository
                .findByKeyTextAndLastActionGreaterThan("BasicAuth", 0)
                .isEmpty());
        assertTrue(!serviceMessageReceiverLoginTypeRepository
                .findByKeyTextAndLastActionGreaterThan("OAuth2", 0)
                .isEmpty());
    }
}
