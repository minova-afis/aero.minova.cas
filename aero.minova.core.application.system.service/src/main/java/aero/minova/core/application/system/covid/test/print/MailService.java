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
    @Value("${mailAddress:}")
    private String mailAddress;
    @Value("${mailPassword:}")
    private String mailPassword;
    @Value("${mailTransportProtocol:smtp}")
    private String mailTransportProtocol;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailServerHost);
        mailSender.setPort(Integer.valueOf(mailServerPort));

        mailSender.setUsername(mailAddress);
        mailSender.setPassword(mailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", mailTransportProtocol);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}