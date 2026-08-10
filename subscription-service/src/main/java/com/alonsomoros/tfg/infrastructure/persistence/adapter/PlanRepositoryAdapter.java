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
        log.debug("Loading active [Plan] list from DB.");
        return planRepository.findByIsActiveTrue().stream()
                .map(planEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Plan findByCode(String code) {
        log.debug("Loading [Plan] from DB | planCode: {}", code);
        return planRepository.findByCode(code).stream()
                .findFirst()
                .map(planEntityMapper::toDomain)
                .orElseThrow(() -> new PlanNotFoundException("Plan not found for code | " + code));
    }

    @Override
    public Plan save(Plan plan) {
        log.debug("Persisting [Plan] in DB | planCode: {}", plan.getCode());
        return planEntityMapper.toDomain(planRepository.save(planEntityMapper.toEntity(plan)));
    }
}
