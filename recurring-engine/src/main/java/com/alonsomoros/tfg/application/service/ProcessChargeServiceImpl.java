package com.alonsomoros.tfg.application.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.alonsomoros.tfg.application.factory.PaymentGatewayFactory;
import com.alonsomoros.tfg.application.port.in.IProcessChargeService;
import com.alonsomoros.tfg.domain.model.PaymentMethod;
import com.alonsomoros.tfg.domain.port.PaymentMethodRepositoryPort;
import com.alonsomoros.tfg.domain.port.out.PaymentGatewayPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcessChargeServiceImpl implements IProcessChargeService {

    private final PaymentMethodRepositoryPort paymentMethodRepository;
    private final PaymentGatewayFactory gatewayFactory;

    @Override
    public void executeCharge(Long mandateId, BigDecimal amount) {
        PaymentMethod mandate = paymentMethodRepository.findById(mandateId);

        PaymentGatewayPort paymentGateway = gatewayFactory.getGateway(mandate.getProvider());

        paymentGateway.charge(mandate.getToken(), amount);
    }
}