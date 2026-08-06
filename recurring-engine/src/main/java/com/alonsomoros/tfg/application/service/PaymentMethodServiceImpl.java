package com.alonsomoros.tfg.application.service;

import org.springframework.stereotype.Service;

import com.alonsomoros.tfg.application.command.RegisterPaymentMethodCommand;
import com.alonsomoros.tfg.application.port.in.IPaymentMethodService;
import com.alonsomoros.tfg.domain.exception.PaymentMethodAlreadyExistsException;
import com.alonsomoros.tfg.domain.model.PaymentMethod;
import com.alonsomoros.tfg.domain.port.PaymentMethodRepositoryPort;
import com.alonsomoros.tfg.infrastructure.web.dto.response.PaymentMandateResponseDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentMethodServiceImpl implements IPaymentMethodService {

    private final PaymentMethodRepositoryPort repositoryPort;

    @Override
    public PaymentMandateResponseDto registerPaymentMandate(RegisterPaymentMethodCommand command) {
        log.info("Saving [Payment Method] for subscription with ID: {}", command.subscriptionId());

        if (repositoryPort.existsActiveBySubscriptionId(command.subscriptionId())) {
            throw new PaymentMethodAlreadyExistsException("Payment Method already exists | ID: " + command.subscriptionId());
        }

        PaymentMethod mandate = new PaymentMethod();
        mandate.setSubscriptionId(command.subscriptionId());
        mandate.setProvider(command.provider());
        mandate.setToken(command.token());
        mandate.setLast4(command.last4());
        mandate.setActive(true);

        PaymentMethod savedMandate = repositoryPort.save(mandate);
        return new PaymentMandateResponseDto(savedMandate.getId());
    }
    
}
