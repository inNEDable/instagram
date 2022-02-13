package com.example.instagramproject.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = {EmailExists.class, UsernameExists.class, InvalidEmail.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        String s = "Registration failed! " + ex.getMessage();
        return handleExceptionInternal(ex, s, new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE, request);
    }

}
