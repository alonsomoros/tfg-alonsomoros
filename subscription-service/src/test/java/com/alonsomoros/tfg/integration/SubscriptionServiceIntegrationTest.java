package com.alonsomoros.tfg.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.alonsomoros.tfg.application.port.out.RecurringEngineClientPort;
import com.alonsomoros.tfg.domain.model.SubscriptionStatusEnum;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.in.PaymentMethodResponseDto;
import com.alonsomoros.tfg.infrastructure.persistence.repository.SubscriptionRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SubscriptionServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @MockitoBean
    private RecurringEngineClientPort recurringEngineClient;

    @Test
    void getPlans_returnsSeededPlansFromDatabase() throws Exception {
        mockMvc.perform(get("/api/v1/plans/getPlans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[?(@.code=='BASIC_MONTHLY')]").exists())
                .andExpect(jsonPath("$[?(@.code=='PRO_MONTHLY')]").exists())
                .andExpect(jsonPath("$[?(@.code=='PRO_YEARLY')]").exists());
    }

    @Test
    void createSubscription_persistsActiveSubscription() throws Exception {
        UUID paymentMethodId = UUID.randomUUID();
        when(recurringEngineClient.sendPaymentToken(any(), any()))
                .thenReturn(new PaymentMethodResponseDto(paymentMethodId));

        String body = """
                {
                  "customerEmail": "integration@example.com",
                  "planId": "PRO_MONTHLY",
                  "paymentInfo": {
                    "provider": "STRIPE",
                    "token": "tok_integration",
                    "cardHolder": "Alonso Moros",
                    "expiryMonth": "01",
                    "expiryYear": "2030",
                    "last4": "4242"
                  }
                }
                """;

        mockMvc.perform(post("/api/v1/subscriptions/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerEmail").value("integration@example.com"))
                .andExpect(jsonPath("$.planId").value("PRO_MONTHLY"));

        assertThat(subscriptionRepository.findAll())
                .anySatisfy(subscription -> {
                    assertThat(subscription.getCustomerEmail()).isEqualTo("integration@example.com");
                    assertThat(subscription.getPlanId()).isEqualTo("PRO_MONTHLY");
                    assertThat(subscription.getStatus()).isEqualTo(SubscriptionStatusEnum.ACTIVE);
                    assertThat(subscription.getExternalPaymentMethodId()).isEqualTo(paymentMethodId);
                });
    }

    @Test
    void createSubscription_whenAlreadyOngoing_returnsConflict() throws Exception {
        UUID paymentMethodId = UUID.randomUUID();
        when(recurringEngineClient.sendPaymentToken(any(), any()))
                .thenReturn(new PaymentMethodResponseDto(paymentMethodId));

        String body = """
                {
                  "customerEmail": "conflict@example.com",
                  "planId": "BASIC_MONTHLY",
                  "paymentInfo": {
                    "provider": "STRIPE",
                    "token": "tok_conflict",
                    "cardHolder": "Alonso Moros",
                    "expiryMonth": "01",
                    "expiryYear": "2030",
                    "last4": "1111"
                  }
                }
                """;

        mockMvc.perform(post("/api/v1/subscriptions/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/subscriptions/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(content().string("Subscription is already ongoing"));
    }

    @Test
    void triggerBilling_chargesDueSubscriptions() throws Exception {
        UUID paymentMethodId = UUID.randomUUID();
        when(recurringEngineClient.sendPaymentToken(any(), any()))
                .thenReturn(new PaymentMethodResponseDto(paymentMethodId));
        when(recurringEngineClient.chargeMandate(eq(paymentMethodId), any()))
                .thenReturn(new com.alonsomoros.tfg.infrastructure.client.feign.dto.in.ChargeMandateResponseDto("ok"));

        String body = """
                {
                  "customerEmail": "billing@example.com",
                  "planId": "PRO_YEARLY",
                  "paymentInfo": {
                    "provider": "STRIPE",
                    "token": "tok_billing",
                    "cardHolder": "Alonso Moros",
                    "expiryMonth": "12",
                    "expiryYear": "2031",
                    "last4": "9999"
                  }
                }
                """;

        mockMvc.perform(post("/api/v1/subscriptions/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        // Force due date to today so billing job picks it up
        subscriptionRepository.findAll().stream()
                .filter(s -> "billing@example.com".equals(s.getCustomerEmail()))
                .forEach(s -> {
                    s.setNextPaymentDate(java.time.LocalDate.now());
                    subscriptionRepository.save(s);
                });

        mockMvc.perform(post("/api/v1/jobs/trigger-billing"))
                .andExpect(status().isOk());
    }
}
