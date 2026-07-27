package com.alonsomoros.tfg.application.service;

import org.springframework.stereotype.Service;

import com.alonsomoros.tfg.application.command.RegisterPaymentMethodCommand;
import com.alonsomoros.tfg.application.port.in.IPaymentMethodService;
import com.alonsomoros.tfg.domain.model.PaymentMethod;
import com.alonsomoros.tfg.domain.port.PaymentMethodRepositoryPort;
import com.alonsomoros.tfg.infrastructure.mapper.PaymentMethodMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentMethodServiceImpl implements IPaymentMethodService {

    private final PaymentMethodRepositoryPort repositoryPort;
    private final PaymentMethodMapper paymentMandateMapper;

    @Override
    public void registerPaymentMandate(RegisterPaymentMethodCommand command) {
        log.info("Saving mandate for subscription: {}", command.subscriptionId());

        if (repositoryPort.existsActiveBySubscriptionId(command.subscriptionId())) {
            log.warn("The subscription {} already has an active mandate. Ignoring request.", command.subscriptionId());
            return;
        }

        // 2. Crear el Dominio
        PaymentMethod mandate = new PaymentMethod();
        mandate.setSubscriptionId(command.subscriptionId());
        mandate.setProvider(command.provider());
        mandate.setToken(command.token());
        mandate.setLast4(command.last4());
        mandate.setActive(true);

        // 3. Guardar usando el Puerto de Salida
        repositoryPort.save(mandate);
    }
    
}
