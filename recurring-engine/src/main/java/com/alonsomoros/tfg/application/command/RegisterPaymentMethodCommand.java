package com.alonsomoros.tfg.application.command;

import java.util.UUID;

public record RegisterPaymentMethodCommand(
        UUID subscriptionId,
        String provider,
        String token,
        String last4) {
}
