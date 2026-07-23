package com.alonsomoros.tfg.infrastructure.web.dto.request;

public record PaymentInfoDto(
    String provider,
    String token,
    String cardHolder,
    String expiryMonth,
    String expiryYear,
    String last4
) {}
