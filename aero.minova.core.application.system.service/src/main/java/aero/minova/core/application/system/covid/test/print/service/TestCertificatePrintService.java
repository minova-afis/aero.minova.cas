package aero.minova.core.application.system.covid.test.print.service;

import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import aero.minova.core.application.system.controller.SqlProcedureController;
import aero.minova.core.application.system.domain.*;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Service;

import ch.minova.xml.XMLServiceEvent;

@Service
public class TestCertificatePrintService {

    final Logger logger = LoggerFactory.getLogger(TestCertificatePrintService.class);

    @Value("${ch.minova.service.xmlprinter.service.port:1506}")
    private String xmlPrinterServicePort;

    @Value("${testTargetPdf:../../tmp/}")
    private String temporaryFolder;

    @Value("${testReport:../reports/CTSTestZertifikat.xsl}")
    private String testReportStylesheet;

    @Autowired
    private SqlProcedureController sqlProcedureController;

    @Autowired
    TestCertificateMailService covidTestTestCertificateMailService;

    public byte[] getTestCertificate(Integer keyLong) throws Exception {
        return Files.readAllBytes(getTestCertificatePath(keyLong));
    }

    private Path getTestCertificatePath(Integer keyLong) throws Exception {
        val testCertificateReportXml = testCertificateReportXml(keyLong);
        val printContainer = Paths.get(temporaryFolder);
        Files.createDirectories(printContainer);
        val inputFile = printContainer.resolve("xpctsXMLTestzertifikat." + keyLong + ".xml").toAbsolutePath();
        if (Files.exists(inputFile)) {
            Files.delete(inputFile);
        }
        Files.write(inputFile, testCertificateReportXml.getBytes(StandardCharsets.UTF_8));
        val outputPath = printContainer.resolve("xpctsXMLTestzertifikat." + keyLong + ".pdf").toAbsolutePath();
        if (Files.exists(outputPath)) {
            Files.delete(outputPath);
        }
        new XMLServiceEvent(inputFile.toString()
                , Paths.get(testReportStylesheet).toAbsolutePath().toString()
                , outputPath.toString())
                .send(new Socket("localhost", Integer.valueOf(xmlPrinterServicePort)));
        for (int i = 0; i < 10; ++i) {
            sleep();
            if (Files.exists(outputPath)) {
                /*
                 * Falls die Datei gerade erstellt wurde, wollen wir hiermit sicherstellen, dass die Schreiboperationen mit erhöhter Wahrscheinlichkeit durch
                 * sind.
                 */
                sleep();
                return outputPath;
            }
        }
        throw new RuntimeException("Could not generate test certificate.");
    }

    public void xpctsInsertTestErgebnis(Table inputTable, Table outputTable) throws Exception {
        if ("xpctsInsertTestErgebnis".equals(inputTable.getName())) {
            val testErgebnisValueKey = inputTable
                    .getRows()
                    .get(0)
                    .getValues()
                    .get(3)
                    .getIntegerValue();
            if (testErgebnisValueKey == 1) {
                val testCertificateKeyLong = outputTable
                        .getRows()
                        .get(0)
                        .getValues()
                        .get(0)
                        .getIntegerValue();
                covidTestTestCertificateMailService.sendCertificateByMail
                        (getTestCertificatePath(testCertificateKeyLong).toFile());
            }
        }
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
