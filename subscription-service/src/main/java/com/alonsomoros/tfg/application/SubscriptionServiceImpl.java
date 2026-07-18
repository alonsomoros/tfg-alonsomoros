package com.alonsomoros.tfg.application;

import org.springframework.stereotype.Service;

import com.alonsomoros.tfg.infrastructure.persistence.SubscriptionRepositoryAdapter;
import com.alonsomoros.tfg.infrastructure.web.dto.request.SubscriptionRequestDto;
import com.alonsomoros.tfg.infrastructure.web.dto.response.SubscriptionResponseDto;
import com.alonsomoros.tfg.domain.model.Subscription;
import com.alonsomoros.tfg.domain.service.ISubscriptionService;
import com.alonsomoros.tfg.mapper.SubscriptionMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements ISubscriptionService {

    private final SubscriptionRepositoryAdapter subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    public SubscriptionResponseDto createSubscription(SubscriptionRequestDto subscriptionRequestDto) {
        log.info("Creating subscription for customer: {}", subscriptionRequestDto.customerEmail());
        Subscription subscription = subscriptionRepository.save(subscriptionMapper.toEntity(subscriptionRequestDto));
        return subscriptionMapper.toResponseDto(subscription);
    }
    
}
