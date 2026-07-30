package com.alonsomoros.tfg.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.domain.model.Plan;
import com.alonsomoros.tfg.infrastructure.persistence.entity.PlanEntity;

@Component
public class PlanMapper {

    public Plan toDomain(PlanEntity entity) {
        if (entity == null) return null;
        
        return Plan.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .billingInterval(entity.getBillingInterval())
                .isActive(entity.getIsActive())
                .build();
    }

    public PlanEntity toEntity(Plan domain) {
        if (domain == null) return null;
        
        return PlanEntity.builder()
                .id(domain.getId())
                .code(domain.getCode())
                .name(domain.getName())
                .description(domain.getDescription())
                .amount(domain.getAmount())
                .currency(domain.getCurrency())
                .billingInterval(domain.getBillingInterval())
                .isActive(domain.getIsActive())
                .build();
    }

}
