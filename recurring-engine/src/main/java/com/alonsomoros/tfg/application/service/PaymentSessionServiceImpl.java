package com.alonsomoros.tfg.application.service;

import org.springframework.stereotype.Service;

import com.alonsomoros.tfg.infrastructure.gateway.stripe.StripePaymentAdapter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentSessionServiceImpl {

    private final StripePaymentAdapter stripeAdapter;

    public String getStripeClientSecret(String customerEmail) {
        return stripeAdapter.createSetupIntent(customerEmail);
    }
}
