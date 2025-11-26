package com.example.backend.domain.event.service.query;

import com.example.backend.domain.event.dto.EventDetail;
import com.example.backend.domain.user.entity.UserEntity;

import static com.example.backend.domain.event.dto.EventResponseDto.*;

public interface EventQueryService {
    SearchList getEventList(UserEntity userEntity);
    EventDetail getEventDetail(UserEntity userEntity, Long eventId);
    IsActive getEventIsActive(Long eventId);
}
