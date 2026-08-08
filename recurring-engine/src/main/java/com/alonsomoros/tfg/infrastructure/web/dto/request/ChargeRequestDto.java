package com.alonsomoros.tfg.infrastructure.web.dto.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChargeRequestDto(
    @JsonProperty("amount") BigDecimal amount
) {}
