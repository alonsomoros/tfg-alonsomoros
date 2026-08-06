package com.alonsomoros.tfg.application.port.in;

import com.alonsomoros.tfg.application.command.RegisterPaymentMethodCommand;
import com.alonsomoros.tfg.infrastructure.web.dto.response.PaymentMandateResponseDto;

public interface IPaymentMethodService {
    PaymentMandateResponseDto registerPaymentMandate(RegisterPaymentMethodCommand request);
}
