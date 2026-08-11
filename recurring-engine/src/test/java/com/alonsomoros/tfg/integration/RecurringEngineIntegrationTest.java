package com.alonsomoros.tfg.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.alonsomoros.tfg.infrastructure.gateway.stripe.StripePaymentAdapter;
import com.alonsomoros.tfg.infrastructure.persistence.repository.PaymentMethodRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RecurringEngineIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @MockitoBean
    private StripePaymentAdapter stripePaymentAdapter;

    @BeforeEach
    void stubStripeGateway() {
        when(stripePaymentAdapter.getProviderName()).thenReturn("STRIPE");
    }

    @Test
    void registerPaymentMethod_andCharge_persistsAndUsesGateway() throws Exception {
        UUID subscriptionId = UUID.randomUUID();
        doNothing().when(stripePaymentAdapter).charge(any(), any());

        String registerBody = """
                {
                  "subscriptionId": "%s",
                  "paymentInfo": {
                    "provider": "STRIPE",
                    "token": "pm_integration_123",
                    "cardHolder": "Alonso Moros",
                    "expiryMonth": "01",
                    "expiryYear": "2030",
                    "last4": "4242"
                  }
                }
                """.formatted(subscriptionId);

        String response = mockMvc.perform(post("/api/v1/payment-methods/tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentMethodId").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UUID paymentMethodId = UUID.fromString(
                response.replaceAll("(?s).*\"paymentMethodId\"\\s*:\\s*\"([^\"]+)\".*", "$1"));

        assertThat(paymentMethodRepository.findById(paymentMethodId)).isPresent();
        assertThat(paymentMethodRepository.existsActiveBySubscriptionId(subscriptionId)).isTrue();

        mockMvc.perform(post("/api/v1/billing/charge/{methodId}", paymentMethodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":19.99}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString(paymentMethodId.toString())));

        verify(stripePaymentAdapter).charge(eq("pm_integration_123"), eq(new BigDecimal("19.99")));
    }

    @Test
    void registerPaymentMethod_whenDuplicate_returnsConflict() throws Exception {
        UUID subscriptionId = UUID.randomUUID();

        String body = """
                {
                  "subscriptionId": "%s",
                  "paymentInfo": {
                    "provider": "STRIPE",
                    "token": "pm_dup",
                    "cardHolder": "Alonso Moros",
                    "expiryMonth": "01",
                    "expiryYear": "2030",
                    "last4": "1111"
                  }
                }
                """.formatted(subscriptionId);

        mockMvc.perform(post("/api/v1/payment-methods/tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/payment-methods/tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(content().string("Payment method already exists"));
    }

    @Test
    void createPaymentSession_returnsClientSecretFromStripe() throws Exception {
        when(stripePaymentAdapter.createSetupIntent("session@example.com")).thenReturn("seti_test_secret");

        mockMvc.perform(post("/api/v1/payment-sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerEmail\":\"session@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientSecret").value("seti_test_secret"));
    }
}
