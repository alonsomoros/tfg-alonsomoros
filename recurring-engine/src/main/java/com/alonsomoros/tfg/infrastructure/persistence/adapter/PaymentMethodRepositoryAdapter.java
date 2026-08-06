package com.alonsomoros.tfg.infrastructure.persistence.adapter;

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
        log.info("Saving payment method in BBDD | ID: {}", paymentMandate.getId());
        PaymentMethodEntity entity = mapper.toEntity(paymentMandate);
        PaymentMethodEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public PaymentMethod findById(Long id) {
        log.info("Finding payment method in BBDD | ID: {}", id);
        PaymentMethodEntity entity = repository.findById(id)
                .orElseThrow(() -> new PaymentMethodNotFoundException("Payment method not found with ID: " + id));
        return mapper.toDomain(entity);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting payment method in BBDD | ID: {}", id);
        repository.deleteById(id);
    }

    @Override
    public boolean existsActiveBySubscriptionId(Long subscriptionId) {
        log.info("Checking for active payment method in BBDD | Subscription ID: {}", subscriptionId);
        return repository.existsActiveBySubscriptionId(subscriptionId);
    }

}
