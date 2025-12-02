package com.example.backend.domain.response.service.command;

import com.example.backend.infra.google.dto.GoogleFormResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseCommandServiceImpl implements ResponseCommandService{

    @Override
    public void saveResponses(List<GoogleFormResponseDto> responses) {

    }
}
