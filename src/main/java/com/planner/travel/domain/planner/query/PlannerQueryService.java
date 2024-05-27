package com.planner.travel.domain.planner.query;

import com.planner.travel.domain.group.entity.QGroupMember;
import com.planner.travel.domain.planner.dto.response.PlannerListResponse;
import com.planner.travel.domain.planner.dto.response.PlannerResponse;
import com.planner.travel.domain.planner.entity.Planner;
import com.planner.travel.domain.planner.entity.QPlanBox;
import com.planner.travel.domain.planner.entity.QPlanner;
import com.planner.travel.domain.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlannerQueryService {
    private final JPAQueryFactory queryFactory;
    private final PlanBoxQueryService planBoxQueryService;

    @Autowired
    public PlannerQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.planBoxQueryService = new PlanBoxQueryService(entityManager);
    }

    public List<PlannerListResponse> findMyPlannersByUserId(Long userId) {
        QUser qUser = QUser.user;
        QGroupMember qGroupMember = QGroupMember.groupMember;
        QPlanner qPlanner = QPlanner.planner;

        List<Planner> planners = queryFactory
                .select(qPlanner)
                .from(qGroupMember)
                .join(qGroupMember.user, qUser)
                .join(qGroupMember.planner, qPlanner)
                .where(qGroupMember.user.id.eq(userId)
                        .and(qGroupMember.isLeaved.isFalse())
                )
                .orderBy(qPlanner.id.desc())
                .fetch();

        return planners.stream()
                .map(planner -> new PlannerListResponse(
                        planner.getId(),
                        planner.getTitle(),
                        planner.getStartDate(),
                        planner.getEndDate(),
                        planner.isPrivate()
                ))
                .collect(Collectors.toList());
    }

    public List<PlannerListResponse> findPlannersByUserId(Long userId) {
        QUser qUser = QUser.user;
        QGroupMember qGroupMember = QGroupMember.groupMember;
        QPlanner qPlanner = QPlanner.planner;

        List<Planner> planners = queryFactory
                .select(qPlanner)
                .from(qGroupMember)
                .join(qGroupMember.user, qUser)
                .join(qGroupMember.planner, qPlanner)
                .where(qGroupMember.user.id.eq(userId)
                        .and(qPlanner.isPrivate.isFalse())
                        .and(qGroupMember.isLeaved.isFalse())
                )
                .orderBy(qPlanner.id.desc())
                .fetch();

        return planners.stream()
                .map(planner -> new PlannerListResponse(
                        planner.getId(),
                        planner.getTitle(),
                        planner.getStartDate(),
                        planner.getEndDate(),
                        planner.isPrivate()
                ))
                .collect(Collectors.toList());
    }

    public PlannerResponse findPlannerById(Long plannerId) {
        QPlanner qPlanner = QPlanner.planner;

        Planner planner = queryFactory
                .selectFrom(qPlanner)
                .where(qPlanner.id.eq(plannerId)
                        .and(qPlanner.isDeleted.isFalse())
                )
                .fetchOne();

        return new PlannerResponse(
                planner.getId(),
                planner.getTitle(),
                planner.getStartDate(),
                planner.getEndDate(),
                planner.isPrivate(),
                planBoxQueryService.findPlanBoxesByPlannerId(planner.getId())
        );
    }

    public List<LocalDate> updateStartDateAndEndDate(Long plannerId) {
        QPlanner qPlanner = QPlanner.planner;
        QPlanBox qPlanBox = QPlanBox.planBox;

        LocalDate minDate = queryFactory
                .select(qPlanBox.planDate.min())
                .from(qPlanBox)
                .where(qPlanBox.planner.id.eq(plannerId))
                .fetchOne();

        LocalDate maxDate = queryFactory
                .select(qPlanBox.planDate.max())
                .from(qPlanBox)
                .where(qPlanBox.planner.id.eq(plannerId))
                .fetchOne();

        return List.of(minDate, maxDate);
    }
}
