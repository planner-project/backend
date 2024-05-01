package com.planner.travel.domain.planner.dto.request;

import java.time.LocalDate;

public record PlannerUpdateRequest(String title, boolean isPrivate) {
}
