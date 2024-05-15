package com.planner.travel.domain.planner.dto.request;

import java.time.LocalDate;

public record PlannerCreateRequest(
        String title,
        LocalDate startDate,
        LocalDate endDate,
        boolean isPrivate) {
}
