package aero.minova.cas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import aero.minova.cas.CustomLogger;

/**
 * Plain unit test (no Spring context) for {@link GrantsDiscoveryService#discoverFormResourcePathsFromMdiXml} — the
 * #1499 fix, parsing a real served {@code application.mdi} directly rather than assuming {@code xtcasMdi} is
 * always the source of truth. Deliberately not a {@code @SpringBootTest} like most of this package's other tests:
 * this logic touches no injected bean, only XML parsing, so a full context would only slow the test down for no
 * benefit.
 * <p>
 * Fixture ({@code src/test/resources/xmls/application-with-forms.mdi}) is the <em>real</em> MDI captured from the
 * SaaS-DEV cluster during the #1499 investigation (see CONTEXT.md) — 85 form entries across 8 top-level menus,
 * not a hand-crafted minimal example, so this test is exercising the actual shape that exposed the original bug.
 */
class GrantsDiscoveryServiceTest {

	private final GrantsDiscoveryService service = new GrantsDiscoveryService();

	@BeforeEach
	void setUp() {
		// discoverFormResourcePathsFromMdiXml logs via `logger` on a parse failure (see the garbage-input test
		// below) -- there's no Spring context here to @Autowired it, so it has to be set directly or that one
		// code path NPEs instead of exercising the fallback it's meant to.
		service.logger = new CustomLogger();
	}

	@Test
	void discoversEveryFormAcrossEveryTopLevelMenu() throws IOException {
		List<String> paths = service.discoverFormResourcePathsFromMdiXml(loadFixture());

		// 71 <entry type="action" id="..."/> elements under <menu id="main"> in the fixture -- NOT the same as a
		// raw grep count of every <entry type="action"> in the file (85), which also catches the fixture's
		// <toolbar> section: 14 more entries there, but those are just quick-access shortcuts to forms already
		// reachable through the menu, deliberately not walked here -- counting them again would double-count
		// forms discovery already found, not add anything real.
		assertEquals(71, paths.size());
	}

	@Test
	void attributesEachFormToItsOwnTopLevelCategory() throws IOException {
		List<String> paths = service.discoverFormResourcePathsFromMdiXml(loadFixture());

		// One from each of the 8 real top-level menus in the fixture -- confirms categorization, not just count.
		assertTrue(paths.contains("/form/masterdata/Contact"));
		assertTrue(paths.contains("/form/definition/LuSupplierCustomer"));
		assertTrue(paths.contains("/form/handling/Dispatch"));
		assertTrue(paths.contains("/form/deliveries/Shipment"));
		assertTrue(paths.contains("/form/information/FlightAnalysis"));
		assertTrue(paths.contains("/form/closing/MonthlyClosing"));
		assertTrue(paths.contains("/form/configuration/Modul"));
		assertTrue(paths.contains("/form/tools/JobDefinition"));
	}

	@Test
	void ignoresSeparatorEntriesAndTopLevelActionElements() throws IOException {
		List<String> paths = service.discoverFormResourcePathsFromMdiXml(loadFixture());

		// <entry type="separator"/> has no id at all -- would produce "/form/masterdata/" (trailing slash, no
		// KeyText) if it were ever mistakenly picked up. The fixture's masterdata menu has several of these.
		assertTrue(paths.stream().noneMatch(p -> p.endsWith("/")));

		// Top-level <action id="Iso" .../> elements (siblings of <menu>, not <entry> children of it) must never
		// be picked up directly -- only the <entry id="Iso" type="action"/> reference inside a category counts.
		// The fixture's top-level <action> list and its <menu> entries deliberately share several ids (e.g. "Iso",
		// "Contact") -- if collectFormEntries ever started walking the wrong element type, this would silently
		// duplicate rather than fail, so the count assertion above is what actually catches that class of bug.
		// Same reasoning applies to <toolbar>'s entries (see the other test's comment) -- also never picked up.
		assertEquals(71, paths.size());
	}

	@Test
	void toleratesGarbageInputByReturningAnEmptyListRatherThanThrowing() {
		byte[] notXml = "this is not xml at all".getBytes(StandardCharsets.UTF_8);

		List<String> paths = service.discoverFormResourcePathsFromMdiXml(notXml);

		assertTrue(paths.isEmpty());
	}

	@Test
	void returnsEmptyListWhenThereIsNoRootMenuElement() {
		byte[] mdiWithNoMenu = "<main icon=\"X.ico\" titel=\"X\"></main>".getBytes(StandardCharsets.UTF_8);

		List<String> paths = service.discoverFormResourcePathsFromMdiXml(mdiWithNoMenu);

		assertTrue(paths.isEmpty());
	}

	@Test
	void flattensNestedSubmenusIntoTheirTopLevelCategory() {
		// A synthetic case, unlike the other tests above -- the real fixture doesn't have genuine multi-level
		// nesting (confirmed against the real MDI schema during the #1499 investigation), but resolveTopLevelMenu
		// (the xtcasMdi-driven equivalent) explicitly supports it, so this parser must too, for parity.
		String nested = "<main icon=\"X.ico\" titel=\"X\">" //
				+ "<menu id=\"main\">" //
				+ "  <menu id=\"category\" text=\"Category\">" //
				+ "    <entry id=\"DirectForm\" type=\"action\"/>" //
				+ "    <menu id=\"subcategory\" text=\"Sub\">" //
				+ "      <entry id=\"NestedForm\" type=\"action\"/>" //
				+ "    </menu>" //
				+ "  </menu>" //
				+ "</menu>" //
				+ "</main>";

		List<String> paths = service.discoverFormResourcePathsFromMdiXml(nested.getBytes(StandardCharsets.UTF_8));

		assertEquals(2, paths.size());
		assertTrue(paths.contains("/form/category/DirectForm"));
		assertTrue(paths.contains("/form/category/NestedForm"), "a form under a nested sub-menu must still be attributed to the top-level category, not the sub-menu");
	}

	private byte[] loadFixture() throws IOException {
		try (InputStream in = getClass().getClassLoader().getResourceAsStream("xmls/application-with-forms.mdi")) {
			if (in == null) {
				throw new IOException("Test fixture xmls/application-with-forms.mdi not found on the classpath");
			}
			return in.readAllBytes();
		}
	}
}
