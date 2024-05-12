package com.planner.travel.domain.planner.service;

import com.planner.travel.domain.planner.dto.request.PlanBoxCreateRequest;
import com.planner.travel.domain.planner.dto.request.PlanBoxUpdateRequest;
import com.planner.travel.domain.planner.dto.response.PlanBoxResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlanBoxService {
    // get 은 쿼리문을 작성 해야 해서 일단은 이를 제외한 기능만 생성 해 주세요.
    // delete 의 경우 이전과 같이 isDeleted 를 true 로 변경 해주면 됩니다.
    // update 의 경우 userInfoUpdateService 와 유저 엔티티를 참고 하여 작성 해 주세요.
    public List<PlanBoxResponse> create(PlanBoxCreateRequest request, Long plannerId) {
        /*
        리퀘스트로 받은 값을 사용하여 플랜박스를 만들어주세요.
        PlanBoxQueryService 를 사용하여 반환값을 void -> List 형식으로 바꿔주세요.
         */

        return new ArrayList<>();
    }

    public void update(PlanBoxUpdateRequest request, Long planBoxId) {
        /*
        리퀘스트로 받은 값을 사용하여 플랜박스를 만들어주세요.
        PlanBoxQueryService 를 사용하여 반환값을 void -> List 형식으로 바꿔주세요.
        이때 이전처럼 set 을 사용하지 말고 변경해주세요.
         */
    }

    public void delete(Long planBoxId) {
        /*
        리퀘스트로 받은 값을 사용하여 플랜박스를 만들어주세요.
        PlanBoxQueryService 를 사용하여 반환값을 void -> List 형식으로 바꿔주세요.
        이때 이전처럼 isDeleted 를 True 로 변경해주세요.
        이때 이전처럼 set 을 사용하지 말고 변경해주세요.
         */
    }
}
