package com.alonsomoros.tfg.infrastructure.mapper;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.application.command.RegisterPaymentMethodCommand;
import com.alonsomoros.tfg.domain.model.PaymentMethod;
import com.alonsomoros.tfg.infrastructure.persistence.entity.PaymentMethodEntity;

@Component
public class PaymentMethodMapper {

    public PaymentMethod toDomain(RegisterPaymentMethodCommand command) {
        return PaymentMethod.builder()
                .subscriptionId(command.subscriptionId())
                .provider(command.provider())
                .token(command.token())
                .last4(command.last4())
                .build();
    }

    public PaymentMethodEntity toEntity(PaymentMethod paymentMethod) {
        return PaymentMethodEntity.builder()
                .subscriptionId(paymentMethod.getSubscriptionId())
                .provider(paymentMethod.getProvider())
                .token(paymentMethod.getToken())
                .last4(paymentMethod.getLast4())
                .active(paymentMethod.isActive())
                .build();
    }

    public PaymentMethod toDomain(PaymentMethodEntity entity) {
        return PaymentMethod.builder()
                .id(entity.getId())
                .subscriptionId(entity.getSubscriptionId())
                .provider(entity.getProvider())
                .token(entity.getToken())
                .last4(entity.getLast4())
                .active(entity.isActive())
                .build();
    }
    
}
