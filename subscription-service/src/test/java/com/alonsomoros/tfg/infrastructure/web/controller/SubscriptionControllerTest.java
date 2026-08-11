package com.alonsomoros.tfg.infrastructure.web.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.alonsomoros.tfg.application.port.in.ISubscriptionService;
import com.alonsomoros.tfg.infrastructure.web.dto.response.SubscriptionResponseDto;
import com.alonsomoros.tfg.infrastructure.web.exception.SubscriptionServiceExceptionHandler;
import com.alonsomoros.tfg.infrastructure.web.mapper.WebSubscriptionMapper;

@WebMvcTest(controllers = SubscriptionController.class)
@Import({ WebSubscriptionMapper.class, SubscriptionServiceExceptionHandler.class })
class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ISubscriptionService subscriptionService;

    @Test
    void health_returnsOkMessage() throws Exception {
        mockMvc.perform(get("/api/v1/subscriptions/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Subscription service is healthy."));
    }

    @Test
    void createSubscription_returnsMappedResponse() throws Exception {
        when(subscriptionService.createSubscription(any()))
                .thenReturn(new SubscriptionResponseDto("user@example.com", "PRO_MONTHLY"));

        String body = """
                {
                  "customerEmail": "user@example.com",
                  "planId": "PRO_MONTHLY",
                  "paymentInfo": {
                    "provider": "STRIPE",
                    "token": "tok_123",
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
                .andExpect(jsonPath("$.customerEmail").value("user@example.com"))
                .andExpect(jsonPath("$.planId").value("PRO_MONTHLY"));

        verify(subscriptionService).createSubscription(any());
    }

    @Test
    void updatePaymentDate_returnsConfirmationMessage() throws Exception {
        UUID subscriptionId = UUID.randomUUID();
        LocalDate newDate = LocalDate.of(2026, 10, 1);

        mockMvc.perform(patch("/api/v1/subscriptions/{id}", subscriptionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"newPaymentDate\":\"" + newDate + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString(subscriptionId.toString())));

        verify(subscriptionService).updatePaymentDate(eq(subscriptionId), eq(newDate));
    }
}
