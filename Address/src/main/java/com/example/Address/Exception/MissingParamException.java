package com.example.Address.Exception;

import org.springframework.http.HttpStatus;

public class MissingParamException extends RuntimeException {
    private String message;
    private HttpStatus status;

    public MissingParamException(String message) {
        this.message = message;
        this.status = HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
