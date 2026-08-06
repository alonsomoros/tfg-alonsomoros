package com.alonsomoros.tfg.infrastructure.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.application.port.in.IProcessRecurringBillingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class BillingCronJob {

    private final IProcessRecurringBillingService processRecurringBillingUseCase;

    @Scheduled(cron = "0 0 2 * * ?")
    public void runDailyBilling() {
        log.info("CRON TRIGGERED: Running daily billing process...");
        processRecurringBillingUseCase.execute();
    }
}
