package com.planner.travel.domain.profile.dto.request;

import java.time.LocalDate;

// phone number 은 제외 상태
public record UserInfoUpdateRequest(String password, String nickname, LocalDate birthday) {
}
