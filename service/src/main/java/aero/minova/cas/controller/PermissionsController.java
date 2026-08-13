package aero.minova.cas.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.cas.service.PermissionsService;
import aero.minova.cas.service.PermissionsService.TreeResponse;
import aero.minova.cas.service.SecurityService;

/**
 * Read-only "Access Rights" permissions tree, backing web.ui's Access Rights view (minova-afis/web.ui#129).
 * See GH-1495 for the full design rationale -- in short, this reports CAS's own live authorization model
 * (xtcasUserPrivilege/xtcasUserGroup/xtcasLuUserPrivilegeUserGroup), not SQL Server-native roles.
 *
 * <p>
 * Admin-only: gated on the caller's resolved authorities containing "admin", the same group
 * {@link aero.minova.cas.service.AuthorizationService#createOrUpdateAdminUser} sets up.
 * </p>
 *
 * <p>
 * Note: returns {@link ResponseEntity} directly rather than throwing on the forbidden case --
 * {@link aero.minova.cas.ControllerExceptionHandler} has a catch-all {@code @ExceptionHandler(RuntimeException.class)}
 * that forces every uncaught RuntimeException (including {@code ResponseStatusException}) to HTTP 500, which would
 * silently swallow an intended 403.
 * </p>
 */
@RestController
public class PermissionsController {

	private static final Set<String> VALID_DEPTHS = Set.of("category", "category+form", "full");

	@Autowired
	PermissionsService permissionsService;

	@Autowired
	SecurityService securityService;

	/**
	 * @param depth
	 *            "category", "category+form" (default), or "full". "full" additionally requires item-level detail
	 *            (same admin gate as the endpoint itself -- there is no separate, lesser gate for it).
	 */
	@GetMapping(value = "/permissions/tree", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TreeResponse> getTree(@RequestParam(defaultValue = "category+form") String depth) {
		if (!isCurrentUserAdmin()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		String effectiveDepth = VALID_DEPTHS.contains(depth) ? depth : "category+form";
		return ResponseEntity.ok(permissionsService.getTree(effectiveDepth));
	}

	private boolean isCurrentUserAdmin() {
		// Defensive, same as SecurityService.getPrivilegePermissions -- the client normally already triggers
		// POST /loadPrivileges after login, but this endpoint shouldn't rely on that having happened.
		securityService.loadAllPrivileges();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null
				&& authentication.getAuthorities().stream().anyMatch(authority -> "admin".equalsIgnoreCase(authority.getAuthority()));
	}
}
