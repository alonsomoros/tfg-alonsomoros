package com.alonsomoros.tfg.infrastructure.web.mapper;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.application.command.RegisterMandateCommand;
import com.alonsomoros.tfg.infrastructure.web.dto.request.PaymentMethodRequestDto;

@Component
public class WebRecurringMapper {

    public RegisterMandateCommand toCommand(PaymentMethodRequestDto requestDto) {
        return new RegisterMandateCommand(
            requestDto.subscriptionId(),
            requestDto.paymentInfo().provider(),
            requestDto.paymentInfo().token(),
            requestDto.paymentInfo().last4()
        );
    }

}
