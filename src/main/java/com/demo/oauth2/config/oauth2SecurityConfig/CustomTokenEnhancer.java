package com.demo.oauth2.config.oauth2SecurityConfig;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
                    OAuth2Authentication authentication) {
        log.info(":::::CustomTokenEnhancer Class, enhance method ::::");
        String authDetails = authentication.getUserAuthentication().getDetails().toString();
        log.info("::::authDetails {}", authDetails);
        Map<String, Object> additionalInfo = new HashMap<>();
        String parentTenant = authDetails.split("tenantId=")[1].split(",")[0];
        log.info("::::::tenantId {}", parentTenant);
        additionalInfo.put("parentTenant", parentTenant);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        log.info("::::::::accessToken {}", accessToken);
        return accessToken;
    }
}
