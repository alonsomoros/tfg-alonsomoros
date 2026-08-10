package com.alonsomoros.tfg.infrastructure.client.adapter;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.application.command.PaymentDetailsCommand;
import com.alonsomoros.tfg.application.port.out.RecurringEngineClientPort;
import com.alonsomoros.tfg.infrastructure.client.feign.RecurringEngineFeignClient;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.in.ChargeMandateResponseDto;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.in.PaymentMandateResponseDto;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.out.ChargeRequestDto;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.out.PaymentInfoRequestDto;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.out.PaymentMandateRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecurringEngineClientAdapter implements RecurringEngineClientPort {

    private final RecurringEngineFeignClient feignClient;

    @Override
    public PaymentMandateResponseDto sendPaymentToken(UUID subscriptionId, PaymentDetailsCommand paymentDetails) {
        log.info("Sending [Payment Method] to <<<Recurring Engine Component>>> | ID: {}", subscriptionId);

        PaymentInfoRequestDto feignDto = 
            new PaymentInfoRequestDto(
                paymentDetails.provider(),
                paymentDetails.token(),
                paymentDetails.cardHolder(),
                paymentDetails.expiryMonth(),
                paymentDetails.expiryYear(),
                paymentDetails.last4()
            );

        PaymentMandateRequestDto request = new PaymentMandateRequestDto(subscriptionId, feignDto);

        return feignClient.sendToken(request);
    }

    @Override
    public ChargeMandateResponseDto chargeMandate(UUID externalPaymentMandateId, ChargeRequestDto requestamount) {
        log.info("Charging [Payment Mandate] to <<<Recurring Engine Component>>> | Mandate ID: {}, Amount: {}", externalPaymentMandateId, requestamount);
        return feignClient.chargeMandate(externalPaymentMandateId, requestamount);
    }

}
