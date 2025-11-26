package com.example.backend.domain.event.service.command;

import com.example.backend.domain.event.converter.EventRequestConverter;
import com.example.backend.domain.event.converter.EventResponseConverter;
import com.example.backend.domain.event.dto.EventRequestDto;
import com.example.backend.domain.event.dto.EventResponseDto;
import com.example.backend.domain.event.entity.EventEntity;
import com.example.backend.domain.event.entity.EventMetadataEntity;
import com.example.backend.domain.event.repository.command.EventCommandRepository;
import com.example.backend.domain.event.repository.command.EventMetadataCommandRepository;
import com.example.backend.domain.event.repository.query.EventQueryRepository;
import com.example.backend.domain.user.entity.UserEntity;
import com.example.backend.infra.google.dto.GoogleFormCreateResponseDto;
import com.example.backend.infra.google.dto.GoogleFormQuestionsIdsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventCommandServiceImpl implements EventCommandService{

    private final EventCommandRepository eventCommandRepository;
    private final EventMetadataCommandRepository eventMetadataCommandRepository;
    private final EventRequestConverter eventRequestConverter;
    private final EventResponseConverter eventResponseConverter;

    @Override
    public EventResponseDto.CreatedFormEvent createEvent(
            UserEntity userEntity, EventRequestDto.CreateFormEvent eventRequestDto,
            GoogleFormCreateResponseDto googleFormCreateResponseDto,
            GoogleFormQuestionsIdsResponseDto googleFormQuestionsIdsResponseDto
    ) {

        EventEntity eventEntity = eventRequestConverter.formDtoToEntity(eventRequestDto, userEntity);
        EventMetadataEntity eventMetadataEntity = eventRequestConverter.formDtoToMetadataEntity(
                eventRequestDto,
                eventEntity,
                googleFormCreateResponseDto,
                googleFormQuestionsIdsResponseDto);

        EventEntity save = eventCommandRepository.save(eventEntity);
        eventMetadataCommandRepository.save(eventMetadataEntity);

        return eventResponseConverter.toCreatedFormEventDto(save, googleFormCreateResponseDto);
    }
}
