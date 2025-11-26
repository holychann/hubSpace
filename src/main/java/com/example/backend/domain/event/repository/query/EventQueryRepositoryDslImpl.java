package com.example.backend.domain.event.repository.query;

import com.example.backend.domain.event.entity.EventEntity;
import com.example.backend.domain.event.entity.QEventEntity;
import com.example.backend.domain.user.entity.UserEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class EventQueryRepositoryDslImpl implements EventQueryRepositoryDsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<EventEntity> findByUserIdAndIsActive(UserEntity userEntity, Boolean isActive) {

        QEventEntity event = QEventEntity.eventEntity;

        return queryFactory
                .selectFrom(event)
                .where(event.userId.eq(userEntity).and(event.isActive.eq(isActive)))
                .fetch();
    }
}
