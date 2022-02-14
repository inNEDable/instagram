package com.example.instagramproject.exceptions;

import com.example.instagramproject.model.DTO.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = {InvalidUserData.class})
    protected ResponseEntity<ErrorDTO> handleConflict(RuntimeException ex, WebRequest request) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage("Invalid user data: " + ex.getMessage());
        errorDTO.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        return new ResponseEntity<>(errorDTO,HttpStatus.NOT_ACCEPTABLE );
    }


}