package com.alonsomoros.tfg.application.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alonsomoros.tfg.application.exception.UnsupportedPaymentProviderException;
import com.alonsomoros.tfg.domain.port.out.PaymentGatewayPort;

@ExtendWith(MockitoExtension.class)
class PaymentGatewayFactoryTest {

    @Mock
    private PaymentGatewayPort stripeGateway;
    @Mock
    private PaymentGatewayPort paypalGateway;

    @Test
    void getGateway_returnsMatchingProviderIgnoringCase() {
        when(stripeGateway.getProviderName()).thenReturn("STRIPE");
        when(paypalGateway.getProviderName()).thenReturn("PAYPAL");
        PaymentGatewayFactory factory = new PaymentGatewayFactory(List.of(stripeGateway, paypalGateway));

        assertThat(factory.getGateway("stripe")).isSameAs(stripeGateway);
        assertThat(factory.getGateway("PAYPAL")).isSameAs(paypalGateway);
    }

    @Test
    void getGateway_whenUnknown_throws() {
        when(stripeGateway.getProviderName()).thenReturn("STRIPE");
        PaymentGatewayFactory factory = new PaymentGatewayFactory(List.of(stripeGateway));

        assertThatThrownBy(() -> factory.getGateway("bitcoin"))
                .isInstanceOf(UnsupportedPaymentProviderException.class);
    }

    @Test
    void gateways_canChargeThroughResolvedProvider() {
        when(stripeGateway.getProviderName()).thenReturn("STRIPE");
        PaymentGatewayFactory factory = new PaymentGatewayFactory(List.of(stripeGateway));

        factory.getGateway("STRIPE").charge("tok", BigDecimal.TEN);

        org.mockito.Mockito.verify(stripeGateway).charge("tok", BigDecimal.TEN);
    }
}
