package com.planner.travel.domain.planner.controller;

import com.planner.travel.domain.planner.dto.request.PlannerCreateRequest;
import com.planner.travel.domain.planner.dto.request.PlannerUpdateRequest;
import com.planner.travel.domain.planner.dto.response.PlannerResponse;
import com.planner.travel.domain.planner.service.PlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/user")
public class PlannerController {
    private final PlannerService plannerService;

    @GetMapping(value = "/{userId}/planners")
    public ResponseEntity<List<PlannerResponse>> getPlanners(@PathVariable Long userId) {
        List<PlannerResponse> planners = plannerService.getAllPlanners(userId);
        return ResponseEntity.ok(planners);
    }

    @GetMapping(value = "/{userId}/planners/{plannerId}")
    public ResponseEntity<PlannerResponse> getSpecificPlanner(@PathVariable Long userId, @PathVariable Long plannerId) {
        PlannerResponse planner = plannerService.getPlannerById(plannerId);
        return ResponseEntity.ok(planner);
    }

    @PostMapping(value = "/{userId}/planners")
    public ResponseEntity<PlannerResponse> createPlanner(@PathVariable Long userId, @RequestBody PlannerCreateRequest request) {
        PlannerResponse planner = plannerService.create(request, userId);
        return ResponseEntity.ok(planner);
    }

    @PatchMapping(value = "/{userId}/planners/{plannerId}")
    public ResponseEntity<PlannerResponse> updatePlanner(@PathVariable Long userId, @PathVariable Long plannerId, @RequestBody PlannerUpdateRequest request) {
        PlannerResponse planner = plannerService.update(request, plannerId);
        return ResponseEntity.ok(planner);
    }

    @DeleteMapping(value = "/{userId}/planners/{plannerId}")
    public ResponseEntity<Void> deletePlanner(@PathVariable Long userId, @PathVariable Long plannerId) {
        plannerService.delete(plannerId);
        return ResponseEntity.noContent().build();
    }
}
