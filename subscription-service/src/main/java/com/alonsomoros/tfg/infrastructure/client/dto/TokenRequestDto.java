package com.alonsomoros.tfg.infrastructure.client.dto;

import com.alonsomoros.tfg.infrastructure.web.dto.request.SubscriptionRequestDto.PaymentInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class TokenRequestDto {

    private Long subscriptionId;
    private PaymentInfo paymentInfo;
    
}
