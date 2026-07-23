package com.alonsomoros.tfg.infrastructure.persistence.adapter;

import java.util.List;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.application.exception.SubscriptionNotFoundException;
import com.alonsomoros.tfg.domain.model.Subscription;
import com.alonsomoros.tfg.domain.port.SubscriptionRepositoryPort;
import com.alonsomoros.tfg.infrastructure.persistence.entity.SubscriptionEntity;
import com.alonsomoros.tfg.infrastructure.persistence.repository.SubscriptionRepository;
import com.alonsomoros.tfg.mapper.SubscriptionMapper;
import com.alonsomoros.tfg.utils.SubscriptionStatusEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionRepositoryAdapter implements SubscriptionRepositoryPort {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionEntityMapper;

    @Override
    public Subscription save(Subscription subscription) {
        log.info("Saving subscription for customer: {}", subscription.getCustomerEmail());
        try {
            SubscriptionEntity subscriptionEntity = subscriptionEntityMapper.toEntity(subscription);
            SubscriptionEntity savedEntity = subscriptionRepository.save(subscriptionEntity);
            return subscriptionEntityMapper.toDomain(savedEntity);
        } catch (Exception e) {
            log.error("Error saving subscription for customer: {}", subscription.getCustomerEmail(), e);
            throw new DataAccessResourceFailureException("Error with Database Connection while saving subscription ID: " + subscription.getId(), e);
        }
    }

    @Override
    public Subscription findById(Long id) {
        log.info("Finding subscription by ID: {}", id);
        SubscriptionEntity subscriptionEntity = subscriptionRepository.findById(id).orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found with ID: " + id));
        return subscriptionEntityMapper.toDomain(subscriptionEntity);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting subscription by ID: {}", id);
        subscriptionRepository.deleteById(id);
    }

    @Override
    public boolean hasOngoingSubscription(String email, String planId) {
        List<SubscriptionStatusEnum> ongoingStatuses = List.of(
            SubscriptionStatusEnum.PENDING, 
            SubscriptionStatusEnum.ACTIVE
        );
        return subscriptionRepository.existsOngoingSubscription(email, planId, ongoingStatuses);
    }
    
}
