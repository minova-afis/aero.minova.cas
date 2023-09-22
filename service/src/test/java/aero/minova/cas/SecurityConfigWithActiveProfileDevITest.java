package aero.minova.cas;

import aero.minova.cas.api.restapi.ClientRestAPI;
import aero.minova.cas.sql.SystemDatabase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest(
		classes = {
				SecurityConfig.class, CustomLogger.class, ClientRestAPI.class, SystemDatabase.class,
				SecurityConfigITest.TestApplication.class },
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class SecurityConfigITest {
	@Autowired
	private WebApplicationContext wac;

	public MockMvc mockMvc;

	@BeforeEach
	void setup() {
		DefaultMockMvcBuilder builder = MockMvcBuilders
				.webAppContextSetup(wac)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.dispatchOptions(true);
		this.mockMvc = builder.build();
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void corsEnabledForHttpTest() throws Exception {
		this.mockMvc
				.perform(options("/test-cors")
						.header("Access-Control-Request-Method", "GET")
						.header("Origin", "http://localhost:8100")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(header().string("Access-Control-Allow-Methods", "GET"))
				.andExpect(content().string(""));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void corsEnabledForHttpsTest() throws Exception {
		this.mockMvc
				.perform(options("/test-cors")
						.header("Access-Control-Request-Method", "GET")
						.header("Origin", "https://localhost:8100")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(header().string("Access-Control-Allow-Methods", "GET"))
				.andExpect(content().string(""));
	}

	@SpringBootApplication
	@Controller
	static class TestApplication {

		public static void main(String[] args) {
			SpringApplication.run(TestApplication.class, args);
		}
	}
}
