package com.planner.travel.domain.planner.query;

import com.planner.travel.domain.planner.dto.response.PlannerResponse;
import com.planner.travel.domain.planner.entity.Planner;
import com.planner.travel.domain.planner.entity.QPlanner;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlannerQueryService {
    private final JPAQueryFactory queryFactory;
    private final PlanBoxQueryService planBoxQueryService;

    @Autowired
    public PlannerQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.planBoxQueryService = new PlanBoxQueryService(entityManager);
    }

    public PlannerResponse findPlannerById(Long plannerId) {
        QPlanner qPlanner = QPlanner.planner;

        Planner planner = queryFactory
                .selectFrom(qPlanner)
                .where(qPlanner.id.eq(plannerId)
                        .and(qPlanner.isDeleted.isFalse())
                )
                .fetchOne();

        PlannerResponse plannerResponse = new PlannerResponse(
                planner.getId(),
                planner.getTitle(),
                planner.getStartDate(),
                planner.getEndDate(),
                planner.isPrivate(),
                planBoxQueryService.findPlanBoxesByPlannerId(planner.getId())
        );

        return plannerResponse;
    }
}
