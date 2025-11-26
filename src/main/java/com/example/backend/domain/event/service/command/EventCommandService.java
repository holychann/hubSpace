package com.example.backend.domain.event.service.command;

import com.example.backend.domain.event.dto.EventRequestDto;
import com.example.backend.domain.event.dto.EventResponseDto;

import static com.example.backend.domain.event.dto.EventRequestDto.*;
import static com.example.backend.domain.event.dto.EventResponseDto.*;

public interface EventCommandService {

    CreatedFormEvent createEvent(Long userId, CreateFormEvent eventRequestDto);

}
