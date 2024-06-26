package com.planner.travel.global.auth.oauth.service;

import com.planner.travel.domain.profile.entity.Profile;
import com.planner.travel.domain.profile.repository.ProfileRepository;
import com.planner.travel.domain.user.entity.Role;
import com.planner.travel.domain.user.entity.Sex;
import com.planner.travel.domain.user.entity.User;
import com.planner.travel.domain.user.repository.UserRepository;
import com.planner.travel.global.auth.oauth.entity.OAuth2UserInfo;
import com.planner.travel.global.util.RandomNumberUtil;
import com.planner.travel.global.util.image.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OAuth2SignupService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final RandomNumberUtil randomNumberUtil;

    public User signup(String provider, OAuth2UserInfo oAuth2UserInfo) {

        Profile profile = Profile.builder()
                .profileImageUrl(oAuth2UserInfo.getProfile())
                .build();

        profileRepository.save(profile);

        User user = User.builder()
                .provider(provider)
                .role(Role.USER)
                .nickname(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .profile(profile)
                .signupDate(LocalDateTime.now())
                .isWithdrawal(false)
                .userTag(randomNumberUtil.set())
                .sex(Sex.NONE)
                .build();

        userRepository.save(user);

        return user;
    }
}
