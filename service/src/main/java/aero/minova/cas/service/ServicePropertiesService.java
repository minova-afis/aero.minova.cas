package aero.minova.cas.service;

import jakarta.annotation.PostConstruct;

import aero.minova.cas.service.model.ServiceProperties;
import org.springframework.stereotype.Service;

@Service
public class ServicePropertiesService extends BaseService<ServiceProperties> {
    @PostConstruct
    public void setup() {
        allowDuplicateMatchcodes = true;
    }
}
