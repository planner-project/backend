package com.planner.travel.domain.planner.service;

import com.planner.travel.domain.planner.dto.request.PlannerCreateRequest;
import com.planner.travel.domain.planner.dto.request.PlannerUpdateRequest;
import com.planner.travel.domain.planner.dto.response.PlannerListResponse;
import com.planner.travel.domain.planner.entity.Planner;
import com.planner.travel.domain.planner.query.PlannerQueryService;
import com.planner.travel.domain.planner.repository.PlannerRepository;
import com.planner.travel.domain.user.entity.User;
import com.planner.travel.domain.user.repository.UserRepository;
import com.planner.travel.global.jwt.token.SubjectExtractor;
import com.planner.travel.global.jwt.token.TokenExtractor;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PlannerListService {
    private final UserRepository userRepository;
    private final PlannerRepository plannerRepository;
    private final PlannerQueryService plannerQueryService;
    private final TokenExtractor tokenExtractor;
    private final SubjectExtractor subjectExtractor;


    @Transactional(readOnly = true)
    public List<PlannerListResponse> getAllPlanners(Long userId, HttpServletRequest request) {
        String accessToken = tokenExtractor.getAccessTokenFromHeader(request);
        Long subject = subjectExtractor.getUserIdFromToken(accessToken);
        List<PlannerListResponse> plannerListResponses = new ArrayList<>();

        if (subject.equals(userId)) {
            plannerListResponses = plannerQueryService.findMyPlannersByUserId(userId);

        } else {
            plannerListResponses = plannerQueryService.findPlannersByUserId(userId);
        }

        return plannerListResponses;
    }

    @Transactional
    public void create(PlannerCreateRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Planner planner = Planner.builder()
                .title(request.title())
                .startDate(null)
                .endDate(null)
                .isPrivate(request.isPrivate())
                .user(user)
                .build();

        plannerRepository.save(planner);
    }

    @Transactional
    public void update(PlannerUpdateRequest request, Long plannerId) {
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new EntityNotFoundException("Planner not found for id: " + plannerId));

        if (isValid(request.title())) {
            planner.updateTitle(request.title());
            planner.updateIsPrivate(request.isPrivate());
        }

        planner.updateIsPrivate(request.isPrivate());

        plannerRepository.save(planner);
    }

    @Transactional
    public void delete(Long plannerId) {
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new EntityNotFoundException("Planner not found for id: " + plannerId));

        planner.updateIsDeleted(true);
        plannerRepository.save(planner);
    }

    private boolean isValid(String value) {
        return value != null && !value.isEmpty();
    }
}