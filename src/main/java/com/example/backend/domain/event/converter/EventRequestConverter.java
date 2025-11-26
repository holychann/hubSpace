package com.example.backend.domain.event.converter;

import com.example.backend.domain.event.dto.EventRequestDto;
import com.example.backend.domain.event.entity.EventEntity;
import com.example.backend.domain.event.entity.EventMetadataEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventRequestConverter {


    /**
     * CreateFormEvent DTO -> Event 엔티티 변환 메서드
     * @param dto : CreateFormEvent DTO
     * @return EventEntity
     */
    public static EventEntity formDtoToEntity(EventRequestDto.CreateFormEvent dto) {
        return EventEntity.builder()
                .eventTitle(dto.getEventTitle())
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
    public static EventMetadataEntity formDtoToMetadataEntity(EventRequestDto.CreateFormEvent dto) {
        return EventMetadataEntity.builder()
                .searchColumns(dto.getSearchColumns())
                .build();
    }

}
