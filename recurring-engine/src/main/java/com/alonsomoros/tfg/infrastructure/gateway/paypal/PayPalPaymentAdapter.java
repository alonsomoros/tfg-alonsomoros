package com.alonsomoros.tfg.infrastructure.gateway.paypal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.domain.port.out.PaymentGatewayPort;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

@Component
@Slf4j
public class PayPalPaymentAdapter implements PaymentGatewayPort {

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.simulation.mode:true}")
    private boolean simulationMode;

    private final String API_BASE_URL = "https://api-m.sandbox.paypal.com";
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public String getProviderName() {
        return "PAYPAL";
    }

    @Override
    public void charge(String vaultToken, BigDecimal amount) {
        log.info("Starting [PayPalCharge] | provider: PAYPAL, tokenSuffix: {}, amount: {}",
                vaultToken.length() > 6 ? vaultToken.substring(vaultToken.length() - 6) : vaultToken,
                amount);

        if (simulationMode) {
            log.warn("Simulation mode enabled; skipping live PayPal reference transaction call.");
            try {
                Thread.sleep(800);
                log.info("Processed [PayPalCharge] in simulation mode successfully | amount: {} EUR", amount);
                return;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Simulated Error with Paypal", e);
            }
        }

        // --- MODO PRODUCCIÓN REAL ---
        try {
            String accessToken = getAccessToken();

            String jsonBody = """
                    {
                      "intent": "CAPTURE",
                      "purchase_units": [
                        {
                          "amount": {
                            "currency_code": "EUR",
                            "value": "%s"
                          }
                        }
                      ],
                      "payment_source": {
                        "token": {
                          "id": "%s",
                          "type": "BILLING_AGREEMENT"
                        }
                      }
                    }
                    """.formatted(amount.toString(), vaultToken);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + "/v2/checkout/orders"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + accessToken)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                log.info("Processed [PayPalCharge] successfully | httpStatus: {}", response.statusCode());
            } else {
                log.error("PayPal charge failed | httpStatus: {}, responseBody: {}", response.statusCode(),
                        response.body());
                throw new RuntimeException("Error in off-session payment for PayPal: " + response.body());
            }

        } catch (Exception e) {
            log.error("PayPal adapter communication failure", e);
            throw new RuntimeException("Failure in PayPal adapter", e);
        }
    }

    private String getAccessToken() throws Exception {
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/v1/oauth2/token"))
                .header("Authorization", "Basic " + encodedAuth)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body().split("\"access_token\":\"")[1].split("\"")[0];
    }
}