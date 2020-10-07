package com.demo.oauth2.config.oauth2SecurityConfig;

import java.util.Arrays;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableAuthorizationServer
@Slf4j
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {


    /*
     * 0oa71ctnSxLOSmSwE5d5
     */
    @Value("${auth.clientId}")
    private String clientId;

    /*
     * $2y$12$JeKuQ5yUQLuZ03cEsRgUm.JX6lm/C6qN3ALHWaWQmHQNrPsc3EKjG
     */
    @Value("${auth.clientSecret}")
    private String clientSecret;

    @Value("${auth.grantTypePassword}")
    private String grantTypePassword;

    @Value("${auth.authorizationCode:authorization_code}")
    private String authorizationCode;

    @Value("${auth.refreshToken:refresh_token}")
    private String refreshToken;

    @Value("${auth.implicit:implicit}")
    private String implicit;

    @Value("${auth.scopeRead:read}")
    private String scopeRead;

    @Value("${auth.scopeWrite:write}")
    private String scopeWrite;

    @Value("${auth.trust:trust}")
    private String trust;

    @Value("${auth.accessTokenValiditySeconds:42000}")
    private int accessTokenValiditySeconds;

    @Value("${auth.refreshTokenValiditySeconds:216000}")
    private int refreshTokenValiditySeconds;

    @Value("${auth.jwt.signingKey}")
    private String signingKey;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        log.info("::::::configure(),AuthorizationServerSecurityConfigurer::::::");
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        log.info("::::::configure(),ClientDetailsServiceConfigurer::::::");
        clients.inMemory().withClient(clientId)
                        .secret(new BCryptPasswordEncoder().encode(clientSecret))
                        .authorizedGrantTypes(grantTypePassword, authorizationCode, refreshToken,
                                        implicit)
                        .scopes(scopeRead, scopeWrite, trust)
                        .accessTokenValiditySeconds(accessTokenValiditySeconds)
                        .refreshTokenValiditySeconds(refreshTokenValiditySeconds);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        log.info("::::::configure(),AuthorizationServerEndpointsConfigurer::::::");
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain
                        .setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
        endpoints.tokenStore(tokenStore()).tokenEnhancer(tokenEnhancerChain)
                        .authenticationManager(authenticationManager);
    }


    @Bean
    public TokenEnhancer tokenEnhancer() {
        log.info(":::::token Enhancer :::::");
        return new CustomTokenEnhancer();
    }

    @Bean
    public TokenStore tokenStore() {
        log.info("::::Token Store:::::");
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        log.info(":::::Inside JwtAccessTokenConverter:::::");
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        log.info("Signingkey : {}", signingKey);
        converter.setSigningKey(new String(Base64.getDecoder().decode(signingKey)));
        log.info("::::::::::::::::::::converter :::::::::::::: " + converter);
        return converter;
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        log.info(":::::token Services::::::");
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

}
