package com.alonsomoros.tfg.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.alonsomoros.tfg.domain.exception.InvalidSubscriptionStateException;

class SubscriptionTest {

    @Test
    void markAsActive_setsStatusAndPaymentMethod() {
        Subscription subscription = new Subscription();
        UUID methodId = UUID.randomUUID();

        subscription.markAsActive(methodId);

        assertThat(subscription.getStatus()).isEqualTo(SubscriptionStatusEnum.ACTIVE);
        assertThat(subscription.getExternalPaymentMethodId()).isEqualTo(methodId);
    }

    @Test
    void markAsActive_whenMethodIdNull_throws() {
        Subscription subscription = new Subscription();

        assertThatThrownBy(() -> subscription.markAsActive(null))
                .isInstanceOf(InvalidSubscriptionStateException.class);
    }

    @Test
    void setNextPaymentDate_fromBillingInterval_monthly() {
        Subscription subscription = new Subscription();
        subscription.setNextPaymentDate(LocalDate.of(2026, 1, 10));

        subscription.setNextPaymentDate(BillingInterval.MONTHLY);

        assertThat(subscription.getNextPaymentDate()).isEqualTo(LocalDate.of(2026, 2, 10));
    }

    @Test
    void setNextPaymentDate_whenNullDate_throws() {
        Subscription subscription = new Subscription();

        assertThatThrownBy(() -> subscription.setNextPaymentDate((LocalDate) null))
                .isInstanceOf(InvalidSubscriptionStateException.class);
    }

    @Test
    void markAsPastDue_updatesStatus() {
        Subscription subscription = Subscription.builder()
                .status(SubscriptionStatusEnum.ACTIVE)
                .build();

        subscription.markAsPastDue();

        assertThat(subscription.getStatus()).isEqualTo(SubscriptionStatusEnum.PAST_DUE);
    }
}
