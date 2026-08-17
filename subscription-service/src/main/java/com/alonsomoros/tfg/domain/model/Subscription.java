package com.alonsomoros.tfg.domain.model;

import java.time.LocalDate;
import java.util.UUID;

import com.alonsomoros.tfg.domain.exception.InvalidSubscriptionStateException;
import com.alonsomoros.tfg.domain.exception.UnsupportedBillingIntervalException;

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

    private UUID id;

    private UUID externalPaymentMethodId;

    private String customerEmail;

    private Plan plan;

    private SubscriptionStatusEnum status;

    private LocalDate nextPaymentDate;

    public void markAsActive(UUID methodId) {
        if (methodId == null) {
            throw new InvalidSubscriptionStateException("A subscription cannot be ACTIVE without a paymentMethodId");
        }
        this.externalPaymentMethodId = methodId;
        this.status = SubscriptionStatusEnum.ACTIVE;
    }

    public void markAsPastDue() {
        this.status = SubscriptionStatusEnum.PAST_DUE;
    }

    public void setNextPaymentDate(BillingInterval billingInterval) {
        if (billingInterval == null) {
            throw new InvalidSubscriptionStateException("Cannot set nextPaymentDate without a billingInterval");
        }

        if (this.nextPaymentDate == null) {
            this.nextPaymentDate = LocalDate.now();
        }
        
        switch (billingInterval) {
            case WEEKLY -> this.nextPaymentDate = this.nextPaymentDate.plusWeeks(1);
            case MONTHLY -> this.nextPaymentDate = this.nextPaymentDate.plusMonths(1);
            case YEARLY -> this.nextPaymentDate = this.nextPaymentDate.plusYears(1);
            default -> throw new UnsupportedBillingIntervalException(billingInterval.name());
        };
    }

    public void setNextPaymentDate(LocalDate nextPaymentDate) {
        if (nextPaymentDate == null) {
            throw new InvalidSubscriptionStateException("Cannot set nextPaymentDate to null");
        }
        this.nextPaymentDate = nextPaymentDate;
    }

}
