package com.example.backend.domain.event.converter;

import com.example.backend.domain.event.dto.EventRequestDto;
import com.example.backend.domain.event.entity.EventEntity;
import com.example.backend.domain.event.entity.EventMetadataEntity;
import com.example.backend.domain.user.entity.UserEntity;
import com.example.backend.infra.google.dto.GoogleFormCreateResponseDto;
import com.example.backend.infra.google.dto.GoogleFormQuestionsIdsResponseDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class EventRequestConverter {


    /**
     * CreateFormEvent DTO -> Event 엔티티 변환 메서드
     * @param dto : CreateFormEvent DTO
     * @return EventEntity
     */
    public static EventEntity formDtoToEntity(EventRequestDto.CreateFormEvent dto, UserEntity userEntity, Long id) {
        return EventEntity.builder()
                .id(id)
                .eventTitle(dto.getEventTitle())
                .user(userEntity)
                .eventType(dto.getEventType())
                .eventStartTime(dto.getEventStartTime())
                .eventEndTime(dto.getEventEndTime())
                .lastResponseTime(LocalDateTime.now().minusDays(1))
                .nextPollingAt(LocalDateTime.now().plusMinutes(1))
                .isActive(true)
                .build();
    }

    /**
     * CreateFormEvent DTO -> EventMetadata 엔티티 변환 메서드
     * @param dto : CreateFormEvent DTO
     * @return EventMetadataEntity
     */
    public static EventMetadataEntity formDtoToMetadataEntity(
            EventRequestDto.CreateFormEvent dto,
            EventEntity eventEntity,
            GoogleFormCreateResponseDto googleFormCreateResponseDto
    ) {
        List<String> questionIds = dto.getSearchColumns().stream()
                .map(column -> googleFormCreateResponseDto.getSearchColumnsIds().get(column))
                .toList();

        return EventMetadataEntity.builder()
                .event(eventEntity)
                .count(0L)
                .searchColumns(dto.getSearchColumns())
                .formId(googleFormCreateResponseDto.getFormId())
                .formUrl(googleFormCreateResponseDto.getFormUrl())
                .searchColumnsIds(questionIds)
                .build();
    }

}
