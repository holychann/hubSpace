package com.example.backend.domain.event.service.command;

import com.example.backend.domain.event.dto.EventRequestDto;
import com.example.backend.domain.event.dto.EventResponseDto;
import com.example.backend.domain.user.entity.UserEntity;
import com.example.backend.infra.google.dto.GoogleFormCreateResponseDto;
import com.example.backend.infra.google.dto.GoogleFormQuestionsIdsResponseDto;

import static com.example.backend.domain.event.dto.EventRequestDto.*;
import static com.example.backend.domain.event.dto.EventResponseDto.*;

public interface EventCommandService {

    CreatedFormEvent createFormEvent(
            UserEntity userEntity,
            CreateFormEvent eventRequestDto,
            GoogleFormCreateResponseDto googleFormCreateResponseDto,
            GoogleFormQuestionsIdsResponseDto googleFormQuestionsIdsResponseDto);
}
