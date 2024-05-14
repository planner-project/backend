package com.planner.travel.domain.planner.dto.response;

import java.time.LocalDate;
import java.util.*;

public record PlanBoxResponse(
        LocalDate planDate,
        boolean isPrivate,
        List<PlanResponse> planResponses
) {
}
