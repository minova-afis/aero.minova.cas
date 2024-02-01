package aero.minova.cas;

import aero.minova.cas.api.restapi.ClientRestAPI;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(classes = { CustomLogger.class, ClientRestAPI.class, Gson.class }, properties = { "PROPERY_SAMPLE1=/opt/hostedtoolcache/CodeQL/2.16.0/x64/codeql/tools/linux64/${LIB}_${PLATFORM}_trace.so"})
@ExtendWith(OutputCaptureExtension.class)
class CustomLoggerTest {
	@Test
	void handleContextRefreshWithUnresolvableVariablesTest(CapturedOutput output) {
		assertThat(output)
				.contains("Property: PROPERY_SAMPLE1 not resolvable");
	}
}
