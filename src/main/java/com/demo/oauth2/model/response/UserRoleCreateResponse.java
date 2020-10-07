package com.demo.oauth2.model.response;

import java.io.Serializable;
import org.springframework.stereotype.Component;

@lombok.Data
public class UserRoleCreateResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String roleName;
    private String msg;

}
