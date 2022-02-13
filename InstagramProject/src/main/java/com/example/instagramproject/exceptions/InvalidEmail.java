package com.example.instagramproject.exceptions;

public class InvalidEmail extends RuntimeException{

    public InvalidEmail() {
    }

    public InvalidEmail(String message) {
        super(message);
    }

    public InvalidEmail(String message, Throwable cause) {
        super(message, cause);
    }
}
