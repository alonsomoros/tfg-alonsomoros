package com.alonsomoros.tfg.infrastructure.client.adapter;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.application.command.CreateSubscriptionCommand.PaymentDetailsCommand;
import com.alonsomoros.tfg.application.port.out.RecurringEngineClientPort;
import com.alonsomoros.tfg.infrastructure.client.feign.RecurringEngineFeignClient;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.in.ChargeMandateResponseDto;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.in.PaymentMandateResponseDto;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.out.ChargeRequestDto;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.out.PaymentMandateRequestDto;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.out.PaymentMandateRequestDto.PaymentInfoRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecurringEngineClientAdapter implements RecurringEngineClientPort {

    private final RecurringEngineFeignClient feignClient;

    @Override
    public PaymentMandateResponseDto sendPaymentToken(UUID subscriptionId, PaymentDetailsCommand paymentDetails) {
        log.info("Sending [PaymentMethodRequest] to <<<Recurring Engine>>> | subscriptionId: {}, provider: {}",
            subscriptionId, paymentDetails.provider());

        PaymentInfoRequestDto paymentInfoRequest = new PaymentInfoRequestDto(
            paymentDetails.provider(),
            paymentDetails.token(),
            paymentDetails.cardHolder(),
            paymentDetails.expiryMonth(),
            paymentDetails.expiryYear(),
            paymentDetails.last4()
        );

        PaymentMandateRequestDto paymentMandateRequest = new PaymentMandateRequestDto(subscriptionId, paymentInfoRequest);

        PaymentMandateResponseDto response = feignClient.sendToken(paymentMandateRequest);
        log.info("Received [PaymentMethodResponse] from <<<Recurring Engine>>> | subscriptionId: {}, paymentMandateId: {}",
            subscriptionId, response.paymentMandateId());
        return response;
    }

    @Override
    public ChargeMandateResponseDto chargeMandate(UUID externalPaymentMandateId, ChargeRequestDto requestAmount) {
    log.info("Sending [ChargeRequest] to <<<Recurring Engine>>> | paymentMandateId: {}, amount: {}",
        externalPaymentMandateId, requestAmount.amount());
    ChargeMandateResponseDto response = feignClient.chargeMandate(externalPaymentMandateId, requestAmount);
    log.info("Received [ChargeResponse] from <<<Recurring Engine>>> | paymentMandateId: {}, message: {}",
        externalPaymentMandateId, response.message());
    return response;
    }

}
