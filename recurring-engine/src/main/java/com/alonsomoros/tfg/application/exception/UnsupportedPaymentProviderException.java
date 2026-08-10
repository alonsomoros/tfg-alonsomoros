package com.alonsomoros.tfg.application.exception;

public class UnsupportedPaymentProviderException extends RuntimeException {

    public UnsupportedPaymentProviderException(String providerName) {
        super("Unsupported payment provider: " + providerName);
    }
}