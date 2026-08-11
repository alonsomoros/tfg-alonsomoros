package com.alonsomoros.tfg.infrastructure.client.feign.dto.in;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PaymentMandateResponseDto(
    @NotNull
    @Schema(description = "The unique identifier of the payment mandate", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID paymentMandateId
) {}
