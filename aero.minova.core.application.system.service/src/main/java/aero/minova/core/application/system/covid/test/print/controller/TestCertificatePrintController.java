package aero.minova.core.application.system.covid.test.print.controller;

import java.io.File;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import aero.minova.core.application.system.controller.SqlProcedureController;
import aero.minova.core.application.system.controller.SqlViewController;
import aero.minova.core.application.system.covid.test.print.MailService;
import aero.minova.core.application.system.domain.*;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ch.minova.xml.XMLServiceEvent;

@RestController
public class TestCertificatePrintController {

    final Logger logger = LoggerFactory.getLogger(TestCertificatePrintController.class);

    @Value("${ch.minova.service.xmlprinter.service.port:1506}")
    private String xmlPrinterServicePort;

    @Value("${testTargetPdf:../../tmp/}")
    private String testTargetPdf;

    @Value("${testReport:../reports/CTSTestZertifikat.xsl}")
    private String testReport;

    @Autowired
    private SqlProcedureController sqlProcedureController;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private MailService mailService;

    @CrossOrigin
    @RequestMapping(value = "covid/test/certificate/print", produces = {MediaType.APPLICATION_PDF_VALUE})
    public @ResponseBody
    byte[] getTestCertificate(Integer keyLong) throws Exception {
        val testCertificateReportXml = testCertificateReportXml(keyLong);
        val folder = Paths.get(testTargetPdf);
        Files.createDirectories(folder);
        val path = folder.resolve("xpctsXMLTestzertifikat." + keyLong + ".xml").toAbsolutePath();
        if (Files.exists(path)) {
            Files.delete(path);
        }
        Files.write(path, testCertificateReportXml.getBytes(StandardCharsets.UTF_8));
        val targetPath = folder.resolve("xpctsXMLTestzertifikat." + keyLong + ".pdf").toAbsolutePath();
        if (Files.exists(targetPath)) {
            Files.delete(targetPath);
        }
        new XMLServiceEvent(path.toString(), Paths.get(testReport).toAbsolutePath().toString(), targetPath.toString())//
                .send(new Socket("localhost", Integer.valueOf(xmlPrinterServicePort)));
        for (int i = 0; i < 10; ++i) {
            sleep();
            if (Files.exists(targetPath)) {
                /*
                 * Falls die Datei gerade erstellt wurde, wollen wir hiermit sicherstellen, dass die Schreiboperationen mit erhöhter Wahrscheinlichkeit durch
                 * sind.
                 */
                sleep();
                val testCertificate = Files.readAllBytes(targetPath);
                sendCertificateByMail(targetPath.toFile());
                return testCertificate;
            }
        }
        throw new RuntimeException("Could not generate test certificate.");
    }

    private void sendCertificateByMail(File testCertificatePdf) {
        try {
            val message = mailSender.createMimeMessage();
            {
                val helper = new MimeMessageHelper(message, true);
                helper.setTo("afis@minova.de");
                helper.setFrom(mailService.mailAddress);
                helper.setSubject("COVID-Test-Zertifikat");
                helper.setText("Test", true);
                helper.addAttachment("COVID-Test-Zertifikat.pdf", testCertificatePdf);
            }
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("Could not send certificate pdf.", e);
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
