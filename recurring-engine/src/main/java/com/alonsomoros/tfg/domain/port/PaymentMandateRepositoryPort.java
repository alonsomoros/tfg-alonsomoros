package com.alonsomoros.tfg.domain.port;

import com.alonsomoros.tfg.domain.model.PaymentMandate;

public interface PaymentMandateRepositoryPort {
    PaymentMandate save(PaymentMandate paymentMandate);

    PaymentMandate findById(Long id);

    void deleteById(Long id);

    boolean existsActiveBySubscriptionId(Long subscriptionId);
}
