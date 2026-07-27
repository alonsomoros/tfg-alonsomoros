package com.alonsomoros.tfg.domain.model;

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
    private Long id;
    private Long subscriptionId;
    private String provider;
    private String token;
    private String last4;
    private boolean active;
}
