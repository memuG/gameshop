package com.przemo.gameshop.web.exceptions;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.security.InvalidParameterException;
import java.util.NoSuchElementException;

//@ControllerAdvice(basePackages = "com.przemo.gameshop.web")
public class GameShopControllerAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation() {
        String responseBody = "\"Invalid request parameter provided\"";
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<?> handleResourceNotFoundByDelete(EmptyResultDataAccessException exc) {
        String responseBody = "\""+ exc.getMessage() +"\"";
        return new ResponseEntity<>(responseBody, HttpStatus.valueOf(204));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException exc) {
        String responseBody = "\""+ exc.getMessage() +"\"";
        return new ResponseEntity<>(responseBody, HttpStatus.valueOf(204));
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<?> handleEntityNotFound(InvalidParameterException exc) {
        String responseBody = "\""+ exc.getMessage() +"\"";
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleResourceNotFoundInRepository() {
        String responseBody = "\"Element could not be found\"";
        return new ResponseEntity<>(responseBody, HttpStatus.valueOf(204));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleJsonNotReadable() {
        String responseBody = "\"JSON request body could not be parsed\"";
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleBaseException() {
        String responseBody = "\"Something went terribly wrong...please try again\"";
        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
