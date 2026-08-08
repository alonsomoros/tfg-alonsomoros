package com.alonsomoros.tfg.application.port.in;

import java.math.BigDecimal;

public interface IProcessChargeService {
    void executeCharge(Long mandateId, BigDecimal amount);
}
