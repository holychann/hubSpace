package com.example.backend.domain.event.dto;

import com.example.backend.domain.event.entity.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 이벤트 도메인 API의 응답 DTO
 */
public class EventResponseDto {

    /**
     * 모든 이벤트 목록
     */
    public static class SearchList {
        Integer count;
        List<EventDetail> events;
    }

    /**
     * Google Form형 이벤트의 상세 정보
     */
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SearchFormDetail implements EventDetail{
        // 공통 멤버
        private Long id;
        private String eventTitle;
        private Boolean isActive;
        private EventType eventType;
        private List<String> searchColumns;
        private LocalDateTime createdAt;
        private Long count;

        // Google Form형 이벤트의 멤버
        private String formUrl;
    }

    /**
     * CSV형 이벤트의 상세 정보
     */
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SearchFileDetail implements EventDetail{
        // 공통 멤버
        private Long id;
        private String eventTitle;
        private Boolean isActive;
        private EventType eventType;
        private List<String> searchColumns;
        private LocalDateTime createdAt;
        private Long count;

        // CSV형 이벤트의 멤버
        private String displayColumn;
    }
}
