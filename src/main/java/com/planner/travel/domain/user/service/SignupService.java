package com.planner.travel.domain.user.service;

import com.planner.travel.domain.user.dto.response.SignupRequest;
import com.planner.travel.domain.user.entity.User;
import com.planner.travel.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService {
    // 아래에 유저 repository 를 주입해주세요.
    private final UserRepository userRepository;

    @Transactional
    public void signup(SignupRequest signupRequest) {
        // 회원가입 로직을 작성해주세요. 빌더 패턴을 사용해주세요.
        boolean userExists = userRepository.findByEmail(signupRequest.getEmail()).isPresent();
        if (userExists) {
            throw new IllegalStateException("이미 등록된 이메일입니다.");
        }

        // 비밀번호 암호화 로직을 추가해야 합니다. 여기서는 단순화를 위해 생략했습니다.

        User user = User.builder()
                .email(signupRequest.getEmail())
                .password(signupRequest.getPassword()) // 실제로는 암호화된 비밀번호를 설정해야 합니다.
                .nickname(signupRequest.getNickname())
                .build();

        userRepository.save(user);


    }
}
