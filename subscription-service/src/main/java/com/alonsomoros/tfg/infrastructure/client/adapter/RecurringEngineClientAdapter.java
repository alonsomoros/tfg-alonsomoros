package com.alonsomoros.tfg.infrastructure.client.adapter;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.application.command.CreateSubscriptionCommand.PaymentDetailsCommand;
import com.alonsomoros.tfg.application.port.out.RecurringEngineClientPort;
import com.alonsomoros.tfg.infrastructure.client.feign.RecurringEngineFeignClient;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.in.ChargeMandateResponseDto;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.in.PaymentMethodResponseDto;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.out.ChargeRequestDto;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.out.PaymentMethodRequestDto;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.out.PaymentMethodRequestDto.PaymentInfoRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecurringEngineClientAdapter implements RecurringEngineClientPort {

    private final RecurringEngineFeignClient feignClient;

    @Override
    public PaymentMethodResponseDto sendPaymentToken(UUID subscriptionId, PaymentDetailsCommand paymentDetails) {
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

        PaymentMethodRequestDto paymentMethodRequest = new PaymentMethodRequestDto(subscriptionId, paymentInfoRequest);

        PaymentMethodResponseDto response = feignClient.sendToken(paymentMethodRequest);
        log.info("Received [PaymentMethodResponse] from <<<Recurring Engine>>> | subscriptionId: {}, paymentMethodId: {}",
            subscriptionId, response.paymentMethodId());
        return response;
    }

    @Override
    public ChargeMandateResponseDto chargeMandate(UUID externalPaymentMethodId, ChargeRequestDto requestAmount) {
    log.info("Sending [ChargeRequest] to <<<Recurring Engine>>> | paymentMethodId: {}, amount: {}",
        externalPaymentMethodId, requestAmount.amount());
    ChargeMandateResponseDto response = feignClient.chargeMandate(externalPaymentMethodId, requestAmount);
    log.info("Received [ChargeResponse] from <<<Recurring Engine>>> | paymentMethodId: {}, message: {}",
        externalPaymentMethodId, response.message());
    return response;
    }

}
