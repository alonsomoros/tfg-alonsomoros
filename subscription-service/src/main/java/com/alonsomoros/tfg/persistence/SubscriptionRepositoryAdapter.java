package com.alonsomoros.tfg.persistence;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.entity.Subscription;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubscriptionRepositoryAdapter {

    private final SubscriptionRepository subscriptionRepository;

    public Subscription save(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    public Subscription findById(Long id) {
        return subscriptionRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        subscriptionRepository.deleteById(id);
    }
    
}
