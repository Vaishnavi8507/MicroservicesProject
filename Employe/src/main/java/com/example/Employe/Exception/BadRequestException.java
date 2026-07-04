package com.example.Employe.Exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RuntimeException{
    private String message;
    private HttpStatus status;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public BadRequestException(String message, HttpStatus serviceUnavailable) {
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
