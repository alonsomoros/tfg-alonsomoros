package com.alonsomoros.tfg.infrastructure.gateway.stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.stripe.Stripe;
import com.stripe.model.SetupIntent;
import com.stripe.param.SetupIntentCreateParams;

@Component
public class StripePaymentAdapter {

    public StripePaymentAdapter(@Value("${stripe.api.key}") String apiKey) {
        Stripe.apiKey = apiKey;
    }

    public String createSetupIntent() {
        try {
            SetupIntentCreateParams params = SetupIntentCreateParams.builder()
                // off_session para pagos recurrentes sin interacción del usuario
                .setUsage(SetupIntentCreateParams.Usage.OFF_SESSION) 
                .addPaymentMethodType("card")
                .build();
                
            SetupIntent setupIntent = SetupIntent.create(params);
            
            return setupIntent.getClientSecret();
        } catch (Exception e) {
            throw new RuntimeException("Error while initializing Stripe setup intent", e);
        }
    }
}
