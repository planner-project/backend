package com.planner.travel.domain.group.controller;

import com.planner.travel.domain.group.dto.request.GroupMemberAddRequest;
import com.planner.travel.domain.group.dto.request.GroupMemberDeleteRequest;
import com.planner.travel.domain.group.service.GroupMemberService;
import com.planner.travel.global.jwt.token.TokenAuthenticator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class GroupMemberWebSocketController {
    private final GroupMemberService groupMemberService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final TokenAuthenticator tokenAuthenticator;

    @MessageMapping(value = "/planner/{plannerId}/members/create")
    public void addGroupMember(@DestinationVariable Long plannerId, @Header("Authorization") String accessToken, @RequestBody GroupMemberAddRequest request) {
        tokenAuthenticator.getAuthenticationUsingToken(accessToken);
        groupMemberService.addGroupMembers(plannerId, request);

        simpMessagingTemplate.convertAndSend("/sub/planner/" + plannerId,
                Map.of("type", "add-group-member", "message", groupMemberService.getAllGroupMembers(plannerId))
        );
    }

    @MessageMapping(value = "/planner/{plannerId}/members/{memberId}/delete")
    public void deleteGroupMember(@DestinationVariable Long plannerId, @Header("Authorization") String accessToken, @RequestBody GroupMemberDeleteRequest request) {
        tokenAuthenticator.getAuthenticationUsingToken(accessToken);
        groupMemberService.deleteGroupMembers(plannerId, request);

        simpMessagingTemplate.convertAndSend("/sub/planner/" + plannerId,
                Map.of("type", "delete-group-member", "message", groupMemberService.getAllGroupMembers(plannerId))
        );
    }
}
