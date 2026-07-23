package com.alonsomoros.tfg.mapper;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.domain.model.Subscription;
import com.alonsomoros.tfg.infrastructure.persistence.entity.SubscriptionEntity;
import com.alonsomoros.tfg.infrastructure.web.dto.request.SubscriptionRequestDto;
import com.alonsomoros.tfg.infrastructure.web.dto.response.SubscriptionResponseDto;
import com.alonsomoros.tfg.utils.SubscriptionStatusEnum;

@Component
public class SubscriptionMapper {
    
    public Subscription toDomain(SubscriptionRequestDto subscriptionRequestDto) {
        return Subscription.builder()
                .customerEmail(subscriptionRequestDto.customerEmail())
                .planId(subscriptionRequestDto.planId())
                .status(SubscriptionStatusEnum.PENDING)
                .build();
    }

    public SubscriptionEntity toEntity(Subscription subscription) {
        return SubscriptionEntity.builder()
                .customerEmail(subscription.getCustomerEmail())
                .planId(subscription.getPlanId())
                .status(subscription.getStatus())
                .id(subscription.getId())
                .build();
    }

    public Subscription toDomain(SubscriptionEntity subscriptionEntity) {
        return Subscription.builder()
                .customerEmail(subscriptionEntity.getCustomerEmail())
                .planId(subscriptionEntity.getPlanId())
                .status(subscriptionEntity.getStatus())
                .id(subscriptionEntity.getId())
                .build();
    }

    public SubscriptionResponseDto toResponseDto(Subscription subscription) {
        return SubscriptionResponseDto.from(subscription.getCustomerEmail(), subscription.getPlanId());
    }

}
