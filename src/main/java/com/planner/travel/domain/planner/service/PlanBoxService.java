package com.planner.travel.domain.planner.service;

import com.planner.travel.domain.planner.dto.request.PlanBoxCreateRequest;
import com.planner.travel.domain.planner.dto.request.PlanBoxUpdateRequest;
import com.planner.travel.domain.planner.dto.response.PlanBoxResponse;
import com.planner.travel.domain.planner.entity.PlanBox;
import com.planner.travel.domain.planner.entity.Planner;
import com.planner.travel.domain.planner.query.PlanBoxQueryService;
import com.planner.travel.domain.planner.query.PlannerQueryService;
import com.planner.travel.domain.planner.repository.PlanBoxRepository;
import com.planner.travel.domain.planner.repository.PlannerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanBoxService {
    private final PlannerRepository plannerRepository;
    private final PlanBoxRepository planBoxRepository;

    private final PlannerQueryService plannerQueryService;
    private final PlanBoxQueryService planBoxQueryService;
    private final PlannerListService plannerListService;

    @Transactional(readOnly = true)
    public List<PlanBoxResponse> getAllPlanBox(Long plannerId) {
        List<PlanBoxResponse> planBoxResponses = planBoxQueryService.findPlanBoxesByPlannerId(plannerId);

        return planBoxResponses;
    }

    @Transactional
    public void create(PlanBoxCreateRequest request, Long plannerId) {
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new EntityNotFoundException("Planner not found"));

        PlanBox planBox = PlanBox.builder()
                .planDate(request.planDate())
                .planner(planner)
                .build();

        planBoxRepository.save(planBox);

        List<String> localDates = plannerQueryService.updateStartDateAndEndDate(plannerId);
        planner.updateStartDate(localDates.get(0));
        planner.updateStartDate(localDates.get(1));
    }

    @Transactional
    public List<PlanBoxResponse> update(PlanBoxUpdateRequest request, Long plannerId, Long planBoxId) {
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new EntityNotFoundException("Planner not found"));

        PlanBox planBox = planBoxRepository.findById(planBoxId)
                .orElseThrow(() -> new EntityNotFoundException("PlanBox not found for id: " + planBoxId));

        if (request.planDate() != null) {
            planBox.updatePlanDate(request.planDate());
        }

        planBoxRepository.save(planBox);

        List<String> localDates = plannerQueryService.updateStartDateAndEndDate(plannerId);
        planner.updateStartDate(localDates.get(0));
        planner.updateStartDate(localDates.get(1));

        return planBoxQueryService.findPlanBoxesByPlannerId(planBoxId);

    }

    @Transactional
    public List<PlanBoxResponse> delete(Long planBoxId) {
        PlanBox planBox = planBoxRepository.findById(planBoxId)
                .orElseThrow(() -> new EntityNotFoundException("PlanBox not found for id: " + planBoxId));

        planBox.deleted(true);
        planBoxRepository.save(planBox);

        return planBoxQueryService.findPlanBoxesByPlannerId(planBoxId);
    }
}
