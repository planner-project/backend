package com.planner.travel.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    // 회원 가입 시 받을 dto 를 작성해주세요. 검증 annotation 은 제가 추가하겠습니다!
    private String email;
}
