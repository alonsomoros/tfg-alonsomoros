package com.alonsomoros.tfg.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.domain.model.PaymentMandate;
import com.alonsomoros.tfg.domain.port.PaymentMandateRepositoryPort;
import com.alonsomoros.tfg.infrastructure.mapper.PaymentMandateMapper;
import com.alonsomoros.tfg.infrastructure.persistence.repository.PaymentMandateRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentMandateRepositoryAdapter implements PaymentMandateRepositoryPort {

    private final PaymentMandateRepository repository;
    private final PaymentMandateMapper mapper;

    @Override
    public PaymentMandate save(PaymentMandate paymentMandate) {
        var entity = mapper.toEntity(paymentMandate);
        var savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public PaymentMandate findById(Long id) {
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
