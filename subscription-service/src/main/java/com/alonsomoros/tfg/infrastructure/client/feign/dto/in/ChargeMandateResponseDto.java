package com.alonsomoros.tfg.infrastructure.client.feign.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ChargeMandateResponseDto(
    @NotBlank
    @Schema(description = "The message received from <<Recurring Enginge>> for charging for the subscription", example = "Charge successful")
    String message
) {}
