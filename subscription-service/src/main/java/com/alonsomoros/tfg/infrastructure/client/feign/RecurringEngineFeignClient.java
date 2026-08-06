package com.alonsomoros.tfg.infrastructure.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.alonsomoros.tfg.infrastructure.client.feign.dto.in.PaymentMandateResponseDto;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.out.PaymentMandateRequestDto;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@FeignClient(name = "recurring-engine", url = "http://localhost:8081/api/v1/payment-methods")
public interface RecurringEngineFeignClient {

    @PostMapping("/tokens")
    PaymentMandateResponseDto sendToken(@RequestBody PaymentMandateRequestDto request);

}
