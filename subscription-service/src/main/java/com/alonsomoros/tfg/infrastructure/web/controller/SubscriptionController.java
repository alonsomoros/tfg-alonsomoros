package com.alonsomoros.tfg.infrastructure.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alonsomoros.tfg.application.port.in.ISubscriptionService;
import com.alonsomoros.tfg.infrastructure.web.dto.request.SubscriptionRequestDto;
import com.alonsomoros.tfg.infrastructure.web.dto.request.UpdatePaymentDateRequestDto;
import com.alonsomoros.tfg.infrastructure.web.dto.response.SubscriptionResponseDto;
import com.alonsomoros.tfg.infrastructure.web.mapper.WebSubscriptionMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RequestMapping("/api/v1/subscriptions")
@RestController
@Tag(name = "Subscription Controller", description = "Endpoints for subscription management")
public class SubscriptionController {

    private final ISubscriptionService subscriptionService;
    private final WebSubscriptionMapper webSubscriptionMapper;

    @Operation(summary = "Check service health", description = "Health check endpoint used to verify that the service is running")
    @GetMapping("/health")
    public String health() {
        log.info("Health check endpoint called.");
        return "Subscription service is healthy.";
    }

    @Operation(summary = "Create a new subscription", description = "Registers a new subscription using the provided request data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription created successfully", content = @Content(schema = @Schema(implementation = SubscriptionResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(EndpointConstants.CREATE_SUBSCRIPTION)
    public SubscriptionResponseDto createSubscription(@RequestBody SubscriptionRequestDto subscriptionRequestDto) {
        log.info("Received [SubscriptionRequest] | email: {}", subscriptionRequestDto.customerEmail());
        SubscriptionResponseDto response = subscriptionService
                .createSubscription(webSubscriptionMapper.toCommand(subscriptionRequestDto));
        log.info("Processed [SubscriptionRequest] successfully | email: {}", response.customerEmail());
        return response;
    }

    @Operation(summary = "Update subscription payment date", description = "Updates the next payment date for a specific subscription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment date updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/{subscriptionId}")
    public String updatePaymentDate(
            @PathVariable("subscriptionId") Long subscriptionId,
            @RequestBody UpdatePaymentDateRequestDto requestDto) {
        log.info("Request to update payment date of subscription {} to {}", subscriptionId, requestDto.newPaymentDate());
        subscriptionService.updatePaymentDate(subscriptionId, requestDto.newPaymentDate());
        return "Payment date of subscription " + subscriptionId + " updated successfully to " + requestDto.newPaymentDate();
    }

}