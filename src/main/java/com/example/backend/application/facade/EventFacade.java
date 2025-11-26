package com.example.backend.application.facade;

import com.example.backend.domain.event.dto.EventDetail;
import com.example.backend.domain.event.dto.EventRequestDto;
import com.example.backend.domain.event.dto.EventResponseDto;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface EventFacade {

    EventResponseDto.SearchList getEventList(String username);
    EventDetail getEventDetail(String username, Long eventId);
    EventResponseDto.CreatedFormEvent createFormEvent(String username, EventRequestDto.CreateFormEvent eventRequestDto);

}
