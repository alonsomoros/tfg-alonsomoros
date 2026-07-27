package com.alonsomoros.tfg.infrastructure.client.adapter;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.application.command.PaymentDetailsCommand;
import com.alonsomoros.tfg.application.port.out.RecurringEngineClientPort;
import com.alonsomoros.tfg.infrastructure.client.feign.RecurringEngineFeignClient;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.PaymentInfoRequestDto;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.TokenRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecurringEngineClientAdapter implements RecurringEngineClientPort {

    private final RecurringEngineFeignClient feignClient;

    @Override
    public void sendPaymentToken(Long subscriptionId, PaymentDetailsCommand paymentDetails) {
        log.info("Calling <<<Recurring Engine>>> for subscription ID: {}", subscriptionId);

        PaymentInfoRequestDto feignDto = 
            new PaymentInfoRequestDto(
                paymentDetails.provider(),
                paymentDetails.token(),
                paymentDetails.cardHolder(),
                paymentDetails.expiryMonth(),
                paymentDetails.expiryYear(),
                paymentDetails.last4()
            );

        TokenRequestDto request = new TokenRequestDto(subscriptionId, feignDto);

        feignClient.sendToken(request);
    }

}
