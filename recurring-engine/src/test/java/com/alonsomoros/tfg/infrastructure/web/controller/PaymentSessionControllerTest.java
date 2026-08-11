package com.alonsomoros.tfg.infrastructure.web.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.alonsomoros.tfg.application.service.PaymentSessionServiceImpl;
import com.alonsomoros.tfg.infrastructure.web.exception.RecurringEngineExceptionHandler;

@WebMvcTest(controllers = PaymentSessionController.class)
@Import(RecurringEngineExceptionHandler.class)
class PaymentSessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentSessionServiceImpl sessionService;

    @Test
    void createSetupIntent_returnsClientSecret() throws Exception {
        when(sessionService.getStripeClientSecret("user@example.com")).thenReturn("seti_secret_abc");

        mockMvc.perform(post("/api/v1/payment-sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerEmail\":\"user@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientSecret").value("seti_secret_abc"));

        verify(sessionService).getStripeClientSecret("user@example.com");
    }
}
