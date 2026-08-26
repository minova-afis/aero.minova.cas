package aero.minova.cas;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import ch.minova.foundation.rest.db.model.RegistryEntry;
import ch.minova.foundation.rest.db.model.RegistryNode;
import ch.minova.foundation.rest.db.service.FileService;
import ch.minova.foundation.rest.db.service.RegistryService;
import ch.minova.foundation.rest.db.service.XMLUtils;

@ExtendWith(MockitoExtension.class)
class ContentPatcherTest {

	@Mock
	private CustomLogger customLoggerMock;

	@Mock
	private FileService dbFileServiceMock;

	@Mock
	private RegistryService registryServiceMock;

	@InjectMocks
	private ContentPatcher contentPatcher;

	private static byte[] load(String resourceName) throws IOException {
		try (InputStream is = ContentPatcherTest.class.getClassLoader().getResourceAsStream(resourceName)) {
			return is.readAllBytes();
		}
	}

	private static byte[] xml(String s) {
		return s.getBytes(StandardCharsets.UTF_8);
	}

	private static RegistryEntry entry(String keyText, String value) {
		RegistryEntry e = new RegistryEntry();
		e.setKeyText(keyText);
		e.setValue(value);
		e.setActive(true);
		return e;
	}

	/** Direct (non-recursive) element children of the given element, in document order. */
	private static List<Element> directChildren(Element el) {
		List<Element> children = new LinkedList<>();
		for (Node n = el.getFirstChild(); n != null; n = n.getNextSibling())
			if (n instanceof Element)
				children.add((Element) n);
		return children;
	}

	@Test
	void testImportInlinesReferencedDetailAndInheritsItsAttributes() throws Exception {
		byte[] shipment = load("xmls/Shipment.form.xml");
		byte[] preDelivery = load("xmls/PreDelivery.form.xml");
		Mockito.when(dbFileServiceMock.getFile("Shipment.xml")).thenReturn(shipment);

		byte[] patched = contentPatcher.resolveImports("PreDelivery.xml", preDelivery);

		Document doc = XMLUtils.getDocument(patched);
		Element detail = XMLUtils.findFirstElement(doc.getDocumentElement(), "detail");
		assertThat(detail.hasAttribute("import")).isFalse();
		assertThat(detail.getAttribute("id")).isEqualTo("ShipmentDetail");
		assertThat(detail.getAttribute("procedure-suffix")).isEqualTo("AFISBookingShipment");
		assertThat(detail.getAttribute("type")).isEqualTo("booking");
		// Actual content of the referenced detail was inlined, not just its attributes
		assertThat(XMLUtils.findFirstElementWithAttribute(detail, "id", v -> "updateRemarks".equals(v))).isNotNull();
	}

	@Test
	void testLocalAttributesOnStubOverrideInheritedOnesFromImportedElement() throws Exception {
		byte[] source = xml("<form><detail id=\"Detail\" type=\"booking\" foo=\"bar\"/></form>");
		byte[] importing = xml("<form><detail import=\"Source.Detail\" type=\"override\"/></form>");
		Mockito.when(dbFileServiceMock.getFile("Source.xml")).thenReturn(source);

		byte[] patched = contentPatcher.resolveImports("Importing.xml", importing);

		Document doc = XMLUtils.getDocument(patched);
		Element detail = XMLUtils.findFirstElement(doc.getDocumentElement(), "detail");
		assertThat(detail.hasAttribute("import")).isFalse();
		assertThat(detail.getAttribute("type")).isEqualTo("override"); // local attribute wins
		assertThat(detail.getAttribute("foo")).isEqualTo("bar"); // inherited from source
		assertThat(detail.getAttribute("id")).isEqualTo("Detail"); // inherited from source
	}

	@Test
	void testMissingReferencedFormLeavesStubUntouchedAndLogsError() throws Exception {
		byte[] importing = xml("<form><detail import=\"Missing.Detail\"/></form>");
		Mockito.when(dbFileServiceMock.getFile("Missing.xml")).thenReturn(null);

		byte[] patched = contentPatcher.resolveImports("Importing.xml", importing);

		Document doc = XMLUtils.getDocument(patched);
		Element detail = XMLUtils.findFirstElement(doc.getDocumentElement(), "detail");
		assertThat(detail.getAttribute("import")).isEqualTo("Missing.Detail");
		Mockito.verify(customLoggerMock).logError(Mockito.anyString(), Mockito.any(Exception.class));
	}

	@Test
	void testMissingTargetIdLeavesStubUntouchedAndLogsError() throws Exception {
		byte[] source = xml("<form><detail id=\"OtherId\"/></form>");
		byte[] importing = xml("<form><detail import=\"Source.Detail\"/></form>");
		Mockito.when(dbFileServiceMock.getFile("Source.xml")).thenReturn(source);

		byte[] patched = contentPatcher.resolveImports("Importing.xml", importing);

		Document doc = XMLUtils.getDocument(patched);
		Element detail = XMLUtils.findFirstElement(doc.getDocumentElement(), "detail");
		assertThat(detail.getAttribute("import")).isEqualTo("Source.Detail");
		Mockito.verify(customLoggerMock).logError(Mockito.anyString(), Mockito.any(Exception.class));
	}

	@Test
	void testImportCycleIsDetectedAndDoesNotLoopForever() throws Exception {
		byte[] formA = xml("<form><detail import=\"FormB.Detail\"/></form>");
		byte[] formB = xml("<form><detail import=\"FormA.Detail\"/></form>");
		Mockito.when(dbFileServiceMock.getFile("FormB.xml")).thenReturn(formB);

		byte[] patched = contentPatcher.resolveImports("FormA.xml", formA);

		Document doc = XMLUtils.getDocument(patched);
		Element detail = XMLUtils.findFirstElement(doc.getDocumentElement(), "detail");
		assertThat(detail.getAttribute("import")).isEqualTo("FormB.Detail");
		Mockito.verify(customLoggerMock, Mockito.atLeastOnce()).logError(Mockito.anyString(), Mockito.any(Exception.class));
	}

	/**
	 * See https://github.com/minova-afis/aero.minova.cas/issues/1501 -- a multi-page option page (a
	 * &lt;head&gt; followed by further &lt;page&gt;s, e.g. entrypartunloading.op.xml's invisible "Debug" page)
	 * must not have all but the first page silently dropped: the further page's fields (needed by the
	 * backend procedure) are expected to survive as a &lt;section&gt; inside the resulting &lt;optionpage&gt;.
	 */
	@Test
	void testMultiPageOptionPageKeepsFirstPageLooseAndTurnsFurtherPagesIntoSections() throws Exception {
		byte[] entryForm = load("xmls/entry.xml");
		byte[] optionPageForm = load("xmls/entrypartunloading.op.xml");
		Mockito.when(dbFileServiceMock.getFile("entrypartunloading.op.xml")).thenReturn(optionPageForm);
		Mockito.when(registryServiceMock.getSubtree("AFIS/entry.xml/OptionPages/")).thenReturn(
				RegistryNode.toTree(List.of(entry("AFIS/entry.xml/OptionPages/entrypartunloading.op.xml/KeyLong", "KeyLong")),
						"AFIS/entry.xml/OptionPages/"));

		byte[] patched = contentPatcher.resolveXMLForm("AFIS", "entry.xml", entryForm);

		Document doc = XMLUtils.getDocument(patched);
		Element detail = XMLUtils.findFirstElement(doc.getDocumentElement(), "detail");
		Element optionPage = XMLUtils.findFirstElement(detail, "optionpage");
		assertThat(optionPage).isNotNull();
		assertThat(optionPage.getAttribute("icon")).isEqualTo("Tank.ico");
		assertThat(optionPage.getAttribute("text")).isEqualTo("@tTank");
		assertThat(optionPage.getAttribute("procedure-suffix")).isEqualTo("PartUnloadingBookingQuantities");

		List<Element> optionPageChildren = directChildren(optionPage);

		// The head's fields stay loose, directly under <optionpage>, exactly as before this fix
		List<Element> looseFields = optionPageChildren.stream().filter(el -> "field".equals(el.getTagName())).toList();
		assertThat(looseFields).extracting(el -> el.getAttribute("name")).contains("KeyLong", "TankKey", "Gallons");
		Element keyLongField = looseFields.stream().filter(el -> "KeyLong".equals(el.getAttribute("name"))).findFirst().orElseThrow();
		assertThat(keyLongField.getAttribute("map-to")).isEqualTo("KeyLong"); // key-mapping still resolves into the loose head fields

		// The further <page text="Debug" visible="false"> became one <section>, sibling of the loose fields
		List<Element> sections = optionPageChildren.stream().filter(el -> "section".equals(el.getTagName())).toList();
		assertThat(sections).hasSize(1);
		Element debugSection = sections.get(0);
		assertThat(debugSection.getAttribute("text")).isEqualTo("Debug");
		assertThat(debugSection.getAttribute("visible")).isEqualTo("false");
		assertThat(debugSection.getAttribute("icon")).isEqualTo("Warning.ico");

		// Its fields -- previously silently dropped -- are now nested inside that section, not loose
		assertThat(looseFields).noneMatch(el -> "Quantity".equals(el.getAttribute("name")) || "UnitKey".equals(el.getAttribute("name")));
		List<Element> debugFields = directChildren(debugSection);
		assertThat(debugFields).extracting(el -> el.getAttribute("name")).containsExactly("Quantity", "UnitKey");

		Mockito.verifyNoInteractions(customLoggerMock); // no swallowed errors
	}

	/**
	 * A further page can itself mix loose fields and a nested &lt;section&gt; (not just be pure fields or
	 * pure sections). Since a &lt;section&gt; can't contain a &lt;section&gt;, the page's own loose fields
	 * become a section of their own (carrying the page's attributes), while its nested section is lifted out
	 * as an additional, separate sibling section instead of staying nested.
	 */
	@Test
	void testPageMixingLooseFieldsAndNestedSectionExtractsTheNestedSectionSeparately() throws Exception {
		byte[] entryForm = xml("<form><detail id=\"Detail\"><head><field name=\"KeyLong\" key-type=\"primary\"/></head></detail></form>");
		byte[] optionPageForm = xml("<form icon=\"Some.ico\" title=\"OpTitle\"><detail procedure-suffix=\"OpSuffix\">" //
				+ "<head><field name=\"KeyLong\" key-type=\"primary\"/></head>" //
				+ "<page text=\"Mixed\" visible=\"false\">" //
				+ "<field name=\"LooseField\"/>" //
				+ "<section text=\"Nested\" icon=\"N.ico\"><field name=\"NestedField\"/></section>" //
				+ "</page>" //
				+ "</detail></form>");
		Mockito.when(dbFileServiceMock.getFile("MixedPage.op.xml")).thenReturn(optionPageForm);
		Mockito.when(registryServiceMock.getSubtree("AFIS/Form.xml/OptionPages/")).thenReturn(
				RegistryNode.toTree(List.of(entry("AFIS/Form.xml/OptionPages/MixedPage.op.xml/KeyLong", "KeyLong")), "AFIS/Form.xml/OptionPages/"));

		byte[] patched = contentPatcher.resolveXMLForm("AFIS", "Form.xml", entryForm);

		Document doc = XMLUtils.getDocument(patched);
		Element detail = XMLUtils.findFirstElement(doc.getDocumentElement(), "detail");
		Element optionPage = XMLUtils.findFirstElement(detail, "optionpage");
		assertThat(optionPage).isNotNull();

		List<Element> sections = directChildren(optionPage).stream().filter(el -> "section".equals(el.getTagName())).toList();
		assertThat(sections).hasSize(2);

		Element mixedSection = sections.stream().filter(el -> "Mixed".equals(el.getAttribute("text"))).findFirst().orElseThrow();
		assertThat(mixedSection.getAttribute("visible")).isEqualTo("false");
		List<Element> mixedSectionChildren = directChildren(mixedSection);
		assertThat(mixedSectionChildren).extracting(Element::getTagName).containsExactly("field"); // nested section was extracted, not left inside
		assertThat(mixedSectionChildren.get(0).getAttribute("name")).isEqualTo("LooseField");

		Element nestedSection = sections.stream().filter(el -> "Nested".equals(el.getAttribute("text"))).findFirst().orElseThrow();
		assertThat(nestedSection.getAttribute("icon")).isEqualTo("N.ico");
		assertThat(directChildren(nestedSection)).extracting(el -> el.getAttribute("name")).containsExactly("NestedField");

		Mockito.verifyNoInteractions(customLoggerMock);
	}
}
