package com.alonsomoros.tfg.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alonsomoros.tfg.domain.model.BillingInterval;
import com.alonsomoros.tfg.domain.model.CurrencyCode;
import com.alonsomoros.tfg.domain.model.Plan;
import com.alonsomoros.tfg.domain.port.PlanRepositoryPort;
import com.alonsomoros.tfg.infrastructure.web.dto.response.PlanResponseDto;
import com.alonsomoros.tfg.infrastructure.web.mapper.WebPlanMapper;

@ExtendWith(MockitoExtension.class)
class PlanServiceImplTest {

    @Mock
    private PlanRepositoryPort planRepository;
    @Mock
    private WebPlanMapper webPlanMapper;

    @InjectMocks
    private PlanServiceImpl planService;

    @Test
    void getPlans_returnsMappedActivePlans() {
        Plan plan = Plan.builder()
                .id(UUID.randomUUID())
                .code("BASIC_MONTHLY")
                .name("Básico")
                .description("Essential")
                .amount(new BigDecimal("9.99"))
                .currency(CurrencyCode.EUR)
                .billingInterval(BillingInterval.MONTHLY)
                .isActive(true)
                .build();
        List<Plan> plans = List.of(plan);
        List<PlanResponseDto> expected = List.of(PlanResponseDto.from(plan));

        when(planRepository.findAllActivePlans()).thenReturn(plans);
        when(webPlanMapper.toResponseDto(plans)).thenReturn(expected);

        List<PlanResponseDto> result = planService.getPlans();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().code()).isEqualTo("BASIC_MONTHLY");
        verify(planRepository).findAllActivePlans();
        verify(webPlanMapper).toResponseDto(plans);
    }
}
