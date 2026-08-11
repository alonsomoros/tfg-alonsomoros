package com.alonsomoros.tfg.infrastructure.web.dto.request;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PaymentMethodRequestDto(

    @NotNull
    @Schema( description = "UUID of the subscription for which the payment method is being added", example = "c3f09cb2-bf6a-4f82-8f8f-508041f83dc3")
    UUID subscriptionId,

    @NotBlank
    @Schema(description = "Info about the payment method saved for the subscription")
    PaymentInfoRequestDto paymentInfo
) {
    public record PaymentInfoRequestDto(
        @NotBlank 
        @Schema(description = "The payment provider", example = "STRIPE") 
        String provider,

        @NotBlank 
        @Schema(description = "The token representing the payment method", example = "xPtZWl263hmJ6ZxkQ8oLPkATHVojJ3Db") 
        String token,

        @NotBlank 
        @Schema(description = "The name of the cardholder", example = "Alonso Moros") 
        String cardHolder,

        @NotBlank @Size(min = 2, max = 2)
        @Schema(description = "The expiry month of the card", example = "01") 
        String expiryMonth,

        @NotBlank @Size(min = 4, max = 4)
        @Schema(description = "The expiry year of the card", example = "2030") 
        String expiryYear,

        @NotBlank @Size(min = 4, max = 4)
        @Schema(description = "The last four digits of the card", example = "1234") 
        String last4
    ) {}
}
