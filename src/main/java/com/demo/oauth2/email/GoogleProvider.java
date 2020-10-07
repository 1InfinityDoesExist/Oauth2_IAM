package com.demo.oauth2.email;

import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class GoogleProvider implements EmailProvider {

    public Map<String, String> getGmailProperties() {
        propertiesMap.put("mail.smtp.auth", "true");
        propertiesMap.put("mail.smtp.starttls.enable", "true");
        propertiesMap.put("mail.smtp.setTLS", "true");
        return propertiesMap;
    }
}
