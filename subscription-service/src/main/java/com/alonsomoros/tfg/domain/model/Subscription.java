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

    public void markAsPastDue() {
        this.status = SubscriptionStatusEnum.PAST_DUE;
    }

    public void setNextPaymentDate(BillingInterval billingInterval) {
        if (billingInterval == null) {
            throw new RuntimeException("Cannot set nextPaymentDate without a billingInterval");
        }

        if (this.nextPaymentDate == null) {
            this.nextPaymentDate = LocalDate.now();
        }
        
        switch (billingInterval) {
            case WEEKLY -> this.nextPaymentDate = this.nextPaymentDate.plusWeeks(1);
            case MONTHLY -> this.nextPaymentDate = this.nextPaymentDate.plusMonths(1);
            case YEARLY -> this.nextPaymentDate = this.nextPaymentDate.plusYears(1);
            default -> throw new IllegalArgumentException("Not supported billing interval: " + billingInterval);
        };
    }

}
