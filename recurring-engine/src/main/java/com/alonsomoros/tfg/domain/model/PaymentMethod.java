package com.alonsomoros.tfg.domain.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethod {
    private UUID id;
    private UUID subscriptionId;
    private String provider;
    private String token;
    private String last4;
    private boolean active;
}
