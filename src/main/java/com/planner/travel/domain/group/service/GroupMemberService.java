package com.planner.travel.domain.group.service;

import com.planner.travel.domain.group.dto.request.GroupMemberAddRequest;
import com.planner.travel.domain.group.dto.request.GroupMemberDeleteRequest;
import com.planner.travel.domain.group.dto.response.GroupMemberResponse;
import com.planner.travel.domain.group.entity.GroupMember;
import com.planner.travel.domain.group.query.GroupMemberQueryService;
import com.planner.travel.domain.group.repository.GroupMemberRepository;
import com.planner.travel.domain.planner.entity.Planner;
import com.planner.travel.domain.planner.repository.PlannerRepository;
import com.planner.travel.domain.user.entity.User;
import com.planner.travel.domain.user.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupMemberService {
    private final GroupMemberQueryService groupMemberQueryService;
    private final PlannerRepository plannerRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<GroupMemberResponse> getAllGroupMembers(Long plannerId) {
        List<GroupMemberResponse> groupMemberResponses = groupMemberQueryService.findGroupMembersByPlannerId(plannerId);
        return groupMemberResponses;
    }

    @Transactional(readOnly = true)
    public boolean isGroupMember(Long userId, Long plannerId) {
        boolean isGroupMember = groupMemberRepository.findByUserIdAndPlannerId(userId, plannerId);
        return isGroupMember;
    }

    @Transactional
    public void addGroupMembers(Long plannerId, GroupMemberAddRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new EntityNotFoundException("Planner not found"));

        if (!groupMemberQueryService.validateGroupMember(user.getId(), planner.getId())) {
            GroupMember groupMember = GroupMember.builder()
                    .isHost(false)
                    .isLeaved(false)
                    .user(user)
                    .planner(planner)
                    .build();

            groupMemberRepository.save(groupMember);

        } else {
            throw new EntityExistsException("이미 존재하는 유저 입니다.");
        }
    }

    @Transactional
    public void deleteGroupMembers(Long plannerId, GroupMemberDeleteRequest request) {
        GroupMember groupMember = groupMemberRepository.findById(request.groupMemberId())
                .orElseThrow(() -> new EntityNotFoundException("User Not found"));

        groupMember.deleteGroupMember(true);
    }
}
