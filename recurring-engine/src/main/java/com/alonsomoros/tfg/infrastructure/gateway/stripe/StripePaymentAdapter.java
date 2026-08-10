package com.alonsomoros.tfg.infrastructure.gateway.stripe;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.application.exception.PaymentGatewayException;
import com.alonsomoros.tfg.domain.port.out.PaymentGatewayPort;
import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.SetupIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodAttachParams;
import com.stripe.param.SetupIntentCreateParams;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StripePaymentAdapter implements PaymentGatewayPort {

    public StripePaymentAdapter(@Value("${stripe.api.key}") String apiKey) {
        Stripe.apiKey = apiKey;
    }

    public String createSetupIntent(String customerEmail) {
        try {
            CustomerCreateParams customerParams = CustomerCreateParams.builder()
                    .setEmail(customerEmail)
                    .setDescription("TFG Customer")
                    .build();
            Customer customer = Customer.create(customerParams);

            SetupIntentCreateParams params = SetupIntentCreateParams.builder()
                    // off_session para pagos recurrentes sin interacción del usuario
                    .setCustomer(customer.getId())
                    .setUsage(SetupIntentCreateParams.Usage.OFF_SESSION)
                    .addPaymentMethodType("card")
                    .build();

            SetupIntent setupIntent = SetupIntent.create(params);

            return setupIntent.getClientSecret();
        } catch (Exception e) {
            throw new PaymentGatewayException("Error while initializing Stripe setup intent", e);
        }
    }

    @Override
    public void charge(String paymentMethodToken, BigDecimal amount) {
        try {
            PaymentMethod pm = PaymentMethod.retrieve(paymentMethodToken);

            String customerId = pm.getCustomer();
            if (customerId == null) {
                log.debug("PaymentMethod has no linked customer in Stripe; creating customer for off-session billing.");
                Customer customer = Customer.create(CustomerCreateParams.builder()
                        .setDescription("TFG Customer " + System.currentTimeMillis())
                        .build());

                customerId = customer.getId();

                pm.attach(PaymentMethodAttachParams.builder().setCustomer(customerId).build());
                log.info("Linked [PaymentMethod] to Stripe customer successfully | customerId: {}", customerId);
            }

            // Stripe no usa decimales -> Céntimos
            long amountInCents = amount.multiply(new BigDecimal("100")).longValue();

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency("eur")
                    .setPaymentMethod(paymentMethodToken)
                    .setCustomer(customerId)
                    .setConfirm(true)
                    .setOffSession(true)
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            if (!"succeeded".equals(paymentIntent.getStatus())) {
                log.warn("Stripe charge returned non-success status | status: {}, customerId: {}, amountInCents: {}",
                        paymentIntent.getStatus(), customerId, amountInCents);
                throw new PaymentGatewayException("Stripe charge failed with status: " + paymentIntent.getStatus());
            }

            log.info("Processed [StripeCharge] successfully | customerId: {}, amountInCents: {}", customerId, amountInCents);

        } catch (Exception e) {
            log.error("Stripe adapter failed to execute recurring charge", e);
            throw new PaymentGatewayException("Error executing recurring charge in Stripe", e);
        }
    }

    @Override
    public String getProviderName() {
        return "STRIPE";
    }
}
