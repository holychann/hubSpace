package com.example.backend.domain.response.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseQueryDto {
    private Long eventId;
    private Map<String, String> responses;
}
