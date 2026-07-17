package com.alonsomoros.tfg.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alonsomoros.dto.SubscriptionRequestDto;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RequestMapping("/subscriptions")
@RestController
public class SubscriptionController {

    @GetMapping("/health")
    public String health() {
        log.info("Health check endpoint called.");
        return "Subscription service is healthy.";
    }

    @PostMapping("/subscribe")
    public String postMethodName(@RequestBody SubscriptionRequestDto subscriptionRequestDto) {
        log.info("Processing subscription request for customer: {}", subscriptionRequestDto.customerEmail());
        // TODO: Llamada al service
        return "Subscription request received for customer: " + subscriptionRequestDto.customerEmail() + " with plan: " + subscriptionRequestDto.planId();
    }

}
