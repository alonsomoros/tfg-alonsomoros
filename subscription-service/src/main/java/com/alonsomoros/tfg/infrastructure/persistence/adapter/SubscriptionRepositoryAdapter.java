package com.alonsomoros.tfg.infrastructure.persistence.adapter;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.application.exception.SubscriptionNotFoundException;
import com.alonsomoros.tfg.domain.model.Subscription;
import com.alonsomoros.tfg.domain.model.SubscriptionStatusEnum;
import com.alonsomoros.tfg.domain.port.SubscriptionRepositoryPort;
import com.alonsomoros.tfg.infrastructure.persistence.entity.SubscriptionEntity;
import com.alonsomoros.tfg.infrastructure.persistence.mapper.SubscriptionMapper;
import com.alonsomoros.tfg.infrastructure.persistence.repository.SubscriptionRepository;

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
        log.info("Saving subscription in BBDD | email: {}", subscription.getCustomerEmail());
        SubscriptionEntity subscriptionEntity = subscriptionEntityMapper.toEntity(subscription);
        SubscriptionEntity savedEntity = subscriptionRepository.save(subscriptionEntity);
        return subscriptionEntityMapper.toDomain(savedEntity);

    }

    @Override
    public Subscription findById(Long id) {
        log.info("Finding subscription in BBDD | ID: {}", id);
        SubscriptionEntity subscriptionEntity = subscriptionRepository.findById(id)
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found in BBDD | ID: " + id));
        return subscriptionEntityMapper.toDomain(subscriptionEntity);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting subscription in BBDD | ID: {}", id);
        subscriptionRepository.deleteById(id);
    }

    @Override
    public boolean hasOngoingSubscription(String email, String planId) {
        log.info("Checking for ongoing subscription in BBDD | email: {}, planId: {}", email, planId);
        List<SubscriptionStatusEnum> ongoingStatuses = List.of(
                SubscriptionStatusEnum.PENDING,
                SubscriptionStatusEnum.ACTIVE);
        return subscriptionRepository.existsOngoingSubscription(email, planId, ongoingStatuses);
    }

    @Override
    public List<Subscription> findSubscriptionsDueForBilling(LocalDate date) {
        log.info("Finding subscriptions due for billing in BBDD | date: {}", date);
        return subscriptionRepository.findSubscriptionsDueForBilling(date).stream()
                .map(subscriptionEntityMapper::toDomain)
                .toList();
    }

}
