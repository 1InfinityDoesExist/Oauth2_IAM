package com.demo.oauth2.config.oauth2SecurityConfig;

import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@Slf4j
public class OAuth2SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Resource(name = "oauth2_service_impl")
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder encoder() {
        log.info("::::::Bean Creation of BCryptPasswordEncoder::::");
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        log.info(":::::globalUserDetails method::::");
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        log.info("::::::Bean Creation of Authentication Manager::::");
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info(":::::configure::::");
        http.headers().httpStrictTransportSecurity()
                        .disable().and().authorizeRequests()
                        .antMatchers("/**", "/resources/**").permitAll().and().cors();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        log.info("::::corsConfigurer:::::");
        return new WebMvcConfigurerAdapter() {
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedHeaders("*")
                                .allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS")
                                .allowedOrigins("*")
                                .exposedHeaders("Content-Type", "Content-Length", "Authorization");
            }
        };
    }
}
