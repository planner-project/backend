package com.planner.travel.domain.group.query;

import com.planner.travel.domain.group.dto.response.GroupMemberResponse;
import com.planner.travel.domain.group.entity.GroupMember;
import com.planner.travel.domain.group.entity.QGroupMember;
import com.planner.travel.domain.profile.entity.QProfile;
import com.planner.travel.domain.user.entity.QUser;
import com.planner.travel.global.util.image.entity.QImage;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupMemberQueryService {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public GroupMemberQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<GroupMemberResponse> findGroupMembersByPlannerId(Long plannerId) {
        QGroupMember qGroupMember = QGroupMember.groupMember;
        QUser qUser = QUser.user;
        QProfile qProfile = QProfile.profile;
        QImage qImage = QImage.image;

        return queryFactory
                .select(Projections.constructor(GroupMemberResponse.class,
                        qGroupMember.id,
                        qUser.id,
                        qUser.nickname,
                        qUser.userTag,
                        qImage.imageUrl.coalesce("").as("imageUrl"),
                        qGroupMember.isHost
                ))
                .from(qGroupMember)
                .join(qGroupMember.user, qUser)
                .leftJoin(qUser.profile, qProfile)
                .leftJoin(qProfile.image, qImage)
                .where(qGroupMember.planner.id.eq(plannerId)
                        .and(qGroupMember.isLeaved.eq(false)))
                .fetch();
    }

    public boolean validateGroupMember(Long userId, Long plannerId) {
        QUser qUser = QUser.user;
        QGroupMember qGroupMember = QGroupMember.groupMember;

        Long count = queryFactory
                .select(qGroupMember.count())
                .from(qGroupMember)
                .join(qGroupMember.user, qUser)
                .where(qUser.id.eq(userId)
                        .and(qGroupMember.planner.id.eq(plannerId)))
                .fetchOne();

        return count != null && count > 0;
    }

    public GroupMember findGroupMemberId(Long userId, Long plannerId) {
        QGroupMember qGroupMember = QGroupMember.groupMember;

        GroupMember groupMember = queryFactory
                .select(qGroupMember)
                .from(qGroupMember)
                .where(qGroupMember.user.id.eq(userId)
                        .and(qGroupMember.planner.id.eq(plannerId))
                        .and(qGroupMember.isLeaved.isFalse()))
                .fetchOne();

        return groupMember;
    }
}
