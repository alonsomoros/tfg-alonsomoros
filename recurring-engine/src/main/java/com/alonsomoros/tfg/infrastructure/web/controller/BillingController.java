package com.alonsomoros.tfg.infrastructure.web.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alonsomoros.tfg.application.port.in.IProcessChargeService;
import com.alonsomoros.tfg.infrastructure.web.dto.request.ChargeRequestDto;
import com.alonsomoros.tfg.infrastructure.web.dto.response.ChargeMandateResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/billing")
@RequiredArgsConstructor
@Slf4j
public class BillingController {

    private final IProcessChargeService processChargeService;

    @PostMapping("/charge/{methodId}")
    public ResponseEntity<ChargeMandateResponseDto> chargeMandate(
            @PathVariable UUID methodId,
            @RequestBody ChargeRequestDto request) {
            
        log.info("Received [ChargeRequest] from <<<Subscription Service>>> | paymentMethodId: {}, amount: {}",
                methodId, request.amount());
        
        ChargeMandateResponseDto response = processChargeService.executeCharge(methodId, request.amount());
        log.info("Processed [ChargeRequest] successfully | paymentMethodId: {}", methodId);
        
        return ResponseEntity.ok(response); 
    }
}
