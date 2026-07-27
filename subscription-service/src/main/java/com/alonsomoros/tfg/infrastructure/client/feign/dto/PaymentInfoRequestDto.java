package com.alonsomoros.tfg.infrastructure.client.feign.dto;

public record PaymentInfoRequestDto(
    String provider,
    String token,
    String cardHolder,
    String expiryMonth,
    String expiryYear,
    String last4
) {
    
}
