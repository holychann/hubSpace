package com.example.backend.domain.event.service.query;

import com.example.backend.domain.event.dto.EventDetail;

import static com.example.backend.domain.event.dto.EventResponseDto.*;

public interface EventQueryService {
    SearchList getEventList(Long userId);
    EventDetail getEventDetail(Long userId, Long eventId);
    IsActive getEventIsActive(Long eventId);
}
