package com.alonsomoros.tfg.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.domain.model.PaymentMethod;
import com.alonsomoros.tfg.domain.port.PaymentMethodRepositoryPort;
import com.alonsomoros.tfg.infrastructure.mapper.PaymentMethodMapper;
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
        var entity = mapper.toEntity(paymentMandate);
        var savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public PaymentMethod findById(Long id) {
        var entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Payment mandate not found with ID: " + id));
        return mapper.toDomain(entity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsActiveBySubscriptionId(Long subscriptionId) {
        return repository.existsActiveBySubscriptionId(subscriptionId);
    }
    
}
