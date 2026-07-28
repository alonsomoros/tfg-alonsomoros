package com.alonsomoros.tfg.domain.exception;

public class PaymentMethodAlreadyExistsException extends RuntimeException {
    public PaymentMethodAlreadyExistsException(String message) {
        super(message);
    }
}
