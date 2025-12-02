package com.example.backend.infra.polling;

import com.example.backend.domain.event.dto.EventWithMetadataDto;
import com.example.backend.domain.response.service.command.ResponseCommandService;
import com.example.backend.infra.google.drive.GoogleDriveService;
import com.example.backend.infra.google.dto.GoogleFormResponseDto;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class ResponseCollector {

    private final GoogleDriveService googleDriveService;
    private final ResponseCommandService responseCommandService;

    public List<GoogleFormResponseDto> collectResponses(EventWithMetadataDto dto) throws IOException {

        String validAccessToken = googleDriveService.getValidAccessToken(dto.event().getUser().getUsername());

        List<GoogleFormResponseDto> formResponses = googleDriveService.getFormResponses(dto.metadata().getFormId(), validAccessToken, dto.metadata().getSearchColumnsIds());

        responseCommandService.saveResponses(formResponses);
        return formResponses;
    }

}
