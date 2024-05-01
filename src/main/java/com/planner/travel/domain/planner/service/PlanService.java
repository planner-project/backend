package com.planner.travel.domain.planner.service;

import com.planner.travel.domain.planner.dto.request.PlanCreateRequest;
import com.planner.travel.domain.planner.dto.request.PlanUpdateRequest;
import org.springframework.stereotype.Service;

@Service
public class PlanService {
    // get 은 쿼리문을 작성 해야 해서 일단은 이를 제외한 기능만 생성 해 주세요.
    // delete 의 경우 이전과 같이 isDeleted 를 true 로 변경 해주면 됩니다.
    // update 의 경우 userInfoUpdateService 와 유저 엔티티를 참고 하여 작성 해 주세요.
    public void create(PlanCreateRequest request, Long planBoxId, Long plannerId) {

    }

    public void update(PlanUpdateRequest request, Long planId) {

    }

    public void delete(Long planId) {

    }
}
