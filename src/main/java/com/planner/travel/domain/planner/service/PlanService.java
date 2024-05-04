package com.planner.travel.domain.planner.service;

import com.planner.travel.domain.planner.dto.request.PlanCreateRequest;
import com.planner.travel.domain.planner.dto.request.PlanUpdateRequest;
import com.planner.travel.domain.planner.entity.Plan;
import com.planner.travel.domain.planner.repository.PlanRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanService {
    // get 은 쿼리문을 작성 해야 해서 일단은 이를 제외한 기능만 생성 해 주세요.
    // delete 의 경우 이전과 같이 isDeleted 를 true 로 변경 해주면 됩니다.
    // update 의 경우 userInfoUpdateService 와 유저 엔티티를 참고 하여 작성 해 주세요.
    private final PlanRepository planRepository;
    public void create(PlanCreateRequest request, Long planBoxId, Long plannerId) {
        Plan plan = Plan.builder()
                .isPrivate(request.isPrivate())
                .title(request.title())
                .time(request.time())
                .content(request.content())
                .address(request.address())
                .isDeleted(false) // 새로 생성된 Plan은 삭제되지 않았음을 의미
                .build();
    }

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

    public void delete(Long planId) {
        // Plan 엔티티 조회
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("Plan not found for id: " + planId));

        // Plan 삭제 (isDeleted를 true로 설정)
        plan.setDeleted(true);

        // Plan 엔티티 저장
        planRepository.save(plan);
    }
}
