package com.alonsomoros.tfg.service;

import org.springframework.stereotype.Service;

import com.alonsomoros.tfg.dto.request.SubscriptionRequestDto;
import com.alonsomoros.tfg.dto.response.SubscriptionResponseDto;
import com.alonsomoros.tfg.entity.Subscription;
import com.alonsomoros.tfg.mapper.SubscriptionMapper;
import com.alonsomoros.tfg.persistence.SubscriptionRepositoryAdapter;

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
