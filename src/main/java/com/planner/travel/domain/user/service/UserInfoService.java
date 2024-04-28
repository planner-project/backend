package com.planner.travel.domain.user.service;

import com.planner.travel.domain.user.dto.response.UserInfoResponse;
import com.planner.travel.domain.user.entity.User;
import com.planner.travel.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class UserInfoService {
    private final UserRepository userRepository;

    public UserInfoResponse get(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 유저 없음 " + userId));

        return new UserInfoResponse(
                user.getId(),
                user.getNickname(),
                user.getUserTag(),
                isBirthdayToday(user.getBirthday())
        );
    }

    private boolean isBirthdayToday(LocalDate birthday) {
        return birthday != null && birthday.getMonth() == LocalDate.now().getMonth() &&
                birthday.getDayOfMonth() == LocalDate.now().getDayOfMonth();
    }
}
