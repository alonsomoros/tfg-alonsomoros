package com.alonsomoros.tfg.infrastructure.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alonsomoros.tfg.infrastructure.web.dto.request.TokenRegistrationRequestDto;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RequestMapping("/recurring")
@RestController
public class RecurringEngineController {

    @GetMapping("/health")
    public String health() {
        log.info("Health check endpoint called.");
        return "Recurring Engine is healthy.";
    }

    @PostMapping("/tokens")
    public ResponseEntity<Void> receiveToken(@RequestBody TokenRegistrationRequestDto request) {
        log.info("Request received from the 8080 service.");
        log.info("-> Subscription ID: {}", request.subscriptionId());
        log.info("-> Payment provider: {}", request.paymentInfo().provider());
        log.info("-> Secure token: {}", request.paymentInfo().token());

        // TODO: In the future, this DTO will be mapped to a domain model
        // and a service will persist the token in the 8081 database.

        // For now, return 200 OK so the 8080 service knows everything succeeded.
        return ResponseEntity.ok().build();
    }
    
    
}
