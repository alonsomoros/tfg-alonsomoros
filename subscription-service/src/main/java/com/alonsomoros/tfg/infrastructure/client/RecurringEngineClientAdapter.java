package com.alonsomoros.tfg.infrastructure.client;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.application.port.out.RecurringEngineClientPort;
import com.alonsomoros.tfg.infrastructure.client.dto.TokenRequestDto;
import com.alonsomoros.tfg.infrastructure.web.dto.request.SubscriptionRequestDto.PaymentInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecurringEngineClientAdapter implements RecurringEngineClientPort {

    private final RecurringEngineClient feignClient;

    @Override
    public void sendPaymentToken(Long subscriptionId, PaymentInfo paymentInfo) {
        log.info("Calling <<<Recurring Engine>>> for subscription ID: {}", subscriptionId);

        TokenRequestDto requestDto = new TokenRequestDto(subscriptionId, paymentInfo);

        feignClient.sendToken(requestDto);
    }

}
