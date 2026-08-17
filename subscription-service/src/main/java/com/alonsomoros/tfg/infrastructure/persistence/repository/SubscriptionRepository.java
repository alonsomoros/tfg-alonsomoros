package com.alonsomoros.tfg.infrastructure.persistence.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alonsomoros.tfg.domain.model.SubscriptionStatusEnum;
import com.alonsomoros.tfg.infrastructure.persistence.entity.SubscriptionEntity;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, UUID> {

        Optional<SubscriptionEntity> findByCustomerEmail(String email);

        @Query("SELECT s FROM SubscriptionEntity s JOIN FETCH s.planEntity WHERE s.id = :id")
        Optional<SubscriptionEntity> findByIdWithPlan(@Param("id") UUID id);

        @Query("SELECT COUNT(s) > 0 FROM SubscriptionEntity s " +
                        "WHERE s.customerEmail = :email " +
                        "AND s.planEntity.id = :planId " +
                        "AND s.status IN :statuses " +
                        "AND s.deletedAt IS NULL")
        boolean existsOngoingSubscription(
                        @Param("email") String email,
                        @Param("planId") UUID planId,
                        @Param("statuses") List<SubscriptionStatusEnum> statuses);

        @Query("SELECT s FROM SubscriptionEntity s JOIN FETCH s.planEntity " +
                        "WHERE s.nextPaymentDate <= :date " +
                        "AND s.status = 'ACTIVE' " +
                        "AND s.deletedAt IS NULL")
        List<SubscriptionEntity> findSubscriptionsDueForBilling(@Param("date") LocalDate date);

}
