package com.alonsomoros.tfg.infrastructure.web.dto.request;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ChargeRequestDto(
    
    @NotNull
    @Schema(description = "The amount to be charged", example = "199.99")
    BigDecimal amount
) {}
