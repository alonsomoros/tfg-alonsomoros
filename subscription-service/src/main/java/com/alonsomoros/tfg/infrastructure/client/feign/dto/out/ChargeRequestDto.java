package com.alonsomoros.tfg.infrastructure.client.feign.dto.out;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChargeRequestDto(
    @JsonProperty("amount") BigDecimal amount
) {}
