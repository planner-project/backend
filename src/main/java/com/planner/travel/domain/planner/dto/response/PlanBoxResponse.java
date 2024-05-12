package com.planner.travel.domain.planner.dto.response;

import java.time.LocalDate;

public record PlanBoxResponse(
        LocalDate planDate,
        boolean isPrivate
) {
}
