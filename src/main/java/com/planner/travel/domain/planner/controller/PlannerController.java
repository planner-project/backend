package com.planner.travel.domain.planner.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/user")
public class PlannerController {
    @GetMapping(value = "/{userId}/planners")
    public ResponseEntity<?> getPlanner(@PathVariable Long userId) {
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{userId}/planners")
    public ResponseEntity<?> createPlanner(@PathVariable Long userId) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{userId}/planners/{plannerId}")
    public ResponseEntity<?> updatePlanner(@PathVariable Long userId, @PathVariable Long plannerId) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{userId}/planners/{plannerId}")
    public ResponseEntity<?> deletePlanner(@PathVariable Long userId, @PathVariable Long plannerId) {
        return ResponseEntity.ok().build();
    }
}
