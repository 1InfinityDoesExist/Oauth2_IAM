package com.demo.oauth2.exception;


/*
 * Note : Do not mention @Component at class level
 */
public class OauthBaseException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public OauthBaseException(String msg) {
        super(msg);
    }
}
