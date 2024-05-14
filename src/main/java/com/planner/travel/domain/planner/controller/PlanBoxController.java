package com.planner.travel.domain.planner.controller;

import com.planner.travel.domain.planner.dto.request.PlanBoxCreateRequest;
import com.planner.travel.domain.planner.service.PlanBoxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PlanBoxController {
    private final PlanBoxService planBoxService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    // 웹소켓 주소 명세는 노션을 참고해주세요.
    // 플랜박스 코드 다 작성하면 웹소켓 테스트 하는 법 알려드리겠습니다!
    // 플랜박스를 CUD 할 때는 전체 planBox 리스트를 다시 보내주세요. 관련 서비스는 PlanBoxQueryService 에 있습니다.

    @MessageMapping(value = "/planner/{plannerId}/create") // <- 프론트가 보내는 주소 입니다.
    public void createDate(@DestinationVariable Long plannerId, @Header("Authorization") String accessToken, PlanBoxCreateRequest request) {
        simpMessagingTemplate.convertAndSend("/sub/planner/" + plannerId,
                Map.of("type", "create-planBox", "message", planBoxService.create(request, plannerId)) // <- 해당 주소를 구독하는 모든 사람이 CUD 과정을 볼 수 있습니다.
        );
    }
}
