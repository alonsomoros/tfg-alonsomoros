package com.alonsomoros.tfg.application.service;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.alonsomoros.tfg.application.command.CreateSubscriptionCommand;
import com.alonsomoros.tfg.application.port.in.ISubscriptionService;
import com.alonsomoros.tfg.application.port.out.RecurringEngineClientPort;
import com.alonsomoros.tfg.domain.exception.SubscriptionAlreadyOngoingException;
import com.alonsomoros.tfg.domain.model.Plan;
import com.alonsomoros.tfg.domain.model.Subscription;
import com.alonsomoros.tfg.domain.model.SubscriptionStatusEnum;
import com.alonsomoros.tfg.domain.port.PlanRepositoryPort;
import com.alonsomoros.tfg.domain.port.SubscriptionRepositoryPort;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.in.PaymentMandateResponseDto;
import com.alonsomoros.tfg.infrastructure.persistence.mapper.SubscriptionMapper;
import com.alonsomoros.tfg.infrastructure.web.dto.response.SubscriptionResponseDto;

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
        log.info("Processing [SubscriptionRequest] from <<<Frontend>>> | customerEmail: {}, planCode: {}",
            createSubscriptionCommand.customerEmail(), createSubscriptionCommand.planId());
        if (subscriptionRepository.hasOngoingSubscription(createSubscriptionCommand.customerEmail(),
                createSubscriptionCommand.planId())) {
            log.warn("Rejected [SubscriptionRequest] because an ongoing subscription already exists | customerEmail: {}, planCode: {}",
                createSubscriptionCommand.customerEmail(), createSubscriptionCommand.planId());
            throw new SubscriptionAlreadyOngoingException("Subscription already ongoing | email: "
                    + createSubscriptionCommand.customerEmail() + ", plan: " + createSubscriptionCommand.planId());
        }

        Subscription subscription = subscriptionMapper.toDomain(createSubscriptionCommand);
        subscription.setStatus(SubscriptionStatusEnum.PENDING);

        Plan plan = planRepository.findByCode(createSubscriptionCommand.planId());
        subscription.setNextPaymentDate(plan.getBillingInterval());

        subscription = subscriptionRepository.save(subscription);
    log.info("Saved [Subscription] with PENDING status | subscriptionId: {}, customerEmail: {}",
        subscription.getId(), subscription.getCustomerEmail());

        try {
        log.info("Sending [PaymentMethodRequest] to <<<Recurring Engine>>> | subscriptionId: {}",
                    subscription.getId());
            PaymentMandateResponseDto paymentMandateResponse = recurringEngineClient.sendPaymentToken(
                    subscription.getId(),
                    createSubscriptionCommand.paymentInfo());
            subscription.markAsActive(paymentMandateResponse.paymentMandateId());
            subscription = subscriptionRepository.save(subscription);
        log.info("Activated [Subscription] successfully | subscriptionId: {}, paymentMandateId: {}",
            subscription.getId(), paymentMandateResponse.paymentMandateId());
        } catch (Exception e) {
        log.warn("Recurring Engine call failed; subscription remains PENDING | subscriptionId: {}",
            subscription.getId(), e);
        }

    log.info("Processed [SubscriptionRequest] successfully | customerEmail: {}, subscriptionId: {}",
        subscription.getCustomerEmail(), subscription.getId());
        return subscriptionMapper.toResponseDto(subscription);
    }

    @Override
    public void updatePaymentDate(UUID subscriptionId, LocalDate newPaymentDate) {
    log.info("Processing [UpdatePaymentDateRequest] | subscriptionId: {}, newPaymentDate: {}",
        subscriptionId, newPaymentDate);
        Subscription subscription = subscriptionRepository.findById(subscriptionId);
        subscription.setNextPaymentDate(newPaymentDate);
        subscriptionRepository.save(subscription);
    log.info("Processed [UpdatePaymentDateRequest] successfully | subscriptionId: {}, nextPaymentDate: {}",
        subscriptionId, newPaymentDate);
    }

}
