package com.example.backend.domain.event.converter;

import com.example.backend.domain.event.dto.EventRequestDto;
import com.example.backend.domain.event.entity.EventEntity;
import com.example.backend.domain.event.entity.EventMetadataEntity;
import com.example.backend.domain.user.entity.UserEntity;
import com.example.backend.infra.google.dto.GoogleFormCreateResponseDto;
import com.example.backend.infra.google.dto.GoogleFormQuestionsIdsResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventRequestConverter {


    /**
     * CreateFormEvent DTO -> Event 엔티티 변환 메서드
     * @param dto : CreateFormEvent DTO
     * @return EventEntity
     */
    public static EventEntity formDtoToEntity(EventRequestDto.CreateFormEvent dto, UserEntity userEntity) {
        return EventEntity.builder()
                .eventTitle(dto.getEventTitle())
                .user(userEntity)
                .eventType(dto.getEventType())
                .eventStartTime(dto.getEventStartTime())
                .eventEndTime(dto.getEventEndTime())
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
            GoogleFormCreateResponseDto googleFormCreateResponseDto,
            GoogleFormQuestionsIdsResponseDto googleFormQuestionsIdsResponseDto
    ) {
        List<String> questionIds = dto.getSearchColumns().stream()
                .map(column -> googleFormQuestionsIdsResponseDto.getQuestionsIds().get(column))
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
