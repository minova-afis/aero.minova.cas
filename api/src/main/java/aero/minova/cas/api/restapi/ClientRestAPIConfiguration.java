package aero.minova.cas.api.restapi;

import aero.minova.cas.api.domain.Value;
import aero.minova.cas.api.domain.ValueDeserializer;
import aero.minova.cas.api.domain.ValueSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientRestAPIConfiguration {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		// Alle Konfigurationen für das RestTemplate hier einfügen.
		return builder.build();
	}

	@Bean
	public Gson myGson() {
		return new GsonBuilder() //
				.registerTypeAdapter(Value.class, new ValueSerializer()) //
				.registerTypeAdapter(Value.class, new ValueDeserializer()) //
				.create();
	}
}