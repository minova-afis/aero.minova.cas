package aero.minova.cas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ch.minova.foundation.rest.auth.grants.Action;
import ch.minova.foundation.rest.auth.grants.check.GrantsChecker;

import aero.minova.cas.service.SecurityService;
import aero.minova.cas.service.TestPasswordCipher;
import aero.minova.cas.service.TestUserService;
import aero.minova.cas.service.TestUserService.CreateResult;

/**
 * Test-only user provisioning: creates a real {@code xtcasUsers} row (database-login mode) with a random
 * password, optionally assigning the new user to a Grants group in the same call. NOT a general-purpose
 * user-management API — see CONTEXT.md's "User creation / credential provisioning" section for the open
 * questions this deliberately sidesteps (Keycloak/AD-managed users, forced-password-reset policy, and a real
 * admin-facing user management UI are all out of scope here).
 * <p>
 * Gated the same way as the rest of the admin Grants CRUD surface — the {@code /grants/admin} resource path,
 * same {@link GrantsChecker#isAllowed} check {@code GrantsAdminController}/{@code GrantsAdminWriteController} use
 * — deliberately no separate opt-in property, per explicit decision this session. This controller only exists at
 * all when {@code foundation.rest.auth.grants.enabled=true}, since that's what makes {@link GrantsChecker} (the
 * thing that gates it) available in the first place — without it there'd be no way to gate this at all, so it's
 * conditioned the same way rather than risk booting ungated.
 * <p>
 * The generated password is never returned in plaintext — see {@link TestPasswordCipher}'s own header comment
 * for why and how; decrypt it via {@code POST /test-users/decrypt-password}. Returns {@link ResponseEntity}
 * directly rather than throwing on the forbidden case, same reason as {@link PermissionsController}:
 * {@link aero.minova.cas.ControllerExceptionHandler}'s catch-all would downgrade a thrown
 * {@code ResponseStatusException} to 500.
 * <p>
 * TODO: duplicates the {@code "/grants/admin"} literal because {@code GrantsPaths.ADMIN} is package-private in
 * foundation.rest.auth — same already-tracked gap as {@code GrantsDiscoveryService.ADMIN_RESOURCE_PATH}.
 */
@RestController
@ConditionalOnProperty(prefix = "foundation.rest.auth.grants", name = "enabled", havingValue = "true")
public class TestUserController {

	private static final String ADMIN_RESOURCE_PATH = "/grants/admin";

	@Autowired
	TestUserService testUserService;

	@Autowired
	TestPasswordCipher cipher;

	@Autowired
	GrantsChecker grantsChecker;

	@Autowired
	SecurityService securityService;

	public record CreateTestUserRequest(String username, String group) {
	}

	public record CreateTestUserResponse(String username, String encryptedPassword, String groupAssignment) {
	}

	public record DecryptPasswordRequest(String encryptedPassword) {
	}

	public record DecryptPasswordResponse(String password) {
	}

	public record ErrorResponse(String message) {
	}

	/** @param request `group` is optional — omit or leave blank to create the user without a Grants group assignment. */
	@PostMapping(value = "/test-users", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createTestUser(@RequestBody CreateTestUserRequest request) {
		if (!isAllowed(Action.WRITE)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		if (request == null || request.username() == null || request.username().isBlank()) {
			return ResponseEntity.badRequest().body(new ErrorResponse("username is required"));
		}

		CreateResult result = testUserService.createTestUser(request.username(), request.group());
		if (result instanceof CreateResult.UsernameTaken taken) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Username '" + taken.username() + "' already exists"));
		}
		CreateResult.Created created = (CreateResult.Created) result;
		return ResponseEntity
				.ok(new CreateTestUserResponse(created.username(), created.encryptedPassword(), created.groupAssignment().name()));
	}

	@PostMapping(value = "/test-users/decrypt-password", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> decryptPassword(@RequestBody DecryptPasswordRequest request) {
		if (!isAllowed(Action.READ)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		if (request == null || request.encryptedPassword() == null) {
			return ResponseEntity.badRequest().body(new ErrorResponse("encryptedPassword is required"));
		}
		try {
			return ResponseEntity.ok(new DecryptPasswordResponse(cipher.decrypt(request.encryptedPassword())));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
		} catch (IllegalStateException e) {
			// ng.api.testUserPasswordSecret not configured — a deployment problem, not a caller error.
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
		}
	}

	private boolean isAllowed(Action action) {
		// Defensive, same reasoning as PermissionsController#isCurrentUserAdmin — make sure this request's
		// Authentication actually has its Grants group memberships resolved, don't rely on POST /loadPrivileges
		// having already run this session.
		securityService.loadAllPrivileges();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null && grantsChecker.isAllowed(authentication, ADMIN_RESOURCE_PATH, action);
	}
}
