package com.alonsomoros.tfg.infrastructure.persistence.adapter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
        log.debug("Persisting [Subscription] in DB | subscriptionId: {}, customerEmail: {}, status: {}",
            subscription.getId(), subscription.getCustomerEmail(), subscription.getStatus());
        SubscriptionEntity subscriptionEntity = subscriptionEntityMapper.toEntity(subscription);
        SubscriptionEntity savedEntity = subscriptionRepository.save(subscriptionEntity);
        Subscription domainSubscription = subscriptionEntityMapper.toDomain(savedEntity);
        if (domainSubscription.getPlan() == null) {
            domainSubscription.setPlan(subscription.getPlan());
        }
        return domainSubscription;
    }

    @Override
    public Subscription findById(UUID id) {
        log.debug("Loading [Subscription] from DB | subscriptionId: {}", id);
        SubscriptionEntity subscriptionEntity = subscriptionRepository.findByIdWithPlan(id)
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found in BBDD | ID: " + id));
        return subscriptionEntityMapper.toDomain(subscriptionEntity);
    }

    @Override
    public void deleteById(UUID id) {
        log.debug("Deleting [Subscription] from DB | subscriptionId: {}", id);
        subscriptionRepository.deleteById(id);
    }

    @Override
    public boolean hasOngoingSubscription(String email, UUID planId) {
        log.debug("Checking ongoing [Subscription] in DB | customerEmail: {}, planId: {}", email, planId);
        List<SubscriptionStatusEnum> ongoingStatuses = List.of(
                SubscriptionStatusEnum.PENDING,
                SubscriptionStatusEnum.ACTIVE);
        return subscriptionRepository.existsOngoingSubscription(email, planId, ongoingStatuses);
    }

    @Override
    public List<Subscription> findSubscriptionsDueForBilling(LocalDate date) {
        log.debug("Loading subscriptions due for billing from DB | executionDate: {}", date);
        return subscriptionRepository.findSubscriptionsDueForBilling(date).stream()
                .map(subscriptionEntityMapper::toDomain)
                .toList();
    }

}
