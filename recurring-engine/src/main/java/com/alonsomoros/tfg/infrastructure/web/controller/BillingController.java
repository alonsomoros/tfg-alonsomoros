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

    @PostMapping("/charge/{mandateId}")
    public ResponseEntity<ChargeMandateResponseDto> chargeMandate(
            @PathVariable UUID mandateId,
            @RequestBody ChargeRequestDto request) {
            
        log.info("Received [ChargeRequest] from <<<Subscription Service>>> | paymentMandateId: {}, amount: {}",
                mandateId, request.amount());
        
        processChargeService.executeCharge(mandateId, request.amount());
        log.info("Processed [ChargeRequest] successfully | paymentMandateId: {}", mandateId);
        
        return ResponseEntity.ok(new ChargeMandateResponseDto("Charge processed successfully for mandate: " + mandateId)); 
        // Si todo va bien devuelve 200 OK. Si la pasarela falla, tu UseCase lanzará una excepción (ej. PaymentFailedException) y devolverás un 400/500.
    }
}
