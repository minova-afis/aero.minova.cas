package aero.minova.cas.controller;

import jakarta.servlet.ServletException;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

/**
 * Serves the embedded React UI JAR (ch.minova:gui.react) when it is present on
 * the classpath.  The JAR places its build output under {@code static/ui/} so
 * Spring's resource handler serves actual files (.js, .css, …) directly with
 * correct MIME types.  Non-file paths (SPA client-side routes) fall back to
 * {@code index.html} so React Router can take over.
 *
 * Also registers a Tomcat engine valve that redirects bare {@code /} (before
 * the servlet context path) to the React UI, so accessing the server root URL
 * directly brings up the UI without knowing the context path.
 *
 * When the GUI JAR is not on the classpath all mappings return 404.
 */
@Controller
@Configuration
public class EmbeddedUIController implements WebMvcConfigurer {

    private static final String INDEX_HTML = "static/ui/index.html";

    @Value("${server.servlet.context-path:/cas}")
    private String contextPath;

    /** Redirect bare /ui → /ui/ so relative asset paths in index.html work. */
    @GetMapping("/ui")
    public String redirectToUI() {
        return "redirect:/ui/";
    }

    /**
     * Explicitly serve index.html for the root UI path.
     * The resource handler below covers /ui/** but its empty-path matching is
     * unreliable, so we handle /ui/ explicitly here.
     */
    @GetMapping("/ui/")
    public ResponseEntity<Resource> serveUIRoot() {
        return serveIndex();
    }

    /**
     * Custom resource handler for /ui/**.
     * <ul>
     *   <li>Existing files (js, css, svg, …) are served directly — Spring sets
     *       the correct Content-Type via the file extension.</li>
     *   <li>Paths without a matching file (SPA routes like /ui/dashboard) fall
     *       back to index.html so React Router handles navigation.</li>
     *   <li>If the GUI JAR is absent (index.html not on classpath) null is
     *       returned, causing a 404.</li>
     * </ul>
     * Note: /ui/ itself is handled by the explicit @GetMapping above; this
     * handler covers deep paths like /ui/dashboard and /ui/assets/index.js.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/ui/**")
                .addResourceLocations("classpath:/static/ui/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource resource = super.getResource(resourcePath, location);
                        if (resource != null && resource.exists() && resource.isReadable()) {
                            return resource; // actual file: js, css, svg, …
                        }
                        // SPA fallback: serve index.html for client-side routes
                        return serveIndex().getBody();
                    }
                });
    }

    /**
     * Tomcat engine valve: redirects bare {@code /} to the React UI when the
     * servlet context-path is not {@code /}.  This handles the convenience case
     * where the app is deployed at e.g. {@code /cas} and the user navigates to
     * the bare server root — without the valve Tomcat would return 404 because
     * no context is registered at {@code /}.
     *
     * <p>When {@code context-path=/} the valve is a no-op: the
     * {@code FrontPageController} already handles {@code GET /} inside the
     * servlet context and a Tomcat-level redirect is neither needed nor reliable
     * (some Tomcat versions strip trailing slashes from {@code sendRedirect}
     * locations, turning {@code /ui/} into {@code /ui}).
     */
    @Bean
    @ConditionalOnClass(TomcatServletWebServerFactory.class)
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> rootRedirectCustomizer() {
        if ("/".equals(contextPath)) {
            // FrontPageController handles GET / — no engine-level valve needed.
            return factory -> {};
        }
        String uiPath = contextPath + "/ui/";
        return factory -> factory.addEngineValves(new ValveBase() {
            @Override
            public void invoke(Request request, Response response) throws IOException, ServletException {
                if ("/".equals(request.getRequestURI())) {
                    response.sendRedirect(uiPath);
                } else {
                    getNext().invoke(request, response);
                }
            }
        });
    }

    private ResponseEntity<Resource> serveIndex() {
        Resource index = new ClassPathResource(INDEX_HTML);
        if (!index.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(index);
    }
}
