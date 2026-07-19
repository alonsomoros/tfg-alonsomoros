package com.alonsomoros.tfg.infrastructure.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alonsomoros.tfg.infrastructure.web.dto.request.SubscriptionRequestDto;
import com.alonsomoros.tfg.infrastructure.web.dto.response.SubscriptionResponseDto;
import com.alonsomoros.tfg.domain.service.ISubscriptionService;
import com.alonsomoros.tfg.utils.EnpointConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@AllArgsConstructor
@RequestMapping("/subscriptions")
@RestController
@Tag(name = "Subscription Controller", description = "Endpoints para la gestión de suscripciones")
public class SubscriptionController {

    private final ISubscriptionService subscriptionService;

    @Operation(summary = "Verificar estado del servicio", description = "Endpoint de health check para comprobar si el servicio está activo")
    @GetMapping("/health")
    public String health() {
        log.info("Health check endpoint called.");
        return "Subscription service is healthy.";
    }

    @Operation(summary = "Crear nueva suscripción", description = "Registra una nueva suscripción basándose en los datos proporcionados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Suscripción creada exitosamente", 
            content = @Content(schema = @Schema(implementation = SubscriptionResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(EnpointConstants.CREATE_SUBSCRIPTION)
    public SubscriptionResponseDto createSubscription(@RequestBody SubscriptionRequestDto subscriptionRequestDto) {
        log.info("START - Processing [SubscriptionRequest] for customer: {}", subscriptionRequestDto.customerEmail());
        SubscriptionResponseDto response = subscriptionService.createSubscription(subscriptionRequestDto);
        log.info("END - Processed [SubscriptionRequest] for customer: {}", response.customerEmail());
        return response;
    }

}