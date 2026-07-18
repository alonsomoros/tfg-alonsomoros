package com.alonsomoros.tfg.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SubscriptionResponseDto(
    @NotBlank @Email 
    @Schema(example = "correo@gmail.com") 
    String customerEmail,

    @NotBlank 
    @Schema(example = "PREMIUM_MENSUAL") 
    String planId
){
    public static SubscriptionResponseDto from(String customerEmail, String planId) {
        return new SubscriptionResponseDto(customerEmail, planId);
    }
}