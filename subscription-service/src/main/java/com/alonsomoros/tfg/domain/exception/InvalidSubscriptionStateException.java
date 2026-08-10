package com.alonsomoros.tfg.domain.exception;

public class InvalidSubscriptionStateException extends RuntimeException {

    public InvalidSubscriptionStateException(String message) {
        super(message);
    }
}