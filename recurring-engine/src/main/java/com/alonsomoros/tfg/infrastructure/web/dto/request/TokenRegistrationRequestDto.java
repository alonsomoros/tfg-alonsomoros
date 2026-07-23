package com.alonsomoros.tfg.infrastructure.web.dto.request;

public record TokenRegistrationRequestDto(
    Long subscriptionId,
    PaymentInfoDto paymentInfo
) {}
