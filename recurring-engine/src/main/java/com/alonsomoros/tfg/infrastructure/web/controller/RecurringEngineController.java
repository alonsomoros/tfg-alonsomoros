package com.alonsomoros.tfg.infrastructure.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alonsomoros.tfg.domain.service.IRecurringEngineService;
import com.alonsomoros.tfg.infrastructure.web.dto.request.PaymentMethodRequestDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RequestMapping("/recurring")
@RestController
@AllArgsConstructor
public class RecurringEngineController {

    private final IRecurringEngineService recurringEngineService;

    @GetMapping("/health")
    public String health() {
        log.info("Health check endpoint called.");
        return "Recurring Engine is healthy.";
    }

    @PostMapping("/tokens")
    public ResponseEntity<Void> receiveToken(@RequestBody PaymentMethodRequestDto request) {
        log.info("Request received from <<<Subscription Service>>> for subscription ID: {}", request.subscriptionId());

        recurringEngineService.processPaymentToken(request);

        return ResponseEntity.ok().build();
    }
    
    
}
