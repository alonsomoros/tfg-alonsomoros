package com.alonsomoros.tfg.infrastructure.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alonsomoros.tfg.application.port.in.IProcessRecurringBillingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/jobs")
@Tag(name = "Jobs Controller", description = "Endpoints for manual job triggering")
@RequiredArgsConstructor
public class JobController {

    private final IProcessRecurringBillingService processRecurringBillingUseCase;

    @Operation(summary = "Trigger recurring billing", description = "Manually triggers the recurring billing process")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Billing job executed successfully", 
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(EndpointConstants.MANUAL_BILLING_TRIGGER)
    public ResponseEntity<String> triggerBillingManually() {
        processRecurringBillingUseCase.execute();
        return ResponseEntity.ok("Billing job executed successfully. Check the logs!");
    }
}
