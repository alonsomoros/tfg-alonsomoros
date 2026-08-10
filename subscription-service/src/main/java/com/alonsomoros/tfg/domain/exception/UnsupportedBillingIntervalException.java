package com.alonsomoros.tfg.domain.exception;

public class UnsupportedBillingIntervalException extends RuntimeException {

    public UnsupportedBillingIntervalException(String billingInterval) {
        super("Unsupported billing interval: " + billingInterval);
    }
}