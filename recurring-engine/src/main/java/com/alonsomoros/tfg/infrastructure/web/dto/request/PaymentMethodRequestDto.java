package com.alonsomoros.tfg.infrastructure.web.dto.request;

public record PaymentMethodRequestDto(
    Long subscriptionId,
    PaymentInfoDto paymentInfo
) {}
