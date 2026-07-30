package com.alonsomoros.tfg.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alonsomoros.tfg.infrastructure.persistence.entity.PlanEntity;

@Repository
public interface PlanRepository extends JpaRepository<PlanEntity, UUID> {

    List<PlanEntity> findByIsActiveTrue();

    List<PlanEntity> findByCode(String code);

}
