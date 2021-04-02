package aero.minova.covid.test.print.controller;

import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ch.minova.xml.XMLServiceEvent;

@RestController
public class TestCertificatePrintController {

	@Value("${ch.minova.service.xmlprinter.service.port:1506}")
	private String xmlPrinterServicePort;

	@Value("${testInputXml:C:\\TEST_SYSTEME\\system.active\\Shared Data\\Daily\\DailySupplierReport_7_3.xml}")
	private String testInputXml;

	@Value("${testTargetPdf:C:\\TEST_SYSTEME\\system.active\\Shared Data\\Daily\\DailySupplierReport_7_3.pdf}")
	private String testTargetPdf;

	@Value("${testReport:\\TEST_SYSTEME\\system.active\\Shared Data\\Program Files\\reports\\DailySupplierReport.xsl}")
	private String testReport;

	@RequestMapping(value = "covid/test/certificate/print", produces = { MediaType.APPLICATION_PDF_VALUE })
	public @ResponseBody byte[] getTestCertificate() throws Exception {
		final Path targetPath = Paths.get(testTargetPdf);
		new XMLServiceEvent(testInputXml, testReport, targetPath.toString())//
				.send(new Socket("localhost", Integer.valueOf(xmlPrinterServicePort)));
		for (int i = 0; i < 10; ++i) {
			sleep();
			if (Files.exists(targetPath)) {
				/*
				 * Falls die Datei gerade erstellt wurde, wollen wir hiermit sicherstellen, dass die Schreiboperationen mit erhöhter Wahrscheinlichkeit durch
				 * sind.
				 */
				sleep();
				return Files.readAllBytes(targetPath);
			}
		}
		throw new RuntimeException("Could not generate test certificate.");
	}

	private void sleep() {
		try {
			Thread.sleep(1000l);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
