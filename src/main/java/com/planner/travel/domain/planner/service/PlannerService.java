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

@Service
@RequiredArgsConstructor
public class PlannerService {
    // get 은 쿼리문을 작성 해야 해서 일단은 이를 제외한 기능만 생성 해 주세요.
    // delete 의 경우 이전과 같이 isDeleted 를 true 로 변경 해주면 됩니다.
    // update 의 경우 userInfoUpdateService 와 유저 엔티티를 참고 하여 작성 해 주세요.
    private final UserRepository userRepository;
    private final PlannerRepository plannerRepository;
    public void create(PlannerCreateRequest request, Long userId) {
        // 요청된 사용자 ID를 사용하여 사용자 엔티티 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found for id: " + userId));

        // 새로운 플래너 엔티티 생성
        Planner planner = Planner.builder()
                .title(request.title())
//                .startDate(request.startDate()) 아직 완성x
//                .endDate(request.endDate())
                .isPrivate(request.isPrivate())
                .user(user) // 생성된 플래너를 사용자에게 연결
                .build();

        // 플래너 엔티티 저장
        plannerRepository.save(planner);
    }

    public void update(PlannerUpdateRequest request, Long plannerId) {
        // 요청된 플래너 ID를 사용하여 플래너 엔티티 조회
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new EntityNotFoundException("Planner not found for id: " + plannerId));

        // 플래너 정보 업데이트
        planner.setTitle(request.title());
//        planner.setStartDate(request.getStartDate());
//        planner.setEndDate(request.getEndDate());
        planner.setPrivate(request.isPrivate());

        // 플래너 엔티티 저장
        plannerRepository.save(planner);
    }

    public void delete(Long plannerId) {
        // 요청된 플래너 ID를 사용하여 플래너 엔티티 조회
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new EntityNotFoundException("Planner not found for id: " + plannerId));

        // 플래너 삭제 (isDeleted를 true로 설정)
        planner.setDeleted(true);

        // 플래너 엔티티 저장
        plannerRepository.save(planner);
    }
}
