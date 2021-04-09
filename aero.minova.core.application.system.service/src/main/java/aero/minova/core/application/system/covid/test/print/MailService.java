package aero.minova.core.application.system.covid.test.print;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class MailService {
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