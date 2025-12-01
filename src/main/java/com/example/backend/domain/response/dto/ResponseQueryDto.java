package com.example.backend.domain.response.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 데이터 조회 요청 DTO 겸 데이터 조회 응답 반환 DTO
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseQueryDto {
    private Long eventId;
    private Map<String, String> answers;
}
