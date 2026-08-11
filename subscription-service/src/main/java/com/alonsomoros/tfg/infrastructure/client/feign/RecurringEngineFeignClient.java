package com.alonsomoros.tfg.infrastructure.client.feign;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.alonsomoros.tfg.infrastructure.client.feign.dto.in.ChargeMandateResponseDto;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.in.PaymentMethodResponseDto;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.out.ChargeRequestDto;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.out.PaymentMethodRequestDto;

@FeignClient(name = "recurring-engine", url = "${recurring.engine.url}")
public interface RecurringEngineFeignClient {

    @PostMapping("/payment-methods/tokens")
    PaymentMethodResponseDto sendToken(@RequestBody PaymentMethodRequestDto request);

    @PostMapping("/billing/charge/{mandateId}")
    ChargeMandateResponseDto chargeMandate(@PathVariable("mandateId") UUID externalPaymentMethodId, @RequestBody ChargeRequestDto requestDto);

}
