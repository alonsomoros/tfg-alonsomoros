package com.alonsomoros.tfg.application.service;

import org.springframework.stereotype.Service;

import com.alonsomoros.tfg.application.command.RegisterMandateCommand;
import com.alonsomoros.tfg.domain.model.PaymentMandate;
import com.alonsomoros.tfg.domain.port.RecurringEngineRepositoryPort;
import com.alonsomoros.tfg.domain.service.IPaymentMandateService;
import com.alonsomoros.tfg.infrastructure.mapper.PaymentMandateMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentMandateServiceImpl implements IPaymentMandateService {

    private final RecurringEngineRepositoryPort repositoryPort;
    private final PaymentMandateMapper paymentMandateMapper;

    @Override
    public void registerPaymentMandate(RegisterMandateCommand command) {
        log.info("Saving mandate for subscription: {}", command.subscriptionId());

        if (repositoryPort.existsActiveBySubscriptionId(command.subscriptionId())) {
            log.warn("The subscription {} already has an active mandate. Ignoring request.", command.subscriptionId());
            return;
        }

        // 2. Crear el Dominio
        PaymentMandate mandate = new PaymentMandate();
        mandate.setSubscriptionId(command.subscriptionId());
        mandate.setProvider(command.provider());
        mandate.setToken(command.token());
        mandate.setLast4(command.last4());
        mandate.setActive(true);

        // 3. Guardar usando el Puerto de Salida
        repositoryPort.save(mandate);
    }
    
}
