package com.alonsomoros.tfg.domain.model;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethod {

    @NotNull
    @Schema(description = "Identificador único del método de pago", example = "9ad19705-dcbe-448c-a7b2-d625e45c05d7")
    private UUID id;

    @NotNull
    @Schema(description = "Identificador único de la suscripción", example = "2d788100-41a4-4faa-af57-359a61e6f4f7")
    private UUID subscriptionId;

    @NotBlank
    @Schema(description = "Proveedor del servicio de pago (ej. stripe, paypal)", example = "stripe")
    private String provider;

    @NotBlank
    @Schema(description = "Token seguro generado por la pasarela de pagos", example = "card_1234567890")
    private String token;

    @Size(min = 4, max = 4)
    @Schema(description = "Últimos 4 dígitos de la tarjeta o método de pago", example = "1234")
    private String last4;

    @Schema(description = "Indica si el método de pago está activo", example = "true")
    private boolean active;
}
