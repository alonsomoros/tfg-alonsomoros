package com.alonsomoros.tfg.application.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alonsomoros.tfg.application.port.in.IProcessRecurringBilling;
import com.alonsomoros.tfg.application.port.out.RecurringEngineClientPort;
import com.alonsomoros.tfg.domain.model.Plan;
import com.alonsomoros.tfg.domain.model.Subscription;
import com.alonsomoros.tfg.domain.port.PlanRepositoryPort;
import com.alonsomoros.tfg.domain.port.SubscriptionRepositoryPort;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessRecurringBilling implements IProcessRecurringBilling {

    private final SubscriptionRepositoryPort subscriptionRepository;
    private final PlanRepositoryPort planRepository;
    // private final RecurringEngineClientPort recurringEngineClient;

    @Override
    @Transactional
    public void execute() {
        LocalDate today = LocalDate.now();
        log.info("Starting billing cycle for date: {}", today);

        List<Subscription> dueSubscriptions = subscriptionRepository.findSubscriptionsDueForBilling(today);
        
        log.info("Found {} subscriptions to bill today", dueSubscriptions.size());

        for (Subscription subscription : dueSubscriptions) {
            try {
                Plan plan = planRepository.findByCode(subscription.getPlanId());

                log.info("Calling <<<Recurring Engine Component>>> to charge subscription {} with mandate {} for plan {} with amount {}", subscription.getId(), subscription.getExternalPaymentMandateId(), plan.getCode(), plan.getAmount());
                
                // TODO: Llamar al 8081 para cobrar
                // recurringEngineClient.chargeMandate(
                //     subscription.getExternalPaymentMandateId(), 
                //     plan.getAmount()
                // );

                subscription.setNextPaymentDate(plan.getBillingInterval());
                subscriptionRepository.save(subscription);
                
                log.info("Successfully billed subscription {}. Next payment in {}", subscription.getId(), subscription.getNextPaymentDate());

            } catch (Exception e) {
                log.error("Failed to bill subscription {}. Marking as PAST_DUE", subscription.getId(), e);
                subscription.markAsPastDue();
                subscriptionRepository.save(subscription);
            }
        }
    }
}
