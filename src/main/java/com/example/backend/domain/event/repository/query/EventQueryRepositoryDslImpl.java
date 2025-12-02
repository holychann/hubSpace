package com.example.backend.domain.event.repository.query;

import com.example.backend.domain.event.dto.EventWithMetadataDto;
import com.example.backend.domain.event.entity.EventEntity;
import com.example.backend.domain.event.entity.QEventEntity;
import com.example.backend.domain.event.entity.QEventMetadataEntity;
import com.example.backend.domain.user.entity.UserEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class EventQueryRepositoryDslImpl implements EventQueryRepositoryDsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<EventWithMetadataDto> findByUserIdAndIsActive(UserEntity userEntity, Boolean isActive) {

        QEventMetadataEntity metadata = QEventMetadataEntity.eventMetadataEntity;
        QEventEntity event = QEventEntity.eventEntity;

        return queryFactory
                .select(Projections.constructor(
                        EventWithMetadataDto.class,
                        event,
                        metadata
                ))
                .from(metadata)
                .join(metadata.event, event)
                .where(event.user.eq(userEntity).and(event.isActive.eq(isActive)))
                .fetch();
    }

    @Override
    public EventWithMetadataDto findByUserIdAndEventIdAndIsActive(UserEntity userEntity, Long eventId, Boolean isActive) {

        QEventMetadataEntity metadata = QEventMetadataEntity.eventMetadataEntity;
        QEventEntity event = QEventEntity.eventEntity;

        return queryFactory
                .select(Projections.constructor(
                        EventWithMetadataDto.class,
                        event,
                        metadata
                ))
                .from(metadata)
                .join(metadata.event, event)
                .where(event.user.eq(userEntity).and(event.id.eq(eventId).and(event.isActive.eq(isActive))))
                .fetchOne();
    }

    @Override
    public EventWithMetadataDto findByEventIdAndIsActive(Long eventId, Boolean isActive) {

        QEventMetadataEntity metadata = QEventMetadataEntity.eventMetadataEntity;
        QEventEntity event = QEventEntity.eventEntity;

        return queryFactory
                .select(Projections.constructor(
                        EventWithMetadataDto.class,
                        event,
                        metadata
                ))
                .from(metadata)
                .join(metadata.event, event)
                .where(event.id.eq(eventId).and(event.isActive.eq(isActive)))
                .fetchOne();
    }

    @Override
    public List<EventWithMetadataDto> findByNextPollingAtBefore(LocalDateTime threshold) {

        QEventMetadataEntity metadata = QEventMetadataEntity.eventMetadataEntity;
        QEventEntity event = QEventEntity.eventEntity;

        return queryFactory
                .select(Projections.constructor(
                        EventWithMetadataDto.class,
                        event,
                        metadata
                ))
                .from(metadata)
                .join(metadata.event, event)
                .where(event.nextPollingAt.before(threshold).and(event.isActive.eq(true)))
                .fetch();
    }
}
