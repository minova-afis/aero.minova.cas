package aero.minova.cas.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import ch.minova.foundation.rest.auth.grants.Action;
import ch.minova.foundation.rest.auth.grants.persistence.GrantEntity;
import ch.minova.foundation.rest.auth.grants.persistence.GrantRepository;
import ch.minova.foundation.rest.auth.grants.persistence.GroupEntity;
import ch.minova.foundation.rest.auth.grants.persistence.GroupGrantEntity;
import ch.minova.foundation.rest.auth.grants.persistence.GroupGrantRepository;
import ch.minova.foundation.rest.auth.grants.persistence.GroupMemberEntity;
import ch.minova.foundation.rest.auth.grants.persistence.GroupMemberRepository;
import ch.minova.foundation.rest.auth.grants.persistence.GroupRepository;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;

/**
 * Bootstraps the admin group + membership for the Groups+Grants model (foundation.rest.auth), mirroring
 * {@link AuthorizationService#createOrUpdateAdminUser}'s bootstrap philosophy for the legacy privilege model —
 * and, since the Claims-&gt;Grants pivot, also <strong>discovers</strong> every currently-known form from
 * {@code xtcasMdi} and makes sure the admin group has a grant for every {@code (form, action)} combination that
 * exists right now.
 * <p>
 * Supersedes the Claims-era {@code ClaimsSeedingService}, which only ever wrote two static wildcard rows
 * ({@code /form/*}, {@code /claims/admin}) once. The Grants model has no wildcards — every combination has to be
 * its own literal, pre-populated row — so this class's job grew from "seed two rows" to "discover the real
 * resource universe and keep admin's grants in sync with it."
 * <p>
 * <strong>Must run on every boot, not just first-time setup</strong> — see CONTEXT.md's "Architecture pivot"
 * section for why: a brand-new form needs to become visible to admin the very next boot after it's added, not
 * stay invisible until someone remembers to reseed. {@link AutoSetupService#autoSetup()} calls
 * {@link #discoverAndSeedAdmin()} unconditionally on every pass now (not just when {@code executeSetup()} itself
 * runs) — see that method's javadoc.
 * <p>
 * {@code @ConditionalOnProperty}-gated the same as the library's own {@code GrantsAutoConfiguration} — this bean
 * (and therefore its otherwise-required repository dependencies) simply doesn't exist unless
 * {@code foundation.rest.auth.grants.enabled} is set. {@link AutoSetupService} injects this optionally
 * ({@code @Autowired(required = false)}) and skips it cleanly if it's absent.
 */
@Service
@ConditionalOnProperty(prefix = "foundation.rest.auth.grants", name = "enabled", havingValue = "true")
public class GrantsDiscoveryService {

	private static final String ADMIN_GROUP = "admin";

	/** Matches the hardcoded username {@code SqlProcedureController#setupDefaultAdminUser()} seeds into xtcasUsers. */
	private static final String ADMIN_USERNAME = "admin";

	/**
	 * Gates {@code GrantsAdminController}/{@code GrantsAdminWriteController}/the group-member controllers in
	 * foundation.rest.auth ({@code GrantsPaths.ADMIN} there) — duplicated here as a literal because that constant
	 * is package-private in the library, not public. TODO: make {@code GrantsPaths.ADMIN} public in a
	 * foundation.rest.auth fast-follow and reference it directly instead of duplicating the string.
	 */
	private static final String ADMIN_RESOURCE_PATH = "/grants/admin";

	/** No {@code ALL} action in the Grants model — every grant is one of these 4 concrete literals. */
	private static final List<Action> ALL_ACTIONS = List.of(Action.READ, Action.WRITE, Action.UPDATE, Action.DELETE);

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	GrantRepository grantRepository;

	@Autowired
	GroupGrantRepository groupGrantRepository;

	@Autowired
	GroupMemberRepository memberRepository;

	@Autowired
	ViewService viewService;

	@Autowired
	protected CustomLogger logger;

	/** Internal representation of one xtcasMdi row — either a menu (category) or a form entry. Mirrors PermissionsService's MdiRow. */
	private record MdiRow(String keyText, String parentMenu, boolean isForm) {
	}

	/**
	 * Idempotent and additive-only — safe, and required, to call on every boot. Seeds the admin group + membership
	 * (unchanged from the Claims-era behavior), then discovers every current form from {@code xtcasMdi}, ensures
	 * a {@code tNgGrants} row exists for every {@code (form, action)} combination (plus the fixed admin-management
	 * paths), and ensures the admin group is assigned every one of those grants. Never deletes a grant or
	 * assignment — see CONTEXT.md's "Architecture pivot" section for why additive-only was the deliberate choice.
	 */
	public void discoverAndSeedAdmin() {
		GroupEntity adminGroup = findOrCreateAdminGroup();
		findOrCreateAdminMembership(adminGroup);

		List<String> resourcePaths = new ArrayList<>(discoverFormResourcePaths());
		resourcePaths.add(ADMIN_RESOURCE_PATH);

		int newGrants = 0;
		for (String resourcePath : resourcePaths) {
			for (Action action : ALL_ACTIONS) {
				GrantEntity grant = findOrCreateGrant(resourcePath, action);
				if (assignToAdminIfMissing(adminGroup, grant)) {
					newGrants++;
				}
			}
		}
		if (newGrants > 0) {
			logger.logSetup("Grants discovery: assigned " + newGrants + " new grant(s) to '" + ADMIN_GROUP + "'");
		}
	}

	// ─── Admin group / membership (unchanged from the Claims-era seeding) ──────

	private GroupEntity findOrCreateAdminGroup() {
		return groupRepository.findByKeyText(ADMIN_GROUP).orElseGet(() -> {
			GroupEntity group = new GroupEntity();
			group.setKeyText(ADMIN_GROUP);
			group.setDescription("Bootstrap admin group, seeded at CAS setup - full access.");
			logger.logSetup("Seeding bootstrap admin group '" + ADMIN_GROUP + "' for the Groups+Grants model");
			return groupRepository.save(group);
		});
	}

	/**
	 * Links the same {@code "admin"} username {@code SqlProcedureController#setupDefaultAdminUser()} seeds into
	 * {@code xtcasUsers} to the new admin group — this is what makes the seeded admin *user* actually resolve as
	 * a member of the seeded admin *group* via {@code SecurityService.loadClaimsGroupTokens(...)}'s login-time
	 * bridging (see #1497).
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

	// ─── Grant vocabulary + admin assignment ────────────────────────────────────

	private GrantEntity findOrCreateGrant(String resourcePath, Action action) {
		String path = resourcePath + "/" + action.name();
		return grantRepository.findByPath(path).orElseGet(() -> {
			GrantEntity grant = new GrantEntity();
			grant.setPath(path);
			return grantRepository.save(grant);
		});
	}

	/** @return {@code true} if a new assignment was created (this grant is new to admin), {@code false} if it already had it. */
	private boolean assignToAdminIfMissing(GroupEntity adminGroup, GrantEntity grant) {
		if (groupGrantRepository.findByGroup_KeyTextAndGrant_Path(ADMIN_GROUP, grant.getPath()).isPresent()) {
			return false;
		}
		GroupGrantEntity assignment = new GroupGrantEntity();
		assignment.setGroup(adminGroup);
		assignment.setGrant(grant);
		groupGrantRepository.save(assignment);
		return true;
	}

	// ─── MDI walk (Category/Form discovery) ─────────────────────────────────────
	// Mirrors PermissionsService#loadMdiRows()/#resolveTopLevelMenu() — that class's item/privilege-matching half
	// isn't reused here, since the Grants model's granularity stops at Form+Action, it never reaches procedures.

	/**
	 * @return {@code /form/{category}/{formName}} for every current top-level-menu-attached form. Nested path
	 *         convention decided this session — matches the worked examples the Grants model schema was designed
	 *         around; NOT the flat {@code /form/{formName}} convention {@code web.ui.common}'s self-service
	 *         {@code claimsPathForTileAction} currently uses. That's a known, accepted follow-up (see CONTEXT.md)
	 *         — self-service needs updating to match before the two halves resolve against the same paths.
	 */
	private List<String> discoverFormResourcePaths() {
		List<MdiRow> mdiRows = loadMdiRows();
		Map<String, MdiRow> menusById = new LinkedHashMap<>();
		for (MdiRow row : mdiRows) {
			if (!row.isForm()) {
				menusById.put(row.keyText(), row);
			}
		}
		List<String> paths = new ArrayList<>();
		for (MdiRow row : mdiRows) {
			if (!row.isForm() || row.parentMenu() == null) {
				continue;
			}
			String category = resolveTopLevelMenu(row.parentMenu(), menusById);
			if (category != null) {
				paths.add("/form/" + category + "/" + row.keyText());
			}
		}
		return paths;
	}

	/**
	 * Reads xtcasMdi unsecurely (i.e. the full table, not row-level-filtered to one user's visible menu) —
	 * discovery needs to know about every form that exists, not just what one user can currently see.
	 */
	private List<MdiRow> loadMdiRows() {
		Table mdiQuery = new Table();
		mdiQuery.setName("xtcasMdi");
		mdiQuery.addColumn(new Column("KeyText", DataType.STRING));
		mdiQuery.addColumn(new Column("Menu", DataType.STRING));
		mdiQuery.addColumn(new Column("MdiTypeKey", DataType.INTEGER));
		mdiQuery.addColumn(new Column("LastAction", DataType.INTEGER));

		Row filterRow = new Row();
		filterRow.setValues(Arrays.asList(null, null, null, new Value(0, ">")));
		mdiQuery.setRows(new ArrayList<>(List.of(filterRow)));

		Table mdiData = viewService.unsecurelyGetIndexView(mdiQuery);

		List<MdiRow> result = new ArrayList<>();
		for (Row r : mdiData.getRows()) {
			Value keyTextValue = mdiData.getValue("KeyText", r);
			Value mdiTypeKeyValue = mdiData.getValue("MdiTypeKey", r);
			if (keyTextValue == null || keyTextValue.getStringValue() == null || mdiTypeKeyValue == null || mdiTypeKeyValue.getIntegerValue() == null) {
				continue;
			}
			int mdiTypeKey = mdiTypeKeyValue.getIntegerValue();
			if (mdiTypeKey != 1 && mdiTypeKey != 2) {
				// 1 = form entry, 2 = menu/sub-menu. 3 = the single top-level application-info row -- irrelevant here.
				continue;
			}
			Value menuValue = mdiData.getValue("Menu", r);
			result.add(new MdiRow(keyTextValue.getStringValue(), menuValue == null ? null : menuValue.getStringValue(), mdiTypeKey == 1));
		}
		return result;
	}

	/** Walks up parent menus (cycle-safe) until it finds one with no parent, flattening nested sub-menus into their top-level ancestor. */
	private String resolveTopLevelMenu(String menuId, Map<String, MdiRow> menusById) {
		Set<String> visited = new HashSet<>();
		String current = menuId;
		while (current != null && visited.add(current)) {
			MdiRow menu = menusById.get(current);
			if (menu == null) {
				return null;
			}
			if (menu.parentMenu() == null) {
				return menu.keyText();
			}
			current = menu.parentMenu();
		}
		return null; // cyclic or dangling menu reference -- skip rather than loop forever
	}
}
