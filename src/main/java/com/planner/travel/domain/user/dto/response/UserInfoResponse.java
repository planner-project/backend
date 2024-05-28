package com.planner.travel.domain.user.dto.response;

import java.time.LocalDate;

public record UserInfoResponse(
        Long userId,
        String nickname,
        Long userTag,
        LocalDate birthday,
        String profileImgUrl,
        boolean isBirthday) {
}
