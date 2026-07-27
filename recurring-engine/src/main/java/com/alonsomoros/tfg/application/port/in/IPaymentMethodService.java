package com.alonsomoros.tfg.application.port.in;

import com.alonsomoros.tfg.application.command.RegisterPaymentMethodCommand;

public interface IPaymentMethodService {
    void registerPaymentMandate(RegisterPaymentMethodCommand request);
}
