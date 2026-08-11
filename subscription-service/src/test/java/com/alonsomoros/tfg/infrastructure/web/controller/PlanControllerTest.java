package com.alonsomoros.tfg.infrastructure.web.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.alonsomoros.tfg.application.port.in.IPlanService;
import com.alonsomoros.tfg.domain.model.BillingInterval;
import com.alonsomoros.tfg.domain.model.CurrencyCode;
import com.alonsomoros.tfg.infrastructure.web.dto.response.PlanResponseDto;
import com.alonsomoros.tfg.infrastructure.web.exception.SubscriptionServiceExceptionHandler;

@WebMvcTest(controllers = PlanController.class)
@Import(SubscriptionServiceExceptionHandler.class)
class PlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IPlanService planService;

    @Test
    void getPlans_returnsPlanList() throws Exception {
        when(planService.getPlans()).thenReturn(List.of(
                new PlanResponseDto(
                        "BASIC_MONTHLY",
                        "Plan Básico",
                        "Essential",
                        new BigDecimal("9.99"),
                        CurrencyCode.EUR,
                        BillingInterval.MONTHLY)));

        mockMvc.perform(get("/api/v1/plans/getPlans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("BASIC_MONTHLY"))
                .andExpect(jsonPath("$[0].amount").value(9.99))
                .andExpect(jsonPath("$[0].billingInterval").value("MONTHLY"));
    }
}
