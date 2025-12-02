package com.example.backend.domain.response.service.command;

import com.example.backend.infra.google.dto.GoogleFormResponseDto;

import java.util.List;

public interface ResponseCommandService {
    void saveResponses(List<GoogleFormResponseDto> responses, Long eventId);
}
