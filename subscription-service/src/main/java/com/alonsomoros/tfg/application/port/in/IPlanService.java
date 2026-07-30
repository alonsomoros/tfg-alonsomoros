package com.alonsomoros.tfg.application.port.in;

import java.util.List;

import com.alonsomoros.tfg.infrastructure.web.dto.response.PlanResponseDto;

public interface IPlanService {
	List<PlanResponseDto> getPlans();
}
