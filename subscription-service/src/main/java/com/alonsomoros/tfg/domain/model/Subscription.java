package com.alonsomoros.tfg.domain.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    private Long id;

    private Long externalPaymentMandateId;

    private String customerEmail;

    private String planId;

    private SubscriptionStatusEnum status;

    private LocalDate nextPaymentDate;

    public void markAsActive(Long mandateId) {
        if (mandateId == null) {
            throw new RuntimeException("A subscription cannot be ACTIVE without a paymentMandateId");
        }
        this.externalPaymentMandateId = mandateId;
        this.status = SubscriptionStatusEnum.ACTIVE;
    }

}
