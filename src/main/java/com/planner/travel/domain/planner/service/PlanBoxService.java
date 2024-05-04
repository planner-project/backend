package com.planner.travel.domain.planner.service;

import com.planner.travel.domain.planner.dto.request.PlanBoxCreateRequest;
import com.planner.travel.domain.planner.dto.request.PlanBoxUpdateRequest;
import com.planner.travel.domain.planner.entity.PlanBox;
import com.planner.travel.domain.planner.entity.Planner;
import com.planner.travel.domain.planner.repository.PlanBoxRepository;
import com.planner.travel.domain.planner.repository.PlannerRepository;
import com.planner.travel.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanBoxService {
    // get 은 쿼리문을 작성 해야 해서 일단은 이를 제외한 기능만 생성 해 주세요.
    // delete 의 경우 이전과 같이 isDeleted 를 true 로 변경 해주면 됩니다.
    // update 의 경우 userInfoUpdateService 와 유저 엔티티를 참고 하여 작성 해 주세요.
    private final UserRepository userRepository;
    private final PlanBoxRepository planBoxRepository;
    private final PlannerRepository plannerRepository;
    public void create(PlanBoxCreateRequest request, Long plannerId) {
        // 요청된 플래너 ID를 사용하여 플래너 엔티티 조회
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new EntityNotFoundException("Planner not found for id: " + plannerId));

        // 새로운 PlanBox 엔티티 생성
        PlanBox planBox = PlanBox.builder()
                .planDate(request.planDate())
                .isPrivate(request.isPrivate())
                .planner(planner) // 생성된 PlanBox를 플래너에게 연결
                .build();

        // PlanBox 엔티티 저장
        planBoxRepository.save(planBox);
    }

    public void update(PlanBoxUpdateRequest request, Long planBoxId) {
        // 요청된 PlanBox ID를 사용하여 PlanBox 엔티티 조회
        PlanBox planBox = planBoxRepository.findById(planBoxId)
                .orElseThrow(() -> new EntityNotFoundException("PlanBox not found for id: " + planBoxId));

        // PlanBox 정보 업데이트
        planBox.setPlanDate(request.planDate());
        planBox.setPrivate(request.isPrivate());

        // PlanBox 엔티티 저장
        planBoxRepository.save(planBox);
    }

    public void delete(Long planBoxId) {
        // 요청된 PlanBox ID를 사용하여 PlanBox 엔티티 조회
        PlanBox planBox = planBoxRepository.findById(planBoxId)
                .orElseThrow(() -> new EntityNotFoundException("PlanBox not found for id: " + planBoxId));

        // PlanBox 삭제 (isDeleted를 true로 설정)
        planBox.setDeleted(true);

        // PlanBox 엔티티 저장
        planBoxRepository.save(planBox);
    }
}
