package aero.minova.cas;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ch.minova.foundation.rest.db.service.FileService;
import ch.minova.foundation.rest.db.service.XMLUtils;

@ExtendWith(MockitoExtension.class)
class ContentPatcherTest {

	@Mock
	private CustomLogger customLoggerMock;

	@Mock
	private FileService dbFileServiceMock;

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
}
