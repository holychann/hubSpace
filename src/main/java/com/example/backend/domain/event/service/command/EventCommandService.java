package com.example.backend.domain.event.service.command;

import com.example.backend.domain.event.entity.EventEntity;
import com.example.backend.domain.user.entity.UserEntity;
import com.example.backend.infra.google.dto.GoogleFormCreateResponseDto;

import java.time.LocalDateTime;

import static com.example.backend.domain.event.dto.EventRequestDto.*;
import static com.example.backend.domain.event.dto.EventResponseDto.*;

public interface EventCommandService {

    CreatedFormEvent createFormEvent(
            UserEntity userEntity,
            CreateFormEvent eventRequestDto,
            GoogleFormCreateResponseDto googleFormCreateResponseDto);

    void updateLastResponseTime(EventEntity eventEntity, LocalDateTime lastResponseTime);
    void updateNextPollingAtAndLastResponseTime(EventEntity eventEntity, LocalDateTime nextPollingAt, LocalDateTime lastResponseTime);
    void updateEventStatus(EventEntity eventEntity, Boolean isActive);
    void updateNextPollingAt(EventEntity eventEntity, LocalDateTime nextPollingAt);
}
