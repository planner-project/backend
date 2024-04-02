package com.planner.travel.domain.user.service;

import com.planner.travel.domain.user.dto.response.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService {
    // 아래에 유저 repository 를 주입해주세요.

    @Transactional
    public void signup(SignupRequest signupRequest) {
        // 회원가입 로직을 작성해주세요. 빌더 패턴을 사용해주세요.
    }
}
