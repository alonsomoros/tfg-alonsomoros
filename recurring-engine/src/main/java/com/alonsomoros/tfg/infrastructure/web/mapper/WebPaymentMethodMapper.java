package com.alonsomoros.tfg.infrastructure.web.mapper;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.application.command.RegisterPaymentMethodCommand;
import com.alonsomoros.tfg.infrastructure.web.dto.request.PaymentMethodRequestDto;

@Component
public class WebPaymentMethodMapper {

    public RegisterPaymentMethodCommand toCommand(PaymentMethodRequestDto requestDto) {
        return new RegisterPaymentMethodCommand(
            requestDto.subscriptionId(),
            requestDto.paymentInfo().provider(),
            requestDto.paymentInfo().token(),
            requestDto.paymentInfo().last4()
        );
    }

}
