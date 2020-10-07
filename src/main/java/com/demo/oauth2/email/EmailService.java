package com.demo.oauth2.email;

import java.util.List;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.thymeleaf.util.StringUtils;
import com.demo.oauth2.util.Constants;

@Component
public class EmailService {
    @Autowired
    private GoogleProvider googleProvider;

    @Value("${reset.password.email.host}")
    private String host;
    @Value("${reset.password.email.port}")
    private int port;
    @Value("${reset.password.email.id}")
    private String emailId;
    @Value("${reset.password.email.pwd}")
    private String password;
    @Value("${reset.password.email.provider}")
    private String provider;
    @Value("${reset.password.email.subject}")
    private String subject;

    public void sendMail(ModelMap modelMap) {

        JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
        javaMailSenderImpl.setHost(host);
        javaMailSenderImpl.setPort(port);
        javaMailSenderImpl.setUsername(emailId);
        javaMailSenderImpl.setPassword(password);
        javaMailSenderImpl.setJavaMailProperties(getJavaMailProperties(provider));
        MimeMessage mimeMessage = javaMailSenderImpl.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setSubject((String) modelMap.get("subject"));
            helper.setText((String) modelMap.get("mailBody"));
            if (modelMap.get("to") instanceof List) {
                helper.setTo(((String) modelMap.get("to")).split(","));
            } else {
                helper.setTo((String) modelMap.get("to"));
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSenderImpl.send(mimeMessage);
    }

    private Properties getJavaMailProperties(String provider) {
        Properties properties = new Properties();
        if (provider.equalsIgnoreCase(StringUtils.capitalize(Constants.GOOGLE))) {
            getJavaMailProperties(properties);
        }
        return properties;
    }

    private void getJavaMailProperties(Properties properties) {
        googleProvider.getGmailProperties().forEach((key, value) -> {
            properties.setProperty(key, value);
        });
    }
}
