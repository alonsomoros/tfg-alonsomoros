package com.alonsomoros.tfg.infrastructure.web.mapper;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.application.command.CreateSubscriptionCommand;
import com.alonsomoros.tfg.application.command.PaymentDetailsCommand;
import com.alonsomoros.tfg.infrastructure.web.dto.request.SubscriptionRequestDto;
import com.alonsomoros.tfg.infrastructure.web.dto.request.SubscriptionRequestDto.PaymentInfo;

@Component
public class WebSubscriptionMapper {

    public CreateSubscriptionCommand toCommand(SubscriptionRequestDto subscriptionRequestDto) {
        return new CreateSubscriptionCommand(
                subscriptionRequestDto.customerEmail(),
                subscriptionRequestDto.planId(),
                toPaymentDetailsCommand(subscriptionRequestDto.paymentInfo())    
            );
    }

    private PaymentDetailsCommand toPaymentDetailsCommand(PaymentInfo paymentInfoDto) {
        return new PaymentDetailsCommand(
                paymentInfoDto.provider(),
                paymentInfoDto.token(),
                paymentInfoDto.cardHolder(),
                paymentInfoDto.expiryMonth(),
                paymentInfoDto.expiryYear(),
                paymentInfoDto.last4()
            );
    }

    // public SubscriptionResponseDto toResponseDto(CreateSubscriptionCommand command) {
    //     return new SubscriptionResponseDto(
    //             command.customerEmail(),
    //             command.planId(),
    //             new SubscriptionResponseDto.PaymentInfo(
    //                     command.paymentInfo().provider(),
    //                     command.paymentInfo().token(),
    //                     command.paymentInfo().cardHolder(),
    //                     command.paymentInfo().expiryMonth(),
    //                     command.paymentInfo().expiryYear(),
    //                     command.paymentInfo().last4()));
    // }

}
