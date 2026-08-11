package com.alonsomoros.tfg.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alonsomoros.tfg.application.command.RegisterPaymentMethodCommand;
import com.alonsomoros.tfg.domain.exception.PaymentMethodAlreadyExistsException;
import com.alonsomoros.tfg.domain.model.PaymentMethod;
import com.alonsomoros.tfg.domain.port.PaymentMethodRepositoryPort;
import com.alonsomoros.tfg.infrastructure.web.dto.response.PaymentMethodResponseDto;

@ExtendWith(MockitoExtension.class)
class PaymentMethodServiceImplTest {

    @Mock
    private PaymentMethodRepositoryPort repositoryPort;

    @InjectMocks
    private PaymentMethodServiceImpl paymentMethodService;

    @Test
    void registerPaymentMethod_whenNoActiveMandate_savesAndReturnsId() {
        UUID subscriptionId = UUID.randomUUID();
        UUID savedId = UUID.randomUUID();
        RegisterPaymentMethodCommand command = new RegisterPaymentMethodCommand(
                subscriptionId, "STRIPE", "tok_123", "4242");

        when(repositoryPort.existsActiveBySubscriptionId(subscriptionId)).thenReturn(false);
        when(repositoryPort.save(any(PaymentMethod.class))).thenAnswer(inv -> {
            PaymentMethod pm = inv.getArgument(0);
            pm.setId(savedId);
            return pm;
        });

        PaymentMethodResponseDto response = paymentMethodService.registerPaymentMethod(command);

        assertThat(response.paymentMethodId()).isEqualTo(savedId);

        ArgumentCaptor<PaymentMethod> captor = ArgumentCaptor.forClass(PaymentMethod.class);
        verify(repositoryPort).save(captor.capture());
        PaymentMethod saved = captor.getValue();
        assertThat(saved.getSubscriptionId()).isEqualTo(subscriptionId);
        assertThat(saved.getProvider()).isEqualTo("STRIPE");
        assertThat(saved.getToken()).isEqualTo("tok_123");
        assertThat(saved.getLast4()).isEqualTo("4242");
        assertThat(saved.isActive()).isTrue();
    }

    @Test
    void registerPaymentMethod_whenActiveExists_throws() {
        UUID subscriptionId = UUID.randomUUID();
        RegisterPaymentMethodCommand command = new RegisterPaymentMethodCommand(
                subscriptionId, "STRIPE", "tok_123", "4242");
        when(repositoryPort.existsActiveBySubscriptionId(subscriptionId)).thenReturn(true);

        assertThatThrownBy(() -> paymentMethodService.registerPaymentMethod(command))
                .isInstanceOf(PaymentMethodAlreadyExistsException.class);

        verify(repositoryPort, never()).save(any());
    }
}
