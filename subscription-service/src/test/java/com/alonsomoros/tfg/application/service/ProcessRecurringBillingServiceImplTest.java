package com.alonsomoros.tfg.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alonsomoros.tfg.application.port.out.RecurringEngineClientPort;
import com.alonsomoros.tfg.domain.model.BillingInterval;
import com.alonsomoros.tfg.domain.model.CurrencyCode;
import com.alonsomoros.tfg.domain.model.Plan;
import com.alonsomoros.tfg.domain.model.Subscription;
import com.alonsomoros.tfg.domain.model.SubscriptionStatusEnum;
import com.alonsomoros.tfg.domain.port.PlanRepositoryPort;
import com.alonsomoros.tfg.domain.port.SubscriptionRepositoryPort;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.out.ChargeRequestDto;

@ExtendWith(MockitoExtension.class)
class ProcessRecurringBillingServiceImplTest {

    @Mock
    private SubscriptionRepositoryPort subscriptionRepository;
    @Mock
    private PlanRepositoryPort planRepository;
    @Mock
    private RecurringEngineClientPort recurringEngineClient;

    @InjectMocks
    private ProcessRecurringBillingServiceImpl billingService;

    @Test
    void execute_whenNoDueSubscriptions_doesNothing() {
        when(subscriptionRepository.findSubscriptionsDueForBilling(LocalDate.now())).thenReturn(List.of());

        billingService.execute();

        verify(recurringEngineClient, never()).chargeMandate(any(), any());
        verify(subscriptionRepository, never()).save(any());
    }

    @Test
    void execute_whenChargeSucceeds_advancesNextPaymentDate() {
        UUID methodId = UUID.randomUUID();
        LocalDate dueDate = LocalDate.now();
        Plan plan = Plan.builder()
                .code("PRO_MONTHLY")
                .amount(new BigDecimal("19.99"))
                .currency(CurrencyCode.EUR)
                .billingInterval(BillingInterval.MONTHLY)
                .build();
        Subscription subscription = Subscription.builder()
                .id(UUID.randomUUID())
                .customerEmail("user@example.com")
                .plan(plan)
                .status(SubscriptionStatusEnum.ACTIVE)
                .externalPaymentMethodId(methodId)
                .nextPaymentDate(dueDate)
                .build();

        when(subscriptionRepository.findSubscriptionsDueForBilling(dueDate)).thenReturn(List.of(subscription));
        when(planRepository.findByCode("PRO_MONTHLY")).thenReturn(plan);
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(inv -> inv.getArgument(0));

        billingService.execute();

        verify(recurringEngineClient).chargeMandate(eq(methodId), eq(new ChargeRequestDto(plan.getAmount())));
        assertThat(subscription.getNextPaymentDate()).isEqualTo(dueDate.plusMonths(1));
        assertThat(subscription.getStatus()).isEqualTo(SubscriptionStatusEnum.ACTIVE);
        verify(subscriptionRepository).save(subscription);
    }

    @Test
    void execute_whenChargeFails_marksPastDue() {
        UUID methodId = UUID.randomUUID();
        Plan plan = Plan.builder()
                .code("PRO_MONTHLY")
                .amount(new BigDecimal("19.99"))
                .currency(CurrencyCode.EUR)
                .billingInterval(BillingInterval.MONTHLY)
                .build();
        Subscription subscription = Subscription.builder()
                .id(UUID.randomUUID())
                .customerEmail("user@example.com")
                .plan(plan)
                .status(SubscriptionStatusEnum.ACTIVE)
                .externalPaymentMethodId(methodId)
                .nextPaymentDate(LocalDate.now())
                .build();

        when(subscriptionRepository.findSubscriptionsDueForBilling(LocalDate.now())).thenReturn(List.of(subscription));
        when(planRepository.findByCode("PRO_MONTHLY")).thenReturn(plan);
        when(recurringEngineClient.chargeMandate(any(), any())).thenThrow(new RuntimeException("charge failed"));
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(inv -> inv.getArgument(0));

        billingService.execute();

        ArgumentCaptor<Subscription> saved = ArgumentCaptor.forClass(Subscription.class);
        verify(subscriptionRepository).save(saved.capture());
        assertThat(saved.getValue().getStatus()).isEqualTo(SubscriptionStatusEnum.PAST_DUE);
    }
}
