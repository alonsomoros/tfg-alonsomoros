package com.alonsomoros.tfg.application.port.in;

import java.math.BigDecimal;
import java.util.UUID;

import com.alonsomoros.tfg.infrastructure.web.dto.response.ChargeMandateResponseDto;

public interface IProcessChargeService {
    ChargeMandateResponseDto executeCharge(UUID mandateId, BigDecimal amount);
}
