package com.alonsomoros.tfg.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;



@Slf4j
@RequestMapping("/recurring-engine")
@RestController
public class RecurringEngineController {

    @GetMapping("/health")
    public String health() {
        log.info("Health check endpoint called.");
        return "Recurring Engine is healthy.";
    }
    
}
