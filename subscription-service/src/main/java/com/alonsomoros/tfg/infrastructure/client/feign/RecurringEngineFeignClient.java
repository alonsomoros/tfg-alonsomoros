package com.alonsomoros.tfg.infrastructure.client.feign;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.alonsomoros.tfg.infrastructure.client.feign.dto.in.ChargeMandateResponseDto;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.in.PaymentMandateResponseDto;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.out.ChargeRequestDto;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.out.PaymentMandateRequestDto;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@FeignClient(name = "recurring-engine", url = "http://localhost:8081/api/v1")
public interface RecurringEngineFeignClient {

    @PostMapping("/payment-methods/tokens")
    PaymentMandateResponseDto sendToken(@RequestBody PaymentMandateRequestDto request);

    @PostMapping("/billing/charge/{mandateId}")
    ChargeMandateResponseDto chargeMandate(@PathVariable("mandateId") Long externalPaymentMandateId, @RequestBody ChargeRequestDto requestDto);

}
