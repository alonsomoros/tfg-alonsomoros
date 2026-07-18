package com.alonsomoros.tfg.application;

import org.springframework.stereotype.Service;

import com.alonsomoros.tfg.infrastructure.web.dto.request.SubscriptionRequestDto;
import com.alonsomoros.tfg.infrastructure.web.dto.response.SubscriptionResponseDto;
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

    @Override
    public SubscriptionResponseDto createSubscription(SubscriptionRequestDto subscriptionRequestDto) {
        log.info("Creating subscription for customer: {}", subscriptionRequestDto.customerEmail());
        if (subscriptionRepository.hasOngoingSubscription(subscriptionRequestDto.customerEmail(), subscriptionRequestDto.planId())) {
            log.warn("Customer {} already has an ongoing subscription for plan {}", subscriptionRequestDto.customerEmail(), subscriptionRequestDto.planId());
            throw new RuntimeException("Customer already has an ongoing subscription for this plan");
        }

        Subscription subscription = subscriptionMapper.toDomain(subscriptionRequestDto);
        subscription.setStatus(SubscriptionStatusEnum.PENDING);
        subscription = subscriptionRepository.save(subscription);

        // TODO: Llamar al 8081 con trycatch y client, en el try meter el save() con estado ACTIVE
        // En el catch no metemos excepcion por que queremos devolver 200 PENDING y gestionar el error internamente

        return subscriptionMapper.toResponseDto(subscription);
    }
    
}
