package com.alonsomoros.tfg.mapper;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.domain.model.Subscription;
import com.alonsomoros.tfg.infrastructure.web.dto.request.SubscriptionRequestDto;
import com.alonsomoros.tfg.infrastructure.web.dto.response.SubscriptionResponseDto;
import com.alonsomoros.tfg.utils.SubscriptionStatusEnum;

@Component
public class SubscriptionMapper {
    
    public Subscription toEntity(SubscriptionRequestDto subscriptionRequestDto) {
        return Subscription.builder()
                .customerEmail(subscriptionRequestDto.customerEmail())
                .planId(subscriptionRequestDto.planId())
                .status(SubscriptionStatusEnum.PENDING)
                .build();
    }

    public SubscriptionResponseDto toResponseDto(Subscription subscription) {
        return SubscriptionResponseDto.from(subscription.getCustomerEmail(), subscription.getPlanId());
    }

}
