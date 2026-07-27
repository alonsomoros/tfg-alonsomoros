package com.alonsomoros.tfg.infrastructure.client.feign.dto;

public record TokenRequestDto(
    Long subscriptionId,
    PaymentInfoRequestDto paymentInfo
) {
    
}
