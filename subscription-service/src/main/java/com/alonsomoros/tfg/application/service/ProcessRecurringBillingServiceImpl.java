package com.alonsomoros.tfg.application.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alonsomoros.tfg.application.port.in.IProcessRecurringBillingService;
import com.alonsomoros.tfg.application.port.out.RecurringEngineClientPort;
import com.alonsomoros.tfg.domain.model.Plan;
import com.alonsomoros.tfg.domain.model.Subscription;
import com.alonsomoros.tfg.domain.port.PlanRepositoryPort;
import com.alonsomoros.tfg.domain.port.SubscriptionRepositoryPort;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.out.ChargeRequestDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessRecurringBillingServiceImpl implements IProcessRecurringBillingService {

    private final SubscriptionRepositoryPort subscriptionRepository;
    private final PlanRepositoryPort planRepository;
    private final RecurringEngineClientPort recurringEngineClient;

    @Override
    @Transactional
    public void execute() {
        LocalDate today = LocalDate.now();
        log.info("Starting [RecurringBillingCycle] | executionDate: {}", today);

        List<Subscription> dueSubscriptions = subscriptionRepository.findSubscriptionsDueForBilling(today);
        
        log.info("Loaded subscriptions due for billing | executionDate: {}, dueCount: {}", today, dueSubscriptions.size());

        if (dueSubscriptions.isEmpty()) {
            log.info("Finished [RecurringBillingCycle] with no due subscriptions | executionDate: {}", today);
            return;
        }

        for (Subscription subscription : dueSubscriptions) {
            try {
                Plan plan = planRepository.findByCode(subscription.getPlan().getCode());

                log.info("Sending [ChargeRequest] to <<<Recurring Engine>>> | subscriptionId: {}, paymentMethodId: {}, planCode: {}, amount: {}",
                        subscription.getId(), subscription.getExternalPaymentMethodId(), plan.getCode(), plan.getAmount());
                ChargeRequestDto requestDto = new ChargeRequestDto(plan.getAmount());

                recurringEngineClient.chargeMandate(
                    subscription.getExternalPaymentMethodId(), 
                    requestDto
                );

                subscription.setNextPaymentDate(plan.getBillingInterval());
                subscriptionRepository.save(subscription);
                
                log.info("Processed [ChargeRequest] successfully | subscriptionId: {}, nextPaymentDate: {}",
                        subscription.getId(), subscription.getNextPaymentDate());

            } catch (Exception e) {
                log.error("Failed to process [ChargeRequest]; subscription will be marked PAST_DUE | subscriptionId: {}",
                        subscription.getId(), e);
                subscription.markAsPastDue();
                subscriptionRepository.save(subscription);
            }
        }

        log.info("Finished [RecurringBillingCycle] | executionDate: {}, processedCount: {}", today, dueSubscriptions.size());
    }
}
