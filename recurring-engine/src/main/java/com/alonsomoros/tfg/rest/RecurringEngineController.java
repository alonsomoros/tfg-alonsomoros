package com.alonsomoros.tfg.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;



@RequestMapping("/recurring-engine")
@RestController
public class RecurringEngineController {

    @GetMapping("/health")
    public String health() {
        return "Recurring Engine is healthy.";
    }
    
}
