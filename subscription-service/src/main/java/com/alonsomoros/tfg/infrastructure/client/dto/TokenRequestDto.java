package com.alonsomoros.tfg.infrastructure.client.dto;

public record TokenRequestDto(
    Long subscriptionId,
    PaymentInfoRequestDto paymentInfo
) {
    
}
