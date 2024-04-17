package com.planner.travel.domain.user.service;

import com.planner.travel.domain.user.dto.response.SignupRequest;
import com.planner.travel.domain.user.entity.Role;
import com.planner.travel.domain.user.entity.User;
import com.planner.travel.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SignupService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RandomNumberGenerator randomNumberGenerator;
    // 랜덤넘버 클래스를 주입
    @Component
    public class RandomNumberGenerator {
        public String generateRandomNumber() {
            // 1000부터 9999까지의 난수를 생성하여 문자열로 반환
            Random random = new Random();
            int randomNumber = random.nextInt(9000) + 1000;
            return String.valueOf(randomNumber);
        }
    }

    @Transactional
    public void signup(SignupRequest signupRequest) {
        boolean userExists = userRepository.findByEmail(signupRequest.getEmail()).isPresent();
        if (userExists) {
            throw new IllegalStateException("이미 등록된 이메일입니다.");
        }

        // 랜덤 넘버 생성하는 메서드를 사용하여 유저 태그에 넣어주세요.
        Long userTag = generateUserTag();
        // birthday, userTag, phoneNumber 도 리퀘스트에 추가해주세요!
        User user = User.builder()
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .nickname(signupRequest.getNickname())
                .role(Role.USER)
                .signupDate(LocalDateTime.now())
                .userTag(userTag) // 랜덤 넘버 추가
                .birthday(signupRequest.getBirthday()) // 생일 추가
                .phoneNumber(signupRequest.getPhoneNumber()) // 전화번호 추가
                .build();

        userRepository.save(user);
    }

    // 랜덤 넘버 생성 메서드
    private Long generateUserTag() {
        String randomNumberString = randomNumberGenerator.generateRandomNumber();
        return Long.parseLong(randomNumberString);
    }

}


