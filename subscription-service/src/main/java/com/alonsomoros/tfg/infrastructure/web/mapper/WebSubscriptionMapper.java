package com.alonsomoros.tfg.infrastructure.web.mapper;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.application.command.CreateSubscriptionCommand;
import com.alonsomoros.tfg.infrastructure.web.dto.request.SubscriptionRequestDto;

@Component
public class WebSubscriptionMapper {

    public CreateSubscriptionCommand toCommand(SubscriptionRequestDto subscriptionRequestDto) {
        return new CreateSubscriptionCommand(
                subscriptionRequestDto.customerEmail(),
                subscriptionRequestDto.planId(),
                new CreateSubscriptionCommand.PaymentDetailsCommand(
                    subscriptionRequestDto.paymentInfo().provider(),
                    subscriptionRequestDto.paymentInfo().token(),
                    subscriptionRequestDto.paymentInfo().cardHolder(),
                    subscriptionRequestDto.paymentInfo().expiryMonth(),
                    subscriptionRequestDto.paymentInfo().expiryYear(),
                    subscriptionRequestDto.paymentInfo().last4()
                )
            );
    }

}
