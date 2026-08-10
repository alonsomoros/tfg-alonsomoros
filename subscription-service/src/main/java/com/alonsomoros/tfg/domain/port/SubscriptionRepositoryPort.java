package com.alonsomoros.tfg.domain.port;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.alonsomoros.tfg.domain.model.Subscription;

public interface SubscriptionRepositoryPort {
    Subscription save(Subscription subscription);
    
    Subscription findById(UUID id);
    
    void deleteById(UUID id);
    
    boolean hasOngoingSubscription(String email, String planId);

    List<Subscription> findSubscriptionsDueForBilling(LocalDate date);
}