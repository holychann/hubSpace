package com.example.backend.domain.event.service.command;

import com.example.backend.domain.event.dto.EventRequestDto;
import com.example.backend.domain.event.dto.EventResponseDto;
import com.example.backend.domain.event.repository.command.EventCommandRepository;
import com.example.backend.domain.event.repository.command.EventMetadataCommandRepository;
import com.example.backend.domain.event.repository.query.EventQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventCommandServiceImpl implements EventCommandService{

    private final EventCommandRepository eventCommandRepository;
    private final EventMetadataCommandRepository eventMetadataCommandRepository;

    @Override
    public EventResponseDto.CreatedFormEvent createEvent(Long userId, EventRequestDto.CreateFormEvent eventRequestDto) {



        return null;
    }
}
