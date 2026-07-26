package com.alonsomoros.tfg.domain.service;

import com.alonsomoros.tfg.infrastructure.web.dto.request.PaymentMethodRequestDto;

public interface IRecurringEngineService {
    void processPaymentToken(PaymentMethodRequestDto request);
}
