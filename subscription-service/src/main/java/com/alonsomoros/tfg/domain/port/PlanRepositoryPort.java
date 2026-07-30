package com.alonsomoros.tfg.domain.port;

import com.alonsomoros.tfg.domain.model.Plan;
import java.util.List;

public interface PlanRepositoryPort {

    List<Plan> findAllActivePlans();

    Plan findByCode(String code);

    Plan save(Plan plan);

}