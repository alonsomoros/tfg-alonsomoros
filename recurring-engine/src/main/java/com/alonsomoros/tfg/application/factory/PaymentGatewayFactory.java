package com.alonsomoros.tfg.application.factory;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.application.exception.UnsupportedPaymentProviderException;
import com.alonsomoros.tfg.domain.port.out.PaymentGatewayPort;

import java.util.List;

@Component
public class PaymentGatewayFactory {

    private final List<PaymentGatewayPort> gateways;

    public PaymentGatewayFactory(List<PaymentGatewayPort> gatewayList) {
        this.gateways = List.copyOf(gatewayList);
    }

    public PaymentGatewayPort getGateway(String providerName) {
        return gateways.stream()
            .filter(gateway -> gateway.getProviderName().equalsIgnoreCase(providerName))
            .findFirst()
            .orElseThrow(() -> new UnsupportedPaymentProviderException(providerName));
    }
}
