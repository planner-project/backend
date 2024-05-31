package com.planner.travel.domain.planner.controller;

import com.planner.travel.domain.planner.dto.request.PlanCreateRequest;
import com.planner.travel.domain.planner.dto.request.PlanUpdateRequest;
import com.planner.travel.domain.planner.service.PlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
@Controller
@Slf4j
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping(value = "/planner/{plannerId}/planBox/{planBoxId}/create") // <- 프론트가 보내는 주소 입니다.
    public void createDate(@DestinationVariable("plannerId") Long plannerId, @DestinationVariable("planBoxId") Long planBoxId, @RequestBody PlanCreateRequest request) {
        // 서비스를 작성해주세요
        planService.create(request, planBoxId);

        // 결과값은 항상 같습니다.
        simpMessagingTemplate.convertAndSend("/sub/planner/" + plannerId,
                Map.of("type", "create-plan", "message", planService.getAllPlan(planBoxId)) //
        );
    }

    @MessageMapping(value = "/planner/{plannerId}/planBox/{planBoxId}/update/{planId}") // <- 프론트가 보내는 주소 입니다.
    public void updateDate( @DestinationVariable Long plannerId,@DestinationVariable("planBoxId") Long planBoxId,@DestinationVariable("planId") Long planId,  @RequestBody PlanUpdateRequest request) {
        // 서비스를 작성해주세요
        planService.update(request, planId);

        // 결과값은 항상 같습니다.
        simpMessagingTemplate.convertAndSend("/sub/planner/" + plannerId,
                Map.of("type", "update-plan", "message", planService.getAllPlan(planBoxId))
        );
    }

    @MessageMapping(value = "/planner/{plannerId}/planBox/{planBoxId}/delete/{planId}") // <- 프론트가 보내는 주소 입니다.
    public void deleteDate( @DestinationVariable("plannerId") Long plannerId,@DestinationVariable("planBoxId") Long planBoxId,@DestinationVariable("planId") Long planId) {
        // 서비스를 작성해주세요
        planService.delete(planId);

        // 결과값은 항상 같습니다.
        simpMessagingTemplate.convertAndSend("/sub/planner/" + plannerId,
                Map.of("type", "delete-plan", "message", planService.getAllPlan(planBoxId))
        );
    }
}
