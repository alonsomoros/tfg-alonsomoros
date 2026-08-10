package com.alonsomoros.tfg.infrastructure.client.feign.dto.in;

import java.util.UUID;

public record PaymentMandateResponseDto(
    UUID paymentMandateId
) {
    
}
