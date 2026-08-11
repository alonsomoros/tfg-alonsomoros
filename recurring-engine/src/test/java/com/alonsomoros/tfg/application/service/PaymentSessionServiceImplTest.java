package com.alonsomoros.tfg.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alonsomoros.tfg.infrastructure.gateway.stripe.StripePaymentAdapter;

@ExtendWith(MockitoExtension.class)
class PaymentSessionServiceImplTest {

    @Mock
    private StripePaymentAdapter stripeAdapter;

    @InjectMocks
    private PaymentSessionServiceImpl paymentSessionService;

    @Test
    void getStripeClientSecret_delegatesToStripeAdapter() {
        when(stripeAdapter.createSetupIntent("user@example.com")).thenReturn("seti_secret_123");

        String secret = paymentSessionService.getStripeClientSecret("user@example.com");

        assertThat(secret).isEqualTo("seti_secret_123");
        verify(stripeAdapter).createSetupIntent("user@example.com");
    }
}
