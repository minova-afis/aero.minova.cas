package aero.minova.core.application.system.covid.test.print.service;

import aero.minova.core.application.system.controller.SqlProcedureController;
import aero.minova.core.application.system.controller.SqlViewController;
import aero.minova.core.application.system.covid.test.print.domain.AvailableTestsPerDate;
import aero.minova.core.application.system.domain.*;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class TestCertificateMailService {
    final Logger logger = LoggerFactory.getLogger(TestCertificateMailService.class);

    @Value("${spring.mail.username:}")
    public String mailAddress;

    @Value("${testErgebnisPositiveKey}")
    public String testErgebnisPositiveKey;

    @Value("${testErgebnisNegativeKey}")
    public String testErgebnisNegativeKey;

    @Autowired
    private SqlProcedureController sqlProcedureController;

    @Autowired
    private SqlViewController sqlViewController;

    @Autowired
    private TestCertificatePrintService testCertificatePrintService;

    @Autowired
    private JavaMailSender mailSender;

    @Scheduled(fixedRate = 1000 * 10)
    public void sendCertificate() throws Throwable {
        val sqlRequest = new Table();
        sqlRequest.setName("xvctsTestTerminIndex");
        sqlRequest.addColumn(new Column("KeyLong", DataType.INTEGER, OutputType.OUTPUT));
        sqlRequest.addColumn(new Column("IsSent", DataType.INTEGER, OutputType.OUTPUT));
        {
            val firstRequestParams = new Row();
            sqlRequest.getRows().add(firstRequestParams);
            firstRequestParams.addValue(null);
            firstRequestParams.addValue(new aero.minova.core.application.system.domain.Value(0, null));
        }
        // Hiermit wird der unsichere Zugriff ermöglicht.
        val requestingAuthority = new Row();
        requestingAuthority.addValue(new aero.minova.core.application.system.domain.Value(false, "1"));
        requestingAuthority.addValue(new aero.minova.core.application.system.domain.Value(false, "2"));
        requestingAuthority.addValue(new aero.minova.core.application.system.domain.Value(false, "3"));


        sqlViewController.unsecurelyGetIndexView(sqlRequest, Arrays.asList(requestingAuthority))
                .getRows()
                .stream()
                .map(row -> row.getValues().get(0).getIntegerValue())
                .forEach(terminKey -> {
                    try {
                        sendCertificateByMail
                                (testCertificatePrintService.getTestCertificatePath(terminKey).toFile(), terminKey);
                        markTerminAsSent(terminKey);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private void markTerminAsSent(Integer terminKey) {
        val sqlRequest = new Table();
        sqlRequest.setName("xpctsUpdateTestTerminAsSent");
        sqlRequest.addColumn(new Column("KeyLong", DataType.INTEGER));
        {
            val requestParam = new Row();
            sqlRequest.getRows().add(requestParam);
            requestParam.addValue(new aero.minova.core.application.system.domain.Value(terminKey, null));
        }
        // Hiermit wird der unsichere Zugriff ermöglicht.
        val requestingAuthority = new Row();
        requestingAuthority.addValue(new aero.minova.core.application.system.domain.Value(false, "1"));
        requestingAuthority.addValue(new aero.minova.core.application.system.domain.Value(false, "2"));
        requestingAuthority.addValue(new aero.minova.core.application.system.domain.Value(false, "3"));
        final String reportBody;
        try {
            sqlProcedureController.calculateSqlProcedureResult(sqlRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* TODO REMOVE public void sendCertificateByMail(File testCertificatePdf, Integer ergebnisKey) throws Exception {
        val ergebnisRequest = new Table();
        ergebnisRequest.setName("xvctsTestErgebnisCasIndex");
        ergebnisRequest.addColumn(new Column("KeyLong", DataType.INTEGER));
        ergebnisRequest.addColumn(new Column("Email", DataType.STRING));
        val ergebnisRequestData = new Row();
        ergebnisRequest.addRow(ergebnisRequestData);
        ergebnisRequestData.addValue(new aero.minova.core.application.system.domain.Value(ergebnisKey, null));
        ergebnisRequestData.addValue(new aero.minova.core.application.system.domain.Value((String) null, null));
        val targetEmail = sqlProcedureController
                .calculateSqlProcedureResult(ergebnisRequest)
                .getResultSet()
                .getRows()
                .get(0)
                .getValues()
                .get(1)
                .getStringValue();
        sendCertificateByMail(testCertificatePdf, targetEmail);
    }*/

    public List<String> targetAddresses(Integer testTerminKeyLong) {
        val sqlRequest = new Table();
        sqlRequest.setName("xpctsReadCertificateTargetAddresses");
        sqlRequest.addColumn(new Column("TerminKey", DataType.INTEGER));
        sqlRequest.addColumn(new Column("TestErgebnisPositiveKey", DataType.INTEGER));
        sqlRequest.addColumn(new Column("TestErgebnisNegativeKey", DataType.INTEGER));
        {
            val requestParam = new Row();
            sqlRequest.getRows().add(requestParam);
            requestParam.addValue(new aero.minova.core.application.system.domain.Value(testTerminKeyLong, null));
            requestParam.addValue(new aero.minova.core.application.system.domain.Value(Integer.parseInt(testErgebnisNegativeKey), null));
            requestParam.addValue(new aero.minova.core.application.system.domain.Value(Integer.parseInt(testErgebnisPositiveKey), null));
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
            val rawAddresses = t
                    .getResultSet()
                    .getRows()
                    .stream()
                    .map(row -> row
                            .getValues()
                            .get(0)
                            .getStringValue())
                    .filter(e -> e != null)
                    .reduce("", (a, b) -> a + "; " + b)
                    .split(";");
            return Arrays.asList(rawAddresses).stream()
                    .filter(e -> e != null)
                    .map(e -> e.trim())
                    .filter(e -> !e.isEmpty())
                    .distinct()
                    .collect(toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendCertificateByMail(File testCertificatePdf, Integer testTerminKeyLong) {
        try {
            logger.info("Sending mail for termin: " + testTerminKeyLong);
            val targetAddresses = targetAddresses(testTerminKeyLong);
            val message = mailSender.createMimeMessage();
            {
                val helper = new MimeMessageHelper(message, true);
                helper.setTo(targetAddresses.toArray(new String[targetAddresses.size()]));
                helper.setFrom(mailAddress);
                helper.setSubject("COVID-Test-Zertifikat");
                helper.setText("<h1>Hallo,</h1><p>das Ergebnis des Corona-Tests ist im Anhang der Mail.</p>", true);
                helper.addAttachment("COVID-Test-Zertifikat.pdf", testCertificatePdf);
            }
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("Could not send certificate pdf.", e);
        }
    }
}