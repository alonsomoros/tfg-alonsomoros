package com.alonsomoros.tfg.infrastructure.persistence.entity;

import java.time.LocalDate;
import java.util.UUID;

import com.alonsomoros.tfg.domain.model.SubscriptionStatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "external_payment_method_id", nullable = true)
    private UUID externalPaymentMethodId;

    @Column(name = "customer_email", nullable = false)
    private String customerEmail;

    @Column(name = "plan_id", nullable = false)
    private String planId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatusEnum status;

    @Column(name = "next_payment_date", nullable = false)
    private LocalDate nextPaymentDate;

}
