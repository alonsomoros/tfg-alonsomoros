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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/billing")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Billing Controller", description = "Endpoints for billing operations")
public class BillingController {

    private final IProcessChargeService processChargeService;

    @Operation(summary = "Charge a payment method", description = "Charges a payment method with the specified amount")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Charge executed successfully", 
            content = @Content(schema = @Schema(implementation = ChargeMandateResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
