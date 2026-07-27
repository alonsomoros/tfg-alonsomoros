package com.alonsomoros.tfg.infrastructure.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PaymentMethodRequestDto(

    @NotBlank 
    @Schema(example = "2026") 
    Long subscriptionId,

    @NotBlank
    PaymentInfoDto paymentInfo
) {
    public record PaymentInfoDto(
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
