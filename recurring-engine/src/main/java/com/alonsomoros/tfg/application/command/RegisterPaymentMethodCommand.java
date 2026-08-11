package com.alonsomoros.tfg.application.command;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterPaymentMethodCommand(
        
        @NotNull(message = "El ID de la suscripción no puede ser nulo")
        @Schema(description = "Identificador único de la suscripción", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID subscriptionId,

        @NotBlank
        @Schema(description = "Proveedor del servicio de pago (ej. stripe, paypal)", example = "stripe")
        String provider,

        @NotBlank
        @Schema(description = "Token seguro generado por la pasarela de pagos", example = "tok_1234567890")
        String token,

        @Size(min = 4, max = 4)
        @Schema(description = "Últimos 4 dígitos de la tarjeta o método de pago", example = "4242")
        String last4
) {}
