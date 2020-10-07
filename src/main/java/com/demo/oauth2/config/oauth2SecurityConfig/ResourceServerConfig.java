package com.demo.oauth2.config.oauth2SecurityConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableResourceServer
@Slf4j
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${auth.resourceId:resource_id}")
    private String resourceId;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        log.info("::::::configure ReserouceServerConfig::::::");
        http.authorizeRequests().antMatchers("/**").permitAll().and().exceptionHandling()
                        .accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        log.info(":::::Configure ResourceServerSecurityConfigurer:::::");
        resources.resourceId(resourceId).stateless(false);
    }

}
