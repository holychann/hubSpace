package com.example.backend.domain.event.dto;

import com.example.backend.domain.event.entity.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class EventRequestDto {

    /**
     * Google Form 생성 요청 DTO
     */
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateFormEvent {
        private String eventTitle;
        private List<String> searchColumns;
        private EventType eventType;
        private LocalDateTime eventStartTime;
        private LocalDateTime eventEndTime;
    }

    /**
     * CSV, TSC 등 파일 생성 요청 DTO
     */
    public static class CreateFileEvent {
        private Integer count;
        private String eventTitle;
        private List<String> searchColumns;
        private String displayColumn;
        private EventType eventType;
        private List<Map<String, String>> rows;
    }

}
