package com.alonsomoros.tfg.application.port.out;

import com.alonsomoros.tfg.application.command.PaymentDetailsCommand;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.in.PaymentMandateResponseDto;

public interface RecurringEngineClientPort {
    PaymentMandateResponseDto sendPaymentToken(Long subscriptionId, PaymentDetailsCommand paymentInfoCommand);
}
