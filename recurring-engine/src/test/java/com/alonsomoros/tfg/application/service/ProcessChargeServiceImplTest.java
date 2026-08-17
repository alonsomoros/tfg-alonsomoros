package com.alonsomoros.tfg.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alonsomoros.tfg.application.exception.PaymentGatewayException;
import com.alonsomoros.tfg.application.factory.PaymentGatewayFactory;
import com.alonsomoros.tfg.domain.model.PaymentMethod;
import com.alonsomoros.tfg.domain.model.Transaction;
import com.alonsomoros.tfg.domain.model.TransactionStatusEnum;
import com.alonsomoros.tfg.domain.port.PaymentMethodRepositoryPort;
import com.alonsomoros.tfg.domain.port.TransactionRepositoryPort;
import com.alonsomoros.tfg.domain.port.out.PaymentGatewayPort;
import com.alonsomoros.tfg.infrastructure.web.dto.response.ChargeMandateResponseDto;

@ExtendWith(MockitoExtension.class)
class ProcessChargeServiceImplTest {

    @Mock
    private PaymentMethodRepositoryPort paymentMethodRepository;
    @Mock
    private TransactionRepositoryPort transactionRepository;
    @Mock
    private PaymentGatewayFactory gatewayFactory;
    @Mock
    private PaymentGatewayPort paymentGateway;

    @InjectMocks
    private ProcessChargeServiceImpl processChargeService;

    @Test
    void executeCharge_whenGatewaySucceeds_savesSuccessTransaction() {
        UUID mandateId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("19.99");
        PaymentMethod mandate = paymentMethod(mandateId);

        when(paymentMethodRepository.findById(mandateId)).thenReturn(mandate);
        when(gatewayFactory.getGateway("STRIPE")).thenReturn(paymentGateway);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

        ChargeMandateResponseDto response = processChargeService.executeCharge(mandateId, amount);

        verify(paymentGateway).charge("pm_123", amount);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());
        Transaction saved = captor.getValue();
        assertThat(saved.getPaymentMethod()).isEqualTo(mandate);
        assertThat(saved.getAmount()).isEqualByComparingTo(amount);
        assertThat(saved.getStatus()).isEqualTo(TransactionStatusEnum.SUCCESS);

        assertThat(response.message()).contains(mandateId.toString());
        assertThat(response.message()).contains("SUCCESS");
    }

    @Test
    void executeCharge_whenGatewayFails_savesFailedTransactionAndThrows() {
        UUID mandateId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("19.99");
        PaymentMethod mandate = paymentMethod(mandateId);

        when(paymentMethodRepository.findById(mandateId)).thenReturn(mandate);
        when(gatewayFactory.getGateway("STRIPE")).thenReturn(paymentGateway);
        doThrow(new PaymentGatewayException("Stripe declined"))
                .when(paymentGateway).charge("pm_123", amount);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

        assertThatThrownBy(() -> processChargeService.executeCharge(mandateId, amount))
                .isInstanceOf(PaymentGatewayException.class)
                .hasMessage("Payment processing failed in gateway")
                .hasCauseInstanceOf(PaymentGatewayException.class);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());
        Transaction saved = captor.getValue();
        assertThat(saved.getPaymentMethod()).isEqualTo(mandate);
        assertThat(saved.getAmount()).isEqualByComparingTo(amount);
        assertThat(saved.getStatus()).isEqualTo(TransactionStatusEnum.FAILED);
    }

    private static PaymentMethod paymentMethod(UUID mandateId) {
        return PaymentMethod.builder()
                .id(mandateId)
                .subscriptionId(UUID.randomUUID())
                .provider("STRIPE")
                .token("pm_123")
                .last4("4242")
                .active(true)
                .build();
    }
}
