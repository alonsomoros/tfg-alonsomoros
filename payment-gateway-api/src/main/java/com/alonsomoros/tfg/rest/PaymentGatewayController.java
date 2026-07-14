package com.alonsomoros.tfg.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;



@RequestMapping("/payment-gateway")
@RestController
public class PaymentGatewayController {

    @GetMapping("/health")
    public String health() {
        return "Payment Gateway API is healthy.";
    }
    
}
