package aero.minova.core.application.system.covid.test.print;

import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class TestCertificateMailService {
    final Logger logger = LoggerFactory.getLogger(TestCertificateMailService.class);

    @Value("${spring.mail.username:}")
    public String mailAddress;

    @Autowired
    private JavaMailSender mailSender;

    public void sendCertificateByMail(File testCertificatePdf) {
        throw new RuntimeException();
    }

    public void sendCertificateByMail(File testCertificatePdf, String... targetAddresses) {
        try {
            val message = mailSender.createMimeMessage();
            {
                val helper = new MimeMessageHelper(message, true);
                helper.setTo(targetAddresses);
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