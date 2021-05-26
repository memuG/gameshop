package com.przemo.gameshop.web.exceptions;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.security.InvalidParameterException;
import java.util.NoSuchElementException;

@ControllerAdvice(basePackages = "com.przemo.gameshop.web")
public class GameShopControllerAdvice {

    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class,
            InvalidParameterException.class,
            HttpMessageNotReadableException.class})
    public ResponseEntity<?> handleRequestConstraintViolation(Exception exc) throws Exception {
        throw exc;
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

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleResourceNotFoundInRepository() {
        String responseBody = "\"Element could not be found\"";
        return new ResponseEntity<>(responseBody, HttpStatus.valueOf(204));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleBaseException() {
        String responseBody = "\"Something went terribly wrong...please try again\"";
        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
