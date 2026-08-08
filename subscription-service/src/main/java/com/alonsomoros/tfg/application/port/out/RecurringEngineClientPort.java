package com.alonsomoros.tfg.application.port.out;

import java.math.BigDecimal;

import com.alonsomoros.tfg.application.command.PaymentDetailsCommand;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.in.ChargeMandateResponseDto;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.in.PaymentMandateResponseDto;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.out.ChargeRequestDto;

public interface RecurringEngineClientPort {
    PaymentMandateResponseDto sendPaymentToken(Long subscriptionId, PaymentDetailsCommand paymentInfoCommand);

    ChargeMandateResponseDto chargeMandate(Long externalPaymentMandateId, ChargeRequestDto requestDto);
}
