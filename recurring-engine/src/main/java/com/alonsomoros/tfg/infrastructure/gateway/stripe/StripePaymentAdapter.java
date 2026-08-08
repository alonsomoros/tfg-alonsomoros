package com.alonsomoros.tfg.infrastructure.gateway.stripe;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
public class StripePaymentAdapter {

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
            throw new RuntimeException("Error while initializing Stripe setup intent", e);
        }
    }

    public void charge(String paymentMethodToken, BigDecimal amount) {
        try {
            // A. Rescatamos la tarjeta de Stripe usando el Token (pm_...)
            PaymentMethod pm = PaymentMethod.retrieve(paymentMethodToken);

            // B. Si la tarjeta NO tiene un cliente asociado (lo que ocurre con tu código
            // actual)
            String customerId = pm.getCustomer();
            if (customerId == null) {
                log.info("Payment Method without Customer, creating a new Customer in Stripe...");
                Customer customer = Customer.create(CustomerCreateParams.builder()
                        .setDescription("TFG Customer " + System.currentTimeMillis())
                        .build());

                customerId = customer.getId();

                // Vinculamos la tarjeta a este nuevo cliente
                pm.attach(PaymentMethodAttachParams.builder().setCustomer(customerId).build());
                log.info("Payment Method linked successfully to Customer: {}", customerId);
            }

            // C. Convertimos a céntimos (Stripe no usa decimales. 9.99€ = 999)
            long amountInCents = amount.multiply(new BigDecimal("100")).longValue();

            // D. Preparamos el hachazo final (El cobro Off-Session)
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency("eur")
                    .setPaymentMethod(paymentMethodToken)
                    .setCustomer(customerId) // <-- ¡Ahora sí lo tenemos!
                    .setConfirm(true) // Cóbralo YA
                    .setOffSession(true) // Sin el usuario delante (salta el 3DSecure)
                    .build();

            // E. Ejecutamos el cobro
            PaymentIntent paymentIntent = PaymentIntent.create(params);

            if (!"succeeded".equals(paymentIntent.getStatus())) {
                log.error("Error while charging in Stripe: {}", paymentIntent.getStatus());
                throw new RuntimeException("Stripe charge failed with status: " + paymentIntent.getStatus());
            }

            log.info("¡Charged {} to Customer {} cents in Stripe!", amountInCents, customerId);

        } catch (Exception e) {
            log.error("Critical error while charging in Stripe: {}", e.getMessage());
            throw new RuntimeException("Error executing recurring charge in Stripe", e);
        }
    }
}
