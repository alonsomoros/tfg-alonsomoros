package com.alonsomoros.tfg.domain.exception;

public class SubscriptionAlreadyOngoingException extends RuntimeException {
    public SubscriptionAlreadyOngoingException(String message) {
        super(message);
    }
}
