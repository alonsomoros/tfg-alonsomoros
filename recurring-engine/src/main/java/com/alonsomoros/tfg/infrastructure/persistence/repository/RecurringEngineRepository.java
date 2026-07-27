package com.alonsomoros.tfg.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alonsomoros.tfg.infrastructure.persistence.entity.PaymentMandateEntity;

@Repository
public interface RecurringEngineRepository extends JpaRepository<PaymentMandateEntity, Long> {
    Optional<PaymentMandateEntity> findBySubscriptionId(Long subscriptionId);

    @Query("SELECT COUNT(p) > 0 FROM PaymentMandateEntity p " +
            "WHERE p.subscriptionId = :subscriptionId " +
            "AND p.active = true")
    boolean existsActiveBySubscriptionId(@Param("subscriptionId") Long subscriptionId);
}

