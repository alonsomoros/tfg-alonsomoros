package com.alonsomoros.tfg.infrastructure.web.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.alonsomoros.tfg.application.service.PaymentSessionServiceImpl;
import com.alonsomoros.tfg.infrastructure.web.dto.request.PaymentSessionRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/payment-sessions")
@RequiredArgsConstructor
public class PaymentSessionController {

    private final PaymentSessionServiceImpl sessionService;

    @PostMapping
    public ResponseEntity<Map<String, String>> createSetupIntent(@Valid @RequestBody PaymentSessionRequestDto request) {
        log.info("Received [PaymentSessionRequest] from <<<Frontend>>> | customerEmail: {}", request.customerEmail());
        String clientSecret = sessionService.getStripeClientSecret(request.customerEmail());
        log.info("Processed [PaymentSessionRequest] successfully | customerEmail: {}", request.customerEmail());
        return ResponseEntity.ok(Map.of("clientSecret", clientSecret));
    }
}
