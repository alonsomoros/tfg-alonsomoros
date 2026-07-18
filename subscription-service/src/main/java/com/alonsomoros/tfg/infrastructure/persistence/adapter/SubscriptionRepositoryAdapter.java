package com.alonsomoros.tfg.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.domain.model.Subscription;
import com.alonsomoros.tfg.infrastructure.persistence.entity.SubscriptionEntity;
import com.alonsomoros.tfg.infrastructure.persistence.repository.SubscriptionRepository;
import com.alonsomoros.tfg.mapper.SubscriptionMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionRepositoryAdapter {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionEntityMapper;

    public Subscription save(Subscription subscription) {
        log.info("Saving subscription for customer: {}", subscription.getCustomerEmail());
        try {
            SubscriptionEntity subscriptionEntity = subscriptionEntityMapper.toEntity(subscription);
            SubscriptionEntity savedEntity = subscriptionRepository.save(subscriptionEntity);
            return subscriptionEntityMapper.toDomain(savedEntity);
        } catch (Exception e) {
            log.error("Error saving subscription for customer: {}", subscription.getCustomerEmail(), e);
            throw new RuntimeException("Error saving subscription", e);
        }
    }

    public Subscription findById(Long id) {
        log.info("Finding subscription by ID: {}", id);
        SubscriptionEntity subscriptionEntity = subscriptionRepository.findById(id).orElseThrow(() -> new RuntimeException("Subscription not found"));
        return subscriptionEntityMapper.toDomain(subscriptionEntity);
    }

    public void deleteById(Long id) {
        log.info("Deleting subscription by ID: {}", id);
        subscriptionRepository.deleteById(id);
    }
    
}
