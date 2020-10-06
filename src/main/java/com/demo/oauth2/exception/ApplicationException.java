package com.demo.oauth2.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private HttpStatus httpStatus;
    private Error error;
    private String debugMessage;

    ApplicationException(HttpStatus status, String debugMessage) {
        super(debugMessage, null);
        this.httpStatus = status;
        this.debugMessage = debugMessage;
    }

    ApplicationException(HttpStatus status, Error error) {
        super(error.getMessage(), null);
        this.httpStatus = status;
        this.error = error;
    }

    ApplicationException(HttpStatus status, Error error, String debugMessage) {
        super(error.getMessage(), null);
        this.httpStatus = status;
        this.debugMessage = debugMessage;
        this.error = error;
    }


}
