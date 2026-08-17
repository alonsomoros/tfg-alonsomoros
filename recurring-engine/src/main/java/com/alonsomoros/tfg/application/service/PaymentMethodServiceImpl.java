package com.alonsomoros.tfg.application.service;

import org.springframework.stereotype.Service;

import com.alonsomoros.tfg.application.command.RegisterPaymentMethodCommand;
import com.alonsomoros.tfg.application.port.in.IPaymentMethodService;
import com.alonsomoros.tfg.domain.exception.PaymentMethodAlreadyExistsException;
import com.alonsomoros.tfg.domain.model.PaymentMethod;
import com.alonsomoros.tfg.domain.port.PaymentMethodRepositoryPort;
import com.alonsomoros.tfg.infrastructure.web.dto.response.PaymentMethodResponseDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentMethodServiceImpl implements IPaymentMethodService {

    private final PaymentMethodRepositoryPort repositoryPort;

    @Override
    public PaymentMethodResponseDto registerPaymentMethod(RegisterPaymentMethodCommand command) {
        log.info(
                "Processing [PaymentMethod] registration from <<<Subscription Service>>> | subscriptionId: {}, provider: {}",
                command.subscriptionId(), command.provider());

        if (repositoryPort.existsActiveBySubscriptionId(command.subscriptionId())) {
            log.warn(
                    "Rejected [PaymentMethod] registration because an active paymentMethod already exists | subscriptionId: {}",
                    command.subscriptionId());
            throw new PaymentMethodAlreadyExistsException(
                    "Payment Method already exists | ID: " + command.subscriptionId());
        }

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setSubscriptionId(command.subscriptionId());
        paymentMethod.setProvider(command.provider());
        paymentMethod.setToken(command.token());
        paymentMethod.setLast4(command.last4());
        paymentMethod.setActive(true);

        PaymentMethod savedpaymentMethod = repositoryPort.save(paymentMethod);
        log.info("Registered [PaymentMethod] successfully | paymentMethodId: {}, subscriptionId: {}, provider: {}",
                savedpaymentMethod.getId(), savedpaymentMethod.getSubscriptionId(), savedpaymentMethod.getProvider());
        return new PaymentMethodResponseDto(savedpaymentMethod.getId());
    }

}
