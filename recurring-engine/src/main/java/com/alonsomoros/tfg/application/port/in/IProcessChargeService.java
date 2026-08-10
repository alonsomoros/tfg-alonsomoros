package com.alonsomoros.tfg.application.port.in;

import java.math.BigDecimal;
import java.util.UUID;

public interface IProcessChargeService {
    void executeCharge(UUID mandateId, BigDecimal amount);
}
