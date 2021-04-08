package aero.minova.core.application.system.covid.test.print.controller;

import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import aero.minova.core.application.system.controller.SqlProcedureController;
import aero.minova.core.application.system.domain.*;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ch.minova.xml.XMLServiceEvent;

@RestController
public class TestCertificatePrintController {

    @Value("${ch.minova.service.xmlprinter.service.port:1506}")
    private String xmlPrinterServicePort;

    @Value("${testTargetPdf:C:\\TEST_SYSTEME\\system.active\\Shared Data\\Daily\\DailySupplierReport_7_3.pdf}")
    private String testTargetPdf;

    @Value("${testReport:\\TEST_SYSTEME\\system.active\\Shared Data\\Program Files\\reports\\DailySupplierReport.xsl}")
    private String testReport;

    @Autowired
    private SqlProcedureController sqlProcedureController;

    @CrossOrigin
    @RequestMapping(value = "covid/test/certificate/print", produces = {MediaType.APPLICATION_PDF_VALUE})
    public @ResponseBody
    byte[] getTestCertificate(Integer keyLong) throws Exception {
        final val testCertificateReportXml = testCertificateReportXml(keyLong);
        final val path = Paths.get("D:/minova-systems/systems/com.minova.vgeibelstadt/Shared Data/tmp").resolve("xpctsXMLTestzertifikat." + keyLong +".xml");
        Files.write(path, testCertificateReportXml.getBytes());
        final Path targetPath = Paths.get(testTargetPdf);
        new XMLServiceEvent(path.toString(), testReport, targetPath.toString())//
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

    private String testCertificateReportXml(Integer keyLong) {
        val sqlRequest = new Table();
        sqlRequest.setName("xpctsXMLTestzertifikat");
        sqlRequest.addColumn(new Column("KeyLong", DataType.INTEGER));
        {
            val requestParam = new Row();
            sqlRequest.getRows().add(requestParam);
            requestParam.addValue(new aero.minova.core.application.system.domain.Value(keyLong, null));
        }
        // Hiermit wird der unsichere Zugriff ermöglicht.
        val requestingAuthority = new Row();
        requestingAuthority.addValue(new aero.minova.core.application.system.domain.Value(false, "1"));
        requestingAuthority.addValue(new aero.minova.core.application.system.domain.Value(false, "2"));
        requestingAuthority.addValue(new aero.minova.core.application.system.domain.Value(false, "3"));
        final String reportBody;
        try {
            val t = sqlProcedureController
                    .calculateSqlProcedureResult(sqlRequest);
            reportBody = t
                    .getResultSet()
                    .getRows()
                    .get(0)
                    .getValues()
                    .get(0)
                    .getStringValue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
                + "<CTSTestZertifikat>"
                + reportBody
                + "</CTSTestZertifikat>";
    }
}
