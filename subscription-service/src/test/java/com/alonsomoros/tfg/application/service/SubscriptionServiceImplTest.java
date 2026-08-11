package com.alonsomoros.tfg.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alonsomoros.tfg.application.command.CreateSubscriptionCommand;
import com.alonsomoros.tfg.application.port.out.RecurringEngineClientPort;
import com.alonsomoros.tfg.domain.exception.SubscriptionAlreadyOngoingException;
import com.alonsomoros.tfg.domain.model.BillingInterval;
import com.alonsomoros.tfg.domain.model.CurrencyCode;
import com.alonsomoros.tfg.domain.model.Plan;
import com.alonsomoros.tfg.domain.model.Subscription;
import com.alonsomoros.tfg.domain.model.SubscriptionStatusEnum;
import com.alonsomoros.tfg.domain.port.PlanRepositoryPort;
import com.alonsomoros.tfg.domain.port.SubscriptionRepositoryPort;
import com.alonsomoros.tfg.infrastructure.client.feign.dto.in.PaymentMethodResponseDto;
import com.alonsomoros.tfg.infrastructure.persistence.mapper.SubscriptionMapper;
import com.alonsomoros.tfg.infrastructure.web.dto.response.SubscriptionResponseDto;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceImplTest {

    @Mock
    private SubscriptionRepositoryPort subscriptionRepository;
    @Mock
    private PlanRepositoryPort planRepository;
    @Mock
    private SubscriptionMapper subscriptionMapper;
    @Mock
    private RecurringEngineClientPort recurringEngineClient;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    private CreateSubscriptionCommand command;
    private Plan plan;
    private Subscription pendingSubscription;

    @BeforeEach
    void setUp() {
        command = new CreateSubscriptionCommand(
                "user@example.com",
                "PRO_MONTHLY",
                new CreateSubscriptionCommand.PaymentDetailsCommand(
                        "STRIPE", "tok_123", "Alonso Moros", "01", "2030", "4242"));

        plan = Plan.builder()
                .id(UUID.randomUUID())
                .code("PRO_MONTHLY")
                .name("Pro")
                .amount(new BigDecimal("19.99"))
                .currency(CurrencyCode.EUR)
                .billingInterval(BillingInterval.MONTHLY)
                .isActive(true)
                .build();

        pendingSubscription = Subscription.builder()
                .id(UUID.randomUUID())
                .customerEmail(command.customerEmail())
                .planId(command.planId())
                .status(SubscriptionStatusEnum.PENDING)
                .build();
    }

    @Test
    void createSubscription_whenNoOngoing_activatesAfterPaymentMethod() {
        UUID paymentMethodId = UUID.randomUUID();
        when(subscriptionRepository.hasOngoingSubscription(command.customerEmail(), command.planId()))
                .thenReturn(false);
        when(subscriptionMapper.toDomain(command)).thenReturn(pendingSubscription);
        when(planRepository.findByCode(command.planId())).thenReturn(plan);
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(inv -> inv.getArgument(0));
        when(recurringEngineClient.sendPaymentToken(eq(pendingSubscription.getId()), eq(command.paymentInfo())))
                .thenReturn(new PaymentMethodResponseDto(paymentMethodId));
        when(subscriptionMapper.toResponseDto(any(Subscription.class)))
                .thenReturn(new SubscriptionResponseDto(command.customerEmail(), command.planId()));

        SubscriptionResponseDto response = subscriptionService.createSubscription(command);

        assertThat(response.customerEmail()).isEqualTo(command.customerEmail());
        assertThat(response.planId()).isEqualTo(command.planId());

        ArgumentCaptor<Subscription> saved = ArgumentCaptor.forClass(Subscription.class);
        verify(subscriptionRepository, org.mockito.Mockito.times(2)).save(saved.capture());
        Subscription activated = saved.getAllValues().get(1);
        assertThat(activated.getStatus()).isEqualTo(SubscriptionStatusEnum.ACTIVE);
        assertThat(activated.getExternalPaymentMethodId()).isEqualTo(paymentMethodId);
        assertThat(activated.getNextPaymentDate()).isEqualTo(LocalDate.now().plusMonths(1));
    }

    @Test
    void createSubscription_whenOngoingExists_throwsConflict() {
        when(subscriptionRepository.hasOngoingSubscription(command.customerEmail(), command.planId()))
                .thenReturn(true);

        assertThatThrownBy(() -> subscriptionService.createSubscription(command))
                .isInstanceOf(SubscriptionAlreadyOngoingException.class);

        verify(subscriptionRepository, never()).save(any());
        verify(recurringEngineClient, never()).sendPaymentToken(any(), any());
    }

    @Test
    void createSubscription_whenRecurringEngineFails_keepsPending() {
        when(subscriptionRepository.hasOngoingSubscription(command.customerEmail(), command.planId()))
                .thenReturn(false);
        when(subscriptionMapper.toDomain(command)).thenReturn(pendingSubscription);
        when(planRepository.findByCode(command.planId())).thenReturn(plan);
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(inv -> inv.getArgument(0));
        when(recurringEngineClient.sendPaymentToken(any(), any()))
                .thenThrow(new RuntimeException("engine down"));
        when(subscriptionMapper.toResponseDto(any(Subscription.class)))
                .thenReturn(new SubscriptionResponseDto(command.customerEmail(), command.planId()));

        subscriptionService.createSubscription(command);

        ArgumentCaptor<Subscription> saved = ArgumentCaptor.forClass(Subscription.class);
        verify(subscriptionRepository).save(saved.capture());
        assertThat(saved.getValue().getStatus()).isEqualTo(SubscriptionStatusEnum.PENDING);
    }

    @Test
    void updatePaymentDate_updatesAndPersists() {
        UUID subscriptionId = UUID.randomUUID();
        LocalDate newDate = LocalDate.of(2026, 9, 1);
        Subscription existing = Subscription.builder()
                .id(subscriptionId)
                .customerEmail("user@example.com")
                .planId("PRO_MONTHLY")
                .status(SubscriptionStatusEnum.ACTIVE)
                .nextPaymentDate(LocalDate.now())
                .build();

        when(subscriptionRepository.findById(subscriptionId)).thenReturn(existing);
        when(subscriptionRepository.save(existing)).thenReturn(existing);

        subscriptionService.updatePaymentDate(subscriptionId, newDate);

        assertThat(existing.getNextPaymentDate()).isEqualTo(newDate);
        verify(subscriptionRepository).save(existing);
    }
}
