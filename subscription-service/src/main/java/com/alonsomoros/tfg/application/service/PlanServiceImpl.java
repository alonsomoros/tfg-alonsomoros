package com.alonsomoros.tfg.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alonsomoros.tfg.application.port.in.IPlanService;
import com.alonsomoros.tfg.domain.model.Plan;
import com.alonsomoros.tfg.domain.port.PlanRepositoryPort;
import com.alonsomoros.tfg.infrastructure.web.dto.response.PlanResponseDto;
import com.alonsomoros.tfg.infrastructure.web.mapper.WebPlanMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements IPlanService {

    private final PlanRepositoryPort planRepository;
    private final WebPlanMapper webPlanMapper;

    @Override
    public List<PlanResponseDto> getPlans() {
        log.info("Retrieving active [Plan] list from DB.");
        List<Plan> plans = planRepository.findAllActivePlans();
        log.info("Retrieved active [Plan] list successfully | count: {}", plans.size());
        return webPlanMapper.toResponseDto(plans);
    }
    
}
