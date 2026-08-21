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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/payment-sessions")
@RequiredArgsConstructor
@Tag(name = "Payment Session Controller", description = "Endpoints for payment session management")
public class PaymentSessionController {

    private final PaymentSessionServiceImpl sessionService;

    @Operation(summary = "Create a payment session", description = "Creates a new payment session for the specified customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment session created successfully", 
            content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Map<String, String>> createSetupIntent(@Valid @RequestBody PaymentSessionRequestDto request) {
        log.info("Received [PaymentSessionRequest] from <<<Frontend>>> | customerEmail: {}", request.customerEmail());
        String clientSecret = sessionService.getStripeClientSecret(request.customerEmail());
        log.info("Processed [PaymentSessionRequest] successfully | customerEmail: {}", request.customerEmail());
        return ResponseEntity.ok(Map.of("clientSecret", clientSecret));
    }
}
