package com.alonsomoros.tfg.infrastructure.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PaymentSessionRequestDto(

    @NotBlank
    @Email
    @Schema(description = "Email of the customer initiating the payment session", example = "user@gmail.com")
    String customerEmail
) {}