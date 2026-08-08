package com.alonsomoros.tfg.domain.port.out;

import java.math.BigDecimal;

public interface PaymentGatewayPort {
    void charge(String token, BigDecimal amount);
    
    String getProviderName();
}
