package com.alonsomoros.tfg.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alonsomoros.tfg.application.factory.PaymentGatewayFactory;
import com.alonsomoros.tfg.domain.model.PaymentMethod;
import com.alonsomoros.tfg.domain.port.PaymentMethodRepositoryPort;
import com.alonsomoros.tfg.domain.port.out.PaymentGatewayPort;
import com.alonsomoros.tfg.infrastructure.web.dto.response.ChargeMandateResponseDto;

@ExtendWith(MockitoExtension.class)
class ProcessChargeServiceImplTest {

    @Mock
    private PaymentMethodRepositoryPort paymentMethodRepository;
    @Mock
    private PaymentGatewayFactory gatewayFactory;
    @Mock
    private PaymentGatewayPort paymentGateway;

    @InjectMocks
    private ProcessChargeServiceImpl processChargeService;

    @Test
    void executeCharge_chargesViaProviderGateway() {
        UUID mandateId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("19.99");
        PaymentMethod mandate = PaymentMethod.builder()
                .id(mandateId)
                .subscriptionId(UUID.randomUUID())
                .provider("STRIPE")
                .token("pm_123")
                .last4("4242")
                .active(true)
                .build();

        when(paymentMethodRepository.findById(mandateId)).thenReturn(mandate);
        when(gatewayFactory.getGateway("STRIPE")).thenReturn(paymentGateway);

        ChargeMandateResponseDto response = processChargeService.executeCharge(mandateId, amount);

        verify(paymentGateway).charge("pm_123", amount);
        assertThat(response.message()).contains(mandateId.toString());
    }
}
