package com.alonsomoros.tfg.application.service;

import org.springframework.stereotype.Service;

import com.alonsomoros.tfg.infrastructure.persistence.mapper.SubscriptionMapper;
import com.alonsomoros.tfg.infrastructure.web.dto.response.SubscriptionResponseDto;
import com.alonsomoros.tfg.application.command.CreateSubscriptionCommand;
import com.alonsomoros.tfg.application.port.in.ISubscriptionService;
import com.alonsomoros.tfg.application.port.out.RecurringEngineClientPort;
import com.alonsomoros.tfg.domain.exception.SubscriptionAlreadyOngoingException;
import com.alonsomoros.tfg.domain.model.Subscription;
import com.alonsomoros.tfg.domain.model.SubscriptionStatusEnum;
import com.alonsomoros.tfg.domain.port.SubscriptionRepositoryPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements ISubscriptionService {

    private final SubscriptionRepositoryPort subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final RecurringEngineClientPort recurringEngineClient;

    @Override
    public SubscriptionResponseDto createSubscription(CreateSubscriptionCommand createSubscriptionCommand) {
        log.info("Creating subscription for customer: {}", createSubscriptionCommand.customerEmail());
        if (subscriptionRepository.hasOngoingSubscription(createSubscriptionCommand.customerEmail(), createSubscriptionCommand.planId())) {
            throw new SubscriptionAlreadyOngoingException("Customer already has an ongoing subscription for plan " + createSubscriptionCommand.planId());
        }

        Subscription subscription = subscriptionMapper.toDomain(createSubscriptionCommand);
        subscription.setStatus(SubscriptionStatusEnum.PENDING);
        subscription = subscriptionRepository.save(subscription);

        try {
            
            recurringEngineClient.sendPaymentToken(
                subscription.getId(), 
                createSubscriptionCommand.paymentInfo()
            );

            subscription.setStatus(SubscriptionStatusEnum.ACTIVE);
            subscription = subscriptionRepository.save(subscription);
        } catch (Exception e) {
            log.error("Error while calling <<<Recurring Engine Component>>>, Subscription ID: {} will remain PENDING", subscription.getId(), e);
        }

        return subscriptionMapper.toResponseDto(subscription);
    }
    
}
