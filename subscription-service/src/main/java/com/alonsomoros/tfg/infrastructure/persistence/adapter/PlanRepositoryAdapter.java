package com.alonsomoros.tfg.infrastructure.persistence.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.application.exception.PlanNotFoundException;
import com.alonsomoros.tfg.domain.model.Plan;
import com.alonsomoros.tfg.domain.port.PlanRepositoryPort;
import com.alonsomoros.tfg.infrastructure.persistence.mapper.PlanMapper;
import com.alonsomoros.tfg.infrastructure.persistence.repository.PlanRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanRepositoryAdapter implements PlanRepositoryPort {

    private final PlanRepository planRepository;
    private final PlanMapper planEntityMapper;

    @Override
    public List<Plan> findAllActivePlans() {
        log.info("Finding all active plans");
        return planRepository.findByIsActiveTrue().stream()
                .map(planEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Plan findByCode(String code) {
        log.info("Finding plan by code | {}", code);
        return planRepository.findByCode(code).stream()
                .findFirst()
                .map(planEntityMapper::toDomain)
                .orElseThrow(() -> new PlanNotFoundException("Plan not found for code | " + code));
    }

    @Override
    public Plan save(Plan plan) {
        log.info("Saving plan in BBDD | {}", plan);
        return planEntityMapper.toDomain(planRepository.save(planEntityMapper.toEntity(plan)));
    }
}
