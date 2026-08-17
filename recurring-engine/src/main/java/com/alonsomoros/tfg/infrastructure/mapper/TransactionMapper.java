package com.alonsomoros.tfg.infrastructure.mapper;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.domain.model.Transaction;
import com.alonsomoros.tfg.infrastructure.persistence.entity.TransactionEntity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class TransactionMapper {

    private final PaymentMethodMapper paymentMethodMapper;
    
    public Transaction toDomain(TransactionEntity entity) {
        if (entity == null) return null;
        return Transaction.builder()
                .id(entity.getTransactionId())
                .paymentMethod(paymentMethodMapper.toDomain(entity.getPaymentMethodEntity()))
                .amount(entity.getAmount())
                .status(entity.getStatus())
                .build();
    }

    public TransactionEntity toEntity(Transaction domain) {
        if (domain == null) return null;
        return TransactionEntity.builder()
                .transactionId(domain.getId())
                .amount(domain.getAmount())
                .status(domain.getStatus())
                .build();
    }
}