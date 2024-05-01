package com.planner.travel.domain.planner.dto.request;

import java.time.LocalDateTime;

public record PlanUpdateRequest(
        boolean isPrivate,
        String title,
        LocalDateTime time,
        String content,
        String address
) {
}
