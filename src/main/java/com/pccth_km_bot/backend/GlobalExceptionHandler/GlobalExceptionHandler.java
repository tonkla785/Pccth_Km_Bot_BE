package com.pccth_km_bot.backend.GlobalExceptionHandler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();

        e.getBindingResult()
                .getFieldErrors()
                .forEach(err ->
                        errors.put(err.getField(), err.getDefaultMessage())
                );

        return ResponseEntity.badRequest().body(Map.of(
                "responseStatus", 400,
                "responseMessage", "Validation failed",
                "errors", errors
        ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraint(ConstraintViolationException e) {

        Map<String, String> errors = new HashMap<>();

        e.getConstraintViolations().forEach(v ->
                errors.put(
                        v.getPropertyPath().toString(),
                        v.getMessage()
                )
        );

        return ResponseEntity.badRequest().body(Map.of(
                "responseStatus", 400,
                "responseMessage", "Validation failed",
                "errors", errors
        ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of(
                "responseStatus", 400,
                "responseMessage", e.getMessage()
        ));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNotFound(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "responseStatus", 404,
                "responseMessage", e.getMessage()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "responseStatus", 500,
                "responseMessage", e.getMessage()
        ));
    }
}
