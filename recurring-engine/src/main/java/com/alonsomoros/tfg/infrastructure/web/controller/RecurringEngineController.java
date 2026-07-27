package com.alonsomoros.tfg.infrastructure.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alonsomoros.tfg.domain.service.IRecurringEngineService;
import com.alonsomoros.tfg.infrastructure.web.dto.request.PaymentMethodRequestDto;
import com.alonsomoros.tfg.infrastructure.web.mapper.WebRecurringMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/recurring")
@RestController
@AllArgsConstructor
public class RecurringEngineController {

    private final IRecurringEngineService recurringEngineService;
    private final WebRecurringMapper paymentMethodMapper;

    @GetMapping("/health")
    public String health() {
        log.info("Health check endpoint called.");
        return "Recurring Engine is healthy.";
    }

    @PostMapping("/tokens")
    public ResponseEntity<Void> receiveToken(@RequestBody PaymentMethodRequestDto requestDto) {
        log.info("Request received from <<<Subscription Service>>> for subscription ID: {}", requestDto.subscriptionId());
        recurringEngineService.registerPaymentMandate(paymentMethodMapper.toCommand(requestDto));
        log.info("Request processed successfully for subscription ID: {}", requestDto.subscriptionId());
        return ResponseEntity.ok().build();
    }
    
    
}
