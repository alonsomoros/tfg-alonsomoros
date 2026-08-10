package com.alonsomoros.tfg.infrastructure.client.feign.dto.out;

import java.util.UUID;

public record PaymentMandateRequestDto(
    UUID subscriptionId,
    PaymentInfoRequestDto paymentInfo
) {
    
}
