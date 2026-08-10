package com.alonsomoros.tfg.infrastructure.persistence.adapter;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.application.exception.PaymentMethodNotFoundException;
import com.alonsomoros.tfg.domain.model.PaymentMethod;
import com.alonsomoros.tfg.domain.port.PaymentMethodRepositoryPort;
import com.alonsomoros.tfg.infrastructure.mapper.PaymentMethodMapper;
import com.alonsomoros.tfg.infrastructure.persistence.entity.PaymentMethodEntity;
import com.alonsomoros.tfg.infrastructure.persistence.repository.PaymentMethodRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentMethodRepositoryAdapter implements PaymentMethodRepositoryPort {

    private final PaymentMethodRepository repository;
    private final PaymentMethodMapper mapper;

    @Override
    public PaymentMethod save(PaymentMethod paymentMandate) {
        log.debug("Persisting [PaymentMethod] in DB | paymentMandateId: {}, subscriptionId: {}",
                paymentMandate.getId(), paymentMandate.getSubscriptionId());
        PaymentMethodEntity entity = mapper.toEntity(paymentMandate);
        PaymentMethodEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public PaymentMethod findById(UUID id) {
        log.debug("Loading [PaymentMethod] from DB | paymentMandateId: {}", id);
        PaymentMethodEntity entity = repository.findById(id)
                .orElseThrow(() -> new PaymentMethodNotFoundException("Payment method not found with ID: " + id));
        return mapper.toDomain(entity);
    }

    @Override
    public void deleteById(UUID id) {
        log.debug("Deleting [PaymentMethod] from DB | paymentMandateId: {}", id);
        repository.deleteById(id);
    }

    @Override
    public boolean existsActiveBySubscriptionId(UUID subscriptionId) {
        log.debug("Checking active [PaymentMethod] in DB | subscriptionId: {}", subscriptionId);
        return repository.existsActiveBySubscriptionId(subscriptionId);
    }

}
