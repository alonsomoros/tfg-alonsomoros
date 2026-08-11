package com.alonsomoros.tfg.infrastructure.web.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.alonsomoros.tfg.application.port.in.IProcessRecurringBillingService;
import com.alonsomoros.tfg.infrastructure.web.exception.SubscriptionServiceExceptionHandler;

@WebMvcTest(controllers = JobController.class)
@Import(SubscriptionServiceExceptionHandler.class)
class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IProcessRecurringBillingService processRecurringBillingUseCase;

    @Test
    void triggerBillingManually_executesUseCase() throws Exception {
        mockMvc.perform(post("/api/v1/jobs/trigger-billing"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Billing job executed successfully")));

        verify(processRecurringBillingUseCase).execute();
    }
}
