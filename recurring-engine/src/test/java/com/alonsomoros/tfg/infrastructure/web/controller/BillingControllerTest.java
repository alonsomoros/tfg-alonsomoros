package com.alonsomoros.tfg.infrastructure.web.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.alonsomoros.tfg.application.port.in.IProcessChargeService;
import com.alonsomoros.tfg.infrastructure.web.dto.response.ChargeMandateResponseDto;
import com.alonsomoros.tfg.infrastructure.web.exception.RecurringEngineExceptionHandler;

@WebMvcTest(controllers = BillingController.class)
@Import(RecurringEngineExceptionHandler.class)
class BillingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IProcessChargeService processChargeService;

    @Test
    void chargeMandate_returnsGatewayMessage() throws Exception {
        UUID methodId = UUID.randomUUID();
        when(processChargeService.executeCharge(eq(methodId), eq(new BigDecimal("19.99"))))
                .thenReturn(new ChargeMandateResponseDto("Charge processed successfully for mandate: " + methodId));

        mockMvc.perform(post("/api/v1/billing/charge/{methodId}", methodId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":19.99}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString(methodId.toString())));
    }
}
