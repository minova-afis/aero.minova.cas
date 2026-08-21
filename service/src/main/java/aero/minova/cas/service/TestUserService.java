package aero.minova.cas.service;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ch.minova.foundation.rest.auth.grants.admin.GroupMembershipService;
import ch.minova.foundation.rest.auth.grants.admin.GroupMembershipService.AddMemberResult;

import aero.minova.cas.service.repository.UsersRepository;

/**
 * Backs {@code POST /test-users} — creates a real, throwaway {@code xtcasUsers} row (database-login mode) with a
 * server-generated random password, optionally assigning the new user to a Grants group in the same call. See
 * CONTEXT.md's "User creation / credential provisioning" section for why this is deliberately scoped narrow (not
 * a general-purpose user-management feature — Keycloak/AD-managed users, forced-reset policy, and a real
 * admin-facing user management UI are all explicitly out of scope here).
 * <p>
 * Guards against a real footgun in {@link AuthorizationService#findOrCreateUser}: that method silently no-ops the
 * password if the username already exists, returning the existing user unchanged. Calling it blind for an
 * existing username would produce a response claiming a freshly-generated password that was never actually
 * stored. This class checks existence itself first and reports {@link CreateResult.UsernameTaken} instead.
 */
@Service
public class TestUserService {

	// Excludes visually-ambiguous characters (0/O, 1/l/I) — this is typed/copy-pasted by a human tester, not
	// just machine-consumed.
	private static final String PASSWORD_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#$%^&*";
	private static final int PASSWORD_LENGTH = 20;

	/**
	 * Every account this service creates is granted this one shared, zero-privilege {@code xtcasUserGroup} /
	 * {@code xtcasAuthorities} authority — required to log in at all under {@code login_dataSource=database} (see
	 * {@link #createTestUser}'s comment on that line), and {@code xtcasAuthorities.Authority} is itself FK-
	 * constrained to an existing {@code xtcasUserGroup.KeyText} row, so an arbitrary/free-form authority string
	 * (e.g. the username itself) doesn't work either — confirmed the hard way via a real
	 * {@code DataIntegrityViolationException} during this feature's own smoke test. One shared group (rather than
	 * a fresh per-user group) avoids cluttering {@code xtcasUserGroup} with one throwaway row per test account,
	 * and is self-documenting to anyone browsing that table later.
	 */
	private static final String TEST_USER_GROUP = "test-users";

	@Autowired
	AuthorizationService authorizationService;

	@Autowired
	UsersRepository usersRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	TestPasswordCipher cipher;

	/**
	 * Only present when {@code foundation.rest.auth.grants.enabled=true} (see GrantsAutoConfiguration) —
	 * required=false so this service still works for the no-group-assignment case on a deployment that hasn't
	 * turned Grants on. (In practice {@code TestUserController} itself is also gated on that same property, since
	 * it needs GrantsChecker to gate the endpoint at all — but this service doesn't need to assume that.)
	 */
	@Autowired(required = false)
	GroupMembershipService groupMembershipService;

	private final SecureRandom random = new SecureRandom();

	public sealed interface CreateResult {
		record Created(String username, String encryptedPassword, GroupAssignmentOutcome groupAssignment) implements CreateResult {
		}

		record UsernameTaken(String username) implements CreateResult {
		}
	}

	public enum GroupAssignmentOutcome {
		/** No `group` was given in the request — nothing to do. */
		NOT_REQUESTED,
		ADDED,
		ALREADY_MEMBER,
		GROUP_NOT_FOUND,
		/** A `group` was given, but foundation.rest.auth.grants.enabled is off, so there's no Grants model to add them to. */
		GRANTS_DISABLED
	}

	public CreateResult createTestUser(String username, String group) {
		if (usersRepository.findByUsername(username).isPresent()) {
			return new CreateResult.UsernameTaken(username);
		}

		String plaintextPassword = generatePassword();
		String hashedPassword = passwordEncoder.encode(plaintextPassword);
		authorizationService.findOrCreateUser(username, hashedPassword);

		// Required for the account to be able to log in at all under login_dataSource=database: Spring's
		// JdbcDaoImpl (which JdbcUserDetailsManager extends) throws UsernameNotFoundException — surfaced to the
		// caller as a plain 401, indistinguishable from a bad password — for any user with zero xtcasAuthorities
		// rows. Root-caused via a real end-to-end login failure during this feature's own smoke test: a user
		// created with no authority at all could never log in, `group` didn't help either since it only writes to
		// foundation.rest.auth's own tNgGroupMembers table, which JdbcUserDetailsManager's
		// authoritiesByUsernameQuery never reads. See TEST_USER_GROUP's own comment for why this has to be a real,
		// pre-existing UserGroup rather than an arbitrary string.
		authorizationService.createOrUpdateUserGroup(TEST_USER_GROUP, "");
		authorizationService.findOrCreateAuthority(username, TEST_USER_GROUP);

		GroupAssignmentOutcome groupAssignment = assignGroupIfRequested(username, group);
		String encryptedPassword = cipher.encrypt(plaintextPassword);
		return new CreateResult.Created(username, encryptedPassword, groupAssignment);
	}

	private GroupAssignmentOutcome assignGroupIfRequested(String username, String group) {
		if (group == null || group.isBlank()) {
			return GroupAssignmentOutcome.NOT_REQUESTED;
		}
		if (groupMembershipService == null) {
			return GroupAssignmentOutcome.GRANTS_DISABLED;
		}
		AddMemberResult result = groupMembershipService.addMember(group, username);
		if (result instanceof AddMemberResult.Added) {
			return GroupAssignmentOutcome.ADDED;
		}
		if (result instanceof AddMemberResult.AlreadyMember) {
			return GroupAssignmentOutcome.ALREADY_MEMBER;
		}
		return GroupAssignmentOutcome.GROUP_NOT_FOUND;
	}

	private String generatePassword() {
		StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
		for (int i = 0; i < PASSWORD_LENGTH; i++) {
			sb.append(PASSWORD_CHARS.charAt(random.nextInt(PASSWORD_CHARS.length())));
		}
		return sb.toString();
	}
}
