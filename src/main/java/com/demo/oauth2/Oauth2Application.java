package com.demo.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.SpringVersion;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import lombok.extern.slf4j.Slf4j;

@EnableAuthorizationServer
@EnableResourceServer
@SpringBootApplication
@Slf4j
@ComponentScan({ "com.fb.demo", "com.demo.oauth2" })
public class Oauth2Application {

	public static void main(String[] args) {
		SpringApplication.run(Oauth2Application.class, args);
		log.info(":::::Spring Version : {}", SpringVersion.getVersion());
	}

}
