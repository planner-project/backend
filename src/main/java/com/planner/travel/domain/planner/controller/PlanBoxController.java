package com.planner.travel.domain.planner.controller;

import com.planner.travel.domain.planner.dto.request.PlanBoxCreateRequest;
import com.planner.travel.domain.planner.dto.request.PlanBoxUpdateRequest;
import com.planner.travel.domain.planner.dto.response.PlanBoxResponse;
import com.planner.travel.domain.planner.service.PlanBoxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PlanBoxController {
    private final PlanBoxService planBoxService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping(value = "/planner/{plannerId}/create") // <- 프론트가 보내는 주소 입니다.
    public void createDate(@DestinationVariable Long plannerId, @Header("Authorization") String accessToken, PlanBoxCreateRequest request) {
        // 서비스를 작성해주세요
        planBoxService.create(request, plannerId);

        // 결과값은 항상 같습니다.
        simpMessagingTemplate.convertAndSend("/sub/planner/" + plannerId,
                Map.of("type", "planBox", "message", planBoxService.getAllPlanBox(plannerId)) // <- 해당 주소를 구독하는 모든 사람이 CUD 과정을 볼 수 있습니다.
        );
    }

    @GetMapping
    public ResponseEntity<List<PlanBoxResponse>> getAllPlanBoxes(@PathVariable("plannerId") Long plannerId) {
        List<PlanBoxResponse> planBoxes = planBoxService.getAllPlanBox(plannerId);
        return ResponseEntity.ok(planBoxes);
    }

    @PostMapping(value = "/ceate-planBox/planner/{plannerId}")
    public ResponseEntity<?> createPlanBox(@PathVariable("plannerId") Long plannerId, @RequestBody PlanBoxCreateRequest request) {
        planBoxService.create(request, plannerId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update-planBox/planner/{plannerId}")
    public ResponseEntity<List<PlanBoxResponse>> updatePlanBox(@PathVariable("plannerId") Long plannerId, @PathVariable("planBoxId") Long planBoxId, @RequestBody PlanBoxUpdateRequest request) {
        List<PlanBoxResponse> updatedPlanBoxes = planBoxService.update(request, planBoxId);
        return ResponseEntity.ok(updatedPlanBoxes);
    }

    @DeleteMapping("/delete-planBox/planner/{plannerId}")
    public ResponseEntity<List<PlanBoxResponse>> deletePlanBox(@PathVariable("plannerId") Long plannerId, @PathVariable("planBoxId") Long planBoxId) {
        List<PlanBoxResponse> updatedPlanBoxes = planBoxService.delete(planBoxId);
        return ResponseEntity.ok(updatedPlanBoxes);
    }
}
