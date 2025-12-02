package com.example.backend.domain.event.repository.query;

import com.example.backend.domain.event.dto.EventWithMetadataDto;
import com.example.backend.domain.event.entity.EventEntity;
import com.example.backend.domain.user.entity.UserEntity;

import java.util.List;

public interface EventQueryRepositoryDsl {
    List<EventWithMetadataDto> findByUserIdAndIsActive(UserEntity userEntity, Boolean isActive);
    EventWithMetadataDto findByUserIdAndEventIdAndIsActive(UserEntity userEntity, Long eventId, Boolean isActive);
    EventWithMetadataDto findByEventIdAndIsActive(Long eventId, Boolean isActive);
}
