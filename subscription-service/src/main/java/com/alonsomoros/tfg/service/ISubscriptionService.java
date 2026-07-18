package com.alonsomoros.tfg.service;

import com.alonsomoros.tfg.dto.request.SubscriptionRequestDto;
import com.alonsomoros.tfg.dto.response.SubscriptionResponseDto;

public interface ISubscriptionService {
    SubscriptionResponseDto createSubscription(SubscriptionRequestDto subscriptionRequestDto);
}
