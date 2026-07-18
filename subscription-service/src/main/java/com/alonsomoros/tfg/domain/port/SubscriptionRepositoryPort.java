package com.alonsomoros.tfg.domain.port;

import com.alonsomoros.tfg.domain.model.Subscription;

public interface SubscriptionRepositoryPort {
    Subscription save(Subscription subscription);
    
    Subscription findById(Long id);
    
    void deleteById(Long id);
    
    boolean hasOngoingSubscription(String email, String planId);
}