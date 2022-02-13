package com.example.instagramproject.exceptions;

public class UsernameExists extends RuntimeException{

    public UsernameExists() {
    }

    public UsernameExists(String message) {
        super(message);
    }

    public UsernameExists(String message, Throwable cause) {
        super(message, cause);
    }
}
