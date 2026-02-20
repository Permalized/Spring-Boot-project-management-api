package com.projects.ProjectManagementAPI.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception){
        ErrorResponse resourceNotFound = new ErrorResponse(LocalDateTime.now(), exception.getMessage(), "Resource Not Found");
        return new ResponseEntity<>(resourceNotFound, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<?> handleUnauthorizedActionException(UnauthorizedActionException exception){
        ErrorResponse unauthorized = new ErrorResponse(LocalDateTime.now(), exception.getMessage(), "Unauthorized Action");
        return new ResponseEntity<>(unauthorized, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<?> handleDuplicateResourceException(DuplicateResourceException exception){
        ErrorResponse duplicate = new ErrorResponse(LocalDateTime.now(), exception.getMessage(), "Duplicate Resource");
        return new ResponseEntity<>(duplicate, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<?> handleAuthenticationFailedException(AuthenticationFailedException exception){
        ErrorResponse authenticationFailed = new ErrorResponse(LocalDateTime.now(), exception.getMessage(), "Authentication Failed");
        return new ResponseEntity<>(authenticationFailed, HttpStatus.UNAUTHORIZED);
    }

}
