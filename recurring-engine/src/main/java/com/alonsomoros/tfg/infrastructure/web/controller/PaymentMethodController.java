package com.alonsomoros.tfg.infrastructure.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alonsomoros.tfg.application.port.in.IPaymentMethodService;
import com.alonsomoros.tfg.infrastructure.web.dto.request.PaymentMethodRequestDto;
import com.alonsomoros.tfg.infrastructure.web.dto.response.PaymentMethodResponseDto;
import com.alonsomoros.tfg.infrastructure.web.mapper.WebPaymentMethodMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/v1/payment-methods")
@RestController
@AllArgsConstructor
@Tag(name = "Payment Method Controller", description = "Endpoints for payment method management")
public class PaymentMethodController {

    private final IPaymentMethodService recurringEngineService;
    private final WebPaymentMethodMapper paymentMethodMapper;

    @Operation(summary = "Check service health", description = "Health check endpoint used to verify that the service is running")
    @GetMapping("/health")
    public String health() {
        log.debug("Health check endpoint called for <<<Recurring Engine>>>.");
        return "Recurring Engine is healthy.";
    }

    @Operation(summary = "Receives the payment token", description = "Registers a new payment method with the provided token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment method registered successfully", content = @Content(schema = @Schema(implementation = PaymentMethodResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/tokens")
    public ResponseEntity<PaymentMethodResponseDto> receiveToken(@RequestBody PaymentMethodRequestDto requestDto) {
        log.info("Received [PaymentMethodRequest] from <<<Subscription Service>>> | subscriptionId: {}, provider: {}",
                requestDto.subscriptionId(), requestDto.paymentInfo().provider());
        PaymentMethodResponseDto responseDto = recurringEngineService
                .registerPaymentMethod(paymentMethodMapper.toCommand(requestDto));
        log.info("Processed [PaymentMethodRequest] successfully | subscriptionId: {}, paymentMethodId: {}",
                requestDto.subscriptionId(), responseDto.paymentMethodId());
        return ResponseEntity.ok(responseDto);
    }

}
