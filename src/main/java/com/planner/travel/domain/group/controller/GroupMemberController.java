package com.planner.travel.domain.group.controller;

import com.planner.travel.domain.group.dto.response.GroupMemberResponse;
import com.planner.travel.domain.group.query.GroupMemberQueryService;
import com.planner.travel.domain.group.service.GroupMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
public class GroupMemberController {
    private final GroupMemberService groupMemberService;
    private final GroupMemberQueryService groupMemberQueryService;

    @GetMapping(value = "/{userId}/planners/{plannerId}/group")
    public ResponseEntity<List<GroupMemberResponse>> getGroupMembers(@PathVariable("userId") Long userId, @PathVariable("plannerId") Long plannerId) {
        List<GroupMemberResponse> groupMemberResponses = groupMemberQueryService.findGroupMembersByPlannerId(plannerId);

        return ResponseEntity.ok(groupMemberResponses);
    }

    @GetMapping(value = "/{userId}/planners/{plannerId}/group/check")
    public ResponseEntity<?> isGroupMember(@PathVariable("userId") Long userId, @PathVariable("plannerId") Long plannerId) {
        boolean isGroupMember = groupMemberService.isGroupMember(userId, plannerId);

        return ResponseEntity.ok(isGroupMember);
    }
}
