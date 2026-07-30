package com.alonsomoros.tfg.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Plan {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private BigDecimal amount;
    private CurrencyCode currency;
    private BillingInterval billingInterval;
    private Boolean isActive;
    

    public boolean isYearly() {
        return BillingInterval.YEARLY == this.billingInterval;
    }

    public boolean isMonthly() {
        return BillingInterval.MONTHLY == this.billingInterval;
    }

    public boolean isWeekly() {
        return BillingInterval.WEEKLY == this.billingInterval;
    }

}