package com.planner.travel.domain.planner.repository;

import com.planner.travel.domain.planner.entity.Planner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlannerRepository extends JpaRepository<Planner, Long> {
}
