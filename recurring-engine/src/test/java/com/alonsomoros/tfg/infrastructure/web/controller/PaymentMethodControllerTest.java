package com.alonsomoros.tfg.infrastructure.web.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.alonsomoros.tfg.application.port.in.IPaymentMethodService;
import com.alonsomoros.tfg.infrastructure.web.dto.response.PaymentMethodResponseDto;
import com.alonsomoros.tfg.infrastructure.web.exception.RecurringEngineExceptionHandler;
import com.alonsomoros.tfg.infrastructure.web.mapper.WebPaymentMethodMapper;

@WebMvcTest(controllers = PaymentMethodController.class)
@Import({ WebPaymentMethodMapper.class, RecurringEngineExceptionHandler.class })
class PaymentMethodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IPaymentMethodService paymentMethodService;

    @Test
    void health_returnsOkMessage() throws Exception {
        mockMvc.perform(get("/api/v1/payment-methods/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Recurring Engine is healthy."));
    }

    @Test
    void receiveToken_registersPaymentMethod() throws Exception {
        UUID paymentMethodId = UUID.randomUUID();
        UUID subscriptionId = UUID.randomUUID();
        when(paymentMethodService.registerPaymentMethod(any()))
                .thenReturn(new PaymentMethodResponseDto(paymentMethodId));

        String body = """
                {
                  "subscriptionId": "%s",
                  "paymentInfo": {
                    "provider": "STRIPE",
                    "token": "tok_123",
                    "cardHolder": "Alonso Moros",
                    "expiryMonth": "01",
                    "expiryYear": "2030",
                    "last4": "4242"
                  }
                }
                """.formatted(subscriptionId);

        mockMvc.perform(post("/api/v1/payment-methods/tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentMethodId").value(paymentMethodId.toString()));

        verify(paymentMethodService).registerPaymentMethod(any());
    }
}
