package aero.minova.cas.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.service.model.LuUserPrivilegeUserGroup;
import aero.minova.cas.service.model.UserGroup;
import aero.minova.cas.service.model.UserPrivilege;
import aero.minova.cas.service.repository.LuUserPrivilegeUserGroupRepository;
import aero.minova.cas.service.repository.UserGroupRepository;
import aero.minova.cas.service.repository.UserPrivilegeRepository;

/**
 * Builds the read-only "Access Rights" permissions tree (Category -&gt; Form -&gt; Item) consumed by web.ui's
 * Access Rights view (minova-afis/web.ui#129, GH-1495). See that issue for the full background on why this is
 * built around CAS's own live authorization tables (xtcasUserPrivilege/xtcasUserGroup/xtcasLuUserPrivilegeUserGroup)
 * rather than SQL Server-native roles.
 *
 * <h2>Two independent, real grant mechanisms -- not one cascading hierarchy</h2>
 * <p>
 * Category nodes are purely organizational (xtcasMdi menus have no SecurityToken of their own). Form and Item
 * nodes each carry <b>real, independently-configured</b> DB state, but from two different tables -- a Form's
 * grant does <b>not</b> cascade down to its Items or vice versa:
 * </p>
 * <ul>
 * <li><b>Form-level</b>: the form's own {@code xtcasMdi.SecurityToken} column -- the same column
 * {@link FilesService#readMDI()}'s row-level-security applies to decide whether a mask even shows up in a user's
 * menu. {@code null} means visible to everyone; otherwise a group is granted if its own KeyText or one of its
 * {@code #}-separated SecurityToken entries matches.</li>
 * <li><b>Item-level</b>: direct rows in {@code xtcasLuUserPrivilegeUserGroup} linking a group to a specific
 * procedure/view privilege -- see below.</li>
 * </ul>
 *
 * <h2>Known simplifications (documented rather than silently assumed)</h2>
 * <ul>
 * <li><b>No inheritance across the tree.</b> Unlike Assist's DB Roles model, neither grant mechanism above
 * cascades from Category to Form to Item -- see the two-mechanisms note above.</li>
 * <li><b>No "Denied" state.</b> This is a flat has-privilege-or-not model -- a group either has a row in
 * xtcasLuUserPrivilegeUserGroup for a privilege, or it doesn't. There is no explicit deny.</li>
 * <li><b>Direct grants only, no nested-group expansion.</b> A UserGroup's own {@code SecurityToken} can reference
 * other groups' tokens, which {@link SecurityService#loadUserGroupPrivileges} expands (one level) when resolving
 * a logged-in *user's* effective access. This read-only reporting endpoint intentionally does not replicate that
 * expansion -- it reports each group's own directly-assigned privileges only, which is what an admin auditing
 * "what does group X have configured" actually wants to see.</li>
 * <li><b>Form matching is a naming-convention heuristic.</b> Privileges are matched to a form by checking whether
 * the privilege's KeyText ends with {@code {Operation}{maskName}} (Insert/Update/Read/Delete) or
 * {@code {maskName}Index}, mirroring {@link AuthorizationService#createDefaultPrivilegesForMask}. Privileges that
 * match no form land in a synthetic "Other" bucket -- same fallback Assist's own tree-builder uses.</li>
 * <li><b>Menu nesting is flattened to one level.</b> xtcasMdi menus can nest arbitrarily deep; this only
 * distinguishes top-level menus (Category) from their direct form children -- a nested sub-menu's forms are
 * attached to their nearest top-level ancestor rather than represented as their own tree level.</li>
 * <li><b>{@code hasHiddenChildren} on a Form node is depth-based, not existence-based, below {@code full}.</b>
 * It's set whenever item-level detail wasn't fetched at all (any depth other than "full"), even for a form that
 * would turn out to have zero matched privileges -- computing the real answer would mean loading and matching
 * every privilege regardless of the requested depth, defeating the point of a shallower request.</li>
 * </ul>
 */
@Service
public class PermissionsService {

	private static final String OTHER_ID = "other";
	private static final String OTHER_LABEL = "Other";
	private static final List<String> MASK_OPERATIONS = List.of("Insert", "Update", "Read", "Delete");

	@Autowired
	ViewService viewService;

	@Autowired
	UserGroupRepository userGroupRepository;

	@Autowired
	UserPrivilegeRepository userPrivilegeRepository;

	@Autowired
	LuUserPrivilegeUserGroupRepository luUserPrivilegeUserGroupRepository;

	// ─── Response DTOs ──────────────────────────────────────────────────────────

	public record RoleDto(String id, String name) {
	}

	public record ItemDto(String id, String name, String kind, Map<String, Boolean> grants) {
	}

	public record FormDto(String id, String name, Map<String, Boolean> grants, List<ItemDto> items, boolean hasHiddenChildren) {
	}

	public record CategoryDto(String id, String name, List<FormDto> forms, boolean hasHiddenChildren) {
	}

	public record TreeResponse(List<RoleDto> roles, List<CategoryDto> categories) {
	}

	/** Internal representation of one xtcasMdi row -- either a menu (category) or a form entry. */
	private record MdiRow(String keyText, String label, String parentMenu, boolean isForm, String securityToken) {
	}

	// ─── Public API ─────────────────────────────────────────────────────────────

	/**
	 * @param depth
	 *            one of "category", "category+form", "full". Anything else is treated as "category+form".
	 */
	public TreeResponse getTree(String depth) {
		boolean includeForms = "category+form".equals(depth) || "full".equals(depth);
		boolean includeItems = "full".equals(depth);

		List<UserGroup> groups = userGroupRepository.findByLastActionGreaterThan(0);
		List<RoleDto> roles = groups.stream().map(g -> new RoleDto(String.valueOf(g.getKeyLong()), g.getKeyText())).toList();
		List<MdiRow> mdiRows = loadMdiRows();

		Map<String, MdiRow> menusById = new LinkedHashMap<>();
		Map<String, List<MdiRow>> formsByTopLevelMenu = new LinkedHashMap<>();
		for (MdiRow row : mdiRows) {
			if (!row.isForm()) {
				menusById.put(row.keyText(), row);
			}
		}
		for (MdiRow row : mdiRows) {
			if (row.isForm() && row.parentMenu() != null) {
				String topLevelMenuId = resolveTopLevelMenu(row.parentMenu(), menusById);
				if (topLevelMenuId != null) {
					formsByTopLevelMenu.computeIfAbsent(topLevelMenuId, k -> new ArrayList<>()).add(row);
				}
			}
		}

		List<UserPrivilege> allPrivileges = includeItems ? userPrivilegeRepository.findByLastActionGreaterThan(0) : List.of();
		Set<Integer> matchedPrivilegeKeys = new HashSet<>();

		List<CategoryDto> categories = new ArrayList<>();
		for (MdiRow menu : menusById.values()) {
			if (menu.parentMenu() != null) {
				// Only top-level menus become Category nodes -- see "Menu nesting" note above.
				continue;
			}
			List<MdiRow> forms = formsByTopLevelMenu.get(menu.keyText());
			if (forms == null || forms.isEmpty()) {
				continue;
			}

			List<FormDto> formDtos = null;
			if (includeForms) {
				formDtos = new ArrayList<>();
				for (MdiRow form : forms) {
					List<ItemDto> items = null;
					boolean formHasHiddenChildren = !includeItems;
					if (includeItems) {
						List<UserPrivilege> matches = matchPrivilegesForMask(allPrivileges, form.keyText());
						matches.forEach(p -> matchedPrivilegeKeys.add(p.getKeyLong()));
						items = matches.stream().map(this::toItemDto).toList();
						formHasHiddenChildren = false;
					}
					Map<String, Boolean> formGrants = formGrantsForToken(form.securityToken(), groups);
					formDtos.add(new FormDto(form.keyText(), form.label(), formGrants, items, formHasHiddenChildren));
				}
			}
			categories.add(new CategoryDto(menu.keyText(), menu.label(), formDtos, !includeForms));
		}

		if (includeItems) {
			List<UserPrivilege> unmatched = allPrivileges.stream().filter(p -> !matchedPrivilegeKeys.contains(p.getKeyLong())).toList();
			if (!unmatched.isEmpty()) {
				List<ItemDto> otherItems = unmatched.stream().map(this::toItemDto).toList();
				// Synthetic bucket, not a real xtcasMdi row -- no form-level SecurityToken to report.
				FormDto otherForm = new FormDto(OTHER_ID, OTHER_LABEL, Map.of(), otherItems, false);
				categories.add(new CategoryDto(OTHER_ID, OTHER_LABEL, List.of(otherForm), false));
			}
		} else if (includeForms) {
			// "Other" category is only meaningful once we know which privileges are unmatched (depth=full).
			// At category+form depth we can't yet say whether it'll be non-empty, so it's omitted entirely --
			// consistent with hasHiddenChildren already telling the client there's more detail available.
		}

		return new TreeResponse(roles, categories);
	}

	// ─── MDI (Category / Form) ──────────────────────────────────────────────────

	/**
	 * Reads xtcasMdi unsecurely (i.e. the full table, not row-level-filtered to one user's visible menu) --
	 * an admin managing permissions needs to see every form, not just their own. Mirrors the columns/query
	 * {@link FilesService#readMDI()} uses to build the same table for application.mdi, minus the final XML step.
	 */
	private List<MdiRow> loadMdiRows() {
		Table mdiQuery = new Table();
		mdiQuery.setName("xtcasMdi");
		mdiQuery.addColumn(new Column("KeyText", DataType.STRING));
		mdiQuery.addColumn(new Column("Label", DataType.STRING));
		mdiQuery.addColumn(new Column("Menu", DataType.STRING));
		mdiQuery.addColumn(new Column("MdiTypeKey", DataType.INTEGER));
		mdiQuery.addColumn(new Column("SecurityToken", DataType.STRING));
		mdiQuery.addColumn(new Column("LastAction", DataType.INTEGER));

		Row filterRow = new Row();
		filterRow.setValues(Arrays.asList(null, null, null, null, null, new Value(0, ">")));
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
			Value labelValue = mdiData.getValue("Label", r);
			Value menuValue = mdiData.getValue("Menu", r);
			Value securityTokenValue = mdiData.getValue("SecurityToken", r);
			result.add(new MdiRow(keyTextValue.getStringValue(), labelValue == null ? null : labelValue.getStringValue(),
					menuValue == null ? null : menuValue.getStringValue(), mdiTypeKey == 1,
					securityTokenValue == null ? null : securityTokenValue.getStringValue()));
		}
		return result;
	}

	/**
	 * Resolves a Form's own {@code xtcasMdi.SecurityToken} into per-role grants -- see the class javadoc's
	 * "Two independent, real grant mechanisms" note. {@code null}/blank means visible to everyone (every current
	 * role is reported as granted); otherwise a group is granted if the token matches its own KeyText (how
	 * {@code xtcasAuthorities} assigns a user to a group by name) or one of its {@code #}-separated
	 * {@code SecurityToken} entries (how nested/aliased groups work, per
	 * {@link AuthorizationService#createOrUpdateUserGroup}).
	 */
	private Map<String, Boolean> formGrantsForToken(String formSecurityToken, List<UserGroup> groups) {
		Map<String, Boolean> grants = new HashMap<>();
		if (formSecurityToken == null || formSecurityToken.isBlank()) {
			groups.forEach(g -> grants.put(String.valueOf(g.getKeyLong()), true));
			return grants;
		}
		for (UserGroup group : groups) {
			if (groupMatchesToken(group, formSecurityToken)) {
				grants.put(String.valueOf(group.getKeyLong()), true);
			}
		}
		return grants;
	}

	private boolean groupMatchesToken(UserGroup group, String token) {
		if (group.getKeyText() != null && group.getKeyText().equalsIgnoreCase(token)) {
			return true;
		}
		String securityToken = group.getSecurityToken();
		if (securityToken == null || securityToken.isBlank()) {
			return false;
		}
		for (String candidate : securityToken.split("#")) {
			if (candidate.trim().equalsIgnoreCase(token)) {
				return true;
			}
		}
		return false;
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

	// ─── Items (UserPrivileges) ─────────────────────────────────────────────────

	/** See the "Form matching is a naming-convention heuristic" note in the class javadoc. */
	private List<UserPrivilege> matchPrivilegesForMask(List<UserPrivilege> allPrivileges, String maskName) {
		if (maskName == null || maskName.isBlank()) {
			return List.of();
		}
		List<UserPrivilege> matches = new ArrayList<>();
		for (UserPrivilege privilege : allPrivileges) {
			String keyText = privilege.getKeyText();
			if (keyText == null) {
				continue;
			}
			if (keyText.endsWith(maskName + "Index")) {
				matches.add(privilege);
				continue;
			}
			for (String op : MASK_OPERATIONS) {
				if (keyText.endsWith(op + maskName)) {
					matches.add(privilege);
					break;
				}
			}
		}
		return matches;
	}

	private ItemDto toItemDto(UserPrivilege privilege) {
		String keyText = privilege.getKeyText();
		String kind = keyText != null && keyText.endsWith("Index") ? "view" : "procedure";
		Map<String, Boolean> grants = grantsForPrivilege(privilege);
		return new ItemDto(String.valueOf(privilege.getKeyLong()), keyText, kind, grants);
	}

	/**
	 * Direct grants only (no nested-group/SecurityToken expansion) -- see the class javadoc's "Direct grants only"
	 * note for why that's the right behavior for this reporting endpoint.
	 */
	private Map<String, Boolean> grantsForPrivilege(UserPrivilege privilege) {
		List<LuUserPrivilegeUserGroup> links = luUserPrivilegeUserGroupRepository.findByUserPrivilegeKeyLongAndLastActionGreaterThan(privilege.getKeyLong(),
				0);
		Map<String, Boolean> grants = new HashMap<>();
		for (LuUserPrivilegeUserGroup link : links) {
			UserGroup group = link.getUserGroup();
			if (group != null) {
				grants.put(String.valueOf(group.getKeyLong()), true);
			}
		}
		return grants;
	}
}
