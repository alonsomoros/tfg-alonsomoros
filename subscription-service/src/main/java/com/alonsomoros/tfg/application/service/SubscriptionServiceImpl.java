package com.alonsomoros.tfg.application.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.alonsomoros.tfg.infrastructure.persistence.mapper.SubscriptionMapper;
import com.alonsomoros.tfg.infrastructure.web.dto.response.SubscriptionResponseDto;
import com.alonsomoros.tfg.application.command.CreateSubscriptionCommand;
import com.alonsomoros.tfg.application.exception.PlanNotFoundException;
import com.alonsomoros.tfg.application.port.in.ISubscriptionService;
import com.alonsomoros.tfg.application.port.out.RecurringEngineClientPort;
import com.alonsomoros.tfg.domain.exception.SubscriptionAlreadyOngoingException;
import com.alonsomoros.tfg.domain.model.BillingInterval;
import com.alonsomoros.tfg.domain.model.Plan;
import com.alonsomoros.tfg.domain.model.Subscription;
import com.alonsomoros.tfg.domain.model.SubscriptionStatusEnum;
import com.alonsomoros.tfg.domain.port.PlanRepositoryPort;
import com.alonsomoros.tfg.domain.port.SubscriptionRepositoryPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements ISubscriptionService {

    private final SubscriptionRepositoryPort subscriptionRepository;
    private final PlanRepositoryPort planRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final RecurringEngineClientPort recurringEngineClient;

    @Override
    public SubscriptionResponseDto createSubscription(CreateSubscriptionCommand createSubscriptionCommand) {
        log.info("Creating new subscription | email: {}, plan: {}", createSubscriptionCommand.customerEmail(), createSubscriptionCommand.planId());
        if (subscriptionRepository.hasOngoingSubscription(createSubscriptionCommand.customerEmail(), createSubscriptionCommand.planId())) {
            throw new SubscriptionAlreadyOngoingException("Subscription already ongoing | email: " + createSubscriptionCommand.customerEmail() + ", plan: " + createSubscriptionCommand.planId());
        }

        Plan plan = planRepository.findByCode(createSubscriptionCommand.planId());
        
        LocalDate nextPaymentDate = calculateNextPaymentDate(plan.getBillingInterval());

        Subscription subscription = subscriptionMapper.toDomain(createSubscriptionCommand);
        subscription.setNextPaymentDate(nextPaymentDate);
        subscription.setStatus(SubscriptionStatusEnum.PENDING);
        subscription = subscriptionRepository.save(subscription);
        log.debug("Subscription PENDING saved in BBDD | subscriptionId: {}", subscription.getId());

        try {
            log.info("Calling <<<Recurring Engine Component>>> to save [PaymentMethod] | subscriptionId: {}", subscription.getId());
            recurringEngineClient.sendPaymentToken(
                subscription.getId(), 
                createSubscriptionCommand.paymentInfo()
            );

            subscription.setStatus(SubscriptionStatusEnum.ACTIVE);
            subscription = subscriptionRepository.save(subscription);
            log.info("Subscription ACTIVE saved in BBDD | subscriptionId: {}", subscription.getId());
        } catch (Exception e) {
            log.error("Error while calling <<<Recurring Engine Component>>> will remain PENDING | subscriptionId: {}", subscription.getId(), e);
        }

        return subscriptionMapper.toResponseDto(subscription);
    }

    private LocalDate calculateNextPaymentDate(BillingInterval interval) {
        LocalDate today = LocalDate.now();
        
        return switch (interval) {
            case WEEKLY -> today.plusWeeks(1);
            case MONTHLY -> today.plusMonths(1);
            case YEARLY -> today.plusYears(1);
            default -> throw new IllegalArgumentException("Not supported billing interval: " + interval);
        };
    }
    
}
