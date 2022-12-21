package aero.minova.cas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = { "server.http2.enabled=true" })
class HTTP2Test {

	@LocalServerPort
	private int port;

	@Test
	void http2Test() throws IOException, InterruptedException {

		java.net.http.HttpClient client = java.net.http.HttpClient.newBuilder().build();
		java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder().uri(URI.create("http://localhost:" + port + "/ping")).build();
		java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

		assertNotNull(response);
		assertEquals(java.net.http.HttpClient.Version.HTTP_2, response.version());
	}

}
