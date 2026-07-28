package com.alonsomoros.tfg.infrastructure.web.exception;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.alonsomoros.tfg.application.exception.PaymentMethodNotFoundException;
import com.alonsomoros.tfg.domain.exception.PaymentMethodAlreadyExistsException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class RecurringEngineExceptionHandler {

    @ExceptionHandler(PaymentMethodNotFoundException.class)
    public ResponseEntity<String> handlePaymentMethodNotFound(PaymentMethodNotFoundException e) {
        log.warn("Payment method not found: {}", e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(PaymentMethodAlreadyExistsException.class)
    public ResponseEntity<String> handlePaymentMethodAlreadyExists(PaymentMethodAlreadyExistsException e) {
        log.warn("Payment method already exists: {}", e.getMessage());
        return ResponseEntity.status(409).body("Payment method already exists");
    }

    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<String> handleDatabaseDown(DataAccessResourceFailureException e) {
        log.error("Critical failure while connecting to the database: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Critical failure while connecting to the database");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericError(Exception ex) {
        log.error("Uncontrolled error occurred", ex);
        return ResponseEntity.internalServerError().body("Internal server error occurred: " + ex.getMessage());
    }
}
