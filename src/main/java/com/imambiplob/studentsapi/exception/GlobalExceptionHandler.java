package com.imambiplob.studentsapi.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handling custom validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> customValidationErrorHandling(MethodArgumentNotValidException exception) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Input Validation Failed",
                Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    //Handling bad credentials exception
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> customBadCredentialsExceptionHandling(BadCredentialsException exception) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Email or Password is INCORRECT!!!", exception.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    //Handling username not found exception
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> customUsernameNotFoundExceptionHandling(UsernameNotFoundException exception) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "User Not Found!!!", exception.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // Handling missing request header exception
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<?> customMissingRequestHeaderExceptionHandling(MissingRequestHeaderException exception) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "No header provided!!!", exception.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // Handling malformed JWT exception
    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<?> customMalformedJwtExceptionHandling(MalformedJwtException exception) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Invalid Token!!!", exception.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // Handling expired JWT exception
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> customExpiredJwtExceptionHandling(ExpiredJwtException exception) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Token Expired!!!", exception.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // Handling access control exception
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> customAccessControlHandling(AccessDeniedException exception) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Access to this endpoint is SECURED!!!", exception.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    // Handling global exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandling(Exception exception, WebRequest request){
        ErrorDetails errorDetails =
                new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}