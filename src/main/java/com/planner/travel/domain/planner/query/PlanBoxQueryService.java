package com.planner.travel.domain.planner.query;

import com.planner.travel.domain.planner.dto.response.PlanBoxResponse;
import com.planner.travel.domain.planner.entity.PlanBox;
import com.planner.travel.domain.planner.entity.QPlanBox;
import com.planner.travel.domain.planner.entity.QPlanner;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanBoxQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public PlanBoxQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<PlanBoxResponse> findPlanBoxesByPlannerId(Long plannerId) {
        QPlanner qPlanner = QPlanner.planner;
        QPlanBox qPlanBox = QPlanBox.planBox;

        /*
        SELECT *
        FROM plan_box
        JOIN planner
        ON planner.id = plan_box.planner_id
        WHERE planner.id = ? <- 찾고자 하는 planBox 가 있는 Planner 의 index
            AND plan_box.isDeleted = 1
        ORDER BY plan_box.planDate
         */
        List<PlanBox> planBoxes = queryFactory
                .selectFrom(qPlanBox)
                .join(qPlanBox.planner, qPlanner)
                .where(qPlanner.id.eq(plannerId)
                        .and(qPlanBox.isDeleted.isFalse())
                )
                .orderBy(qPlanBox.planDate.asc())
                .fetch();

        List<PlanBoxResponse> planBoxResponses = planBoxes.stream()
                .map(planBox -> new PlanBoxResponse(
                        planBox.getPlanDate(),
                        planBox.isPrivate()
                ))
                .collect(Collectors.toList());

        return planBoxResponses;
    }
}
