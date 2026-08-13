package aero.minova.cas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import ch.minova.foundation.rest.auth.claims.Action;
import ch.minova.foundation.rest.auth.claims.Effect;
import ch.minova.foundation.rest.auth.claims.persistence.ClaimEntity;
import ch.minova.foundation.rest.auth.claims.persistence.ClaimRepository;
import ch.minova.foundation.rest.auth.claims.persistence.GroupEntity;
import ch.minova.foundation.rest.auth.claims.persistence.GroupMemberEntity;
import ch.minova.foundation.rest.auth.claims.persistence.GroupMemberRepository;
import ch.minova.foundation.rest.auth.claims.persistence.GroupRepository;

import aero.minova.cas.CustomLogger;

/**
 * Seeds the bootstrap admin group + claims for the new Groups+Claims model (foundation.rest.auth), mirroring
 * {@link AuthorizationService#createOrUpdateAdminUser}'s bootstrap philosophy for the legacy privilege model:
 * guarantee at least one group always has full access, so a fresh/upgraded deployment can never end up in a
 * state where the Claims model is live but nobody can reach it.
 * <p>
 * {@code @ConditionalOnProperty}-gated the same as the library's own {@code ClaimsAutoConfiguration} — this bean
 * (and therefore its otherwise-required {@link GroupRepository}/{@link ClaimRepository}/
 * {@link GroupMemberRepository} dependencies) simply doesn't exist unless
 * {@code foundation.rest.auth.claims.enabled} is set. {@link AutoSetupService} injects this optionally
 * ({@code @Autowired(required = false)}) and skips it cleanly if it's absent, rather than this class doing its
 * own internal null-checking — the point is that a deployment which hasn't opted into Claims never even
 * constructs this bean, let alone calls it, so there's nothing that could NPE on a missing dependency.
 */
@Service
@ConditionalOnProperty(prefix = "foundation.rest.auth.claims", name = "enabled", havingValue = "true")
public class ClaimsSeedingService {

	/**
	 * Gates {@code ClaimsAdminController}/{@code ClaimsAdminWriteController} in foundation.rest.auth
	 * ({@code ClaimsPaths.ADMIN} there) — duplicated here as a literal because that constant is package-private
	 * in the library, not public. TODO: make {@code ClaimsPaths.ADMIN} public in a foundation.rest.auth
	 * fast-follow and reference it directly instead of duplicating the string — not done as part of this change.
	 */
	private static final String CLAIMS_ADMIN_PATH = "/claims/admin";

	private static final String ADMIN_GROUP = "admin";

	/** Matches the hardcoded username {@code SqlProcedureController#setupDefaultAdminUser()} seeds into xtcasUsers. */
	private static final String ADMIN_USERNAME = "admin";

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	ClaimRepository claimRepository;

	@Autowired
	GroupMemberRepository memberRepository;

	@Autowired
	protected CustomLogger logger;

	/** Idempotent — safe to call on every boot, same as {@code createOrUpdateAdminUser}. */
	public void seedAdminClaims() {
		GroupEntity adminGroup = findOrCreateAdminGroup();
		findOrCreateFullAccessClaim(adminGroup, "/form/*");
		findOrCreateFullAccessClaim(adminGroup, CLAIMS_ADMIN_PATH);
		findOrCreateAdminMembership(adminGroup);
	}

	private GroupEntity findOrCreateAdminGroup() {
		return groupRepository.findByKeyText(ADMIN_GROUP).orElseGet(() -> {
			GroupEntity group = new GroupEntity();
			group.setKeyText(ADMIN_GROUP);
			group.setDescription("Bootstrap admin group, seeded at CAS setup - full access.");
			logger.logSetup("Seeding bootstrap admin group '" + ADMIN_GROUP + "' for the Groups+Claims model");
			return groupRepository.save(group);
		});
	}

	/**
	 * Grants {@code (admin, pathPattern, ALL, ALLOW)} unless an identical row already exists — checked by hand
	 * rather than relying on a DB-level unique constraint, since {@code tNgGrants} deliberately doesn't have one
	 * (a redundant-but-harmless duplicate is a soft warning at the application layer via {@code ClaimValidator},
	 * not something the schema itself blocks).
	 */
	private void findOrCreateFullAccessClaim(GroupEntity group, String pathPattern) {
		boolean exists = claimRepository.findByGroup_KeyText(ADMIN_GROUP).stream()
				.anyMatch(claim -> pathPattern.equals(claim.getPathPattern()) && claim.getAction() == Action.ALL
						&& claim.getEffect() == Effect.ALLOW);
		if (exists) {
			return;
		}
		ClaimEntity claim = new ClaimEntity();
		claim.setGroup(group);
		claim.setPathPattern(pathPattern);
		claim.setAction(Action.ALL);
		claim.setEffect(Effect.ALLOW);
		claimRepository.save(claim);
		logger.logSetup("Seeded admin claim: " + pathPattern + " ALL ALLOW");
	}

	/**
	 * Links the same {@code "admin"} username {@code SqlProcedureController#setupDefaultAdminUser()} seeds into
	 * {@code xtcasUsers} to the new admin group — once login-time bridging exists (still open, see #1497), this
	 * is what makes the seeded admin *user* actually resolve as a member of the seeded admin *group*, not just
	 * the group existing in isolation with nobody in it.
	 */
	private void findOrCreateAdminMembership(GroupEntity group) {
		if (memberRepository.findByGroup_KeyTextAndUsername(ADMIN_GROUP, ADMIN_USERNAME).isPresent()) {
			return;
		}
		GroupMemberEntity membership = new GroupMemberEntity();
		membership.setGroup(group);
		membership.setUsername(ADMIN_USERNAME);
		memberRepository.save(membership);
		logger.logSetup("Added '" + ADMIN_USERNAME + "' as a member of the seeded admin group");
	}
}
