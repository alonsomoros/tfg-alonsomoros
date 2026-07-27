package com.alonsomoros.tfg.application.command;

public record RegisterPaymentMethodCommand(
        Long subscriptionId,
        String provider,
        String token,
        String last4) {
}
