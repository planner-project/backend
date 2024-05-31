package com.planner.travel.domain.planner.service;


import com.planner.travel.domain.planner.dto.request.PlanCreateRequest;
import com.planner.travel.domain.planner.dto.request.PlanUpdateRequest;
import com.planner.travel.domain.planner.dto.response.PlanResponse;
import com.planner.travel.domain.planner.entity.Plan;
import com.planner.travel.domain.planner.entity.PlanBox;
import com.planner.travel.domain.planner.entity.Planner;
import com.planner.travel.domain.planner.query.PlanQueryService;
import com.planner.travel.domain.planner.repository.PlanBoxRepository;
import com.planner.travel.domain.planner.repository.PlanRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanBoxRepository planBoxRepository;
    private final PlanRepository planRepository;
    private final PlanQueryService planQueryService;

    @Transactional(readOnly = true)
    public List<PlanResponse> getAllPlan(Long plannerId) {

        return planQueryService.findPlanByPlanBoxId(plannerId);
    }


    @Transactional(readOnly = true)
    public void create(PlanCreateRequest request, Long planBoxId, Long plannerId) {
        PlanBox planBox = planBoxRepository.findById(plannerId)
                .orElseThrow(() -> new EntityNotFoundException("PlanBox not found"));

        Plan plan = Plan.builder()
                .isPrivate(request.isPrivate())
                .title(request.title())
                .time(request.time())
                .content(request.content())
                .address(request.address())
                .isDeleted(false)
                .build();

        planRepository.save(plan);
    }
    @Transactional(readOnly = true)
    public void update(PlanUpdateRequest request, Long planId) {
        // Plan 엔티티 조회
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("Plan not found for id: " + planId));

        // Plan 정보 업데이트
        plan.setPrivate(request.isPrivate());
        plan.setTitle(request.title());
        plan.setTime(request.time());
        plan.setContent(request.content());
        plan.setAddress(request.address());

        // Plan 엔티티 저장
        planRepository.save(plan);
    }
    @Transactional(readOnly = true)
    public void delete(Long planId) {
        // Plan 엔티티 조회
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("Plan not found for id: " + planId));

        plan.setDeleted(true);

        // Plan 엔티티 저장
        planRepository.save(plan);
    }


}
