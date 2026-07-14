package com.alonsomoros.tfg.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;



@RequestMapping("/subscriptions")
@RestController
public class SubscriptionController {

    @GetMapping("/health")
    public String health() {
        return "Subscription service is healthy.";
    }
    
}
