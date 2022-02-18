package com.example.instagramproject.exceptions;

public class InvalidUserData extends RuntimeException{

    public InvalidUserData() {
    }

    public InvalidUserData(String message) {
        super(message);
    }

    public InvalidUserData(String message, Throwable cause) {
        super(message, cause);
    }
}
