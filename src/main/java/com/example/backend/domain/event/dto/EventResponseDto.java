package com.example.backend.domain.event.dto;

public class EventResponseDto {

    /**
     * 모든 이벤트 목록
     */
    public static class SearchList {
        int count;

    }

    /**
     * Google Form형 이벤트의 상세 정보
     */
    public static class SearchFormDetail {

    }

    /**
     * CSV형 이벤트의 상세 정보
     */
    public static class SearchFileDetail {

    }
}
