package com.alonsomoros.tfg.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.application.command.CreateSubscriptionCommand;
import com.alonsomoros.tfg.domain.model.Subscription;
import com.alonsomoros.tfg.domain.model.SubscriptionStatusEnum;
import com.alonsomoros.tfg.infrastructure.persistence.entity.SubscriptionEntity;
import com.alonsomoros.tfg.infrastructure.web.dto.response.SubscriptionResponseDto;

@Component
public class SubscriptionMapper {
    
    public Subscription toDomain(CreateSubscriptionCommand command) {
        return Subscription.builder()
                .customerEmail(command.customerEmail())
                .planId(command.planId())
                .status(SubscriptionStatusEnum.PENDING)
                .build();
    }

    public SubscriptionEntity toEntity(Subscription subscription) {
        return SubscriptionEntity.builder()
                .customerEmail(subscription.getCustomerEmail())
                .planId(subscription.getPlanId())
                .status(subscription.getStatus())
                .id(subscription.getId())
                .nextPaymentDate(subscription.getNextPaymentDate())
                .build();
    }

    public Subscription toDomain(SubscriptionEntity subscriptionEntity) {
        return Subscription.builder()
                .customerEmail(subscriptionEntity.getCustomerEmail())
                .planId(subscriptionEntity.getPlanId())
                .status(subscriptionEntity.getStatus())
                .id(subscriptionEntity.getId())
                .nextPaymentDate(subscriptionEntity.getNextPaymentDate())
                .build();
    }

    public SubscriptionResponseDto toResponseDto(Subscription subscription) {
        return SubscriptionResponseDto.from(subscription.getCustomerEmail(), subscription.getPlanId());
    }

}
