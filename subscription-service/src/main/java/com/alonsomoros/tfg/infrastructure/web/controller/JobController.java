package com.alonsomoros.tfg.infrastructure.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alonsomoros.tfg.application.port.in.IProcessRecurringBillingService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/jobs")
@Tag(name = "Jobs Controller", description = "Endpoints ocultos para triggers manuales")
@RequiredArgsConstructor
public class JobController {

    private final IProcessRecurringBillingService processRecurringBillingUseCase;

    @PostMapping(EndpointConstants.MANUAL_BILLING_TRIGGER)
    public ResponseEntity<String> triggerBillingManually() {
        processRecurringBillingUseCase.execute();
        return ResponseEntity.ok("Billing job executed successfully. Check the logs!");
    }
}
