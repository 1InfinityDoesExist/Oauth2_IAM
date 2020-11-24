package com.demo.oauth2.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@lombok.Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRoleUpdateRequest {
	private String description;

}
