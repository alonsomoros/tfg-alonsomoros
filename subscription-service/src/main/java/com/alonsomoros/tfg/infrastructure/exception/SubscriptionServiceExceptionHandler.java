package com.alonsomoros.tfg.infrastructure.exception;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.alonsomoros.tfg.application.exception.SubscriptionNotFoundException;
import com.alonsomoros.tfg.domain.exception.SubscriptionAlreadyOngoingException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class SubscriptionServiceExceptionHandler {

    @ExceptionHandler(SubscriptionNotFoundException.class)
    public ResponseEntity<String> handleSubscriptionNotFound(SubscriptionNotFoundException e) {
        log.warn("Subscription not found: {}", e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(SubscriptionAlreadyOngoingException.class)
    public ResponseEntity<String> handleSubscriptionAlreadyOngoing(SubscriptionAlreadyOngoingException e) {
        log.warn("Subscription is already ongoing: {}", e.getMessage());
        return ResponseEntity.status(409).body("Subscription is already ongoing");
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
