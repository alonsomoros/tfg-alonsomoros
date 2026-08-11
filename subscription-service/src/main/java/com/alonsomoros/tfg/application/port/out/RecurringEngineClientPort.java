package com.alonsomoros.tfg.application.port.out;

import java.util.UUID;

import com.alonsomoros.tfg.application.command.CreateSubscriptionCommand.PaymentDetailsCommand;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.in.ChargeMandateResponseDto;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.in.PaymentMethodResponseDto;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.out.ChargeRequestDto;

public interface RecurringEngineClientPort {
    PaymentMethodResponseDto sendPaymentToken(UUID subscriptionId, PaymentDetailsCommand paymentInfoCommand);

    ChargeMandateResponseDto chargeMandate(UUID externalPaymentMethodId, ChargeRequestDto requestDto);
}
