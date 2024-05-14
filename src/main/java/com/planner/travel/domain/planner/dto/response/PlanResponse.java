package com.planner.travel.domain.planner.dto.response;

import java.time.LocalDateTime;

public record PlanResponse(
    boolean isPrivate,
    String title,
    LocalDateTime time,
    String content,
    String address
) { }
