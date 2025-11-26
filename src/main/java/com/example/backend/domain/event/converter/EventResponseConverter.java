package com.example.backend.domain.event.converter;

import com.example.backend.domain.event.dto.EventDetail;
import com.example.backend.domain.event.dto.EventResponseDto;
import com.example.backend.domain.event.entity.EventEntity;
import com.example.backend.domain.event.entity.EventMetadataEntity;
import com.example.backend.domain.event.entity.EventType;

import java.util.List;

import static com.example.backend.domain.event.dto.EventResponseDto.*;

/**
 * 이벤트 Response 용 DTO 변환 클래스
 */
public class EventResponseConverter {

    /**
     * TODO: QueryDSL 구현 후 구현 예정
     * 모든 이벤트의 Entity -> DTO 변환 메서드
     * @return SearchList
     */
    public static SearchList toSearchListDto(List<EventEntity> eventEntityList) {

        return null;
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
}
