package com.agn1kobi.e_commerce_backend.config.api;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String FIELD = "field";
    private static final String MESSAGE = "message";
    private static final String ERRORS = "errors";
    private static final String MALFORMED_REQUEST = "Malformed request";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
    List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
        .map(err -> Map.of(
            FIELD, err.getField(),
            MESSAGE, err.getDefaultMessage() == null ? "Invalid value" : err.getDefaultMessage()
        ))
                .toList();

    Map<String, Object> body = new HashMap<>();

    body.put(ERRORS, errors);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        List<Map<String, String>> errors = ex.getConstraintViolations().stream()
        .map(v -> Map.of(
            FIELD, v.getPropertyPath() == null ? "" : v.getPropertyPath().toString(),
            MESSAGE, v.getMessage() == null ? "Invalid value" : v.getMessage()
        ))
                .toList();

    Map<String, Object> body = new HashMap<>();

    body.put(ERRORS, errors);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, BindException.class})
    public ResponseEntity<Map<String, Object>> handleMalformedRequest(Exception ex) {
        Map<String, Object> body = new HashMap<>();

        body.put(MESSAGE, MALFORMED_REQUEST);
        
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }
}
