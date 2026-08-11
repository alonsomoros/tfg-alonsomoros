package com.alonsomoros.tfg.application.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateSubscriptionCommand(

    @NotNull
    @Email
    @Schema(description = "The email of the customer subscribing to the plan", example = "john.doe@example.com")
    String customerEmail,

    @NotNull
    @Schema(description = "The unique identifier of the plan to which the customer is subscribing", example = "PRO_MONTHLY")
    String planId,

    @Schema(description = "The payment information required to create the subscription", implementation = PaymentDetailsCommand.class)
    PaymentDetailsCommand paymentInfo
) {
    public record PaymentDetailsCommand(
        
        @NotBlank
        @Schema(description = "The payment provider for the subscription", example = "STRIPE")
        String provider,

        @NotBlank
        @Schema(description = "The payment token", example = "550e8400-e29b-41d4-a716-446655440000")
        String token,

        @NotBlank
        @Schema(description = "The name of the card holder", example = "Alonso Moros")
        String cardHolder,

        @NotBlank
        @Schema(description = "The expiry month of the card", example = "12")
        String expiryMonth,

        @NotBlank
        @Schema(description = "The expiry year of the card", example = "2030")
        String expiryYear,

        @NotBlank
        @Schema(description = "The last four digits of the card", example = "4242")
        String last4
) {}
}
