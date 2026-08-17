package com.alonsomoros.tfg.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alonsomoros.tfg.infrastructure.persistence.entity.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {

    List<TransactionEntity> findByPaymentMethodEntity_Id(UUID paymentMethodId);
}
