package com.alonsomoros.tfg.domain.port;

import java.util.UUID;

import com.alonsomoros.tfg.domain.model.PaymentMethod;

public interface PaymentMethodRepositoryPort {
    PaymentMethod save(PaymentMethod paymentMethod);

    PaymentMethod findById(UUID id);

    void deleteById(UUID id);

    boolean existsActiveBySubscriptionId(UUID subscriptionId);
}
