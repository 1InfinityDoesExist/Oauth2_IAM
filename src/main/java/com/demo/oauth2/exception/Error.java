package com.demo.oauth2.exception;

import lombok.Getter;

@Getter
public enum Error {

    INVALID_INPUT(401, "invalid_input");

    private int statusCode;
    private String message;

    Error(int statusCode, String message) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
