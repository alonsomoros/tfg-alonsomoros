package com.alonsomoros.tfg.application.command;

public record PaymentDetailsCommand(
    String provider,
    String token,
    String cardHolder,
    String expiryMonth,
    String expiryYear,
    String last4
) {}
