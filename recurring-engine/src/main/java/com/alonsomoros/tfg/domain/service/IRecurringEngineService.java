package com.alonsomoros.tfg.domain.service;

import com.alonsomoros.tfg.application.command.RegisterMandateCommand;

public interface IRecurringEngineService {
    void registerPaymentMandate(RegisterMandateCommand request);
}
