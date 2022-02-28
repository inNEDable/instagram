package com.example.instagramproject.exceptions;

public class PicturePurifyException extends RuntimeException{

    public PicturePurifyException() {
    }

    public PicturePurifyException(String message) {
        super(message);
    }

    public PicturePurifyException(String message, Throwable cause) {
        super(message, cause);
    }
}
