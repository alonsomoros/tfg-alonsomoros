package com.alonsomoros.tfg.infrastructure.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ChargeMandateResponseDto(
    @NotBlank
    @Schema(example = "Message for the <<Subscription Service>> indicating the result of the charge operation")
    String message
) {}
