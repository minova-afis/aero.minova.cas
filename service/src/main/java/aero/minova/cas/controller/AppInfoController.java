package aero.minova.cas.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes static application metadata consumed by the NextGen UI on login.
 *
 * <p>The UI uses this to determine the internal application name (used as a
 * path prefix when persisting user settings), whether server-side settings
 * persistence is supported, and general backend type/version information.</p>
 *
 * <p>Example response:
 * <pre>
 * {
 *   "name": "afis",
 *   "saveSupport": true,
 *   "backend": { "type": "CAS", "version": "1.2.3" }
 * }
 * </pre>
 * </p>
 */
@RestController
public class AppInfoController {

    /** Internal application identifier, e.g. {@code afis} or {@code tta}. */
    @Value("${application:}")
    private String application;

    /** Application version, defaults to {@code 1.0.0} if not configured. */
    @Value("${application.version:1.0.0}")
    private String applicationVersion;

    @GetMapping(value = "/appinfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> appInfo() {
        return Map.of(
            "name",        application,
            "saveSupport", true,
            "backend",     Map.of(
                "type",    "CAS",
                "version", applicationVersion
            )
        );
    }
}
