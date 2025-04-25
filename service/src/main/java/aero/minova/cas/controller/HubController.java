package aero.minova.cas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.cas.CustomLogger;
import ch.minova.assist.net.client.HubClient;
import jakarta.annotation.PostConstruct;


@RestController
public class HubController {
	@Autowired
	CustomLogger customLogger;
	
	@Autowired
	private Environment env;
	
	@PostConstruct
	private void init() {
		String hubHost = env.getProperty("minova.hub.host");
		String clientName = env.getProperty("minova.hub.client.name");
		String appName = env.getProperty("spring.application.name");
		
		if(hubHost == null)
			return;
		// If no client name is set, try application name instead
		if(clientName == null)
			clientName = (appName == null ? "CAS" : appName.replace(" ", "-"));
		
		try {
			customLogger.logInfo("Connect to Hub " + hubHost + " as " + clientName + "...");
			ch.minova.assist.data.Environment.get().setInstanceName(clientName);
			HubClient hc = ch.minova.assist.data.Environment.get().getDeployment().getHubClient();
			if(hubHost.contains(":")) {
				hc.setPort(Integer.parseInt(hubHost.substring(hubHost.indexOf(":") + 1)));
				hc.setHost(hubHost.substring(0, hubHost.indexOf(":")));
			} else {
				hc.setHost(hubHost);
			}
			hc.start();
			customLogger.logInfo("Hub client started");
		} catch(Exception ex) {
			customLogger.logError("Failed to build-up Hub connection", ex);
		}
	}
}
