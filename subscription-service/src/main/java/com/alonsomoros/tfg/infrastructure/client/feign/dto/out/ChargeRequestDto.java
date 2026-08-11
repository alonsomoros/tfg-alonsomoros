package com.alonsomoros.tfg.infrastructure.client.feign.dto.out;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ChargeRequestDto(
    
    @NotNull
    @Schema(description = "The amount to be charged for the subscription", example = "9.99")
    BigDecimal amount
) {}
