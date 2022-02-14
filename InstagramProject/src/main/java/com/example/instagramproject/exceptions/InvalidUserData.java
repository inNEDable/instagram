package com.example.instagramproject.exceptions;

public class EmailExists extends RuntimeException{

    public EmailExists() {
    }

    public EmailExists(String message) {
        super(message);
    }

    public EmailExists(String message, Throwable cause) {
        super(message, cause);
    }
}
