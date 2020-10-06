package com.demo.oauth2.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

@lombok.Data
public class ApiResponse {
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;

    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    private ApiResponse(HttpStatus status) {
        this();
        this.status = status;
    }
}
