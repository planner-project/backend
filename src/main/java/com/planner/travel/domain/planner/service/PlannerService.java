package com.planner.travel.domain.planner.service;

import com.planner.travel.domain.planner.dto.request.PlannerCreateRequest;
import com.planner.travel.domain.planner.dto.request.PlannerUpdateRequest;
import com.planner.travel.domain.planner.entity.Planner;
import com.planner.travel.domain.planner.repository.PlannerRepository;
import com.planner.travel.domain.user.entity.User;
import com.planner.travel.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlannerService {
    private final UserRepository userRepository;
    private final PlannerRepository plannerRepository;

    @Transactional
    public void create(PlannerCreateRequest request, Long userId) {
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
    }

    @Transactional
    public void update(PlannerUpdateRequest request, Long plannerId) {
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new EntityNotFoundException("Planner not found for id: " + plannerId));

        if (isValid(request.title())) {
            planner.updateTitle(request.title());
        }

        planner.setStartDate(request.startDate());
        planner.setEndDate(request.endDate());
        planner.setPrivate(request.isPrivate());

        // 플래너 엔티티 저장
        plannerRepository.save(planner);
    }

    @Transactional
    public void delete(Long plannerId) {
        // 요청된 플래너 ID를 사용하여 플래너 엔티티 조회
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new EntityNotFoundException("Planner not found for id: " + plannerId));

        // 플래너 삭제 (isDeleted를 true로 설정)
        planner.setDeleted(true);

        // 플래너 엔티티 저장
        plannerRepository.save(planner);
    }

    private boolean isValid(String value) {
        return value != null && !value.isEmpty();
    }
}
