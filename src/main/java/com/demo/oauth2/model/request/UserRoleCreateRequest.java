package com.demo.oauth2.model.request;

import java.io.Serializable;

@lombok.Data
public class UserRoleCreateRequest implements Serializable {
    /**
    * 
    */
    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    private String parentTenant;

}
