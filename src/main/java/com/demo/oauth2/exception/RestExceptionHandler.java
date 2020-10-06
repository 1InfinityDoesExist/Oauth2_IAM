package com.demo.oauth2.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Object> handleApplicationException(ApplicationException ex) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(ex.getMessage());
        apiResponse.setDebugMessage(ex.getDebugMessage());
        return buildResponseEntity(apiResponse, ex.getHttpStatus());
    }

    private ResponseEntity<Object> buildResponseEntity(ApiResponse apiResponse,
                    HttpStatus httpStatus) {
        return new ResponseEntity<Object>(apiResponse, httpStatus);
    }

}
