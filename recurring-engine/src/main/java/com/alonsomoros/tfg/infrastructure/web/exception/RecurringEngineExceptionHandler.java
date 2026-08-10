package com.alonsomoros.tfg.infrastructure.web.exception;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.alonsomoros.tfg.application.exception.PaymentGatewayException;
import com.alonsomoros.tfg.application.exception.PaymentMethodNotFoundException;
import com.alonsomoros.tfg.application.exception.UnsupportedPaymentProviderException;
import com.alonsomoros.tfg.domain.exception.PaymentMethodAlreadyExistsException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class RecurringEngineExceptionHandler {

    @ExceptionHandler(PaymentMethodNotFoundException.class)
    public ResponseEntity<String> handlePaymentMethodNotFound(PaymentMethodNotFoundException e) {
        log.info("[PaymentMethodNotFound] {}", e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(PaymentMethodAlreadyExistsException.class)
    public ResponseEntity<String> handlePaymentMethodAlreadyExists(PaymentMethodAlreadyExistsException e) {
        log.warn("Payment method already exists: {}", e.getMessage());
        return ResponseEntity.status(409).body("Payment method already exists");
    }

    @ExceptionHandler(UnsupportedPaymentProviderException.class)
    public ResponseEntity<String> handleUnsupportedPaymentProvider(UnsupportedPaymentProviderException e) {
        log.warn("Unsupported payment provider requested: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(PaymentGatewayException.class)
    public ResponseEntity<String> handlePaymentGatewayError(PaymentGatewayException e) {
        log.error("Payment gateway error: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Payment gateway error occurred");
    }

    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<String> handleDatabaseDown(DataAccessResourceFailureException e) {
        log.error("Critical failure while connecting to the database: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Critical failure while connecting to the database");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericError(Exception ex) {
        log.error("Unhandled exception while processing request", ex);
        return ResponseEntity.internalServerError().body("Internal server error occurred: " + ex.getMessage());
    }
}
