package com.alonsomoros.tfg.domain.model;

import java.time.LocalDate;

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
public class Subscription {

    private Long id;

    private String customerEmail;

    private String planId;

    private SubscriptionStatusEnum status;

    private LocalDate nextPaymentDate;

}
