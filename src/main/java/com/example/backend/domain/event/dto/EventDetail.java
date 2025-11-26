package com.example.backend.domain.event.dto;

import com.example.backend.domain.event.entity.EventType;

import java.util.List;

public interface EventDetail {
    String getId();
    String getEventTitle();
    Boolean getIsActive();
    EventType getEventType();
    List<String> getSearchColumns();
}
