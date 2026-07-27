package com.alonsomoros.tfg.infrastructure.mapper;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.application.command.RegisterMandateCommand;
import com.alonsomoros.tfg.domain.model.PaymentMandate;
import com.alonsomoros.tfg.infrastructure.persistence.entity.PaymentMandateEntity;

@Component
public class PaymentMandateMapper {

    public PaymentMandate toDomain(RegisterMandateCommand command) {
        return PaymentMandate.builder()
                .subscriptionId(command.subscriptionId())
                .provider(command.provider())
                .token(command.token())
                .last4(command.last4())
                .build();
    }

    public PaymentMandateEntity toEntity(PaymentMandate paymentMandate) {
        return PaymentMandateEntity.builder()
                .subscriptionId(paymentMandate.getSubscriptionId())
                .provider(paymentMandate.getProvider())
                .token(paymentMandate.getToken())
                .last4(paymentMandate.getLast4())
                .active(paymentMandate.isActive())
                .build();
    }

    public PaymentMandate toDomain(PaymentMandateEntity entity) {
        return PaymentMandate.builder()
                .id(entity.getId())
                .subscriptionId(entity.getSubscriptionId())
                .provider(entity.getProvider())
                .token(entity.getToken())
                .last4(entity.getLast4())
                .active(entity.isActive())
                .build();
    }
    
}
