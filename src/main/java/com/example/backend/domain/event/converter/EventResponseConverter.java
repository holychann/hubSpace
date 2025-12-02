package com.example.backend.domain.event.converter;

import com.example.backend.domain.event.dto.EventDetail;
import com.example.backend.domain.event.dto.EventResponseDto;
import com.example.backend.domain.event.dto.EventWithMetadataDto;
import com.example.backend.domain.event.entity.EventEntity;
import com.example.backend.domain.event.entity.EventMetadataEntity;
import com.example.backend.domain.event.entity.EventType;
import com.example.backend.infra.google.dto.GoogleFormCreateResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.backend.domain.event.dto.EventResponseDto.*;

/**
 * 이벤트 Response 용 DTO 변환 클래스
 */
@Component
public class EventResponseConverter {

    /**
     * 모든 이벤트의 Entity -> DTO 변환 메서드
     * @return SearchList
     */
    public static SearchList toSearchListDto(List<EventWithMetadataDto> eventList) {

        List<EventDetail> results = eventList.stream()
                .map(dto -> {
                    EventEntity event = dto.event();
                    EventMetadataEntity metadata = dto.metadata();

                    if (event.getEventType() == EventType.FORM) {
                        return toSearchFormDto(event, metadata);
                    } else {
                        return toSearchFileDto(event, metadata);
                    }
                })
                .toList();

        return SearchList.builder()
                .count(eventList.size())
                .events(results)
                .build();

    }

    /**
     * Google Form형 이벤트의 Entity -> DTO 변환 메서드
     * @return SearchFormDetail
     */
    public static SearchFormDetail toSearchFormDto(EventEntity eventEntity, EventMetadataEntity eventMetadataEntity) {
        return SearchFormDetail.builder()
                .id(eventEntity.getId())
                .eventTitle(eventEntity.getEventTitle())
                .eventType(eventEntity.getEventType())
                .isActive(eventEntity.getIsActive())
                .createdAt(eventEntity.getCreatedAt())
                .searchColumns(eventMetadataEntity.getSearchColumns())
                .count(eventMetadataEntity.getCount())
                .formUrl(eventMetadataEntity.getFormUrl())
                .build();
    }

    /**
     * CSV형 이벤트의 Entity -> DTO 변환 메서드
     * @return SearchFileDetail
     */
    public static SearchFileDetail toSearchFileDto(EventEntity eventEntity, EventMetadataEntity eventMetadataEntity) {
        return SearchFileDetail.builder()
                .id(eventEntity.getId())
                .eventTitle(eventEntity.getEventTitle())
                .isActive(eventEntity.getIsActive())
                .eventType(eventEntity.getEventType())
                .createdAt(eventEntity.getCreatedAt())
                .count(eventMetadataEntity.getCount())
                .displayColumn(eventMetadataEntity.getDisplayColumn())
                .build();
    }

    /**
     * 활성화 여부 DTO
     * @param eventId : 이벤트 ID
     * @param isActive : 활성화 여부
     * @return IsActive
     */
    public static IsActive toIsActiveDto(Long eventId, Boolean isActive) {
        return IsActive.builder()
                .eventId(eventId)
                .isActive(isActive)
                .build();
    }

    /**
     * 생성된 Google Form 이벤트 DTO
     * @param eventEntity : EventEntity
     * @param googleFormCreateResponseDto : Google Form 생성 결과 DTO
     * @return CreatedFormEvent
     */
    public static CreatedFormEvent toCreatedFormEventDto(EventEntity eventEntity, GoogleFormCreateResponseDto googleFormCreateResponseDto) {
        return CreatedFormEvent.builder()
                .eventId(eventEntity.getId())
                .formUrl(googleFormCreateResponseDto.getFormUrl())
                .build();
    }

    /**
     * 이벤트 ID와 검색 열 정보를 담은 DTO
     * @param eventWithMetadataDto : EventWithMetadataDto
     * @return SearchColumnsAndEventId
     */
    public static SearchColumnsAndEventId toSearchColumnsAndEventIdDto(EventWithMetadataDto eventWithMetadataDto) {
        return SearchColumnsAndEventId.builder()
                .eventId(eventWithMetadataDto.event().getId())
                .eventTitle(eventWithMetadataDto.event().getEventTitle())
                .searchColumns(eventWithMetadataDto.metadata().getSearchColumns())
                .build();
    }
}
