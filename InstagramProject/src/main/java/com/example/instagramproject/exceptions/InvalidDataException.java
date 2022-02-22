package com.example.instagramproject.exceptions;

public class InvalidDataException extends RuntimeException{

    public InvalidDataException() {
    }

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
