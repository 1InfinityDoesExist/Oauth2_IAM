package com.demo.oauth2.model;

import org.springframework.stereotype.Component;

@Component
@lombok.Data
public class UserVerificationDetails {
    private String parentTenant;
    private String email;
    private String mobile;
    private Integer emailOtp;
    private Integer moibleOtp;
    private String userName;
}
