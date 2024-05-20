package com.planner.travel.domain.group.query;

import com.planner.travel.domain.group.dto.response.GroupMemberResponse;
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
                        qImage.imageUrl,
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
}
