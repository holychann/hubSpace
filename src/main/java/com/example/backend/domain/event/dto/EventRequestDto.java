package com.example.backend.domain.event.dto;

import com.example.backend.domain.event.entity.EventType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class EventRequestDto {

    /**
     * Google Form 생성 요청 DTO
     * TODO: 유효성 검사 Validation 로직 추가
     */
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateFormEvent {
        private String eventTitle;
        private List<String> searchColumns;
        private EventType eventType;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss") // 명시적 포맷 지정
        private LocalDateTime eventStartTime;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss") // 명시적 포맷 지정
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
