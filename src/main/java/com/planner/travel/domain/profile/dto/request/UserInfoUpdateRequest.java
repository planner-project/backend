package com.planner.travel.domain.profile.dto.request;

import java.time.LocalDate;

public record UserInfoUpdateRequest(String password, String nickname, LocalDate birthday, String phoneNumber) {
}
