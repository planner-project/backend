package com.planner.travel.domain.planner.dto.request;

import java.time.LocalDateTime;

public record PlanCreateRequest(
        boolean isPrivate,
        String title,
        LocalDateTime time,
        String content,
        String address,
        boolean isDeleted
) {
}
