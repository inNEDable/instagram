package com.example.instagramproject.exceptions;

public class UnauthorizedAccess extends RuntimeException{

    public UnauthorizedAccess() {
    }

    public UnauthorizedAccess(String message) {
        super(message);
    }

    public UnauthorizedAccess(String message, Throwable cause) {
        super(message, cause);
    }
}
