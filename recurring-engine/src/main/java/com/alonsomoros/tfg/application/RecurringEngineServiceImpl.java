package com.alonsomoros.tfg.application;

import org.springframework.stereotype.Service;

import com.alonsomoros.tfg.domain.service.IRecurringEngineService;
import com.alonsomoros.tfg.infrastructure.web.dto.request.PaymentMethodRequestDto;

@Service
public class RecurringEngineServiceImpl implements IRecurringEngineService {

    @Override
    public void processPaymentToken(PaymentMethodRequestDto request) {
        
    }
    
}
