package com.alonsomoros.tfg.application.port.out;

import com.alonsomoros.tfg.infrastructure.web.dto.request.SubscriptionRequestDto.PaymentInfo;

public interface RecurringEngineClientPort {
    void sendPaymentToken(Long subscriptionId, PaymentInfo paymentInfo);
}
