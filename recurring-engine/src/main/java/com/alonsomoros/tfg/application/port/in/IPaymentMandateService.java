package com.alonsomoros.tfg.application.port.in;

import com.alonsomoros.tfg.application.command.RegisterMandateCommand;

public interface IPaymentMandateService {
    void registerPaymentMandate(RegisterMandateCommand request);
}
