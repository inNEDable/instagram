package com.example.instagramproject.exceptions;

import com.example.instagramproject.model.DTO.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorHandlerException extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = {InvalidDataException.class})
    protected ResponseEntity<ErrorDTO> handleConflict(RuntimeException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage("Invalid data: " + ex.getMessage());
        errorDTO.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        return new ResponseEntity<>(errorDTO,HttpStatus.NOT_ACCEPTABLE );
    }

    @ExceptionHandler(value = {UnauthorizedAccessException.class})
    protected ResponseEntity<ErrorDTO> unauthorizedAccess (RuntimeException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage("Unauthorized access : " + ex.getMessage());
        errorDTO.setStatus(HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(errorDTO,HttpStatus.UNAUTHORIZED );
    }


}
