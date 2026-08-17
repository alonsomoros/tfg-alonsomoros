package com.alonsomoros.tfg.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.application.command.CreateSubscriptionCommand;
import com.alonsomoros.tfg.domain.model.Subscription;
import com.alonsomoros.tfg.domain.model.SubscriptionStatusEnum;
import com.alonsomoros.tfg.infrastructure.persistence.entity.SubscriptionEntity;
import com.alonsomoros.tfg.infrastructure.web.dto.response.SubscriptionResponseDto;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@Component
public class SubscriptionMapper {
    
    private final PlanMapper planMapper;

    public Subscription toDomain(CreateSubscriptionCommand command) {
        return Subscription.builder()
                .customerEmail(command.customerEmail())
                .status(SubscriptionStatusEnum.PENDING)
                .build();
    }

    public SubscriptionEntity toEntity(Subscription subscription) {
        return SubscriptionEntity.builder()
                .customerEmail(subscription.getCustomerEmail())
                .planEntity(planMapper.toEntity(subscription.getPlan()))
                .status(subscription.getStatus())
                .id(subscription.getId())
                .nextPaymentDate(subscription.getNextPaymentDate())
                .externalPaymentMethodId(subscription.getExternalPaymentMethodId())
                .build();
    }

    public Subscription toDomain(SubscriptionEntity subscriptionEntity) {
        return Subscription.builder()
                .customerEmail(subscriptionEntity.getCustomerEmail())
                .plan(planMapper.toDomain(subscriptionEntity.getPlanEntity()))
                .status(subscriptionEntity.getStatus())
                .id(subscriptionEntity.getId())
                .nextPaymentDate(subscriptionEntity.getNextPaymentDate())
                .externalPaymentMethodId(subscriptionEntity.getExternalPaymentMethodId())
                .build();
    }

    public SubscriptionResponseDto toResponseDto(Subscription subscription) {
        return SubscriptionResponseDto.from(subscription.getCustomerEmail(), subscription.getPlan().getCode());
    }

}
