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
        log.info(
                "Processing [PaymentMethod] registration from <<<Subscription Service>>> | subscriptionId: {}, provider: {}",
                command.subscriptionId(), command.provider());

        if (repositoryPort.existsActiveBySubscriptionId(command.subscriptionId())) {
            log.warn(
                    "Rejected [PaymentMethod] registration because an active mandate already exists | subscriptionId: {}",
                    command.subscriptionId());
            throw new PaymentMethodAlreadyExistsException(
                    "Payment Method already exists | ID: " + command.subscriptionId());
        }

        PaymentMethod mandate = new PaymentMethod();
        mandate.setSubscriptionId(command.subscriptionId());
        mandate.setProvider(command.provider());
        mandate.setToken(command.token());
        mandate.setLast4(command.last4());
        mandate.setActive(true);

        PaymentMethod savedMandate = repositoryPort.save(mandate);
        log.info("Registered [PaymentMethod] successfully | paymentMandateId: {}, subscriptionId: {}, provider: {}",
                savedMandate.getId(), savedMandate.getSubscriptionId(), savedMandate.getProvider());
        return new PaymentMandateResponseDto(savedMandate.getId());
    }

}
