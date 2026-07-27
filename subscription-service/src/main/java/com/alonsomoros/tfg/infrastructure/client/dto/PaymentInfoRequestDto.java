package com.alonsomoros.tfg.infrastructure.client.dto;

public record PaymentInfoRequestDto(
    String provider,
    String token,
    String cardHolder,
    String expiryMonth,
    String expiryYear,
    String last4
) {
    
}
