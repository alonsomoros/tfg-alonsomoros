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
        log.info("¡Llamada recibida desde el 8080!");
        log.info("-> ID Suscripción: {}", request.subscriptionId());
        log.info("-> Pasarela (Provider): {}", request.paymentInfo().provider());
        log.info("-> Token seguro: {}", request.paymentInfo().token());

        // TODO: En el futuro, aquí mapearemos este DTO a un modelo de Dominio 
        // y llamaremos a un Service para guardar el token en la BBDD del 8081.

        // Por ahora, devolvemos un 200 OK para que el 8080 sepa que todo ha ido bien
        return ResponseEntity.ok().build();
    }
    
    
}
