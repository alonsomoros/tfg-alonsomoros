package com.alonsomoros.tfg.infrastructure.client.feign.dto.out;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentMethodRequestDto(
    @NotNull
    @Schema(description = "The unique identifier of the subscription for which the payment method is being created", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID subscriptionId,

    @NotNull
    @Schema(description = "The payment information required to create the payment method", implementation = PaymentInfoRequestDto.class)
    PaymentInfoRequestDto paymentInfo
) {
    public record PaymentInfoRequestDto(
   
        @NotBlank
        @Schema(description = "The payment provider for the subscription", example = "stripe")
        String provider,

        @NotBlank
        @Schema(description = "The payment token", example = "tok_visa")
        String token,

        @NotBlank
        @Schema(description = "The name of the card holder", example = "John Doe")
        String cardHolder,
    
        @NotBlank
        @Schema(description = "The expiry month of the card", example = "12")
        String expiryMonth,
        
        @NotBlank
        @Schema(description = "The expiry year of the card", example = "2023")
        String expiryYear,
        
        @NotBlank
        @Schema(description = "The last four digits of the card", example = "4242")
        String last4 
    ) {}
}
