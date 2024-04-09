package com.planner.travel.domain.user.service;

import com.planner.travel.domain.user.dto.response.SignupRequest;
import com.planner.travel.domain.user.entity.Role;
import com.planner.travel.domain.user.entity.User;
import com.planner.travel.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SignupService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    // 랜덤넘버 클래스를 주입

    @Transactional
    public void signup(SignupRequest signupRequest) {
        boolean userExists = userRepository.findByEmail(signupRequest.getEmail()).isPresent();
        if (userExists) {
            throw new IllegalStateException("이미 등록된 이메일입니다.");
        }

        // 랜덤 넘버 생성하는 메서드를 사용하여 유저 태그에 넣어주세요.
        // birthday, userTag, phoneNumber 도 리퀘스트에 추가해주세요!
        User user = User.builder()
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .nickname(signupRequest.getNickname())
                .role(Role.USER)
                .signupDate(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }
}
