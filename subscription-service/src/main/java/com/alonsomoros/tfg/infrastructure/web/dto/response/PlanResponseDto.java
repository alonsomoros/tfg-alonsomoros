package com.alonsomoros.tfg.infrastructure.web.dto.response;

import java.math.BigDecimal;

import com.alonsomoros.tfg.domain.model.BillingInterval;
import com.alonsomoros.tfg.domain.model.CurrencyCode;
import com.alonsomoros.tfg.domain.model.Plan;

import io.swagger.v3.oas.annotations.media.Schema;

public record PlanResponseDto(
    @Schema(example = "PREMIUM_MENSUAL")
    String code,

    @Schema(example = "Premium")
    String name,

    @Schema(example = "Acceso completo a todas las funciones")
    String description,

    @Schema(example = "9.99")
    BigDecimal amount,

    @Schema(example = "EUR")
    CurrencyCode currency,

    @Schema(example = "MONTHLY")
    BillingInterval billingInterval
) {
    public static PlanResponseDto from(Plan plan) {
        return new PlanResponseDto(
                plan.getCode(),
                plan.getName(),
                plan.getDescription(),
                plan.getAmount(),
                plan.getCurrency(),
                plan.getBillingInterval());
    }
}