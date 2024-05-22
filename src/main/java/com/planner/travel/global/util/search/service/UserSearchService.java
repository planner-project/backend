package com.planner.travel.global.util.search.service;

import com.planner.travel.domain.user.entity.User;
import com.planner.travel.domain.user.repository.UserRepository;
import com.planner.travel.global.util.search.dto.UserSearchResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSearchService {
    private final UserRepository userRepository;

    public UserSearchResponse findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        UserSearchResponse userSearchResponse = new UserSearchResponse(
                user.getId(),
                user.getNickname(),
                user.getUserTag(),
                user.getProfile().getImage().getImageUrl()
        );

        return userSearchResponse;
    }
}
