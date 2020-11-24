package com.demo.oauth2.model.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@lombok.Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoUpdateRequest {

	private String username;
	private String password;
	private List<String> roles;
	private String parentTenant;
	private String firstName;
	private String lastName;
	private String imageUrl;
	private List<String> authorities;
	private String email;
	private String mobile;
	private boolean isMobileVerified;
	private boolean isEmailVerified;
	private Long otpDetails;
}
