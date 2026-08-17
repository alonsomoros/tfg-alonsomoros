package com.alonsomoros.tfg.application.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alonsomoros.tfg.application.exception.PaymentGatewayException;
import com.alonsomoros.tfg.application.factory.PaymentGatewayFactory;
import com.alonsomoros.tfg.application.port.in.IProcessChargeService;
import com.alonsomoros.tfg.domain.model.PaymentMethod;
import com.alonsomoros.tfg.domain.model.Transaction;
import com.alonsomoros.tfg.domain.model.TransactionStatusEnum;
import com.alonsomoros.tfg.domain.port.PaymentMethodRepositoryPort;
import com.alonsomoros.tfg.domain.port.TransactionRepositoryPort;
import com.alonsomoros.tfg.domain.port.out.PaymentGatewayPort;
import com.alonsomoros.tfg.infrastructure.web.dto.response.ChargeMandateResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessChargeServiceImpl implements IProcessChargeService {

    private final PaymentMethodRepositoryPort paymentMethodRepository;
    private final TransactionRepositoryPort transactionRepository;
    private final PaymentGatewayFactory gatewayFactory;

    @Override
    @Transactional(noRollbackFor = PaymentGatewayException.class)
    public ChargeMandateResponseDto executeCharge(UUID paymentMethodId, BigDecimal amount) {
        log.info("Charging payment method: {} | Amount: {}", paymentMethodId, amount);
        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId);

        PaymentGatewayPort paymentGateway = gatewayFactory.getGateway(paymentMethod.getProvider());

        TransactionStatusEnum status;
        Exception chargeError = null;
        try {
            paymentGateway.charge(paymentMethod.getToken(), amount);
            status = TransactionStatusEnum.SUCCESS;
        } catch (Exception e) {
            log.error("Error processing charge for payment method: {} | Amount: {} | Error: {}",
                    paymentMethodId, amount, e.getMessage());
            status = TransactionStatusEnum.FAILED;
            chargeError = e;
        }

        log.info("Charge result: {} for payment method: {} | Amount: {}", status, paymentMethodId, amount);
        Transaction transaction = Transaction.builder()
                .paymentMethod(paymentMethod)
                .amount(amount)
                .status(status)
                .build();

        transactionRepository.save(transaction);
        log.info("Transaction saved with status: {} for payment method: {} | Amount: {}",
                status, paymentMethodId, amount);

        if (chargeError != null) {
            throw new PaymentGatewayException("Payment processing failed in gateway", chargeError);
        }

        return new ChargeMandateResponseDto(
                "Charge processed " + status + " for payment method: " + paymentMethodId);
    }
}