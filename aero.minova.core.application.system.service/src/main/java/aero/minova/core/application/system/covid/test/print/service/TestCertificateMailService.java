package aero.minova.core.application.system.covid.test.print.service;

import aero.minova.core.application.system.controller.SqlProcedureController;
import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.Table;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class TestCertificateMailService {
    final Logger logger = LoggerFactory.getLogger(TestCertificateMailService.class);

    @Value("${spring.mail.username:}")
    public String mailAddress;

    @Autowired
    private SqlProcedureController sqlProcedureController;

    @Autowired
    private JavaMailSender mailSender;

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

    public void sendCertificateByMail(File testCertificatePdf, List<String> targetAddresses) {
        try {
            val message = mailSender.createMimeMessage();
            {
                val helper = new MimeMessageHelper(message, true);
                helper.setTo(targetAddresses.toArray(new String[targetAddresses.size()]));
                helper.setFrom(mailAddress);
                helper.setSubject("COVID-Test-Zertifikat");
                helper.setText("<h1>Hallo,</h1><p>das Ergebnis des Covidtest ist im Anhang der Mail.</p>", true);
                helper.addAttachment("COVID-Test-Zertifikat.pdf", testCertificatePdf);
            }
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("Could not send certificate pdf.", e);
        }
    }
}