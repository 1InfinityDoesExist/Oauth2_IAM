package com.demo.oauth2.model.request;

import java.io.Serializable;
import java.util.List;

@lombok.Data

public class UserInfoCreateRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
