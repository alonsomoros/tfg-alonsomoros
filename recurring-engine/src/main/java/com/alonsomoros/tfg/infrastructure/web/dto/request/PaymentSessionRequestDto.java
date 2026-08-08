package com.alonsomoros.tfg.infrastructure.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PaymentSessionRequestDto(

    @NotBlank
    @Email
    String customerEmail
) {}