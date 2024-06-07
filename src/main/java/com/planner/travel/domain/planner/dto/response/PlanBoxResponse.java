package com.planner.travel.domain.planner.dto.response;

import java.time.LocalDate;
import java.util.*;

public record PlanBoxResponse(
        Long planBoxId,
        LocalDate planDate,
        List<PlanResponse> planResponses
) {
}
