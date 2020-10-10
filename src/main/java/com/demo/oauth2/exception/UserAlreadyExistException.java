package com.demo.oauth2.exception;

public class UserAlreadyExistException extends OauthBaseException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public UserAlreadyExistException(String msg) {
        super(msg);
    }

}
