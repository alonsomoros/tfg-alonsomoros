package com.alonsomoros.tfg.infrastructure.web.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alonsomoros.tfg.domain.model.Plan;
import com.alonsomoros.tfg.infrastructure.web.dto.response.PlanResponseDto;

@Component
public class WebPlanMapper {

    public PlanResponseDto toResponseDto(Plan plan) {
        return PlanResponseDto.from(plan);
    }

    public List<PlanResponseDto> toResponseDto(List<Plan> plans) {
        return plans.stream()
                .map(this::toResponseDto)
                .toList();
    }
}