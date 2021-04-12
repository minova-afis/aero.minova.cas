package aero.minova.core.application.system.covid.test.print;

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

@Service
public class CovidTestMailService {
    final Logger logger = LoggerFactory.getLogger(CovidTestMailService.class);

    @Value("${mailServerHost:}")
    private String mailServerHost;
    @Value("${mailServerPort:}")
    private String mailServerPort;
    @Value("${spring.mail.username:}")
    public String mailAddress;
    @Value("${mailPassword:}")
    private String mailPassword;
    @Value("${mailTransportProtocol:smtp}")
    private String mailTransportProtocol;
    @Value("${mail.smtp.auth:true}")
    private String mailSmtpAuth;
    @Value("${mail.smtp.starttls.enable:true}")
    private String mailSmtpStarttlEnable;
    @Value("${mail.debug:false}")
    private String mailDebug;

    @Autowired
    private JavaMailSender mailSender;

    public void sendCertificateByMail(File testCertificatePdf) {
        try {
            val message = mailSender.createMimeMessage();
            {
                val helper = new MimeMessageHelper(message, true);
                helper.setTo("afis@minova.de");
                helper.setFrom(mailAddress);
                helper.setSubject("COVID-Test-Zertifikat");
                helper.setText("Test", true);
                helper.addAttachment("COVID-Test-Zertifikat.pdf", testCertificatePdf);
            }
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("Could not send certificate pdf.", e);
        }
    }

    /*@Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailServerHost);
        if (mailServerPort != null || mailServerPort.isEmpty()) {
            mailSender.setPort(Integer.parseInt(mailServerPort));
        }

        mailSender.setUsername(mailAddress);
        mailSender.setPassword(mailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", mailTransportProtocol);
        props.put("mail.smtp.auth", mailSmtpAuth);
        props.put("mail.smtp.starttls.enable", mailSmtpStarttlEnable);
        props.put("mail.debug", mailDebug);

        return mailSender;
    }*/
}