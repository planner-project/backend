package com.planner.travel.domain.user.service;

import com.planner.travel.domain.user.dto.response.SignupRequest;
import com.planner.travel.domain.user.entity.Role;
import com.planner.travel.domain.user.entity.User;
import com.planner.travel.domain.user.repository.UserRepository;
import com.planner.travel.global.util.RandomNumberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignupService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RandomNumberUtil randomNumberUtil;

    @Transactional
    public void signup(SignupRequest signupRequest) {
        userRepository.findByEmail(signupRequest.getEmail())
                .ifPresent(u -> {
                    throw new IllegalArgumentException();
                });

        User user = User.builder()
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .nickname(signupRequest.getNickname())
                .userTag(randomNumberUtil.set())
                .role(Role.USER)
                .signupDate(LocalDateTime.now())
                .birthday(signupRequest.getBirthday())
//                .phoneNumber(signupRequest.getPhoneNumber())
                .isWithdrawal(false)
                .build();

        userRepository.save(user);
    }
}
