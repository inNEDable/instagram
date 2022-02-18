package com.example.instagramproject.exceptions;

public class InvalidData extends RuntimeException{

    public InvalidData() {
    }

    public InvalidData(String message) {
        super(message);
    }

    public InvalidData(String message, Throwable cause) {
        super(message, cause);
    }
}
