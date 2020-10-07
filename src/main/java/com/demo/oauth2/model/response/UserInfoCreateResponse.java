package com.demo.oauth2.model.response;

import java.io.Serializable;

@lombok.Data
public class UserInfoCreateResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private String userName;
    private String email;
    private String mobile;

}
