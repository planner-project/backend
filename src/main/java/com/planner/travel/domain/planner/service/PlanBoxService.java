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

    // get 은 쿼리문을 작성 해야 해서 일단은 이를 제외한 기능만 생성 해 주세요.
    // delete 의 경우 이전과 같이 isDeleted 를 true 로 변경 해주면 됩니다.
    // update 의 경우 userInfoUpdateService 와 유저 엔티티를 참고 하여 작성 해 주세요.
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
                .isPrivate(request.isPrivate())
                .build();

        planBoxRepository.save(planBox);

        List<LocalDate> localDates = plannerQueryService.updateStartDateAndEndDate(plannerId);
        planner.updateStartDate(localDates.get(0));
        planner.updateStartDate(localDates.get(1));
    }

    @Transactional
    public List<PlanBoxResponse> update(PlanBoxUpdateRequest request, Long planBoxId) {
        /*
        리퀘스트로 받은 값을 사용하여 플랜박스를 만들어주세요.
        PlanBoxQueryService 를 사용하여 반환값을 void -> List 형식으로 바꿔주세요.
        이때 이전처럼 set 을 사용하지 말고 변경해주세요.
         */
        PlanBox planBox = planBoxRepository.findById(planBoxId)
                .orElseThrow(() -> new EntityNotFoundException("PlanBox not found for id: " + planBoxId));

        if (request.planDate() != null) {
            planBox.updatePlanDate(request.planDate());
            planBox.updatePrivate(request.isPrivate());
        }

        planBoxRepository.save(planBox);

        return planBoxQueryService.findPlanBoxesByPlannerId(planBoxId);

    }

    @Transactional
    public List<PlanBoxResponse> delete(Long planBoxId) {
        /*
        리퀘스트로 받은 값을 사용하여 플랜박스를 만들어주세요.
        PlanBoxQueryService 를 사용하여 반환값을 void -> List 형식으로 바꿔주세요.
        이때 이전처럼 isDeleted 를 True 로 변경해주세요.
        이때 이전처럼 set 을 사용하지 말고 변경해주세요.
         */
        PlanBox planBox = planBoxRepository.findById(planBoxId)
                .orElseThrow(() -> new EntityNotFoundException("PlanBox not found for id: " + planBoxId));

        planBox.Deleted(true);

        planBoxRepository.save(planBox);

        return planBoxQueryService.findPlanBoxesByPlannerId(planBoxId);
    }
}
