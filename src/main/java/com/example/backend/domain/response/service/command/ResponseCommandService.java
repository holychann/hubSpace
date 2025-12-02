package com.example.backend.domain.response.service.command;

import com.example.backend.infra.google.dto.GoogleFormResponseDto;

import java.util.List;

public interface ResponseCommandService {
    // 응답 저장 시 마지막 응답의 생성 시간 저장해야함!!!
    void saveResponses(List<GoogleFormResponseDto> responses);
}
