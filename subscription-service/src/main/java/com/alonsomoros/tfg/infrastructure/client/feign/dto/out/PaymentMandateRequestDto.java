package com.alonsomoros.tfg.infrastructure.client.feign.dto.out;

public record PaymentMandateRequestDto(
    Long subscriptionId,
    PaymentInfoRequestDto paymentInfo
) {
    
}
