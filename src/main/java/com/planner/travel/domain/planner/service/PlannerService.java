package com.planner.travel.domain.planner.service;

import com.planner.travel.domain.planner.dto.request.PlannerCreateRequest;
import com.planner.travel.domain.planner.dto.request.PlannerUpdateRequest;
import com.planner.travel.domain.planner.dto.response.PlannerListResponse;
import com.planner.travel.domain.planner.dto.response.PlannerResponse;
import com.planner.travel.domain.planner.entity.Planner;
import com.planner.travel.domain.planner.query.PlannerQueryService;
import com.planner.travel.domain.planner.repository.PlannerRepository;
import com.planner.travel.domain.user.entity.User;
import com.planner.travel.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlannerService {
    private final UserRepository userRepository;
    private final PlannerRepository plannerRepository;
    private final PlannerQueryService plannerQueryService;

    public List<PlannerListResponse> getAllPlanners(Long userId) {
        List<PlannerListResponse> plannerListResponses = plannerQueryService.findPlannersByUserId(userId);
        return plannerListResponses;
    }

    @Transactional
    public PlannerResponse create(PlannerCreateRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Planner planner = Planner.builder()
                .title(request.title())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .isPrivate(request.isPrivate())
                .user(user)
                .build();

        plannerRepository.save(planner);

        PlannerResponse plannerResponse = plannerQueryService.findPlannerById(planner.getId());

        return plannerResponse;
    }

    @Transactional
    public PlannerResponse update(PlannerUpdateRequest request, Long plannerId) {
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new EntityNotFoundException("Planner not found for id: " + plannerId));

        if (isValid(request.title())) {
            planner.updateTitle(request.title());
            planner.updateStartDate(request.startDate());
            planner.updateEndDate(request.endDate());
            planner.updatePrivate(request.isPrivate());
        }

        PlannerResponse plannerResponse = plannerQueryService.findPlannerById(plannerId);

        return plannerResponse;
    }

    @Transactional
    public PlannerResponse delete(Long plannerId) {
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new EntityNotFoundException("Planner not found for id: " + plannerId));

        planner.updatePrivate(true);
        plannerRepository.save(planner);

        PlannerResponse plannerResponse = plannerQueryService.findPlannerById(plannerId);

        return plannerResponse;
    }

    public PlannerResponse getPlannerById(Long plannerId) {
        return plannerQueryService.findPlannerById(plannerId);
    }

    private boolean isValid(String value) {
        return value != null && !value.isEmpty();
    }
}
