package com.alonsomoros.tfg.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.application.exception.PaymentMethodNotFoundException;
import com.alonsomoros.tfg.domain.model.Transaction;
import com.alonsomoros.tfg.domain.port.TransactionRepositoryPort;
import com.alonsomoros.tfg.infrastructure.mapper.TransactionMapper;
import com.alonsomoros.tfg.infrastructure.persistence.entity.PaymentMethodEntity;
import com.alonsomoros.tfg.infrastructure.persistence.entity.TransactionEntity;
import com.alonsomoros.tfg.infrastructure.persistence.repository.PaymentMethodRepository;
import com.alonsomoros.tfg.infrastructure.persistence.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionRepositoryAdapter implements TransactionRepositoryPort {

    private final TransactionRepository jpaRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final TransactionMapper mapper;

    @Override
    public Transaction save(Transaction transaction) {
        log.debug("Persisting [Transaction] in DB | transactionId: {}, paymentMethodId: {}, amount: {}, status: {}",
                transaction.getId(), transaction.getPaymentMethod().getId(), transaction.getAmount(),
                transaction.getStatus());

        PaymentMethodEntity paymentMethodEntity = paymentMethodRepository
                .findById(transaction.getPaymentMethod().getId())
                .orElseThrow(() -> new PaymentMethodNotFoundException(
                        "Payment method not found with ID: " + transaction.getPaymentMethod().getId()));

        TransactionEntity entity = mapper.toEntity(transaction);
        entity.setPaymentMethodEntity(paymentMethodEntity);

        TransactionEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
}
