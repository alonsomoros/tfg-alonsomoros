package com.alonsomoros.tfg.infrastructure.persistence.entity;

import java.time.LocalDate;

import com.alonsomoros.tfg.domain.model.SubscriptionStatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_email", nullable = false)
    private String customerEmail;

    @Column(name = "plan_id", nullable = false)
    private String planId;

    @Column(nullable = false)
    private SubscriptionStatusEnum status;

    @Column(name = "next_payment_date", nullable = false)
    private LocalDate nextPaymentDate;

}
