package com.alonsomoros.tfg.infrastructure.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alonsomoros.tfg.infrastructure.web.dto.request.SubscriptionRequestDto;
import com.alonsomoros.tfg.infrastructure.web.dto.response.SubscriptionResponseDto;
import com.alonsomoros.tfg.domain.service.ISubscriptionService;
import com.alonsomoros.tfg.utils.EnpointConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@AllArgsConstructor
@RequestMapping("/subscriptions")
@RestController
public class SubscriptionController {

    private final ISubscriptionService subscriptionService;

    @GetMapping("/health")
    public String health() {
        log.info("Health check endpoint called.");
        return "Subscription service is healthy.";
    }

    @PostMapping(EnpointConstants.CREATE_SUBSCRIPTION)
    public SubscriptionResponseDto postMethodName(@RequestBody SubscriptionRequestDto subscriptionRequestDto) {
        log.info("START - Processing [SubscriptionRequest] for customer: {}", subscriptionRequestDto.customerEmail());
        SubscriptionResponseDto response = subscriptionService.createSubscription(subscriptionRequestDto);
        log.info("END - Processed [SubscriptionRequest] for customer: {}", response.customerEmail());
        return response;
    }

}
