package com.alonsomoros.tfg.domain.port;

import java.time.LocalDate;
import java.util.List;

import com.alonsomoros.tfg.domain.model.Subscription;

public interface SubscriptionRepositoryPort {
    Subscription save(Subscription subscription);
    
    Subscription findById(Long id);
    
    void deleteById(Long id);
    
    boolean hasOngoingSubscription(String email, String planId);

    List<Subscription> findSubscriptionsDueForBilling(LocalDate date);
}