package com.planner.travel.domain.chat.dto;

public record ChatDto(
        Long userId,
        Long nickname,
        String profileImgUrl,
        String message
) {
}
