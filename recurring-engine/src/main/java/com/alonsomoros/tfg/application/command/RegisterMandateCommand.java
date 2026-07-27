package com.alonsomoros.tfg.application.command;

public record RegisterMandateCommand(
        Long subscriptionId,
        String provider,
        String token,
        String last4) {
}
