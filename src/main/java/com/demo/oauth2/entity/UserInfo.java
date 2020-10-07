package com.demo.oauth2.entity;

import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "UserInfo")
@Table(name = "user_info")
@lombok.Data
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @ElementCollection(targetClass = Long.class)
    private List<Long> roles;
    private Integer parentTenant;
    private String firstName;
    private String lastName;
    private String imageUrl;
    @ElementCollection(targetClass = String.class)
    private List<String> authorities;
    private String email;
    private String mobile;
    private boolean isMobileVerified;
    private boolean isEmailVerified;
    private Long otpDetails;
}
