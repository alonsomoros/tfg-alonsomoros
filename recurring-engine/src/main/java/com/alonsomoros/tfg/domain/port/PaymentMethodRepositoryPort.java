package com.alonsomoros.tfg.domain.port;

import com.alonsomoros.tfg.domain.model.PaymentMethod;

public interface PaymentMethodRepositoryPort {
    PaymentMethod save(PaymentMethod paymentMandate);

    PaymentMethod findById(Long id);

    void deleteById(Long id);

    boolean existsActiveBySubscriptionId(Long subscriptionId);
}
