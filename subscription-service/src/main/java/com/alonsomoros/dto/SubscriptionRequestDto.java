package com.alonsomoros.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SubscriptionRequestDto(
    @NotBlank @Email 
    @Schema(example = "correo@gmail.com") 
    String customerEmail,

    @NotBlank 
    @Schema(example = "PREMIUM_MENSUAL") 
    String planId,

    @NotNull 
    PaymentInfo paymentInfo
) {
    public record PaymentInfo(
        @NotBlank 
        @Schema(example = "STRIPE") 
        String provider,

        @NotBlank 
        @Schema(example = "xPtZWl263hmJ6ZxkQ8oLPkATHVojJ3Db") 
        String token,

        @NotBlank 
        @Schema(example = "Alonso Moros") 
        String cardHolder,

        @NotBlank @Size(min = 2, max = 2)
        @Schema(example = "01") 
        String expiryMonth,

        @NotBlank @Size(min = 4, max = 4)
        @Schema(example = "2030") 
        String expiryYear,

        @NotBlank @Size(min = 4, max = 4)
        @Schema(example = "1234") 
        String last4
    ) {}
}