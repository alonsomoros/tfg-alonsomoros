package com.alonsomoros.tfg.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alonsomoros.tfg.infrastructure.persistence.entity.PaymentMethodEntity;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethodEntity, Long> {
    Optional<PaymentMethodEntity> findBySubscriptionId(Long subscriptionId);

    @Query("SELECT COUNT(p) > 0 FROM PaymentMethodEntity p " +
            "WHERE p.subscriptionId = :subscriptionId " +
            "AND p.active = true")
    boolean existsActiveBySubscriptionId(@Param("subscriptionId") Long subscriptionId);
}

