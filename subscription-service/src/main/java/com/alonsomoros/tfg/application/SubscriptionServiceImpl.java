package com.alonsomoros.tfg.application;

import org.springframework.stereotype.Service;

import com.alonsomoros.tfg.infrastructure.web.dto.request.SubscriptionRequestDto;
import com.alonsomoros.tfg.infrastructure.web.dto.response.SubscriptionResponseDto;
import com.alonsomoros.tfg.application.port.out.RecurringEngineClientPort;
import com.alonsomoros.tfg.domain.exception.SubscriptionAlreadyOngoingException;
import com.alonsomoros.tfg.domain.model.Subscription;
import com.alonsomoros.tfg.domain.port.SubscriptionRepositoryPort;
import com.alonsomoros.tfg.domain.service.ISubscriptionService;
import com.alonsomoros.tfg.mapper.SubscriptionMapper;
import com.alonsomoros.tfg.utils.SubscriptionStatusEnum;

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
    public SubscriptionResponseDto createSubscription(SubscriptionRequestDto subscriptionRequestDto) {
        log.info("Creating subscription for customer: {}", subscriptionRequestDto.customerEmail());
        if (subscriptionRepository.hasOngoingSubscription(subscriptionRequestDto.customerEmail(), subscriptionRequestDto.planId())) {
            throw new SubscriptionAlreadyOngoingException("Customer already has an ongoing subscription for plan " + subscriptionRequestDto.planId());
        }

        Subscription subscription = subscriptionMapper.toDomain(subscriptionRequestDto);
        subscription.setStatus(SubscriptionStatusEnum.PENDING);
        subscription = subscriptionRepository.save(subscription);

        log.info("🔥🔥🔥 ID tras el primer save: {}", subscription.getId());

        try {
            
            recurringEngineClient.sendPaymentToken(
                subscription.getId(), 
                subscriptionRequestDto.paymentInfo()
            );

            subscription.setStatus(SubscriptionStatusEnum.ACTIVE);
            subscription = subscriptionRepository.save(subscription);
        } catch (Exception e) {
            log.error("Error while calling <<<Recurring Engine Component>>>, Subscription ID: {} will remain PENDING", subscription.getId(), e);
        }

        return subscriptionMapper.toResponseDto(subscription);
    }
    
}
