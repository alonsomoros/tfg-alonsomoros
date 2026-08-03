package com.alonsomoros.tfg.infrastructure.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alonsomoros.tfg.application.port.in.IPaymentMethodService;
import com.alonsomoros.tfg.infrastructure.web.dto.request.PaymentMethodRequestDto;
import com.alonsomoros.tfg.infrastructure.web.mapper.WebPaymentMethodMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/v1/payment-methods")
@RestController
@AllArgsConstructor
public class PaymentMethodController {

    private final IPaymentMethodService recurringEngineService;
    private final WebPaymentMethodMapper paymentMethodMapper;

    @GetMapping("/health")
    public String health() {
        log.info("Health check endpoint called.");
        return "Recurring Engine is healthy.";
    }

    @PostMapping("/tokens")
    public ResponseEntity<Void> receiveToken(@RequestBody PaymentMethodRequestDto requestDto) {
        log.info("Received [Payment Method Request] from <<<Subscription Service Component>>> | ID: {}", requestDto.subscriptionId());
        recurringEngineService.registerPaymentMandate(paymentMethodMapper.toCommand(requestDto));
        log.info("Processed [Payment Method Request] successfully | ID: {}", requestDto.subscriptionId());
        return ResponseEntity.ok().build();
    }
    
}
