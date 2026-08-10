package com.alonsomoros.tfg.application.factory;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.application.exception.UnsupportedPaymentProviderException;
import com.alonsomoros.tfg.domain.port.out.PaymentGatewayPort;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PaymentGatewayFactory {

    private final Map<String, PaymentGatewayPort> gateways;

    public PaymentGatewayFactory(List<PaymentGatewayPort> gatewayList) {
        this.gateways = gatewayList.stream()
            .collect(Collectors.toMap(
                PaymentGatewayPort::getProviderName, 
                Function.identity()
            ));
    }

    public PaymentGatewayPort getGateway(String providerName) {
        PaymentGatewayPort gateway = gateways.get(providerName.toUpperCase());
        if (gateway == null) {
            throw new UnsupportedPaymentProviderException(providerName);
        }
        return gateway;
    }
}
