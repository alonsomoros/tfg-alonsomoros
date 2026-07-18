package com.alonsomoros.tfg.domain.service;

import com.alonsomoros.tfg.infrastructure.web.dto.request.SubscriptionRequestDto;
import com.alonsomoros.tfg.infrastructure.web.dto.response.SubscriptionResponseDto;

public interface ISubscriptionService {
    SubscriptionResponseDto createSubscription(SubscriptionRequestDto subscriptionRequestDto);
}
